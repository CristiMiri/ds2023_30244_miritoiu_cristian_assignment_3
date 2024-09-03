import React, { useRef, useEffect, useState } from "react";
import { Button, Form, InputGroup, ListGroup } from "react-bootstrap";
import { Message } from "../models/message";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faHeadset } from "@fortawesome/free-solid-svg-icons";
import "../ChatWindow.css";
import { User } from "../models/user";
import ChatService from "../Api/ChatService";
import { Client, IMessage } from "@stomp/stompjs";

const ChatWindow: React.FC = ({}) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [connected, setConnected] = useState(false);
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputMessage, setInputMessage] = useState("");

  // const [message, setMessage] = useState("");
  const [chat, setChat] = useState<Message[]>([]);
  const [isVisible, setIsVisible] = useState(false);
  const chatContainerRef = useRef<HTMLDivElement>(null);
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const brokerURL = "ws://localhost:8084/websocket";
  const subscribtionPrivate: string = "/user/" + user.username + "/private";
  const adminEndPoint = "/app/broadcast";

  useEffect(() => {
    const client = new Client({
      brokerURL: brokerURL,
      onConnect: (frame) => {
        setConnected(true);
        console.log("Connected: " + frame);
        client.subscribe(subscribtionPrivate, (message) => {
          console.log("Message received: " + message.body);
          filterTyping();
          handleReadNotification(message);
          handlePrivateMessage(message);
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

  const handleReadNotification = (message: IMessage) => {
    var Data = JSON.parse(message.body);
    const foundMessage = chat.find((msg) => msg.id === Data.id);
    if (foundMessage) {
      foundMessage.read = true;
      setChat([...chat]);
    }
  };

  const createMessage = (content: string): Message => {
    return {
      id: chat.length + 1, // You might want a more robust way to generate IDs
      content: content,
      sender: user.username, // Replace with actual sender value if necessary
      receiver: "broadcast", // Replace with actual receiver value if necessary
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

  const handleSendTyping = () => {
    if (stompClient) {
      stompClient.publish({
        destination: adminEndPoint,
        body: JSON.stringify(createMessage("Typing...")),
      });
    }
  };

  const handlesendBroadcast = () => {
    if (stompClient) {
      var chatMessage = createMessage(inputMessage);
      stompClient.publish({
        destination: adminEndPoint,
        body: JSON.stringify(chatMessage),
      });
      setChat([...chat, chatMessage]);
      setInputMessage("");
    }
  };

  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  }, [chat]);

  const toggleVisibility = () => {
    setIsVisible(!isVisible);
  };

  const buttonStyle: React.CSSProperties = {
    position: "fixed",
    bottom: "20px",
    right: "20px",
    zIndex: 999, // Ensures it's on top of other content
  };

  function handldeBroadcastMessage(message: IMessage) {
    console.log("BroadCast message received: " + message.body);
    var Data = JSON.parse(message.body);
    setChat((prevMessages) => [...prevMessages, Data]);
  }
  function handlePrivateMessage(message: IMessage) {
    console.log("Private message received: " + message.body);
    var Data = JSON.parse(message.body);
    setChat((prevMessages) => [...prevMessages, Data]);
    // setMessages((prevMessages) => [...prevMessages, Data]);
  }

  const filterTyping = () => {
    setChat(
      chat.filter(
        (msg) =>
          !(msg.content.includes("Typing...") && msg.sender !== user.username)
      )
    );
  };

  function handleMessageRead(msg: Message) {
    msg.read = true;
    stompClient?.publish({
      destination: adminEndPoint,
      body: JSON.stringify(msg),
    });
    setChat([...chat]);
  }

  return (
    <div>
      <ChatService />
      {isVisible && (
        <div
          style={{
            position: "fixed",
            bottom: "20px",
            right: "20px",
            width: "400px",
            height: "300px",
            border: "1px solid #ccc",
            borderRadius: "5px",
            backgroundColor: "#FfFfFF ",
            opacity: "1",
            boxShadow: "0px 0px 10px rgba(0,0,0,0.2)",
          }}
        >
          <div
            style={{
              padding: "10px",
              height: "100%",
              display: "flex",
              flexDirection: "column",
            }}
          >
            <h5>Hello {user.username}</h5>
            <div
              ref={chatContainerRef}
              style={{ flex: 1, overflowY: "auto", marginBottom: "10px" }}
            >
              <ListGroup>
                {chat.map((msg) => (
                  <ListGroup.Item
                    key={msg.id}
                    className={
                      msg.sender === user.username
                        ? "message-bubble sender-bubble"
                        : "message-bubble receiver-bubble"
                    }
                    onClick={() => {
                      handleMessageRead(msg);
                    }}
                  >
                    {msg.content}
                    {msg.sender === user.username && msg.read && (
                      <FontAwesomeIcon
                        icon={faCheck}
                        size="xs"
                        style={{
                          color: "#74C0FC",
                          marginLeft: "10px",
                          marginTop: "10px",
                        }}
                      />
                    )}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            </div>
            <Form.Group>
              <InputGroup>
                <Form.Control
                  type="text"
                  placeholder="Type your message..."
                  value={inputMessage}
                  onChange={(e) => setInputMessage(e.target.value)}
                  onClick={handleSendTyping}
                />
                <InputGroup>
                  <Button
                    variant="success"
                    onClick={handlesendBroadcast}
                    className="mt-2"
                  >
                    Send
                  </Button>
                </InputGroup>
              </InputGroup>
            </Form.Group>
          </div>
        </div>
      )}
      <Button
        variant="success"
        onClick={() => {
          handleConnectDisconnect();
          toggleVisibility();
        }}
        style={buttonStyle} // Apply the buttonStyle to position it
      >
        {isVisible ? "Chat" : "Chat"}
        <FontAwesomeIcon
          icon={faHeadset}
          style={{ color: "#ffffff", marginLeft: "5px" }}
        />
      </Button>
      {/* <ChatComponent /> */}
    </div>
  );
};

export default ChatWindow;
