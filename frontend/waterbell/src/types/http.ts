import axios from 'axios'

export default axios.create({
  baseURL: 'http://localhost:8080',
<<<<<<< HEAD
  timeout: 7000,
=======
  timeout: 4000,
>>>>>>> c8bf90cfab35e601c02dd2b68905663423cd6729
  headers: { 'X-Custom-Header': 'foobar' }
})
