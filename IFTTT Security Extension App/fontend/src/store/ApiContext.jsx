// src/contexts/ApiContext.js
import React, { createContext, useState } from 'react';
import axios from 'axios';

const ApiContext = createContext();

const ApiProvider = ({ children }) => {
  const [data, setData] = useState([]);
  const [dataPopUp, setDataPopUp] = useState([]);
  const [predict, setPredict] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [filteredElements, setFilteredElements] = useState([]);
  const [selectedElement, setSelectedElement] = useState(null);

  const fetchData = async ({ endPoint }) => {
    setLoading(true);
    setError(null);

    // Check if data is already in session storage
    const sessionData = sessionStorage.getItem(endPoint);
    if (sessionData) {
      try {
        const data = JSON.parse(sessionData);
        
            setData(data);
            console.log("data: {}", data);

      } catch (err) {
        setError('Recupero dati dalla sessione fallito');
      } finally {
        setLoading(false);
      }
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/${endPoint}`);

      // Ensure response is JSON
      if (response.headers['content-type']?.includes('application/json')) {

        setData(response.data);
        console.log("data: {}", response.data);

        sessionStorage.setItem(endPoint, JSON.stringify(response.data));

      } else {
        throw new Error('Unexpected content type: ' + response.headers['content-type']);
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'An unknown error occurred.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <ApiContext.Provider value={{ 
      data, setData, loading, setLoading, error, setError, fetchData, predict , setPredict,
      page, setPage, filteredElements, setFilteredElements, selectedElement, setSelectedElement, dataPopUp ,setDataPopUp
    }}>
      {children}
    </ApiContext.Provider>
  );
};

export { ApiContext, ApiProvider };
