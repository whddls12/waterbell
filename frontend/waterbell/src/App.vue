<template>
  <div id="nav">
    <Home />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
// import TheHeader from './components/TheHeader.vue'
import Home from './views/Home.vue'
import webSocket from './types/webSocket_alarm'
import { watch } from 'vue'
import { useStore } from 'vuex'
export default defineComponent({
  name: 'App',
  components: {
    Home
    // TheHeader
  },
  setup() {
    const store = useStore()
    const alarm_socket = ref<WebSocket | null>(null)

    //로그인 상태를 관찰하다가 변화하면 웹소켓 연결하기
    watch(
      () => store.state.isLoggedIn,
      (newValue) => {
        //로그인 감지
        if (newValue) {
          webSocket.connectWebSocket(alarm_socket.value)

          //로그아웃 감지
        } else {
          webSocket.closeWebSocket(alarm_socket.value)
        }
      }
    )
  }
})
</script>

<style>
/* template {
  background-color: #f2f7ff;
} */
#app {
  text-align: center;
  color: #2c3e50;
  background-color: #f2f7ff;
}

#nav {
  display: flex-inline;
  flex-direction: column;
  justify-content: center;
  align-content: flex-end;
}

/* 홈페이지 배경색 설정 & 테두리여백 제거 */
* body {
  background-color: #f2f7ff;
  margin: 0;
  padding: 0;
}

/* 홈페이지 구성요소 세로로 정렬 */
#nav {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: inherit;
  padding: 0;
}

/* 헤더상단 환영메시지 정렬을 위해 블럭화 */
#hello-msg {
  display: inline-block;
}
</style>
