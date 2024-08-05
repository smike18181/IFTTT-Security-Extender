// src/components/Card.jsx
import React, {useState} from 'react';
import './Card.css';
import DetailsApplet from '../../popups/DetailsApplet/DetailsApplet';

const Card = ({ element, isService, handleElementClick }) => {

  const [showDetails, setShowDetails] = useState(false);
 
  if (!element) {
    return null; // Render nothing if element is undefined
  }

  if (isService) {
    return (
      <div className='card' onClick={() => handleElementClick(element)}>
        <img src={element.url_img} alt={element.nome} className="card-image" />
        <div className="card-content">
          <h3 className="card-title">{element.nome}</h3>
        </div>
      </div>
    );
  } else {
    const ChannelImgTrigger = element.trigger?.canale?.url_img;
    const ChannelImgAction = element.action?.canale?.url_img;
    const creatorName = element.creator?.nome;

    const installs = (element.installs_count > 1000) ? `${Math.floor(element.installs_count / 1000)}k` : element.installs_count;

    return (
      <>
        <div className='card-applet' onClick={() => setShowDetails(true)}>
          <div className='channels-img'>
            {ChannelImgTrigger && (
              <img src={ChannelImgTrigger} alt={`${element.nome} channel`} className="channel-card-image" />
            )}
            {ChannelImgAction && (
              <img src={ChannelImgAction} alt={`${element.nome} channel`} className="channel-card-image" />
            )}
          </div>

          <div className="card-content-applet">
            <h3 className="card-title">{element.nome}</h3>
            {creatorName && (
              <label className="card-creator"><label className='by'>by:</label> {creatorName}</label>
            )}
          </div>

          <div className='card-installs'>
            <img src='card/user-icon.png' alt='user-icon' className='user-icon' />
            <label className='installs-count'>{installs}</label>
          </div>

        </div>

       {showDetails &&
         <DetailsApplet element={element} onClose={() => setShowDetails(false)} />}

      </>
    );
  }
};

export default Card;
