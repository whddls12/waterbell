import axios from 'axios'
import { computed } from 'vue'
import store from '@/store/index'

// 토큰을 가져오는 함수 //로그인되어 있는 경우 토큰을, 없는 경우 그냥 axios 요청을 보내는 axios 함수
// state에 저장할 예정. 가져와야 함.
const isLogin = computed(() => store.getters['auth/isLogin'])
const accessToken = function getToken() {
  return computed(() => store.getters['auth/accessToken'])
}

// axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: 'http://localhost:8080'
})

// request interceptor 설정
apiClient.interceptors.request.use(
  (config) => {
    console.log(isLogin.value)
    console.log(accessToken())
    if (isLogin.value) {
      const token = accessToken()
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default apiClient
