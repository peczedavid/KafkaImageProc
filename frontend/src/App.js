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
  const processTimeMillisRef = useRef();

  function ProcessGrayscale() {
    SendProcessRequest("http://localhost:8080/api/process/grayscale");
  }

  function ProcessBlackAndWhite() {
    SendProcessRequest("http://localhost:8080/api/process/black-and-white");
  }

  function SendProcessRequest(url) {
    axios
      .post(url, { src: baseImageRef.current.src })
      .then((result) => {
        HandleResult(result);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function HandleResult(result) {
    processedBase64Ref.current.src = result.data.base64;
    processedBase64Ref.current.style.width = "275px";
    processTimeMillisRef.current.innerHTML = result.data.timeMillis + " ms";
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
          <img
            className="me-4"
            ref={baseImageRef}
            src="https://via.placeholder.com/272x275"
            alt="Original"
          />
          <img
            ref={processedBase64Ref}
            src="https://via.placeholder.com/272x275"
            alt="Processed"
          />
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
          <label ref={processTimeMillisRef}></label>
        </div>
      </Row>
    </Container>
  );
}

export default App;
