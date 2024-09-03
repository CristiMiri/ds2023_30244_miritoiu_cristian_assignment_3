import { Client } from "@stomp/stompjs";
import React, { useState, useEffect } from "react";
import { Button, InputGroup, FormControl, ListGroup } from "react-bootstrap";
import { Message } from "../models/message";

const ChatService: React.FC = () => {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [connected, setConnected] = useState(false);
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputMessage, setInputMessage] = useState("");
  const brokerURL = "ws://localhost:8084/websocket";
  useEffect(() => {
    const client = new Client({
      brokerURL: brokerURL,
      onConnect: (frame) => {
        setConnected(true);
        console.log("Connected: " + frame);
        client.subscribe("/topic/broadcast", (message) => {
          console.log(message.body);
          setMessages((prevMessages) => [
            ...prevMessages,
            JSON.parse(message.body),
          ]);
        });
      },
      onWebSocketError: (error) => {
        console.error("WebSocket Error:", error);
      },
      onStompError: (frame) => {
        console.error("STOMP Error:", frame.headers.message, frame.body);
      },
    });

    setStompClient(client);

    return () => {
      client.deactivate();
    };
  }, []);

  const handleSendMessage = () => {
    if (stompClient && inputMessage.trim()) {
      createMessage(inputMessage);
      stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify(createMessage(inputMessage)),
      });
      console.log("Sent message:", createMessage(inputMessage));

      setInputMessage("");
    }
  };

  const createMessage = (content: string): Message => {
    return {
      id: messages.length + 1, // You might want a more robust way to generate IDs
      content: content,
      sender: "sender", // Replace with actual sender value if necessary
      receiver: "receiver", // Replace with actual receiver value if necessary
      timestamp: new Date(),
      read: false,
    };
  };

  const handleConnectDisconnect = () => {
    if (stompClient) {
      if (connected) {
        stompClient.deactivate();
        setConnected(false);
      } else {
        stompClient.activate();
      }
    }
  };

  return (
    <div>
      {/* <Button
        variant={connected ? "danger" : "success"}
        onClick={handleConnectDisconnect}
      >
        {connected ? "Disconnect" : "Connect"}
      </Button>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Enter message"
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          disabled={!connected}
        />
        <Button
          variant="primary"
          onClick={handleSendMessage}
          disabled={!connected}
        >
          Send
        </Button>
      </InputGroup>
      <ListGroup>
        {messages.map((msg, index) => (
          <ListGroup.Item key={index}>{msg.content}</ListGroup.Item>
        ))}
      </ListGroup> */}
    </div>
  );
};

export default ChatService;
