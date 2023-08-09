import axios from 'axios'

export default axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 4000,
  headers: { 'X-Custom-Header': 'foobar' }
})
