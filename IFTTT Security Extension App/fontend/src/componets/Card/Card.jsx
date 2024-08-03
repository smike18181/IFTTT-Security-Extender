// src/components/Card.jsx
import React from 'react';
import './Card.css';

const Card = ({ element, isService }) => {
    console.log(element);
  if (isService) {
    return (
      <div className='card' onClick={() => console.log('Card clicked')}>
        <img src={element.url_img} alt={element.nome} className="card-image" />
        <div className="card-content">
          <h3 className="card-title">{element.nome}</h3>
        </div>
      </div>
    );
  } else {
    return (
      <div className='card-alternate' onClick={() => console.log('Alternate Card clicked')}>
        <p>Non è un servizio</p>
      </div>
    );
  }
};

export default Card;
