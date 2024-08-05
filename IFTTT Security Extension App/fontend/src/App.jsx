
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import HomePage from './pages/Homepage/HomePage.jsx';
import ServicesPage from './pages/ServicesPage/ServicesPage.jsx';
import HistoryPage from './pages/HystoryPage/HystoryPage.jsx';
import AppletsPage from './pages/AppletsPage/AppletsPage.jsx';
import PredictionPage from './pages/PredictionPage/PredictionPage.jsx';
import Layout from './componets/Layout/Layout.jsx';
import { ApiProvider } from './store/ApiContext.jsx';

function App() {
  return (
    <Router>
       <Layout>
         <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/services" element={<ServicesPage />} />
          <Route path="/history" element={<HistoryPage />} />
          <Route path="/applets" element={<AppletsPage />} />
          <Route path="/predizione" element={<PredictionPage />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
