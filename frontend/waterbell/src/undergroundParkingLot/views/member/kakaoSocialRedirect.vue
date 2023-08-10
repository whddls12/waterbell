<template>
  <div>Redirecting...</div>
</template>

<script>
import { defineComponent, onCreated } from 'vue'

import { useRouter, useRoute } from 'vue-router'
import http from '@/types/http'
import store from '@/store/index'
export default defineComponent({
  name: 'kakaoSocialRedirect',
  setup() {
    const code = new URL(window.location.href).searchParams.get('code')
    console.log(code)

    const router = useRouter()
    const route = useRoute()

    const kvalidateMember = async (code) => {
      const response = await http.get(
        `/oauth2/kakao?code=${code}`,
        {},
        { withCredentials: true }
      )

      if (response.data.type == 'join') {
        //vuex 임시에 이메일 주소 넣어둬야할까?
        router.push('/social-join/extra')
      } else {
        store.dispatch('auth/socialLogin', response.member)
      }

      // console.log(response.data)
      //  = { ...response.data }
    }
    const invalidateMember = async () => {
      alert('로그인 실패')
    }

    const kLogin = async (code) => {
      try {
        await kvalidateMember(code)
        alert('카카오 로그인 성공')
        router.push({ name: 'Home' })
      } catch (err) {
        alert('카카오 로그인 실패')
        console.error(err)
      }
    }
    onCreated(() => {
      if (route.query.code) {
        console.log(route.query.code)
        kLogin(route.query.code)
      }
    })

    return {
      kLogin,
      invalidateMember,
      kvalidateMember
    }
  }
  // create()
  // }
})
</script>

<style></style>
