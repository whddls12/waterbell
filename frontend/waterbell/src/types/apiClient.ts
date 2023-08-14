import axios, { AxiosInstance } from 'axios'
import router from '@/router/index'

// import { computed } from 'vue'
import store from '@/store/index'
// <<<<<<< HEAD
// // import { setInterceptors } from '@/common/interceptors'
// function setInterceptors(instance: any) {
//   instance.interceptors.request.use(
//     function (config: any) {
//       //accessToken을 store.auth 에서 가져오기
//       config.headers.Authorization = store.getters['auth/accessToken']
//       console.log(store.getters['auth/accessToken'])
//       return config
//     },
//     function (error: any) {
//       return Promise.reject(error)
//     }
//   )

//   instance.interceptors.response.use(
//     (response: any) => {
//       return response
//     },
//     async (error: any) => {
//       const {
//         config,
//         response: { status }
//       } = error
//       if (status === 401) {
//         if (error.response.data.code === 'UNAUTHORIZED') {
//           console.log('Interceptors 파일')
//           console.log('UNAUTHORIZED 에러 발생')
//           const originRequest = config
//           const refreshToken = store.getters['auth/refreshToken']

//           const { data } = await instance.post(
//             `/member/refresh-token`,
//             refreshToken
//           )

//           const newAccessToken = data.accessToken
//           await store.commit('/auth/setAccessToken')
//           instance.defaults.headers.Authorization = `Bearer ${newAccessToken}`
//           originRequest.headers.Authorization = `Bearer ${newAccessToken}`

//           return instance(originRequest)
//         }
//       } else if (
//         status === 400 &&
//         error.response.data.error == '유효하지 않은 토큰입니다.'
//       ) {
//         await store.commit('auth/logout')
//         localStorage.removeItem('auth')
//         console.log('removeItem 함')
//         router.push('/park/login')
//       }
//       return Promise.reject(error)
//     }
//   )

//   return instance
// }
// =======
import { setInterceptors } from '@/common/interceptors'

// 토큰을 가져오는 함수 //로그인되어 있는 경우 토큰을, 없는 경우 그냥 axios 요청을 보내는 axios 함수
// state에 저장할 예정. 가져와야 함.
// const isLogin = computed(() => store.getters['auth/isLogin'])
// const accessToken = function getToken() {
//   return computed(() => store.getters['auth/accessToken'])
// }

//accessToken 넣지 않은 일반 axios
const api = axios.create({
  baseURL: process.env.VUE_APP_API,
  timeout: 2000,
  headers: { 'X-Custom-Header': 'waterbell' }
})

export function apiClient(store: any): AxiosInstance {
  return setInterceptors(api, store)
}

// //accessToken을 헤더에 담은 axios
// const apiClient = setInterceptors(api, store) as AxiosInstance

export default { api, apiClient }
