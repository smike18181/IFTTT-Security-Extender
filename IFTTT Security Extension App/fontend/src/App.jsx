// src/App.jsx
import React, { useContext, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import HomePage from './pages/Homepage/HomePage.jsx';
import ServicesPage from './pages/ServicesPage/ServicesPage.jsx';
import HistoryPage from './pages/HistoryPage/HistoryPage.jsx';
import AppletsPage from './pages/AppletsPage/AppletsPage.jsx';
import PredictionPage from './pages/PredictionPage/PredictionPage.jsx';
import Layout from './componets/Layout/Layout.jsx';
import { ApiContext } from './store/ApiContext.jsx';

function App() {
  const { setError } = useContext(ApiContext);

  useEffect(() => {
    // Pulizia dell'errore quando il componente viene montato
    return () => {
      setError(null);
    };
  }, [setError]);

  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/services" element={<ServicesPage />} />
          <Route path="/history" element={<HistoryPage />} />
          <Route path="/applets" element={<AppletsPage />} />
          <Route path="/predizione" element={<PredictionPage />} />
          {/* Route catch-all per percorsi non definiti */}
          <Route
            path="*"
            element={
              <ErrorRedirect />
            }
          />
        </Routes>
      </Layout>
    </Router>
  );
}

// Componente per gestire i percorsi non definiti e impostare l'errore
const ErrorRedirect = () => {
  const { setError } = useContext(ApiContext);

  useEffect(() => {
    setError('Pagina non trovata');
  }, [setError]);

  // Redirect alla home page o qualsiasi altra pagina
  return <Navigate to="/" />;
};

export default App;
