// src/components/Footer.jsx
import React from 'react';
import './Footer.css';

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
            <li><a href="/">Home</a></li>
            <li><a href="/servizi">Servizi</a></li>
            <li><a href="/history">Storico Predizioni</a></li>
            <li><a href="/applets">Applets</a></li>
            <li><a href="/predizione">Predizione</a></li>
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
