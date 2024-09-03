import React from "react";
import { Col, Container, Row } from "react-bootstrap";
import ChatWindow from "../../Components/ChatWindow";
import AdminChatWindow from "../../Components/AdminChatWindow";

const TestingPage = () => {
  return (
    <div>
      <Container>
        <Row>
          <Col>
            <h1>My App</h1>
          </Col>
        </Row>
        <Row>
          <Col>
            {/* <ChatWindow
              receiver={user.role === "user" ? dummyUsers[0] : dummyUsers[1]}
            /> */}
            <AdminChatWindow />
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default TestingPage;
