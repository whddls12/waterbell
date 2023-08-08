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
        <span id="hello-msg">김동현님 어서오세요!</span>
        <button @click="goToAlarm">알림함</button>
        <button>마이페이지</button>
        <button @click="logout">로그아웃</button>
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
        <router-link to="/road/dash">대시보드</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/report">신고접수</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/controll">제어</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/systemlog">시스템 로그</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/manage">관리</router-link>
      </div>
    </div>
    <!-- 지역 선택 바 -->
    <div class="select-region">
      <div class="select-region-box">시도</div>
      <div class="select-region-box">시구군</div>
      <div>
        <button class="go-selected-btn">이동</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { mapGetters } from 'vuex'
// import apiModule from '@/types/apiClient'

// import roadControlView from '../underroad/views/roadControlView.vue'
// import roadDashboardView from '../underroad/views/roadDashboardView.vue'
// import roadManageView from '../underroad/views/roadManageView.vue'
// import roadReportView from '../underroad/views/roadReportView.vue'
// import roadSystemLogView from '../underroad/views/roadSystemLogView.vue'

export default defineComponent({
  name: 'RoadHeader',
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
    const store = useStore()
    const isMainPage = computed(() => store.state.isMainpage)
    const router = useRouter()
    // const apiClient = apiModule.apiClient

    function goToMain() {
      store.commit('setIsMainpage', true)
      router.push({ path: '/' })
    }

    function goToAlarm() {
      router.push({ path: '/alarm' })
    }

    function goToLogin() {
      router.push({ path: '/park/login' })
    }

    function goToJoin() {
      router.push({ path: '/park/join' })
    }

    function logout() {
      store.dispatch('auth/logout') // 로그아웃 액션을 호출 (액션 이름은 프로젝트에 맞게 수정하세요)
      router.push({ path: '/park/login' }) // 로그아웃 후 리디렉션될 경로
    }

    // const loginUser = () => {
    //   apiClient.get('/member/findMember/token').then((res) => {
    //     console.log(res)
    //     return res
    //   })
    // }
    // const name = loginUser()
    // console.log(name)
    // const loginUser = computed(() => store.getters['auth/loginUser'])
    return {
      isMainPage,
      goToMain,
      goToAlarm,
      goToLogin,
      logout,
      goToJoin
    }
  },
  methods: {}
})
</script>

<style scoped>
.header-top {
  display: flex;
  justify-content: space-between;
  margin-left: 200px;
  margin-right: 200px;
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

.select-region {
  display: flex;
  justify-content: center;
}

.select-region-box {
  border: 1px solid #939393;
  border-radius: 8px;
  background-color: white;

  width: 100px;
  margin: 10px 10px;
}

.go-selected-btn {
  display: block;
  border: 1px solid #10316b;
  border-radius: 8px;
  color: white;
  background-color: #10316b;

  width: 50px;
  margin: 10px 10px;
}
</style>
