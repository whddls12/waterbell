import axios from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router/index'

const apiClient = axios.apiClient(store)
// const api = axios.api
// const axios = api.api
async function getUserInfo(): Promise<any> {
  const res = await apiClient
    .get('/member/findMember/token')
    .then(async (res) => {
      console.log(res)
      if (res.data.message == 'success') {
        return res.data.member
      } else {
        const { data } = await apiClient.post(`/member/refresh-token`, {
          refreshToken: store.getters['auth/refreshToken']
        })
        if (data.accessToken) {
          store.commit('auth/setAccessToken', data.accessToken)
          apiClient.defaults.headers.Authorization = `Bearer ${data.accessToken}`
          return await getUserInfo()
        } else {
          store.commit('logout')
          console.log('토큰이 만료되어 로그아웃합니다.')
          router.push('/park/login')
        }
      }
    })
}

export { getUserInfo }
