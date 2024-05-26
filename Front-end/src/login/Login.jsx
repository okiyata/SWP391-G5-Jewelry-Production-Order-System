import React from "react";
import { Link } from "react-router-dom";
import { Container, Form, Button, Row, Col } from "react-bootstrap";

export default function Login() {
  return (
    <Container
    style={{paddingTop:'10%',paddingBottom:'10%'}}
      className="d-flex justify-content-center align-items-center "
    >
      <div className=" p-4  " style={{ width: "30%",backgroundColor:'rgba(217, 217, 217, 0.7)',borderRadius:20 }}>
        <h2 className="text-center mb-4">Sign in</h2>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Username:</Form.Label>
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
            <div className="text-end mt-2 ">
              <Link to="/reset_password" className="text-muted text-decoration-none">
                Forget password
              </Link>
            </div>
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
              Sign in
            </Button>
          </div>
        </Form>
        <p className="text-center text-muted">
          Don’t have an account?{" "}
          <Link to="/signup" className="text-decoration-underline text-muted">
            Sign up
          </Link>
        </p>
      </div>
    </Container>
  );
}
