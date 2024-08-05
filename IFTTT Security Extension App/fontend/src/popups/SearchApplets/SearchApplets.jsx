import React, { useState, useContext } from 'react';
import { ApiContext } from '../../store/ApiContext';
import './SearchApplets.css'; 
import Card from '../../componets/Card/Card';
import FetchData from '../../hooks/FetchData';

const SearchApplets = ({ onClose, handleClosePopUp }) => {
  const [query, setQuery] = useState('');
  const [endpoint, setEndpoint] = useState('');
  const { dataPopUp, setDataPopUp, loading, error, setError } = useContext(ApiContext);

  const handleSearch = () => {
    if (!validateInput(query)) {
      setError("Inserire almeno 3 lettere");
      return;
    } else {
      setError(''); // Resetta l'errore
      const searchEndpoint = `api/applets/search?name=${encodeURIComponent(query)}`;
      setEndpoint(searchEndpoint); // Aggiorna l'endpoint da passare a FetchData
    }
  };

  const handleClose = () => {
    setError('');
    setQuery('');
    setEndpoint('');
    setDataPopUp([]);
    handleClosePopUp(true);
  };

  function validateInput(value) {
    // Rimuove eventuali spazi all'inizio e alla fine e conta il numero di lettere
    const letterCount = value.replace(/[^a-zA-Z]/g, '').length;
    return letterCount >= 3;
  }

  return (
    !onClose && (
      <>
        <div className="overlay">
          <div className="search-popup">
            <button className="close-button" onClick={handleClose}>×</button>
            <h2>Cerca Applet</h2>
            <div className="search-header">
              <input
                className='search-input2'
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Enter search term..."
              />
              <button className='search-button' onClick={handleSearch} disabled={loading}>
                {loading ? 'Searching...' : 'Search'}
              </button>
            </div>
            {error && <div className="error">{error}</div>}
            
            {endpoint && <FetchData endpoint={endpoint} isSearchPopUp={true} />} 
            
            <div className="results">
              {dataPopUp && dataPopUp.length > 0 ? (
                <div className="card-container">
                  {dataPopUp.map((item) => (
                    <Card key={item.id} element={item} isService={false} />
                  ))}
                </div>
              ) : (
                !loading && <div>Nessun risultato</div>
              )}
            </div>
          </div>
        </div>
      </>
    )
  );
};

export default SearchApplets;
