import axios from 'axios'
import { computed } from 'vue'
import { useStore } from 'vuex'

// 토큰을 가져오는 함수 //로그인되어 있는 경우 토큰을, 없는 경우 그냥 axios 요청을 보내는 axios 함수
// state에 저장할 예정. 가져와야 함.
const store = useStore()
const isLogin = computed(() => store.getters.isLogin)
const accessToken = function getToken() {
  return computed(() => store.getters.loginUser).value.accessToken
}

// axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: 'http://localhost:8080'
})

// request interceptor 설정
apiClient.interceptors.request.use(
  (config) => {
    if (isLogin.value) {
      const token = accessToken()
      if (token) {
        config.headers['Authorization'] = `Bearer ${accessToken()}`
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default apiClient
