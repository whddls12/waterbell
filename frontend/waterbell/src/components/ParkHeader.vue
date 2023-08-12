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
      <!-- 로그인 상태-->
      <div class="header-btn" v-if="accessToken">
        <p id="hello-msg">김동현님 어서오세요!</p>
        <button @click="goToAlarm">알림함</button>
        <button @click="goToMypage">마이페이지</button>
        <button @click="Logout">로그아웃</button>
      </div>
      <!-- 지하차도는 로그인 버튼 불필요 -->
      <div class="header-btn" v-else>
        <button @click="goToLogin">로그인</button>
        <button @click="goToJoin">회원가입</button>
      </div>
    </div>
    <!-- 메뉴 내비게이션바 -->
    <div class="menu-navbar">
      <div class="each-menu">
        <router-link to="/park/dash">대시보드</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/park/report">신고접수</router-link>
      </div>
      <div
        class="each-menu"
        v-bind:style="{ visibility: isManager ? 'visible' : 'hidden' }"
      >
        <router-link to="/park/controll">제어</router-link>
      </div>
      <div
        class="each-menu"
        v-bind:style="{ visibility: isManager ? 'visible' : 'hidden' }"
      >
        <router-link to="/park/systemlog">시스템 로그</router-link>
      </div>
      <div
        class="each-menu"
        v-bind:style="{ visibility: isManager ? 'visible' : 'hidden' }"
      >
        <router-link to="/park/manage">관리</router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import store from '@/store/index'
import { mapGetters } from 'vuex'
import { logout } from '@/types/authFunctionModule'
import router from '@/router/index'

export default defineComponent({
  name: 'ParkHeader',
  components: {},
  computed: {
    ...mapGetters('auth', [
      'loginUser',
      'isLogin',
      'role',
      'accessToken',
      'refreshToken'
    ])
  },
  setup() {
    const isMainPage = computed(() => store.state.isMainpage)
    const router = useRouter()
    const role = computed(() => store.getters['auth/role'])
    const isManager = ref(false)

    function goToMain() {
      store.commit('setIsMainpage', true)
      router.push({ path: '/' })
    }

    function goToAlarm() {
      router.push({ path: '/alarm' })
    }

    function goToMypage() {
      router.push({ path: '/park/mypage' })
    }

    async function Logout() {
      await logout() // 로그아웃 액션을 호출 (액션 이름은 프로젝트에 맞게 수정하세요)
      router.push({ path: '/' }) // 로그아웃 후 리디렉션될 경로
    }

    function goToLogin() {
      router.push({ path: '/park/login' })
    }

    function goToJoin() {
      router.push({ path: '/park/join' })
    }

    const checkRole = async () => {
      if (role.value == 'APART_MANAGER') isManager.value = true
      else isManager.value = false
    }

    onMounted(async () => {
      await checkRole()
    })

    watch(
      () => role.value, // role.value를 반환하는 함수
      async (newRole) => {
        console.log('Role changed:', newRole) // Debugging
        await checkRole()
      }
    )

    return {
      isMainPage,
      goToMain,
      goToAlarm,
      goToLogin,
      goToMypage,
      Logout,
      goToJoin,
      isManager
    }
  }
})
</script>

<style scoped>
.header-top {
  display: flex;
  justify-content: space-between;
  margin-left: 200px;
  margin-right: 100px;
  margin-top: 30px;
  margin-bottom: 10px;
  /* padding-top: 100px; */
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
