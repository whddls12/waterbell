<template>
  <div>Redirecting...</div>
</template>

<script>
import { defineComponent, onMounted } from 'vue'
import { connectWebSocket } from '@/types/webSocket_alarm'
import { useRouter, useRoute } from 'vue-router'
import store from '@/store/index'
import http from '@/types/http'
export default defineComponent({
  name: 'naverSocialRedirect',
  setup() {
    const router = useRouter()
    const route = useRoute()

    onMounted(async () => {
      const code = route.query.code
      const state = route.query.state
      if (code && state) {
        try {
          const response = await http.get(
            `/login/oauth2/code/naver?code=${code}&state=${state}`,
            {},
            { withCredentials: true }
          )

          if (response.data.type === 'join') {
            const key = response.data.key
            router.push(`/social-join/extra?key=${key}`)
          } else if (response.data.type === 'login') {
            console.log(response.data)
            await store.dispatch('auth/socialLogin', response)
            connectWebSocket()
          }
        } catch (error) {
          console.error(error)
          // alert('네이버 로그인 실패')
        }
      }
    })

    // create()
  }
})
</script>

<style></style>
