<template>
  <div>Redirecting...</div>
</template>

<script>
import { defineComponent, onCreated } from 'vue'
import apiModule from '@/types/apiClient'
import router from 'vue-router'
export default defineComponent({
  name: 'naverSocialRedirect',
  setup() {
    const api = apiModule.api
    console.log('이건/')
    onCreated(() => {
      function create() {
        const uri = router.parseQuery
        console.log(uri)
        console.log('이거 실행?')
        const urlParams = new URLSearchParams(window.location.search)
        const code = urlParams.get('code')
        const state = urlParams.get('state')

        if (code && state) {
          // 서버에 인증 코드와 상태를 보내 액세스 토큰을 얻는다.
          api
            .post(`/login/oauth2/code/naver`, { code, state })
            .then((res) => {
              console.log(res.data)
              // 성공적으로 토큰을 받았다면, 이 토큰을 사용하여 사용자 정보를 얻거나 다른 작업을 수행할 수 있다.
              // 필요에 따라 다른 페이지로 리다이렉트할 수도 있다.
            })
            .catch((err) => console.error(err))
        }
      }
    })
    // create()
  }
})
</script>

<style></style>
