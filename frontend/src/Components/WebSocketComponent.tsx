import React, { useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { useUser } from "../Hooks/UserContext";
import { NavDropdown } from "react-bootstrap";
const WebSocketComponent = () => {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [, setConnected] = useState(false);
  const [messages, setMessages] = useState<String[]>([]);
  const [inputMessage, setInputMessage] = useState("");
  const [notification, setNotification] = useState("");
  const { user } = useUser();
  useEffect(() => {
    const initializeWebSocket = () => {
      const client = new Client({
        brokerURL: "ws://localhost:8082/websocket",
      });

      client.onConnect = (frame) => {
        // setConnected(true);
        const subscribtion: string = "/user/" + user?.id + "/queue/messages";
        console.log("Connected: " + frame);
        client.subscribe(subscribtion, (greeting) => {
          console.log(greeting.body);
          showGreeting(greeting.body);
          setNotification("Notifications  " + messages.length.toString());
        });
      };

      client.onWebSocketError = (error) => {
        console.error("Error with websocket", error);
      };

      client.onStompError = (frame) => {
        console.error("Broker reported error: " + frame.headers["message"]);
        console.error("Additional details: " + frame.body);
      };

      setStompClient(client);
    };

    initializeWebSocket();

    // Clean up the WebSocket connection on component unmount
    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, []); // Dependency array to ensure this effect runs only once on mount

  useEffect(() => {
    if (stompClient) {
      stompClient.activate();
    }
  }, [stompClient]);

  useEffect(() => {
    setNotification("Notifications  " + messages.length.toString());
  }, [messages]);

  const connect = () => {
    if (stompClient) {
      stompClient.activate();
      setConnected(true);
    }
  };

  const disconnect = () => {
    if (stompClient) {
      stompClient.deactivate();
      setConnected(false);
      console.log("Disconnected");
    }
  };

  const showGreeting = (message: String) => {
    setMessages((messages) => [...messages, message]);
  };
  const removeMessage = (index: number) => {
    setMessages((messages) => [
      ...messages.slice(0, index),
      ...messages.slice(index + 1),
    ]);
    setNotification("Notifications  " + messages.length.toString());
  };

  return (
    <>
      {/* the title of dropdown should contain the messages size */}
      <NavDropdown title={notification}>
        {messages.map(
          (
            message:
              | string
              | number
              | boolean
              | React.ReactElement<
                  any,
                  string | React.JSXElementConstructor<any>
                >
              | Iterable<React.ReactNode>
              | React.ReactPortal
              | null
              | undefined,
            index: React.Key | null | undefined
          ) => (
            <NavDropdown.Item
              key={index}
              onClick={() => removeMessage(index as number)}
            >
              {message}
            </NavDropdown.Item>
          )
        )}
      </NavDropdown>
    </>
  );
};

export default WebSocketComponent;
