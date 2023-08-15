<template>
  <div class="home">
    <!-- 진입화면 v-if="isMainpage"로 처음화면에 온 걸 구분? -->
    <div v-if="isMainPage" class="page-start">
      <!-- 서비스 로고 -->
      <div class="service-logo">
        <img
          class="logo"
          src="@/assets/images/waterbell-logo.png"
          alt="waterbell-logo"
        />
      </div>
      <!-- 서비스 선택 메뉴 -->
      <div class="service-select">
        <div
          class="select-box park"
          @click="goToOther1"
          :class="{ 'orange-border': role == 'APART_MANAGER' }"
        >
          <div class="select-box-text">
            <div class="service-title">지하주차장</div>
            <div class="service-feature">
              지하주차장 침수 수위 측정 및 자동 알림
            </div>
            <div class="service-feature">
              지하주차장 침수 대응 차수판 원격 제어
            </div>
          </div>
          <div class="select-box-image-park-one">
            <img
              class="service-image-park-one"
              src="@/assets/images/mainpage/지하주차장1.jpg"
              width="400px"
              height="300px"
              alt="지하주차장1"
            />
          </div>
          <div class="select-box-image-park-two">
            <img
              class="service-image-park-two"
              src="@/assets/images/mainpage/지하주차장2.jpg"
              width="400px"
              height="300px"
              alt="지하주차장2"
            />
          </div>
        </div>
        <div
          class="select-box road"
          @click="goToOther2"
          :class="{ 'orange-border': role == 'PUBLIC_MANAGER' }"
        >
          <div class="select-box-text">
            <div class="service-title">지하차도</div>
            <div class="service-feature">
              지하차도 침수 수위 측정 및 자동 알림
            </div>
            <div class="service-feature">지하차도 침수 시 진입 금지 안내</div>
          </div>
          <div class="select-box-image-road">
            <img
              class="service-image-road"
              src="@/assets/images/mainpage/지하차도1.png"
              width="400px"
              height="300px"
              alt="지하차도1"
            />
          </div>
        </div>
      </div>
      <button class="button-managerLogin" @click="moveToLogin" v-show="!role">
        <img class="icon" src="@/assets/images/icon.png" />
        <div class="labelWrap">
          <div class="label">관리자 로그인</div>
        </div>
      </button>
    </div>

    <!-- 서비스 화면 -->
    <div class="service" v-else>
      <ParkHeader v-if="isPark" />
      <RoadHeader v-else />
      <div class="router-view-container">
        <router-view class="router-view"></router-view>
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
import { useRouter } from 'vue-router'
import { getClosestLocation } from '@/types/getMylocation'
import { getUserInfo } from '@/types/getUserInfo'
// import RoadDash from '../underroad/views/roadDashboardView.vue'

export default defineComponent({
  name: 'Home',
  components: {
    RoadHeader,
    ParkHeader
  },
  setup() {
    const router = useRouter()
    const store = useStore()
    const isMainPage = computed(() => store.state.isMainpage)
    const isPark = computed(() => store.state.isPark)
    const underroadList = computed(() => store.getters['auth/underroadList'])
    // const facilityId = computed(() => store.getters['auth/facilityId'])
    const isLogin = computed(() => store.getters['auth/isLogin'])
    const role = computed(() => store.getters['auth/role'])
    let firstEnter = computed(() => store.getters['auth/firstEnter']) //한번이라도 지하차도에 들어간 적이 있는가.
    //지하주차장
    async function goToOther1() {
      const shouldNavigateToDashboard = await moveToMemberLogin()
      store.commit('setIspark', true)
      if (shouldNavigateToDashboard) {
        await setParkFacilityId()
        // window.location.href = '/park/dash'
        router.push('/park/dash')
      }
    }

    //로그인 유저의 역할을 파악하고, 아파트주민 또는 관리자인 경우만
    //facilityId 설정하기

    function moveToMemberLogin() {
      if (role.value == 'PUBLIC_MANAGER') {
        alert('지하차도 관리인은 로그아웃 후 다시 시도해주세요.')
        // router.push('/road/dash')
        // window.location.href = '/road/dash'
        return false
      } else if (
        !(role.value == 'APART_MEMBER' || role.value == 'APART_MANAGER')
      ) {
        router.push('/park/login')
        return false
      } else {
        return true
      }
    }

    async function setParkFacilityId() {
      if (
        isLogin.value &&
        (role.value == 'APART_MEMBER' || role.value == 'APART_MANAGER')
      ) {
        const member = await getUserInfo()
        store.commit('auth/setFacilityId', member.facilityId)
        //--------------------------------------------------------------------------------
        //여기에 nowUnderroad null 만들고 현재 보고 있는 아파트 정보 넣기.
        // 혹은 nowUnderroad를 nowFacility의 의미로 사용?
        //----------------------------------------------------------------------------------------

        //만약 firstEnter가 false로 바뀌어있다면, 다시 지하차도로 접근했을 때, 최근 위치로 잡도록
        //firstEnter 값 바꿔주기
        if (!firstEnter.value) {
          store.commit('auth/switchFirstEnter', true)
        }
      }
    }

    //지하차도 페이지로 이동.
    //접속자의 현재 위치를 파악하고, 가장 가까운 facilityId를 잡기.
    async function goToOther2() {
      //vuex에 firstEnter를 boolean 으로 잡아
      //true이면 현재위치 파악, 가까운 위치 잡으면서 firstEnter를 false로 바꾸기
      //false이면 기존 facilityId 설정하기
      await setRoadFacilityId()

      store.commit('setIsMainpage', false)
      store.commit('setIspark', false)
      router.push('/road/dash')
      // window.location.href = '/road/dash'
    }

    async function setRoadFacilityId() {
      //지하차도 첫 진입여부 확인 후, 첫진입이면 가장 근처 지하차도 위치로 세팅하기
      console.log(firstEnter.value)
      if (firstEnter.value) {
        const result = await getClosestLocation(underroadList.value)
        console.log(result)
        store.commit('auth/setFacilityId', result.id)
        // console.log(result)
        store.commit('auth/setNowUnderroad', result)
        store.commit('auth/switchFirstEnter', false)
      }
    }

    function moveToLogin() {
      router.push('/manager/login')
    }

    return {
      isMainPage,
      isPark,
      goToOther1,
      goToOther2,
      moveToLogin,
      moveToMemberLogin,
      setParkFacilityId,
      setRoadFacilityId,
      role
    }
  }
})
</script>

