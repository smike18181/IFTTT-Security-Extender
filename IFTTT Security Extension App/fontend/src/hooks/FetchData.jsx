import { useContext, useEffect } from 'react';
import axios from 'axios';
import { ApiContext } from '../store/ApiContext';

const FetchData = ({ endpoint, onDataLoaded, isSearchPopUp, applet ,isDetailsPopUp }) => {
  const { setData, setDataPopUp, setPredict, setLoading, setError } = useContext(ApiContext);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);

      // Funzione per ottenere l'item dalla sessione
      const getSessionItem = (key) => {
        const item = sessionStorage.getItem(key);
        return item ? JSON.parse(item) : null;
      };

      // Recupera e stampa i dati dalla sessione
      const sessionData = getSessionItem(endpoint);
      if (sessionData) {
        try {
          const data = sessionData.data;
          console.log("sessione: ", data);
          
          if (isDetailsPopUp) {
            setPredict(data);
            console.log("prediction: ", data);
          } else if (isSearchPopUp) {
            setDataPopUp(data);
          } else {
            setData(data);
          }

          if (onDataLoaded) onDataLoaded();

        } catch (err) {
          setError('Recupero dati dalla sessione fallito');
        } finally {
          setLoading(false);
        }
        return;
      }

      try {
        const response = await axios.get(`http://localhost:8080/${endpoint}`);

        if (response.headers['content-type']?.includes('application/json')) {
          const responseData = response.data;
          console.log("API: ", responseData);

          if (isDetailsPopUp) {
            setPredict(responseData);

            // Salva un oggetto con applet.nome e responseData nella sessione
            const dataToStore = { nome: applet.nome, data: responseData };

            sessionStorage.setItem(endpoint, JSON.stringify(dataToStore));
            console.log("prediction: {}", responseData);
          } else if (isSearchPopUp) {
            setDataPopUp(responseData);
            console.log("dataPopUp: {}", responseData);
          } else {
            setData(responseData);
            console.log("data: {}", responseData);
          }

          if (onDataLoaded) onDataLoaded();
        } else {
          throw new Error('Unexpected content type: ' + response.headers['content-type']);
        }
      } catch (err) {
        setError(err.response?.data?.message || err.message || 'An unknown error occurred.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [endpoint, setData, setDataPopUp, setLoading, setError, onDataLoaded, isDetailsPopUp, isSearchPopUp, setPredict]);

  return null;
};

export default FetchData;
