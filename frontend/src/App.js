import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import axios from "axios";
import { useRef } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  const baseImageRef = useRef();
  const processedBase64Ref = useRef();
  const fileRef = useRef();

  function ProcessGrayscale() {
    const url = "http://localhost:8080/api/process/grayscale";
    axios
      .post(url, { src: baseImageRef.current.src })
      .then((result) => {
        processedBase64Ref.current.src = result.data;
        processedBase64Ref.current.style.width = "275px";
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function ProcessBlackAndWhite() {
    const url = "http://localhost:8080/api/process/black-and-white";
    axios
      .post(url, { src: baseImageRef.current.src })
      .then((result) => {
        processedBase64Ref.current.src = result.data;
        processedBase64Ref.current.style.width = "275px";
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function encodeImage() {
    var file = fileRef.current.files[0];
    var reader = new FileReader();
    reader.onloadend = function () {
      baseImageRef.current.src = reader.result;
      baseImageRef.current.style.width = "275px";
    };
    reader.readAsDataURL(file);
  }

  return (
    <Container>
      <Row>
        <h1>Image processing app</h1>
      </Row>
      <Row>
        <div>
          <img className="me-4" ref={baseImageRef} src="https://via.placeholder.com/272x275" alt="Original" />
          <img ref={processedBase64Ref} src="https://via.placeholder.com/272x275" alt="Processed" />
        </div>
      </Row>
      <Row>
        <div>
          <input type="file" ref={fileRef} onChange={encodeImage}></input>
          <Button variant="primary" onClick={ProcessGrayscale}>
            Grayscale
          </Button>
          <Button variant="primary" onClick={ProcessBlackAndWhite}>
            Black and white
          </Button>
        </div>
      </Row>
    </Container>
  );
}

export default App;
