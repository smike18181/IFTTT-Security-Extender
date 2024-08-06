// src/pages/PredictionPage/PredictionPage.jsx
import React, { useContext, useEffect, useState } from 'react';
import { useLocation, Navigate } from 'react-router-dom';
import PredictionDetails from './json/PreictionDetails.json';
import './PredictionPage.css';
import { ApiContext } from '../../store/ApiContext';

const PredictionPage = () => {
  const location = useLocation();
  const { state } = location;
  const p = state?.predict;
  const { predict, setPredict, setError } = useContext(ApiContext);

  // Stato per gestire il reindirizzamento
  const [shouldRedirect, setShouldRedirect] = useState(false);

  console.log(p);

  useEffect(() => {
    if (p!=null) {
      setPredict(p);
      setShouldRedirect(false);
      window.scrollTo(0, 0);
    } else {
      setError('Impossibile accedere alla pagina direttamente.');
      setShouldRedirect(true); // Imposta il flag per il reindirizzamento
    }
  }, [setPredict, setError]);

  // Se deve avvenire un reindirizzamento, eseguilo
  if (shouldRedirect) {
    return <Navigate to="/" />;
  }

  const predictionInfo = PredictionDetails[predict];

  if (!predictionInfo) {
    return <div>Invalid prediction.</div>;
  }

  return (
    <>
      <h1 className='layout-title'>Risultato della Predizione</h1>
      <div className="prediction-page">
        <div className="prediction-details">
          <label className={predict !== 0 ? 'prediction-label not-secure' : 'prediction-label'}>
            {predictionInfo.nome}
          </label>
          <p>{predictionInfo.descrizione}</p>
          <h3>Esempio:</h3>
          <p>{predictionInfo.esempio}</p>
          {predictionInfo.image && (
            <img src={predictionInfo.image} alt={predictionInfo.nome} className="prediction-image" />
          )}
        </div>
      </div>
    </>
  );
};

export default PredictionPage;
