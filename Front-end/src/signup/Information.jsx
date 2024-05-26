import React from "react";
import { Container, Form, Button, Row, Col } from "react-bootstrap";

export default function Information() {
  return (
    <Container   style={{paddingTop:'10%',paddingBottom:'10%'}} className="d-flex justify-content-center align-items-center  py-32">
      <div className=" p-4  " style={{ width: "30%" ,backgroundColor:'rgba(217, 217, 217, 0.7)',borderRadius:20 }}>
        <h2 className="text-center mb-4">Input information</h2>
        <Form>
          <Row className="mb-3">
            <Col>
              <Form.Group>
                <Form.Label>First Name:</Form.Label>
                <Form.Control
                  type="text"
                  className="border-2 "
                   style={{ borderColor: "#000", borderRadius: 10, }}
                />
              </Form.Group>
            </Col>
            <Col>
              <Form.Group>
                <Form.Label>Last Name:</Form.Label>
                <Form.Control
                  type="text"
                  className="border-2 "
                   style={{ borderColor: "#000", borderRadius: 10, }}
                />
              </Form.Group>
            </Col>
          </Row>
          <Form.Group className="mb-3">
            <Form.Label>Date of birth:</Form.Label>
            <Form.Control
              type="date"
              className="border-2 "
               style={{ borderColor: "#000", borderRadius: 10, }}
            />
          </Form.Group>
          <Form.Group className=" d-flex flex-row align-items-center justify-items-center ">
            <Form.Label>Gender:</Form.Label>
            <div className="d-flex align-items-center">
              <Form.Check
                type="radio"
                label="Male"
                name="gender"
                value="male"
                className="me-3"
              />
              <Form.Check
                type="radio"
                label="Female"
                name="gender"
                value="female"
              />
            </div>
          </Form.Group>
          <Form.Group className="mb-3 mt-3">
            <Form.Label>Phone:</Form.Label>
            <Form.Control
              type="tel"
              className="border-2 "
               style={{ borderColor: "#000", borderRadius: 10, }}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Address:</Form.Label>
            <Form.Control
              type="text"
              className="border-2 "
               style={{ borderColor: "#000", borderRadius: 10, }}
            />
          </Form.Group>
          <div className="d-flex justify-content-center">
            <Button
              type="submit"
              className="w-75 border-2 "
              style={{
                backgroundColor: "#ccc",
                borderColor: "#000",
                color: "#000",
              }}
            >
              Next
            </Button>
          </div>
        </Form>
      </div>
    </Container>
  );
}
