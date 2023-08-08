<template>
  <div id="nav">
    <Home />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'
// import TheHeader from './components/TheHeader.vue'
import Home from './views/Home.vue'
import webSocket from '@/types/webSocket_alarm'
import store from '@/store/index'
// import { useStore } from 'vuex'
export default defineComponent({
  name: 'App',
  components: {
    Home
    // TheHeader
  },
  setup() {
    // const store = useStore()
    onMounted(() => {
      const isLogin = computed(() => store.getters['auth/isLogin'])
      console.log(isLogin.value)
      if (isLogin.value) {
        webSocket.connectWebSocket() //새로고침 시, 웹소켓 연결
      }
    })
  }
})
</script>

<style>
#app {
  text-align: center;
  color: #2c3e50;
  background-color: #f2f7ff;
}

/* 홈페이지 배경색 설정 & 테두리여백 제거 */
* body {
  background-color: #f2f7ff;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 80px;
}

/* 홈페이지 구성요소 세로로 정렬 */
#nav {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  /* justify-content: center; */
  /* align-content: flex-end; */
  padding: 0;
  align-items: stretch;
  width: 100%;

  /* height: 100vh; */
}

/* 헤더상단 환영메시지 정렬을 위해 블럭화 */
#hello-msg {
  display: inline-block;
}
</style>
