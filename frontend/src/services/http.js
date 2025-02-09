import axios from 'axios'
// import { jwtDecode } from 'jwt-decode'


const http = axios.create({
  headers: {
    Accept: 'application/json',
  },
  timeout: 100000,
})


const signup = async (input) => {
  const { data: response } = await http({
    method: 'POST',
    url: '/api/signup',
    data: input,
  })

  return response
}

const login = async (input) => {
  const { data: response } = await http({
    method: 'POST',
    url: '/api/login',
    data: input,
  })

  return response
}


const loginRequest = async (username, password) => {
  const response = await fetch('/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw new Error('Invalid credentials');
  }

  return response.json();
};



const httpService = {
  signup,
  login,
  http,
  loginRequest
}

export default httpService