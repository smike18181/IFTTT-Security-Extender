// src/components/Alert.js
import React, { useEffect, useState } from 'react';
import './Alert.css'; // Aggiungi lo stile per l'alert

const Alert = ({ message, onClose }) => {
  const [show, setShow] = useState(false);
  const [currentMessage, setCurrentMessage] = useState('');

  useEffect(() => {
    if (message) {
      setCurrentMessage(message); // Aggiorna il messaggio corrente
      setShow(true); // Mostra l'alert

      // Nascondi l'alert dopo 5 secondi
      const timer = setTimeout(() => {
        setShow(false);
      }, 5000);

      // Pulizia del timer se il componente viene smontato
      return () => {
        clearTimeout(timer);
      };
    }
  }, [message]);

  useEffect(() => {
    if (!show && currentMessage) {
      // Se l'alert è nascosto e c'era un messaggio precedente, chiama onClose
      if (onClose) {
        onClose();
      }
    }
  }, [show, currentMessage, onClose]);

  return (
    <div className={`alert ${show ? 'show' : 'hide'}`}>
      {currentMessage}
    </div>
  );
};

export default Alert;
