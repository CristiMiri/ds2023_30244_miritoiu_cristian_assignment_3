import React, { useEffect, useState } from "react";
import { Message } from "../models/message";
import { User } from "../models/user";
import { Client } from "@stomp/stompjs";

type Props = {};

const ChatContext = (props: Props) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [receiver, setReceiver] = useState<User | null>(null);
  const [sender, setSender] = useState<User | null>(null);
  const [currentchat, setCurrentchat] = useState(null);
  const [typingSender, setTypingSender] = useState(false);
  const [typingReceiver, setTypingReceiver] = useState(false);
  const [socket, setSocket] = useState<Client | null>(null);

  console.log(
    "ChatContext",
    messages,
    receiver,
    sender,
    currentchat,
    typingSender,
    typingReceiver,
    socket
  );

  //initializing socket
  useEffect(() => {
    const socket = new Client({
      brokerURL: "ws://localhost:8084/websocket",
    });
    socket.onConnect = (frame) => {
      console.log("Connected: " + frame);
    };
  }, []);
  return <div>ChatContext</div>;
};

export default ChatContext;
