import http from '@/types/http'
import store from '@/store/index'
import { computed } from 'vue'
import { closeWebSocket } from '@/types/webSocket_alarm'

export async function logout() {
  try {
    const accessToken = computed(() => store.getters['auth/accessToken'])
    await http
      .post('/member/logout', {
        headers: {
          Authorization: `Bearer ${accessToken.value}`
        }
      })
      .then(() => {
        store.commit('auth/logout')
      })
    await closeWebSocket()
  } catch (error) {
    console.log(error.response)
  }
}
