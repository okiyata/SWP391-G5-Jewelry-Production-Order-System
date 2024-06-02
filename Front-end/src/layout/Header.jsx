import React from "react";
import { Link } from "react-router-dom";
import { IoSearchCircle } from "react-icons/io5";
import { IoNotificationsOutline } from "react-icons/io5";
import { GoPerson } from "react-icons/go";
import { Button } from "react-bootstrap";

export default function Header() {
  return (
    <div
      style={{ boxShadow: "0 4px 4px 0 rgba(0, 0, 0, 0.25)" }}
      className="w-100 d-flex justify-content-between align-items-center px-5 py-2 h-20"
    >
      <Link to="/home" className="text-decoration-none">
        <h1 className="display-4 px-5" style={{ fontSize: '2rem', color: 'black' }}>宝石店</h1>
      </Link>
      <div className="d-flex flex-column justify-content-center gap-2">
        <div className="position-relative w-100">
          <IoSearchCircle
            size={30}
            className="position-absolute top-50 translate-middle"
            style={{ right: "-2%" }}
          />
          <input
            className="w-100 py-1"
            style={{
              borderRadius: 20,
              backgroundColor: "rgba(255, 251, 251, 1)",
            }}
            type="text"
          />
        </div>
        <div className="d-flex flex-row justify-content-center gap-5 ">
          <Link className="text-decoration-none text-dark" to="/home">
            Home
          </Link>
          <Link className="text-decoration-none text-dark" to="/collections">
            Collections
          </Link>
          <Link className="text-decoration-none text-dark" to="/blogs">
            Blogs
          </Link>
          <Link className="text-decoration-none text-dark" to="/live_price">
            Live Price
          </Link>
          <Link className="text-decoration-none text-dark" to="/about">
            About
          </Link>
        </div>
      </div>
      <div className="d-flex flex-row align-items-center gap-3">
        <IoNotificationsOutline size={30} color="black" />
        <GoPerson size={30} color="black" />
        <Button
          style={{ borderRadius: 25, backgroundColor: "#4B4B4B" }}
          variant="outline-light"
          className="shadow px-4 py-2 fw-bolder"
        >
          Make jewelry
        </Button>
      </div>
    </div>
  );
}
