import { useContext, useEffect } from 'react';
import axios from 'axios';
import { ApiContext } from '../store/ApiContext';

const FetchData = ({ endpoint, onDataLoaded, isSearchPopUp, isDetailsPopUp }) => {
  const { setData, setDataPopUp, setPredict, setLoading, setError } = useContext(ApiContext);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);

      const sessionData = sessionStorage.getItem(endpoint);
      if (sessionData) {
        try {
          const data = JSON.parse(sessionData);
          console.log("sessione: {}", data);
          
          if (isDetailsPopUp) {
            setPredict(data);
            console.log("prediction: {}", data);
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
          console.log("API: {}", responseData);

          if (isDetailsPopUp) {
            setPredict(responseData);
            sessionStorage.setItem(endpoint, JSON.stringify(responseData));
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
