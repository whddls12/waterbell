import { apiClient } from '@/types/apiClient'
import store from '@/store/index'

const axios = apiClient(store)
function getUserInfo(): any {
  axios.get('/member/findMember/token').then((res) => {
    // console.log(res.data)
    // console.log(res.data.member)
    return res.data.member
  })
}

export { getUserInfo }
