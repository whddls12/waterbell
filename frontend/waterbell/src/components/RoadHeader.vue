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
        <button @click="Logout">로그아웃</button>
      </div>
      <!-- 지하차도는 로그인 버튼 불필요 -->

      <!-- <div class="header-btn" v-else>
        <button @click="goToLogin">로그인</button>
        <button @click="goToJoin">회원가입</button>
      </div> -->
    </div>
    <!-- 메뉴 내비게이션바 -->
    <div class="menu-navbar" :key="isManager.toString()">
      <div class="each-menu">
        <router-link to="/road/dash">현황판</router-link>
      </div>
      <div class="each-menu">
        <router-link to="/road/report">신고접수</router-link>
      </div>
      <div class="each-menu" v-show="isManager">
        <router-link to="/road/control">제어</router-link>
      </div>
      <div class="each-menu" v-show="!isManager"></div>
      <div class="each-menu" v-show="isManager">
        <router-link to="/road/systemlog">센서 내역</router-link>
      </div>
      <div class="each-menu" v-show="!isManager"></div>
      <div class="each-menu" v-show="isManager">
        <router-link to="/road/manage">관리</router-link>
      </div>
      <div class="each-menu" v-show="!isManager"></div>
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
import { computed, defineComponent, ref, onMounted, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import store from '@/store/index'
// import { useStore } from 'vuex'
import { mapGetters } from 'vuex'
import { logout } from '@/types/authFunctionModule'
// import { getUserInfo } from '@/types/getUserInfo'

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
    const isMainPage = computed(() => store.state.isMainpage)
    const router = useRouter()
    const role = computed(() => store.getters['auth/role'])
    const isManager = ref(false)

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

    async function Logout() {
      await logout() // 로그아웃 액션을 호출 (액션 이름은 프로젝트에 맞게 수정하세요)
      router.push({ path: '/road/dash' }) // 로그아웃 후 리디렉션될 경로
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

    const checkRole = async () => {
      if (role.value == 'PUBLIC_MANAGER') isManager.value = true
      else isManager.value = false
      await nextTick()
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
      Logout,
      goToJoin,
      isManager
    }
  }
})
</script>

<style scoped>
/* 헤더 상단 로고와 버튼 포함하는 박스 */
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
  margin-top: 20px;
}

/* 헤더 상단 버튼 모아놓은 박스 */
.header-btn button {
  border: 1px solid #10316b;
  border-radius: 5px;
  width: 90px;
  font-size: 12px;
  padding: 5px 10px;
  margin-right: 5px;
  background-color: #10316b;
  color: white;
  transition: 0.3s;
}

.header-btn button:hover {
  background-color: #31558c;
}

/* 메뉴 내비게이션 바 */
.menu-navbar {
  display: flex;
  justify-content: space-around;
  padding-left: 200px;
  padding-right: 200px;
  background-color: #10316b;
}

/* 각 메뉴 박스 */
.each-menu {
  width: 100px;
  margin: 10px 10px;
}

/* 메뉴 이름 */
a {
  color: white;
  text-decoration: none;
}

/* 지역 선택 부분 */
.select-region {
  display: flex;
  justify-content: center;
  margin: 10px 0px;
}

/* 지역 선택 박스 (시도, 시구군) */
.select-region-box {
  border: 1px solid #939393;
  border-radius: 8px;
  background-color: white;

  width: 100px;
  margin: 10px 10px;
}
/* 이동 버튼 */
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
