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
    <div v-else>
      <RoadHeader />
      <!-- <ParkHeader /> -->
      <div class="router-view-container">
        <router-view></router-view>
      </div>
      <footer></footer>
    </div>
  </div>
</template>

<script lang="ts">
import RoadHeader from '@/components/RoadHeader.vue'
// import ParkHeader from '@/components/ParkHeader.vue'
import { computed, defineComponent } from 'vue'
import { useStore } from 'vuex'
// import RoadDash from '../underroad/views/roadDashboardView.vue'

export default defineComponent({
  name: 'Home',
  components: {
    RoadHeader
    // ParkHeader
  },
  setup() {
    const store = useStore()
    const isMainPage = computed(() => store.state.isMainpage)

    function goToOther() {
      store.commit('setIsMainpage', false)
    }

    return {
      isMainPage,
      goToOther
    }
  }
})
</script>

<style>
.home {
  width: 60%;
}
/* 서비스 선택 메뉴 */
.service-select {
  display: flex;
  /* 가운데 정렬 */
  align-content: center;
  justify-content: center;
}

/* 지하주차장, 지하차도 박스 */
.select-box {
  border: 1px solid #939393;
  border-radius: 8px;
  background-color: white;

  width: 500px;
  height: 300px;
  margin: 20px;
}

/* 메뉴 화면 (대시보드, 신고접수, 제어, 시스템로그, 관리) */
.router-view-container {
  box-sizing: border-box; /* 콘텐츠 영역이 아닌 테두리 기준으로 박스 크기 설정 */
  padding: 10px 20px;
  display: flex;
  justify-content: center;
  width: 100%;
  height: 100%;
  /* overflow: auto; */
  background-color: white;
}
</style>
