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

/* 자주 쓰이는 컴포넌트 */

.container {
  border-radius: 10px;
  background: var(--unnamed, #f2f7ff);
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
}

/* 각 대시보드 박스들 */
.dash-box {
  display: flex;
  flex-direction: column;
  padding: 10px;
}
/* 대시보드 제목 */
.dash-box-title {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 4px; /* 아이콘과 제목 사이 간격 */
}
/* 대시보드 내용 */
.dash-box-content {
  border: 1px solid #cdd1de;
  background-color: white;
  border-radius: 10px;
  padding: 20px;
}
</style>
