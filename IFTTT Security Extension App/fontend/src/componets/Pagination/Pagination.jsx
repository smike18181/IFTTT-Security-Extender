import React from 'react';
import PropTypes from 'prop-types';
import './Pagination.css';

const Pagination = ({ handlePage, numberOfCards, elementsPerPage, currentPage }) => {
  if (elementsPerPage <= 0) {
    console.error('elementsPerPage deve essere maggiore di zero');
    return null;
  }

  const totalPages = Math.ceil(numberOfCards / elementsPerPage);

  // Funzione per gestire i clic sui pulsanti di pagina
  const handleClick = (page) => {
    handlePage(page - currentPage);
  };

  // Funzione per generare i pulsanti di paginazione
  const renderPagination = () => {
    const pages = [];
    const visiblePages = 5; // Numero massimo di pagine visibili
    const startPage = Math.max(0, currentPage - Math.floor(visiblePages / 2));
    const endPage = Math.min(totalPages - 1, startPage + visiblePages - 1);

    // Gestione dei puntini e della prima pagina
    if (startPage > 0) {
      pages.push(0);
      if (startPage > 1) pages.push('...');
    }

    // Aggiungi le pagine centrali
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    // Gestione dei puntini e dell'ultima pagina
    if (endPage < totalPages - 1) {
      if (endPage < totalPages - 2) pages.push('...');
      pages.push(totalPages - 1);
    }

    // Rendi i pulsanti di paginazione
    return pages.map((page, index) => (
      page === '...' ? (
        <span key={`dots-${index}`} className="pagination-dots">...</span>
      ) : (
        <button
          key={`page-${page}`}
          onClick={() => handleClick(page)}
          className={`pagination-button ${page === currentPage ? 'selected-button' : ''}`}
        >
          {page + 1}
        </button>
      )
    ));
  };

  return (
    <div className="pagination">
      {renderPagination()}
    </div>
  );
};

Pagination.propTypes = {
  handlePage: PropTypes.func.isRequired,
  numberOfCards: PropTypes.number.isRequired,
  elementsPerPage: PropTypes.number.isRequired,
  currentPage: PropTypes.number.isRequired,
};

export default Pagination;
