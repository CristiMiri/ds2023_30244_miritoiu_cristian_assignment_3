import axios, { AxiosResponse } from "axios";
import { Device } from "../models/device";

const apiBaseUrl = "http://localhost:8081/devices";
const createApiUrl = (endpoint: string) => `${apiBaseUrl}/${endpoint}`;

// Add a function to get the JWT token
const getAuthToken = () => {
  // Replace this with your method of retrieving the JWT token
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
  },
});

export const getAllDevices = async () => {
  return axiosInstance.get("").then(handleResponse).catch(handleError);
};

export const getUserDevices = async (id: number) => {
  return axiosInstance
    .get(`/getdevicesbyuserid/${id}`)
    .then(handleResponse)
    .catch(handleError);
};

export const getDeviceById = async (id: number) => {
  return axiosInstance
    .get(`/getdevicebyid/${id}`)
    .then(handleResponse)
    .catch(handleError);
};

export const insertDevice = async (device: Device) => {
  return axiosInstance
    .post("/insert", device)
    .then(handleResponse)
    .catch(handleError);
};

export const updateDevice = async (device: Device) => {
  return axiosInstance
    .put("/update", device)
    .then(handleResponse)
    .catch(handleError);
};

export const deleteDevice = async (serialNumber: string) => {
  return axiosInstance
    .delete(`/delete/${serialNumber}`)
    .then(handleResponse)
    .catch(handleError);
};

export const getDeviceUsers = async () => {
  const apiBaseUrlUsers = "http://localhost:8081/device_users";
  return axiosInstance
    .get(apiBaseUrlUsers)
    .then(handleResponse)
    .catch(handleError);
};
