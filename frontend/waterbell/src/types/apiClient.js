import axios from 'axios'

// axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: 'http://localhost:8080'
})
// request interceptor 설정
apiClient.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)
export default apiClient
//# sourceMappingURL=apiClient.js.map
