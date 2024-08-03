import React from "react";
import './Button.css';

function Button({key, text, funct, dis, className }) {
    return (
        <button id={key} className={`genericButton ${className}`} onClick={funct} disabled={dis}>
            {text}
        </button>
    );
}

export default Button;
