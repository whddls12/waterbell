import store from '@/store/index'
import router from '@/router/index'
export function setInterceptors(instance: any) {
  instance.interceptors.request.use(
    function (config: any) {
      //accessToken을 store.auth 에서 가져오기
      config.headers.Authorization = store.getters['auth/accessToken']
      console.log(store.getters['auth/accessToken'])
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
      if (status === 401) {
        if (error.response.data.code === 'UNAUTHORIZED') {
          console.log('Interceptors 파일')
          console.log('UNAUTHORIZED 에러 발생')
          const originRequest = config
          const refreshToken = store.getters['auth/refreshToken']

          const { data } = await instance.post(
            `/member/refresh-token`,
            refreshToken
          )

          const newAccessToken = data.accessToken
          await store.commit('/auth/setAccessToken')
          instance.defaults.headers.Authorization = `Bearer ${newAccessToken}`
          originRequest.headers.Authorization = `Bearer ${newAccessToken}`

          return instance(originRequest)
        }
      } else if (
        status === 400 &&
        error.response.data.error == '유효하지 않은 토큰입니다.'
      ) {
        await store.commit('auth/logout')
        localStorage.removeItem('auth')
        console.log('removeItem 함')
        router.push('/park/login')
      }
      return Promise.reject(error)
    }
  )

  return instance
}
