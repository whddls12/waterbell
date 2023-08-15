import axios from 'axios'

export default axios.create({
  baseURL: process.env.VUE_APP_API,

  timeout: 10000,
  headers: { 'X-Custom-Header': 'WaterBell' }
})
