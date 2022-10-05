import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Dropdown from "react-bootstrap/Dropdown";
import axios from "axios";
import { useRef } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  const baseImageRef = useRef();
  const processedBase64Ref = useRef();

  const fileRef = useRef();
  const processTimeMillisRef = useRef();
  const selectionRef = useRef();

  const processUrlBase = "http://localhost:8080/api/process/";

  function SendProcessRequest() {
    axios
      .post(processUrlBase + selectionRef.current.value, {
        src: baseImageRef.current.src,
      })
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
    if (fileRef.current.files.length == 0) return;
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
      <Card className="mt-5 mx-auto" style={{ width: "675px" }}>
        <Card.Body>
          <div className="d-flex justify-content-center">
            <img
              ref={baseImageRef}
              src="https://via.placeholder.com/272x275"
              alt="Original"
              className="me-5"
            />
            <img
              ref={processedBase64Ref}
              src="https://via.placeholder.com/272x275"
              alt="Original"
            />
          </div>
          <hr></hr>
          <div className="d-flex justify-content-around">
            <input type="file" ref={fileRef} onChange={encodeImage}></input>
            <label
              className="pt-1"
              ref={processTimeMillisRef}
              style={{ width: "75px" }}
            ></label>
            <Form.Select ref={selectionRef} style={{ width: "200px" }}>
              <option value="grayscale">Grayscale</option>
              <option value="black-and-white">Black and white</option>
              <option value="contrast">Contrast</option>
              <option value="blur">Blur</option>
            </Form.Select>
            <Button variant="primary" onClick={SendProcessRequest}>
              Process
            </Button>
            <Dropdown>
              <Dropdown.Toggle variant="outline-primary">
                Options
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item href="#/action-1">Action</Dropdown.Item>
                <Dropdown.Item href="#/action-2">Another action</Dropdown.Item>
                <Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default App;
