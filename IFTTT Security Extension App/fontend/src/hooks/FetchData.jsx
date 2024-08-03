// src/components/FetchData.js
import { useContext, useEffect } from 'react';
import axios from 'axios';
import { ApiContext } from '../store/ApiContext';

const FetchData = ({ endpoint }) => {
  const { setData, setLoading, setError } = useContext(ApiContext);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await axios.get(`http://localhost:8080/${endpoint}`);
        setData(response.data);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [endpoint, setData, setLoading, setError]);

  return null;
};

export default FetchData;
