import axios from '@/types/apiClient'
import store from '@/store/index'

const apiClient = axios.apiClient(store)
// const api = axios.api
// const axios = api.api
async function getUserInfo(): Promise<any> {
  const res = await apiClient.get('/member/findMember/token')
  return res.data.member
}

export { getUserInfo }
