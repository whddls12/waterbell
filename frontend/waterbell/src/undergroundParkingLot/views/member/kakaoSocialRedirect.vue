<template>
  <div>Redirecting...</div>
</template>

<script>
import { defineComponent, onMounted } from 'vue'

import { useRouter, useRoute } from 'vue-router'
import http from '@/types/http'
import store from '@/store/index'
export default defineComponent({
  name: 'kakaoSocialRedirect',
  setup() {
    const router = useRouter()
    const route = useRoute()

    onMounted(async () => {
      const code = route.query.code
      const state = route.query.state
      if (code && state) {
        try {
          const response = await http.get(
            `/login/oauth2/code/kakao?code=${code}`,
            {},
            { withCredentials: true }
          )
          console.log(response)

          if (response.data.type === 'join') {
            const key = response.data.key
            router.push(`/social-join/extra?key=${key}`)
          } else if (response.data.type === 'login') {
            console.log(response.data)
            await store.dispatch('auth/socialLogin', response.data)
            router('/park/dash')
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

// setup() { // const code = new
URL(window.location.href).searchParams.get('code') // console.log(code) // const
router = useRouter() // const route = useRoute() // const kvalidateMember =
async (code) => { // const response = await http.get( //
`login/oauth2/code/kakao?code=${code}`, // {}, // { withCredentials: true } // )
// console.log(response) // if (response.data.type == 'join') { // //vuex 임시에
이메일 주소 넣어둬야할까? // const key = response.data.key //
console.log('회원가입입니다.') // router.push(`/social-join/extra?key=${key}`)
// } else if (response.data.type == 'login') { //
store.dispatch('auth/socialLogin', response) // router.push('/park/dash') // }
// // console.log(response.data) // // = { ...response.data } // } // const
invalidateMember = async () => { // alert('로그인 실패') // } // const kLogin =
async (code) => { // try { // await kvalidateMember(code) // // router.push({
name: 'Home' }) // } catch (err) { // alert('카카오 로그인 실패') //
console.error(err) // } // } // onMounted(() => { // if (route.query.code) { //
// console.log(route.query.code) // kLogin(route.query.code) // } // }) //
return { // kLogin, // invalidateMember, // kvalidateMember // } // } //
create() // }
