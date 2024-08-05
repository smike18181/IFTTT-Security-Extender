// src/components/Header.jsx
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Header.css';
import SearchApplets from '../../popups/SearchApplets/SearchApplets';

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [onClose, setOnClose] = useState(true);

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

  const handleClosePopUp = (result) => setOnClose(result);

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
          <Link to="/history" className="nav-link">
            <img src="header/history.png" alt="History Icon" />
            History Prediction
          </Link>
          <Link to="/services" className="nav-link">
            <img src="header/vehicle.png" alt="Services Icon" />
            Services
          </Link>
          <Link className="nav-link" onClick={() => handleClosePopUp(false)}>
            <img src="header/chef.png" alt="Cerca Applets Icon" />
            Cerca Applets
          </Link>

          <SearchApplets onClose={onClose} handleClosePopUp={handleClosePopUp}/>

        </div>
      </nav>
    </header>
  );
};

export default Header;
