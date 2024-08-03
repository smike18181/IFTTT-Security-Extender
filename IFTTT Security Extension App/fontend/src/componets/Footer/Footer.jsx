// src/components/Footer.jsx
import React from 'react';
import './Footer.css';
import { Link } from 'react-router-dom';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-left">
          <img src="/header/logoIFTTTSE.png" alt="Logo" className="footer-logo" />
          <p className="footer-name">Michele Pesce</p>
          <p className="footer-app-name">IFTTT Security Extension</p>
        </div>
        <div className="footer-center">
          <h4>Navigazione</h4>
            <ul className="footer-nav">
                <li><Link to="/">Home</Link></li>
                <li><Link to="/servizi">Servizi</Link></li>
                <li><Link to="/history">Storico Predizioni</Link></li>
                <li><Link to="/applets">Applets</Link></li>
                <li><Link to="/predizione">Predizione</Link></li>
            </ul>
        </div>
        <div className="footer-right">
          <h4>Contatti</h4>
          <p>Email:  <i>pesce.michele188@gmail.com</i></p>
          <div className="footer-social">
            <a href="https://instagram.com"><img src="/social/instagram.png" alt="Facebook" /></a>
            <a href="https://linkedin.com"><img src="/social/linkedin.png" alt="LinkedIn" /></a>
          </div>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2024 IFTTT Security Extension. Tutti i diritti riservati.</p>
      </div>
    </footer>
  );
};

export default Footer;
