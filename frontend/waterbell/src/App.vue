<template>
  <div id="nav">
    <Home />
    <!-- <TheHeader /> -->
    <!-- <div class="router-view-container">
      <router-view></router-view>
    </div> -->
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
template {
  background-color: #f2f7ff;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  flex-basis: 100%;
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

* body {
  background-color: #f2f7ff;
  margin: 0;
  padding: 0;
}

div {
  margin: 0;
}

#nav {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  padding: 0;
  align-items: stretch;
  width: 100%;

  /* height: 100vh; */
}
#hello-msg {
  display: inline-block;
}
</style>
