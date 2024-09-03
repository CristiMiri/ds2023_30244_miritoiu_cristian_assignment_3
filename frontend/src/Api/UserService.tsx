import axios, { AxiosResponse } from "axios";
import { User } from "../models/user";

const apiBaseUrl = "http://localhost:8080/users";
const createApiUrl = (endpoint: string) => `${apiBaseUrl}/${endpoint}`;

// Add a function to get the JWT token
const getAuthToken = () => {
  return localStorage.getItem("jwtToken") || "";
};

const handleError = (error: any) => {
  console.error("Error:", error);
  throw error;
};

const handleResponse = (response: AxiosResponse) => {
  console.table(response.data);
  return response.data;
};

const axiosInstance = axios.create({
  baseURL: apiBaseUrl,
  headers: {
    Authorization: `Bearer ${getAuthToken()}`,
    ContentType: "application/json",
  },
});

export const getAllUsers = async () => {
  console.log(getAuthToken());
  return axiosInstance.get("").then(handleResponse).catch(handleError);
};

export const getUserById = async (id: number) => {
  return axiosInstance
    .get(`/getuserbyid/${id}`)
    .then(handleResponse)
    .catch(handleError);
};

export const registerUser = async (user: User) => {
  return axiosInstance
    .post("/register", user)
    .then(handleResponse)
    .catch(handleError);
};

export const updateUser = async (user: User) => {
  return axiosInstance
    .put("/update", user)
    .then(handleResponse)
    .catch(handleError);
};

export const deleteUser = async (id: number) => {
  return axiosInstance
    .delete(`/delete/${id}`)
    .then(handleResponse)
    .catch(handleError);
};

export const loginUser = async (email: string, password: string) => {
  const userData = {
    email: email,
    password: password,
  };
  return axios
    .post(`${apiBaseUrl}/login`, userData)
    .then((response) => {
      if (response.status === 200) {
        // Store JWT token in local storage or a suitable place
        localStorage.setItem("jwtToken", response.data.token);
        axiosInstance.defaults.headers.common[
          "Authorization"
        ] = `Bearer ${response.data.token}`;
        localStorage.setItem("user", JSON.stringify(response.data));
        localStorage.setItem("jwtToken", response.data.access_token);

        console.log(localStorage.getItem("jwtToken"));
        return response.data;
      } else {
        alert("Invalid email or password");
      }
    })
    .catch((error) => {
      alert("Invalid email or password");
    });
};
