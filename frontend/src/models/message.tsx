export interface Message {
  id: number | null;
  content: string;
  sender: string;
  receiver: string;
  timestamp: Date;
  read: boolean;
}

const dummyMessages: Message[] = [
  {
    id: 1,
    content: "Hello, how are you?",
    sender: "Alice",
    receiver: "Bob",
    timestamp: new Date(),
    read: false,
  },
  {
    id: 2,
    content: "I'm doing well, thanks!",
    sender: "Bob",
    receiver: "Alice",
    timestamp: new Date(),
    read: true,
  },
  {
    id: 3,
    content: "What's the plan for today?",
    sender: "Alice",
    receiver: "Bob",
    timestamp: new Date(),
    read: false,
  },
  {
    id: 4,
    content: "Let's meet up for coffee.",
    sender: "Bob",
    receiver: "Alice",
    timestamp: new Date(),
    read: false,
  },
];

export default dummyMessages;
