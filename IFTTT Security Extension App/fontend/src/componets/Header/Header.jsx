// src/components/Header.jsx
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

  return (
    <header className="header">
      <div className="logo">
        <Link to="/">
          <img src="header/logoIFTTTSE.png" alt="Logo" />
        </Link>
      </div>
      <button className="menu-toggle" onClick={toggleMenu}>
        <img src="header/menu-icon.png" alt="Menu Icon" />
      </button>
      <nav className={`nav-links ${isMenuOpen ? 'open' : ''}`}>
        <div className="nav-items">
          <Link to="/hystory" className="nav-link">
            <img src="header/history.png" alt="History Icon" />
            History Prediction
          </Link>
          <Link to="/servizi" className="nav-link">
            <img src="header/vehicle.png" alt="Services Icon" />
            Services
          </Link>
          <Link to="/applets" className="nav-link">
            <img src="header/chef.png" alt="Cerca Applets Icon" />
            Cerca Applets
          </Link>
        </div>
      </nav>
    </header>
  );
};

export default Header;
