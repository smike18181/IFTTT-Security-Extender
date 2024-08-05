import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import GridLayout from '../../componets/GridContents/GridLayout';
import FetchData from '../../hooks/FetchData';
import './AppletsPage.css';

const AppletsPage = () => {
  const location = useLocation();
  const { element } = location.state || {};
  const [dataLoaded, setDataLoaded] = useState(false);

  const apiEndpoint = 'api/applets/by-canale-id/';

  if (!element) {
    return <div>Nessun elemento selezionato</div>;
  }

  const handleDataLoaded = () => {
    setDataLoaded(true);
  };

  return (
    <div className="container">
      <div className='service-info'>
        <img src={element.url_img} alt='service image' className="service-image" />
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