<style>
.service {
  width: 100%;
}

.home {
  width: 100%;
}

/* 진입화면 워터벨 로고 가운데 정렬 */
.service-logo {
  display: flex;
  justify-content: center;
  width: 907px;
  height: 215px;
  padding: 10px;
  justify-content: center;
  align-items: center;
  display: inline-flex;
  margin-top: 150px;
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
  position: relative;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0px 8px 20px 0px rgba(0, 0, 0, 0.25);

  width: 500px;
  height: 350px;
  margin: 20px;
  overflow: hidden;
}

.select-box:hover {
  cursor: pointer;
}

.select-box-text {
  padding: 20px;
  text-align: start;
}

.service-title {
  font-size: 30px;
  font-weight: 700;
}

.service-feature {
  padding-left: 5px;
}

.select-box-image-park-one {
  position: absolute;
  left: -120px;
  top: 120px;
}

.select-box-image-park-two {
  position: absolute;
  left: 280px;
}

.select-box-image-road {
  position: absolute;
  left: 130px;
  top: 120px;
}

.service-image-park-one {
  transform: rotate(-0.3deg);
  opacity: 0.5;
}

.service-image-park-two {
  transform: rotate(-1.358deg);
  opacity: 0.5;
}
/* 메뉴 화면 (대시보드, 신고접수, 제어, 시스템로그, 관리) */
.header {
  display: flex;
  width: 100%;
}

.router-view-container {
  box-sizing: border-box; /* 콘텐츠 영역이 아닌 테두리 기준으로 박스 크기 설정 */
  display: flex;
  justify-content: center;
  background-color: white;
  margin-left: 208px;
  margin-right: 210px;
  margin-top: 50px;
  margin-bottom: 80px;

  width: 1100px;
  padding: 10px 0px;
  flex-direction: column;
  align-items: center;
  gap: 13px;
  border-radius: 10px;
  background: var(--unnamed, #fff);
}

router-view {
  flex-flow: 1;
}

.button-managerLogin {
  border: 0;
  width: 200px;
  height: 50px;
  padding-left: 16px;
  padding-right: 16px;
  padding-top: 11px;
  padding-bottom: 11px;
  background: #ffa132;
  border-radius: 10px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  display: inline-flex;
}

.icon {
  width: 26px;
  height: 26px;
  position: relative;
}

.labelWrap {
  justify-content: center;
  align-items: center;
  display: flex;
}

.label {
  text-align: center;
  color: white;
  font-size: 18px;
  font-family: Roboto;
  font-weight: 600;
  line-height: 32px;
  word-wrap: break-word;
}

.router-view {
  margin-left: 50px;
  margin-right: 50px;
}

.page-start {
  height: 900px;
}

.orange-border {
  /* border: 10px solid #ffa132; */
  box-shadow: 0px 0px 10px 0px #ffa132;
}
</style>
