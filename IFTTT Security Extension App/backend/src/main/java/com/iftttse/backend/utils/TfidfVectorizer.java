package com.iftttse.backend.utils;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TfidfVectorizer {
    private Map<String, Integer> vocabulary;

    public TfidfVectorizer() {
        this.vocabulary = loadVocabulary();
    }

    public double[] transform(String text) {
        double[] vector = new double[vocabulary.size()];
        Map<String, Double> termFreq = new HashMap<>();
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(text));
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                if (vocabulary.containsKey(term)) {
                    termFreq.put(term, termFreq.getOrDefault(term, 0.0) + 1);
                }
            }
            tokenStream.end();
            tokenStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Double> entry : termFreq.entrySet()) {
            String term = entry.getKey();
            double tf = entry.getValue();
            int index = vocabulary.get(term);
            vector[index] = tf; // In realtà, dovresti applicare la formula TF-IDF qui
        }

        return vector;
    }

    private Map<String, Integer> loadVocabulary() {
        // Carica il vocabolario dal file o da altra fonte
        // Questa mappa deve essere implementata per restituire le parole e i loro indici
        return new HashMap<>(); // Implementa il caricamento effettivo
    }
}
