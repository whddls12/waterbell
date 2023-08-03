<template>
  <div class="header-container">
    <div class="header-top">
      <!-- 서비스 로고 -->
      <div class="header-logo">
        <img
          src="../assets/images/waterbell-logo.png"
          alt="waterbell-logo"
          width="200"
          height="50"
          @click="goToMain"
        />
      </div>
      <!-- 각종 버튼들 (로그인 로그아웃 회원가입 알림함 마이페이지) -->
      <div class="header-btn">
        <!-- 로그아웃 상태-->
        <button>로그인</button>
        <button>회원가입</button>
        <!-- 로그인 상태-->
        <button>알림함</button>
        <p id="hello-msg">@@님 어서오세요!</p>
        <button>마이페이지</button>
        <button>로그아웃</button>
      </div>
    </div>
    <!-- 메뉴 내비게이션바 -->
    <div class="menu-navbar">
      <div class="each-menu">
        <router-link to="/road/dash">대시보드</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/report">신고접수</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/control">제어</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/systemlog">시스템 로그</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/manage">관리</router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'TheHeader',
  components: {},
  setup() {
    const store = useStore()
    const isMainPage = computed(() => store.state.isMainpage)
    const router = useRouter()

    function goToMain() {
      store.commit('setIsMainpage', true)
      router.push({ path: '/' })
    }

    return {
      isMainPage,
      goToMain
    }
  }
})
</script>

<style scoped>
.header-top {
  display: flex;
  justify-content: space-between;

  margin-bottom: 50px;
}

.header-btn {
  display: flex;
}

.menu-navbar {
  display: flex;
  justify-content: center;
  background-color: #10316b;
  padding-right: 20px;
  padding-left: 20px;
}

.each-menu {
  width: 100px;
  margin: 10px 10px;
}

a {
  color: white;
  text-decoration: none;
}
</style>
