import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './DetailsApplet.css';
import { ApiContext } from '../../store/ApiContext';
import FetchData from '../../hooks/FetchData';

const DetailsApplet = ({ element, onClose }) => {
    const { loading, predict, setLoading } = useContext(ApiContext);
    const [endPoint, setEndPoint] = useState("");
    const [dataLoaded, setDataLoaded] = useState(false); // Stato per monitorare il caricamento dei dati
    const navigate = useNavigate();

    const handlePrediction = () => {
        const baseEndPoint = `api/predict/${element.id}`;
        setEndPoint(baseEndPoint);
    };

    const handleDataLoaded = () => {
        setDataLoaded(true);
    };

    useEffect(() => {
        if (!loading && dataLoaded && predict !== null) {
            navigate('/predizione', { state: { predict } });
        }
    }, [loading, dataLoaded, predict, navigate]);

    const handleClose = () => {
        if (onClose) {
            setEndPoint('');
            setDataLoaded(false); // Resetta lo stato del caricamento dei dati
            onClose(false);
        }
    };

    const trigger = element.trigger;
    const action = element.action;

    const ChannelImgTrigger = element.trigger?.canale?.url_img;
    const ChannelImgAction = element.action?.canale?.url_img;
    const creatorName = element.creator?.nome;
    const installs = (element.installs_count > 1000) ? `${Math.floor(element.installs_count / 1000)}k` : element.installs_count;

    return (
        <>
            <div className="overlay" onClick={handleClose}>
                <div className="details-popup" onClick={(e) => e.stopPropagation()}>
                    <button className="close-button-details" onClick={handleClose}>×</button>

                    <label className='title-popup'>Applet details</label>

                    <div className="parent-container">
                        <div className="details-header details-box">
                            <div className="channels-img">
                                {ChannelImgTrigger && (
                                    <img src={ChannelImgTrigger} alt={`${element.nome} channel`} className="channel-card-image" />
                                )}
                                {ChannelImgAction && (
                                    <img src={ChannelImgAction} alt={`${element.nome} channel`} className="channel-card-image" />
                                )}
                            </div>

                            <div className="details-content-applet">
                                <h3 className="details-title">{element.nome}</h3>
                                {creatorName && (
                                    <label className="details-creator"><label className="by">by:</label> {creatorName}</label>
                                )}
                            </div>

                            <div className="details-installs">
                                <img src="card/user-icon.png" alt="user-icon" className="user-icon" />
                                <label className="installs-count">{installs}</label>
                            </div>
                        </div>

                        <div className="details-box">
                            <h3 className="details-title" style={{ color: '#9ACD32'}}>Trigger & Action</h3><br />
                            <div className='trigger'>
                                <img src={ChannelImgTrigger} alt="Trigger icon" className="icon" />
                                <div className='text'>
                                    <h4>{trigger.nome}</h4>
                                    <p>{trigger.descrizione}</p>
                                </div>
                            </div>
                            <div className='action'>
                                <img src={ChannelImgAction} alt="Action icon" className="icon" />
                                <div className='text'>
                                    <h4>{action.nome}</h4>
                                    <p>{action.descrizione}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className='footer-popup'>
                        <div className='prediction-container' onClick={handlePrediction}>
                            {loading ? (
                                <div className="loading"></div>
                            ) : (
                                <div>Predict</div>
                            )}
                        </div>
                    </div>

                    {endPoint && <FetchData endpoint={endPoint} isDetailsPopUp={true} applet={element} onDataLoaded={handleDataLoaded} />}
                </div>
            </div>
        </>
    );
};

export default DetailsApplet;
