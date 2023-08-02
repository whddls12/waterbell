<template>
  <div class="home">
    <!-- 서비스 로고 -->
    <div class="service-logo">
      <img src="@/assets/images/waterbell-logo.png" alt="waterbell-logo" />
    </div>
    <!-- 서비스 선택 메뉴 -->
    <div class="service-select"></div>
    <!-- 관리자 로그인 버튼 -->
    <div class="manager-login"></div>
    <!-- <router-link to="/park/dash">지하주차장</router-link> -->

    <router-link to="/road/dash">지하차도</router-link>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
// import Map from '../underroad/components/dashboard/roadDashNearUnderroad.vue'

export default defineComponent({
  name: 'Home',
  components: {}
})
</script>
