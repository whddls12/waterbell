import axios from '@/types/apiClient'
import store from '@/store/index'

const apiClient = axios.apiClient(store)
// const api = axios.api
// const axios = api.api
function getUserInfo(): any {
  apiClient.get('/member/findMember/token').then((res) => {
    // console.log(res.data)
    // console.log(res.data.member)
    return res.data.member
  })
}

export { getUserInfo }
