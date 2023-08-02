<template>
  <div class="home">
    <!-- 진입화면 v-if="isMainpage"로 처음화면에 온 걸 구분? -->
    <div v-if="isMainPage">
      <!-- 서비스 로고 -->
      <div class="service-logo">
        <img src="../assets/images/waterbell-logo.png" alt="waterbell-logo" />
      </div>
      <!-- 서비스 선택 메뉴 -->
      <div class="service-select">
        <router-link to="/park/dash">
          <div class="select-box park" @click="goToOther">지하주차장</div>
        </router-link>
        <router-link to="/road/dash">
          <div class="select-box road" @click="goToOther">지하차도</div>
        </router-link>
      </div>
      <!-- 관리자 로그인 버튼 -->
      <div class="manager-login">
        <button>관리자 로그인</button>
      </div>
    </div>

    <!-- 서비스 화면 -->
    <div v-if="!isMainPage">
      <TheHeader />
      <hr />
      <router-view></router-view>
      <footer></footer>
    </div>
  </div>
</template>

<script lang="ts">
import TheHeader from '@/components/TheHeader.vue'
import { ref, computed, defineComponent } from 'vue'
import { useStore } from 'vuex'
// import RoadDash from '../underroad/views/roadDashboardView.vue'
function isInMain() {
  const store = useStore()
  let isMainPage = computed(() => store.state.isMainpage)

  return { isMainPage }
}
export default defineComponent({
  name: 'Home',
  components: {
    TheHeader
  },
  setup() {
    return {
      ...isInMain()
    }
    // function goToOther() {
    //   isMainPage = !isMainPage
    // }
    // function goToMain() {
    //   isMainPage = true
    // }
  }
})
</script>
