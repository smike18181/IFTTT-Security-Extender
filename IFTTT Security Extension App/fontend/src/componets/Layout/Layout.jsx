// src/components/Layout.js
import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../Header/Header';
import './Layout.css';
import Footer from '../Footer/Footer';

const Layout = ({ children }) => {
  
  return (
    <div className="layout">

      <Header />

      <main className="layout-content">
        {children}
      </main>
      
      <Footer />
    </div>
  );
};

export default Layout;
