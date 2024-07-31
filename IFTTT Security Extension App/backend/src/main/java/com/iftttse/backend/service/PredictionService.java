package com.iftttse.backend.service;

import com.iftttse.backend.entity.*;
import com.iftttse.backend.utils.TfidfVectorizer;
import com.iftttse.backend.repository.AppletRepository;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Service
public class PredictionService {

    @Autowired
    private AppletRepository appletRepository;

    private Evaluator evaluator;
    private Map<String, TfidfVectorizer> vectorizers;
    private Map<String, Integer> featureCounts;

    private static final Set<String> RELEVANT_FIELDS = new HashSet<>();
    private static final String CLASSIFICATOR_ROOT = "/predictor/ClassificatoreSicurezzaApplets.pmml";
    private static final String FEATURE_COUNT_PATH = "/predictor/feature_counts.json";

    public PredictionService() {
        loadModel();
        this.vectorizers = loadVectorizers();
        this.featureCounts = loadFeatureCounts();
    }

    private void loadModel() {
        try (InputStream is = getClass().getResourceAsStream(CLASSIFICATOR_ROOT)) {
            if (is == null) {
                throw new RuntimeException("Modello PMML non trovato nella cartella resources.");
            }
            evaluator = new LoadingModelEvaluatorBuilder() {
                @Override
                protected void checkSchema(ModelEvaluator<?> modelEvaluator) {
                    // Bypass il limite dei 1000 elementi del modello
                }
            }.load(is).build();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento del modello PMML", e);
        }
    }

    private Map<String, TfidfVectorizer> loadVectorizers() {
        // Carica i TfidfVectorizer per ogni campo di testo
        Map<String, TfidfVectorizer> vectorizers = new HashMap<>();
        vectorizers.put("name", new TfidfVectorizer());
        vectorizers.put("description", new TfidfVectorizer());
        vectorizers.put("triggers_category", new TfidfVectorizer());
        vectorizers.put("actions_category", new TfidfVectorizer());
        vectorizers.put("service_triggers", new TfidfVectorizer());
        vectorizers.put("service_actions", new TfidfVectorizer());
        vectorizers.put("services", new TfidfVectorizer());
        return vectorizers;
    }

    private Map<String, Integer> loadFeatureCounts() {
        Map<String, Integer> featureCounts = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream(FEATURE_COUNT_PATH)) {
            if (is == null) {
                throw new RuntimeException("File feature_count.json non trovato nella cartella resources.");
            }
            String json = new String(is.readAllBytes());
            JSONObject jsonObject = new JSONObject(json);
            for (String key : jsonObject.keySet()) {
                featureCounts.put(key, jsonObject.getInt(key));
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento del file di feature counts", e);
        }
        return featureCounts;
    }

    public Integer predict(Long appletId) {
        Applet applet = appletRepository.findById(appletId).orElse(null);
        if (applet == null) {
            throw new RuntimeException("Applet non trovata con ID: " + appletId);
        }

        Map<String, Object> input = new HashMap<>();
        input.put("name", applet.getNome() != null ? applet.getNome() : "Unknown");
        input.put("installs_count", applet.getInstalls_count());
        input.put("services_len", applet.getNum_services());
        input.put("requires_android_app", applet.isRequired_android_app() ? 1 : 0);
        input.put("requires_ios_app", applet.isRequired_ios_app() ? 1 : 0);
        input.put("requires_mobile_app", (applet.isRequired_ios_app() && applet.isRequired_android_app()) ? 1 : 0);

        Action action = applet.getAction();
        Triggers trigger = applet.getTrigger();
        String triggerCategory = trigger != null ? trigger.getNome() : "Unknown";
        String actionCategory = action != null ? action.getNome() : "Unknown";
        String description = (trigger != null ? trigger.getDescrizione() : "") + " " + (action != null ? action.getDescrizione() : "");

        input.put("triggers_category", triggerCategory);
        input.put("actions_category", actionCategory);
        input.put("description", description);

        Servizio canaleAction = action != null ? action.getCanale() : null;
        Servizio canaleTrigger = trigger != null ? trigger.getCanale() : null;
        input.put("service_triggers", canaleTrigger != null ? canaleTrigger.getDescrizione() : "Unknown");
        input.put("service_actions", canaleAction != null ? canaleAction.getDescrizione() : "Unknown");
        input.put("services", (canaleAction != null ? canaleAction.getNome() : "Unknown") + " " + (canaleTrigger != null ? canaleTrigger.getNome() : "Unknown"));

        Creatore creatore = applet.getCreator();
        input.put("by_service_owner", creatore != null && creatore.isBy_service_owner());

        System.out.println("Input Data: " + input);

        Map<String, Object> transformedInput = transformTextData(input);

        Map<FieldName, FieldValue> arguments = prepareArguments(transformedInput);

        System.out.println("Arguments for Prediction: " + arguments);

        Map<String, ?> results = predict(arguments);

        return (Integer) results.get("target");
    }

    public Map<String, Object> transformTextData(Map<String, Object> input) {
        Map<String, Object> transformedInput = new HashMap<>();

        // Per ogni voce nell'input
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String && vectorizers.containsKey(key)) {
                String text = (String) value;
                TfidfVectorizer vectorizer = vectorizers.get(key);
                int numFeatures = featureCounts.get(key);
                double[] tfidfVector = vectorizer.transform(text);

                // Assicurati di avere abbastanza spazio nel vettore
                double[] vector = new double[numFeatures];

                // Riempi il vettore con i valori TF-IDF
                for (int i = 0; i < tfidfVector.length; i++) {
                    vector[i] = tfidfVector[i];
                }

                // Se il vettore TF-IDF è più corto del previsto, lascia le altre posizioni come 0
                for (int i = tfidfVector.length; i < numFeatures; i++) {
                    vector[i] = 0;
                }

                // Aggiungi ogni valore al map con il nome "nomeparametro_index"
                for (int i = 0; i < vector.length; i++) {
                    transformedInput.put(key + "_" + (i + 1), vector[i]);
                }
            } else {
                // Aggiungi il valore originale se non è una stringa o se non è da vettorizzare
                transformedInput.put(key, value);
            }
        }

        return transformedInput;
    }

    private Map<FieldName, FieldValue> prepareArguments(Map<String, Object> input) {
        Map<FieldName, FieldValue> arguments = new HashMap<>();

        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = entry.getKey();
            FieldName fieldName = FieldName.create(key);
            Object value = entry.getValue();
            FieldValue fieldValue = FieldValueUtil.create(value);
            arguments.put(fieldName, fieldValue);
        }

        return arguments;
    }

    private Map<String, ?> predict(Map<FieldName, FieldValue> arguments) {
        Map<FieldName, ?> results = evaluator.evaluate(arguments);
        return EvaluatorUtil.decodeAll(results);
    }
}
