// src/hooks/FetchData.js
import { useContext, useEffect } from 'react';
import axios from 'axios';
import { ApiContext } from '../store/ApiContext';

const FetchData = ({ endpoint, onDataLoaded, isSearchPopUp, isDetailsPopUp}) => {
  const { setData, setDataPopUp, setPredict ,setLoading, setError } = useContext(ApiContext);


  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);

      // Check if data is already in session storage
      const sessionData = sessionStorage.getItem(endpoint);
      if (sessionData) {

        try {
          const data = JSON.parse(sessionData);
          console.log("sessione: {}",data);
          
          if(!isSearchPopUp && !isDetailsPopUp){
              setData(data);
              console.log("data: {}", data);
          }else if(isDetailsPopUp){
              setPredict(data);
              console.log("prediction: {}", data);
          } else {
            setDataPopUp(data);
            console.log("dataPopUp: {}", data);
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

        // Ensure response is JSON
        if (response.headers['content-type']?.includes('application/json')) {

          console.log("API: {}",response.data);

          if(!isSearchPopUp && !isDetailsPopUp){
            setData(response.data);
            console.log("data: {}", response.data);
          }else if(isDetailsPopUp){
            setPredict(response.data);
            console.log("predizione: {}", response.data);
          } else {
            setDataPopUp(response.data);
            console.log("dataPopUp: {}", response.data);
          }


          // Save the response data to session storage
          if(!isSearchPopUp) sessionStorage.setItem(endpoint, JSON.stringify(response.data));
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
  }, [endpoint, setData, setDataPopUp ,setLoading, setError, onDataLoaded]);

  return null;
};

export default FetchData;
