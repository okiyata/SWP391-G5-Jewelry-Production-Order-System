import React from "react";
import { useNavigate } from "react-router-dom";

export default function Header() {
  const navigate = useNavigate();

  const goToHomePage = () => {
    navigate('/');
  };

  return (
    <div style={{boxShadow:'0 4px 4px 0 rgba(0, 0, 0, 0.25)'}} className="w-100 d-flex align-items-center px-5 py-2 h-20">
      <button onClick={goToHomePage} className="text-3xl bg-transparent border-0 p-0" style={{backgroundColor: 'transparent', cursor: 'pointer',fontSize: '2rem'}}>
        宝石店
      </button>
    </div>
  );
}
