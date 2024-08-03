// src/contexts/ApiContext.js
import React, { createContext, useState } from 'react';

const ApiContext = createContext();

const ApiProvider = ({ children }) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [filteredElements, setFilteredElements] = useState([]);
  const [selectedElement, setSelectedElement] = useState(null);

  return (
    <ApiContext.Provider value={{ 
      data, setData, loading, setLoading, error, setError,
      page, setPage, filteredElements, setFilteredElements, selectedElement, setSelectedElement
    }}>
      {children}
    </ApiContext.Provider>
  );
};

export { ApiContext, ApiProvider };
