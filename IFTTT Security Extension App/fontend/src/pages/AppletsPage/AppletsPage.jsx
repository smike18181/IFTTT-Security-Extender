import React, { useState, useEffect, useCallback, useContext } from 'react';
import { useLocation, Navigate } from 'react-router-dom';
import GridLayout from '../../componets/GridContents/GridLayout'; // Assicurati che il percorso sia corretto
import FetchData from '../../hooks/FetchData';
import './AppletsPage.css';
import { ApiContext } from '../../store/ApiContext';

const AppletsPage = () => {
  const location = useLocation();
  const { e } = location.state || {};
  const [dataLoaded, setDataLoaded] = useState(false);
  const { element, setElement, setError } = useContext(ApiContext);

  // Stato per gestire il reindirizzamento
  const [shouldRedirect, setShouldRedirect] = useState(false);

  useEffect(() => {
    if (e) {
      setElement(e);
      setShouldRedirect(false);
      window.scrollTo(0, 0); // Scroll to the top of the page
    } else {
      console.log(e);
      setError('Impossibile accedere direttamente.');
      setShouldRedirect(true);
    }
  }, [element, setElement, setError]);

  const handleDataLoaded = useCallback(() => {
    console.log('Data loaded successfully');
    setDataLoaded(true);
  }, []);

  // Effettua il redirect solo se necessario
  if (shouldRedirect) {
    return <Navigate to="/" />;
  }

  if (!element) {
    return <div>Nessun elemento selezionato</div>;
  }

  const apiEndpoint = 'api/applets/by-canale-id/';

  return (
    <div className="container">
      <div className='service-info'>
        {element.url_img && <img src={element.url_img} alt='service image' className="service-image" />}
        <h1>{element.nome}</h1>
        <p>{element.descrizione}</p>
        <label className='layout-subtitle'>Applets</label>
      </div>

      <FetchData endpoint={apiEndpoint + element.id} onDataLoaded={handleDataLoaded} />
      {dataLoaded && <GridLayout isServices={false} />}
    </div>
  );
};

export default AppletsPage;
