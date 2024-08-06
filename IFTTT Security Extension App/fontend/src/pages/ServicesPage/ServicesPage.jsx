import React, { useContext } from "react";
import './ServicesPage.css';
import GridLayout from "../../componets/GridContents/GridLayout";
import FetchData from "../../hooks/FetchData";

export default function ServicesPage() {

  window.scrollTo(0, 0); // Scorri all'inizio della pagina
  const apiEndpoint = 'api/services';
  const title = 'Servizi partner';

  return (
    <>
      <main>
        <h1 className="layout-title">{title}</h1>
        <FetchData endpoint={apiEndpoint} />
        <GridLayout isServices={true} />  
      </main>
    </>
  );
}
