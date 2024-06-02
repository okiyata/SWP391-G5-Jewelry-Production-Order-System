import { Col, Container, Nav, Row } from "react-bootstrap";
import Carousel from "../components/Carousel";
import { Link } from "react-router-dom";
import ImageRotator from "../components/ImageRotator";

function Home() {
  return (
    <>
      <Carousel />

      {/*Collections section*/}
      <div>
        <div
          className="slider-bar d-flex align-content-center justify-content-center p-2"
          style={{ backgroundColor: "#4B4B4B", marginBottom: "10px" }}
        >
          <Link
            to={"/collections_page"}
            className="text-decoration-none text-white fw-bold"
          >
            <h2>OUR COLLECTIONS</h2>
          </Link>
        </div>
        <ImageRotator />
      </div>

      {/*About us section*/}
      <section
        className="about-us-section text-white mb-2 mt-2 p-2 pb-5"
        style={{ backgroundColor: "#4B4B4B" }}
      >
        <Row>
          <h2 className="text-center p-2 fw-bold">ABOUT US</h2>
        </Row>
        <Row>
          <Col className="about-us-context align-content-center">
            <div
              className="w-75"
              style={{
                margin: "0px auto",
                padding: "0px auto",
                textAlign: "justify",
              }}
            >
              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
                enim ad minim veniam, quis nostrud exercitation ullamco laboris
                nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
                in reprehenderit in voluptate velit esse cillum dolore eu fugiat
                nulla pariatur. Excepteur sint occaecat cupidatat non proident,
                sunt in culpa qui officia deserunt mollit anim id est laborum.
              </p>
              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
                enim ad minim veniam, quis nostrud exercitation ullamco laboris
                nisi ut aliquip ex ea commodo consequat.
              </p>
            </div>
          </Col>
          <Col className="about-us-image d-flex align-content-center justify-content-center">
            <img
              src="https://www.greatplacetowork.ca/images/Asset_3.webp"
              alt="about-us-image"
              width={500}
              height={300}
              style={{ borderRadius: "15px" }}
            />
          </Col>
        </Row>
      </section>
      {/*Feedback section*/}
      <section className="feedback-section mb-2">
        <div className="d-flex justify-content-center">
          <div
            className="d-flex content-align-center justify-content-center"
            style={{ borderBottom: "2px solid black", width: "70%" }}
          >
            <h2 className="fw-bold p-2">FEEDBACKS</h2>
            
          </div>
        </div>
      </section>
    </>
  );
}
export default Home;
