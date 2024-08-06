import React, { useState, useEffect } from 'react';
import './HistoryPage.css';

const HistoryPage = () => {
  const [predictions, setPredictions] = useState([]);

  // Funzione per recuperare i dati dalla sessione
  const fetchSessionData = () => {
    const sessionKeys = Object.keys(sessionStorage).filter(key => key.startsWith('api/predict/'));
    return sessionKeys.map(key => {
      const item = sessionStorage.getItem(key);
      const parsedItem = item ? JSON.parse(item) : null;
      
      // Assicurati che parsedItem sia un oggetto e abbia le chiavi 'nome' e 'data'
      if (parsedItem && typeof parsedItem.nome === 'string' && typeof parsedItem.data === 'number') {
        return { nome: parsedItem.nome, data: parsedItem.data };
      }
      
      return null; // Se l'oggetto non è valido, restituisci null
    }).filter(item => item !== null); // Rimuovi i valori nulli
  };

  // Carica i dati dalla sessione all'avvio
  useEffect(() => {
    setPredictions(fetchSessionData());
  }, []);

  // Funzione per ottenere la descrizione basata sul valore di data
const getDataDescription = (data) => {
    switch (data) {
      case 0:
        return 'Innocua';
      case 1:
        return 'Danni personali';
      case 2:
        return 'Danni fisici';
      case 3:
        return 'Violazione sicurezza informatica';
      default:
        return 'Sconosciuto'; // Valore di default per valori non previsti
    }
  };

   // Funzione per ottenere la classe basata sul valore di data
   const getDataClass = (data) => {
    return data === 0 ? 'safe' : 'danger'; // Aggiungi classe 'danger' per valori diversi da 0
  };

  // Componente per la tabella
  const renderTable = () => (
    <table className="results-table">
      <thead>
        <tr>
          <th>Applet</th>
          <th>Predizione</th>
        </tr>
      </thead>
      <tbody>
        {predictions.map(({ nome, data }, index) => (
          <tr key={index}>
            <td>{nome}</td>
            <td className={getDataClass(data)}>{getDataDescription(data)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );

  return (
    <div>
    <h1 className='layout-title'>Storia delle Predizioni</h1>
    <div className="history-page">
      {renderTable()}
    </div>
    </div>
  );
};

export default HistoryPage;
