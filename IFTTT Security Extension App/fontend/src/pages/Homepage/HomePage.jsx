// src/pages/Homepage/HomePage.jsx
import React, { useEffect, useRef } from 'react';
import './HomePage.css';
import { Link } from 'react-router-dom';

const HomePage = () => {
  const videoRef = useRef(null);

  useEffect(() => {
    if (videoRef.current) {
      videoRef.current.playbackRate = 0.65; // Rallenta la velocità di riproduzione del video
    }
  }, []);

  return (
    <div className="homepage">
      <video autoPlay loop muted className="background-video" ref={videoRef}>
        <source src="/background.mp4" type="video/mp4" />
        Your browser does not support the video tag.
      </video>
      <div className="homepage-content">
        <h1>Benvenuti in</h1>
        <label className='title-homepage'>IFTTT Security Extension</label>
        <p><Link to="/services">Espora i servizi disponibili sulla piattaforma IFTTT.</Link></p>
      </div>
    </div>
  );
};

export default HomePage;
