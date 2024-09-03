export interface User {
  id?: number;
  username: string;
  password: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  role?: string; //admin or user
}

export const dummyUsers: User[] = [
  {
    id: 1,
    firstName: "admin",
    username: "admin",
    password: "admin",
    email: "admin@mail.com",
    role: "admin",
  },
  {
    id: 2,
    username: "user2",
    password: "password2",
    email: "user2@example.com",
    role: "user",
  },
];
