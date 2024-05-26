import React from 'react';
import { useNavigate } from 'react-router-dom';
import { RiArrowLeftLine, RiArrowRightLine } from "react-icons/ri";
import { Container, Form, Button } from 'react-bootstrap';

export default function OtpScreen() {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate("/reset_password");
  };

  const handleNext = () => {
    navigate("/new_password");
  };

  return (
    <Container   style={{paddingTop:'10%',paddingBottom:'10%'}} className="d-flex justify-content-center  align-items-center py-32">
      <div className=" p-4" style={{ width: "30%" ,backgroundColor:'rgba(217, 217, 217, 0.7)',borderRadius:20 }}>
        <h2 className="text-center mb-4">Enter OTP</h2>
        <Form>
          <Form.Group className="mb-4">
            <Form.Control
              type="test"
              pattern="[0-9]*"
              placeholder="Enter OTP"
              className="border-2 "
              style={{ borderColor: "#000", borderRadius: 10, }}
            />
          </Form.Group>
          <div className="d-flex flex-row justify-content-center  gap-4  ">
            <Button
              type="button"
              onClick={handleBack}
              className="d-flex align-items-center border-2 "
              style={{ backgroundColor: "rgba(201, 201, 201, 1)", borderColor: "#000", borderRadius: 10,  color: "#000" }}
            >
              <RiArrowLeftLine size={20} /> <span className="ms-2">Back</span>
            </Button>
            <Button
              type="button"
              onClick={handleNext}
              className="d-flex align-items-center border-2 "
              style={{ backgroundColor: "rgba(201, 201, 201, 1)", borderColor: "#000", borderRadius: 10,  color: "#000" }}
            >
              <span className="me-2">Next</span> <RiArrowRightLine size={20} />
            </Button>
          </div>
        </Form>
      </div>
    </Container>
  );
}
