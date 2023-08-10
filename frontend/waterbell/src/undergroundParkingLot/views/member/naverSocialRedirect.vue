<template>
  <div>Redirecting...</div>
</template>

<script>
import { defineComponent, onCreated } from 'vue'

import { useRouter, useRoute } from 'vue-router'
import store from '@/store/index'
import http from '@/types/http'
export default defineComponent({
  name: 'naverSocialRedirect',
  setup() {
    const router = useRouter()
    const route = useRoute()
    // const api = apiModule.api
    // console.log('이건/')
    // onCreated(() => {
    //   function create() {
    //     const uri = router.parseQuery
    //     console.log(uri)
    //     console.log('이거 실행?')
    //     const urlParams = new URLSearchParams(window.location.search)
    //     const code = urlParams.get('code')
    //     const state = urlParams.get('state')

    //     if (code && state) {
    //       // 서버에 인증 코드와 상태를 보내 액세스 토큰을 얻는다.
    //       api
    //         .post(`/login/oauth2/code/naver`, { code, state })
    //         .then((res) => {
    //           console.log(res.data)
    //           // 성공적으로 토큰을 받았다면, 이 토큰을 사용하여 사용자 정보를 얻거나 다른 작업을 수행할 수 있다.
    //           // 필요에 따라 다른 페이지로 리다이렉트할 수도 있다.
    //         })
    //         .catch((err) => console.error(err))
    //     }
    //   }
    // })

    const nvalidateMember = async (code, state) => {
      await http
        .get(
          `/oauth2/naver?code=${code}&state=${state}`,
          {},
          { withCredentials: true }
        )
        .then((response) => {
          if (response.data.type == 'join') {
            //vuex 임시에 이메일 주소 넣어둬야할까?
            const key = response.data.key
            router.push(`/social-join/extra?key=${key}`)
          } else {
            store.dispatch('auth/socialLogin', response.member)
          }
        })
    }
    const invalidateMember = async () => {
      alert('로그인 실패')
    }

    const nLogin = async (code, state) => {
      try {
        await nvalidateMember(code, state)
      } catch (err) {
        alert('네이버 로그인 실패')
        console.error(err)
      }
    }
    onCreated(() => {
      if (route.query.code && route.query.state) {
        console.log(route.query.code)
        nLogin(route.query.code, route.query.state)
      }
    })

    return {
      nLogin,
      invalidateMember,
      nvalidateMember
    }
    // create()
  }
})
</script>

<style></style>
