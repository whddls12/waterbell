<template>
  <div id="nav">
    <!-- 서비스 로고 -->
    <div class="service-logo">
      <img src="./assets/images/waterbell-logo.png" alt="waterbell-logo" />
    </div>
    <!-- 서비스 선택 메뉴 -->
    <div class="service-select"></div>
    <!-- 관리자 로그인 버튼 -->
    <div class="manager-login"></div>
    <!-- <router-link to="/park/dash">지하주차장</router-link> -->
    <router-link to="/road/dash">지하차도</router-link>
    <router-view></router-view>
  </div>
</template>

<script setup>
// import { defineComponent } from 'vue'
import RoadDash from './underroad/views/roadDashboardView.vue'
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
