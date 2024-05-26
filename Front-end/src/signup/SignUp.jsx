import React from "react";
import { RiArrowLeftLine } from "react-icons/ri";
import { Link, useNavigate } from "react-router-dom";
import { Container, Form, Button } from "react-bootstrap";

export default function SignUp() {
  const navigate = useNavigate();
  const handleSubmit = (e) => {
    e.preventDefault();
    navigate("/info");
  };

  return (
    <Container
      style={{ paddingTop: "10%", paddingBottom: "10%" }}
      className="d-flex justify-content-center align-items-center "
    >
      <div
        className="position-relative  p-4 "
        style={{
          width: "30%",
          backgroundColor: "rgba(217, 217, 217, 0.7)",
          borderRadius: 20,
        }}
      >
        <Link
          to="/login"
          className="position-absolute"
          style={{ top: "7%", left: "7%" }}
        >
          <RiArrowLeftLine size={30} color="black" />
        </Link>
        <h2 className="text-center mb-4">Sign up</h2>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Email:</Form.Label>
            <Form.Control
              type="email"
              placeholder="Email"
              className="border-2 "
              style={{ borderColor: "#000", borderRadius: 10 }}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Password:</Form.Label>
            <Form.Control
              type="password"
              className="border-2 "
              style={{ borderColor: "#000", borderRadius: 10 }}
            />
          </Form.Group>
          <Form.Group className="mb-4">
            <Form.Label>Confirm Password:</Form.Label>
            <Form.Control
              type="password"
              className="border-2 "
              style={{ borderColor: "#000", borderRadius: 10 }}
            />
          </Form.Group>
          <div className="d-flex justify-content-center mb-3">
            <Button
              type="submit"
              className="w-75 border-2 "
              style={{
                backgroundColor: "rgba(201, 201, 201, 1)",
                borderColor: "#000",
                color: "#000",
                borderRadius: 10,
              }}
            >
              Sign up
            </Button>
          </div>
        </Form>
      </div>
    </Container>
  );
}
