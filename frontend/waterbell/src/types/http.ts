import axios from 'axios'

export default axios.create({
  baseURL: 'http://localhost:8080',
<<<<<<< HEAD
  timeout: 5000,
  headers: {
    'X-Custom-Header': 'foobar'
  }
=======
  timeout: 4000,
  headers: { 'X-Custom-Header': 'foobar' }
>>>>>>> ef6d89b01203712d149a5c9462a6400f9bf64329
})
