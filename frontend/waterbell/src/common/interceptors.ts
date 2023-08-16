// import store from '@/store/index'
import router from '@/router/index'
import { logout } from '@/types/logout'
export function setInterceptors(instance: any, store: any) {
  instance.interceptors.request.use(
    function (config: any) {
      //accessToken을 store.auth 에서 가져오기
      config.headers.Authorization = store.getters['auth/accessToken']

      return config
    },
    function (error: any) {
      return Promise.reject(error)
    }
  )

  instance.interceptors.response.use(
    (response: any) => {
      return response
    },
    async (error: any) => {
      const {
        config,
        response: { status }
      } = error
      if (status == 401) {
        if (error.response.data.code == 'UNAUTHORIZED') {
          console.log('Interceptors 파일')
          console.log('UNAUTHORIZED 에러 발생')
          const originRequest = config
          const refreshToken = store.getters['auth/refreshToken']

          const { data } = await instance.post(`/member/refresh-token`, {
            refreshToken: refreshToken
          })
          if (data.accessToken) {
            const newAccessToken = data.accessToken
            await store.commit('auth/setAccessToken', newAccessToken)
            console.log('새로운 accessToken을 발급받아 저장하였습니다.')
            instance.defaults.headers.Authorization = `Bearer ${newAccessToken}`
            originRequest.headers.Authorization = `Bearer ${newAccessToken}`

            return instance(originRequest)
          } else {
            //토큰 만료
            store.commit('logout')
            console.log('토큰이 만료되어 로그아웃합니다.')
          }
        }
      } else if (
        status == 400 &&
        error.response.data.error == '유효하지 않은 토큰입니다.'
      ) {
        store.commit('logout')
        console.log('로그아웃함')
        router.push('/park/login')
      }
      return Promise.reject(error)
    }
  )

  return instance
}
