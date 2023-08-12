<template>
  <div class="header-container">
    <div>
      <img
        src="../assets/images/waterbell-logo.png"
        alt="waterbell-logo"
        width="200"
        height="50"
      />
    </div>
    <div v-if="!isLogin">
      <!-- 로그아웃 상태일 때 보여줄 메뉴 -->
      <button @click="moveToLogin()">로그인</button>
      <button>회원가입</button>
    </div>
    <!-- 로그인 상태일 때 보여줄 메뉴 -->
    <div v-if="isLogin">
      <router-link to="/alarmBox">
        <button>알림함</button>
      </router-link>
      <p id="hello-msg" v-if="loginUser">
        {{ loginUser.value.name }}님 어서오세요!
      </p>
      <button>마이페이지</button>
      <button @click="logout()">로그아웃</button>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from 'vue'
import router from '../router/index'
import axios from '@/types/apiClient'
import store from '@/store/index'
import { logout } from '@/types/authFunctionModule'
export default defineComponent({
  name: 'TheHeader',
  setup() {
    // const accessToken = computed(() => store.getters['auth.accessToken'])
    const apiClient = axios.apiClient(store)
    const api = axios.api

    const loginUser = () => {
      apiClient.get('/member/findMember/token').then((res) => {
        console.log(res)
        return res
      })
    }
    const name = loginUser()
    // console.log(name)
    const isLogin = computed(() => store.getters['auth/isLogin'])

    const moveToLogin = () => {
      router.push('/park/login')
      location.reload
    }
    // const name = () => {
    //   if (isLogin) return loginUser.value.name
    //   return ''
    // }
    const logout = () => {
      logout()
    }

    const isUnderroad = () => {
      const now = window.location.pathname
      console.log(now)
    }
    onMounted(() => {
      loginUser()
    })
    return { store, loginUser, isLogin, moveToLogin, logout, isUnderroad }
  }
})
</script>

<style></style>
