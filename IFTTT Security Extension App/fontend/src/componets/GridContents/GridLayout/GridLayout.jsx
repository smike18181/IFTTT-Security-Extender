import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import './GridLayout.css';
import Pagination from "../../Pagination/Pagination";
import SearchBar from "../../SearchBar/SearchBar";
import { ApiContext } from "../../../store/ApiContext";
import Card from "../../Card/Card";

const GridLayout = ({ isServices }) => {
  const { data, page, setPage, filteredElements, setFilteredElements, selectedElement, setSelectedElement } = useContext(ApiContext);

  const elements = data;

  const elementsPerPage = 12;
  const [startIndex, setStartIndex] = useState(0);
  const [currentElements, setCurrentElements] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    setFilteredElements(elements);
  }, [elements, setFilteredElements]);

  useEffect(() => {
    const newStartIndex = page * elementsPerPage;
    setStartIndex(newStartIndex);
    setCurrentElements(filteredElements.slice(newStartIndex, newStartIndex + elementsPerPage));
  }, [page, filteredElements]);

  const numberOfElements = filteredElements.length;

  const handlePage = (shift) => {
    setPage((prevPage) => {
      const totalPages = Math.ceil(numberOfElements / elementsPerPage);
      const newPage = prevPage + shift;
      // Assicurati che la nuova pagina sia valida
      if (newPage < 0 || newPage >= totalPages) {
        return prevPage;
      }
      return newPage;
    });
  };

  const handleSearch = (title) => {
    if (!title || typeof title !== 'string') {
      setFilteredElements(elements);
      setPage(0);
      return;
    }

    const filtered = elements.filter(element => {
      return element.nome && typeof element.nome === 'string' &&
        element.nome.toLowerCase().includes(title.toLowerCase());
    });

    setFilteredElements(filtered);
    setPage(0);
  };

  const handleElementClick = (e) => {
    if(isServices){
        setPage(0);
        navigate('/applets', { state: { e } });
    }
  };


  return (
    <div className="Layout">
      <SearchBar onSearch={handleSearch} />

      {numberOfElements > elementsPerPage && (
        <Pagination
          handlePage={handlePage}
          numberOfCards={numberOfElements}
          elementsPerPage={elementsPerPage}
          currentPage={page}
        />
      )}

      <div className="gridContainer">
        {currentElements.map((element, index) => (
          <Card
            key={element.id || startIndex + index}
            element={element}
            isService={isServices}
            handleElementClick={() => handleElementClick(element)}
          />
        ))}
      </div>

      {numberOfElements > elementsPerPage && (
        <Pagination
          handlePage={handlePage}
          numberOfCards={numberOfElements}
          elementsPerPage={elementsPerPage}
          currentPage={page}
        />
      )}

    </div>
  );
};

export default GridLayout;
