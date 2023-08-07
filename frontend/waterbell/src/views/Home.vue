<template>
  <div class="home">
    <!-- 진입화면 v-if="isMainpage"로 처음화면에 온 걸 구분? -->
    <div v-if="isMainPage" class="page-start">
      <!-- 서비스 로고 -->
      <div class="service-logo">
        <img src="@/assets/images/waterbell-logo.png" alt="waterbell-logo" />
      </div>
      <!-- 서비스 선택 메뉴 -->
      <div class="service-select">
        <router-link to="/park/dash">
          <div class="select-box park" @click="goToOther1">지하주차장</div>
        </router-link>
        <router-link to="/road/dash">
          <div class="select-box road" @click="goToOther2">지하차도</div>
        </router-link>
      </div>
      <!-- 관리자 로그인 버튼 -->
      <div class="manager-login">
        <button>관리자 로그인</button>
      </div>
    </div>

    <!-- 서비스 화면 -->
    <div v-else>
      <ParkHeader v-if="isPark" />
      <RoadHeader v-else />
      <div class="router-view-container">
        <router-view></router-view>
      </div>
      <footer></footer>
    </div>
  </div>
</template>

<script lang="ts">
import RoadHeader from '@/components/RoadHeader.vue'
import ParkHeader from '@/components/ParkHeader.vue'
import { computed, defineComponent } from 'vue'
import { useStore } from 'vuex'
// import RoadDash from '../underroad/views/roadDashboardView.vue'

export default defineComponent({
  name: 'Home',
  components: {
    RoadHeader,
    ParkHeader
  },
  setup() {
    const store = useStore()
    const isMainPage = computed(() => store.state.isMainpage)
    const isPark = computed(() => store.state.isPark)

    function goToOther1() {
      store.commit('setIsMainpage', false)
      store.commit('setIspark', true)
    }

    function goToOther2() {
      store.commit('setIsMainpage', false)
      store.commit('setIspark', false)
    }

    return {
      isMainPage,
      isPark,
      goToOther1,
      goToOther2
    }
  }
})
</script>

<style>
.service-select {
  display: flex;
  align-content: center;
  justify-content: center;
}

.select-box {
  border: 1px solid #939393;
  background-color: white;

  width: 500px;
  height: 300px;
  margin: 20px;
}

.header {
  display: flex;
}

.router-view-container {
  box-sizing: border-box; /* 콘텐츠 영역이 아닌 테두리 기준으로 박스 크기 설정 */
  padding: 10px 20px;
  display: flex;
  justify-content: center;
  /* width: 100%; */
  /* height: 100%; */
  overflow: auto; /* prevent components from going out of bounds */
  background-color: white;
  margin-left: 200px;
  margin-right: 200px;
  margin-top: 50px;
  min-height: 600px;
  overflow-y: auto; /* 세로 스크롤이 필요한 경우 자동으로 스크롤 생성 */
}

router-view {
  flex-flow: 1;
}
</style>
