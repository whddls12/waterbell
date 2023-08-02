<template>
  <div class="home">
    <!-- 진입화면 v-if="isMainpage"로 처음화면에 온 걸 구분? -->
    <div v-if="isMainPage" class="page-start">
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
    <div v-else>
      <TheHeader />
      <hr />
      <div class="router-view-container">
        <router-view></router-view>
      </div>
      <footer></footer>
    </div>
  </div>
</template>

<script lang="ts">
import TheHeader from '@/components/TheHeader.vue'
import { computed, defineComponent } from 'vue'
import { useStore } from 'vuex'
// import RoadDash from '../underroad/views/roadDashboardView.vue'

export default defineComponent({
  name: 'Home',
  components: {
    TheHeader
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
  padding: 10px 20px;
  display: flex;
  justify-content: center;
  width: 100%;
  height: 100%;
  overflow: auto; /* prevent components from going out of bounds */
  background-color: white;
}

router-view {
  flex-flow: 1;
}
</style>
