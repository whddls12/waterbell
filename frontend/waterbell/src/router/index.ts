import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { computed } from 'vue'
// import apiModule from '@/types/apiClient'
import http from '@/types/http'
import Home from '@/views/Home.vue'
import NotFound from '@/views/NotFound.vue'
import store from '@/store/index'

//관리자 로그인
import managerLogin from '@/views/ManagerLogin.vue'
//지하차도 페이지
import RoadDash from '@/underroad/views/roadDashboardView.vue' // 대쉬보드
import RoadReport from '@/underroad/views/roadReportView.vue' // 신고접수
import RoadSystemlog from '@/underroad/views/roadSystemLogView.vue' // 시스템로그
import RoadManage from '@/underroad/views/roadManageView.vue' // 관리
import RoadControl from '@/underroad/views/roadControlView.vue' // 제어

//지하차도 신고접수
import roadReportItemVue from '../underroad/components/report/roadReportItem.vue'
// import roadReportListVue from '../underroad/components/report/roadReportList.vue'
import roadReportCreateVue from '../underroad/components/report/roadReportCreate.vue'
import roadReportUpdateVue from '../underroad/components/report/roadReportUpdate.vue'
import roadReportVerification from '../underroad/components/report/roadReportVerification.vue'

//지하차도 시스템 로그
import roadMeasureLog from '../underroad/components/systemLog/roadSensorMeasureLog.vue'
import roadDeviceStatusLog from '../underroad/components/systemLog/roadDeviceStatusLog.vue'
import roadDeviceControlLog from '../underroad/components/systemLog/roadDeviceControlLog.vue'

//지하주차장 회원관련(로그인, 회원가입, 마이페이지)
import parkLogin from '../undergroundParkingLot/views/member/parkLoginView.vue'
import parkJoin from '../undergroundParkingLot/views/member/parkSignupView.vue'
import parkJoinAgree from '../undergroundParkingLot/views/member/parkJoinAgree.vue'
import parkMypage from '../undergroundParkingLot/views/member/parkMypageView.vue' // 마이페이지
import parkPasswordCheck from '../undergroundParkingLot/views/member/parkPasswordCheck.vue' // 비밀번호 확인
import parkMypageUpdate from '../undergroundParkingLot/views/member/parkMypageUpdate.vue'
import parkCustom from '../undergroundParkingLot/components/manage/parkMessageAndValueCustom.vue'
import kakaoRedirectPage from '@/undergroundParkingLot/views/member/kakaoSocialRedirect.vue'
import socialJoinExtra from '@/undergroundParkingLot/views/member/socialLoginExtraInfo.vue'
import parkFindId from '@/undergroundParkingLot/views/member/parkFindId.vue'
import parkFindPassword from '@/undergroundParkingLot/views/member/parkFindPassword.vue'
import parkPasswordReset from '@/undergroundParkingLot/views/member/parkPasswordReset.vue'

//지하주차장 페이지
import ParkDash from '@/undergroundParkingLot/views/parkDashboardView.vue' // 대쉬보드
import ParkReport from '@/undergroundParkingLot/views/parkReportView.vue' // 신고접수
import ParkSystemlog from '@/undergroundParkingLot/views/parkSystemLogView.vue' // 시스템로그
import ParkManage from '@/undergroundParkingLot/views/parkManageView.vue' // 관리
import ParkControl from '@/undergroundParkingLot/views/parkControlView.vue' // 제어
import parkManageMember from '@/undergroundParkingLot/components/manage/parkResidentList.vue'
//지하주차장 시스템 로그
import parkMeasureLog from '../undergroundParkingLot/components/systemLog/parkSensorMeasureLog.vue'
import parkAlarmLog from '../undergroundParkingLot/components/systemLog/parkDeviceAlarmLog.vue'
import parkDeviceStatusLog from '../undergroundParkingLot/components/systemLog/parkDeviceStatusLog.vue'
import parkDeviceControlLog from '../undergroundParkingLot/components/systemLog/parkDeviceControlLog.vue'

// 지하주차장 신고접수
import parkReportItem from '../undergroundParkingLot/components/report/parkReportListItem.vue'
import parkReportCreate from '../undergroundParkingLot/components/report/parkCreateReport.vue'
import parkReportUpdate from '../undergroundParkingLot/components/report/parkReportUpdate.vue'

//알림함
import alarmBox from '@/alarm/alarmBox.vue'
import alarmDetail from '@/alarm/AlarmDetail.vue'
// const api = apiModule.api
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: [
    {
      path: '',
      redirect: { name: 'Home' }
    },
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    // 사용자가 의도하지 않은 URL로 접근 시 뷰 라우터에 404 오류 페이지로 리다이렉션 설정
    {
      path: '/notFound',
      name: 'notFound',
      component: NotFound
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/notFound'
    },
    //매니저 로그인
    { path: '/manager/login', name: 'managerLogin', component: managerLogin },
    // 지하차도 라우터
    {
      path: '/road/dash',
      name: 'RoadDash',
      component: RoadDash
    },
    // 신고접수
    {
      path: '/road/report',
      name: 'RoadReport',
      component: RoadReport
    },
    {
      path: '/road/report/create',
      name: 'roadReportCreate',
      component: roadReportCreateVue
    },
    {
      path: '/road/report/verification',
      component: roadReportVerification
    },
    {
      path: '/road/report/update/:report_id',
      component: roadReportUpdateVue
    },
    {
      path: '/road/report/:report_id/detail',
      component: roadReportItemVue
    },
    {
      path: '/road/control',
      name: 'RoadControl',
      component: RoadControl,
      meta: { allowedRoles: ['PUBLIC_MANAGER'] }
    },
    {
      path: '/road/manage',
      name: 'RoadManage',
      component: RoadManage,
      meta: { allowedRoles: ['PUBLIC_MANAGER'] }
    },
    {
      path: '/road/systemlog',
      name: 'RoadSystemlog',
      component: RoadSystemlog,
      meta: { allowedRoles: ['PUBLIC_MANAGER'] },
      children: [
        {
          path: 'measureLog',
          name: 'roadMeasureLog',
          component: roadMeasureLog,
          meta: { allowedRoles: ['PUBLIC_MANAGER'] }
        },
        {
          path: 'statusLog',
          name: 'roadDeviceStatusLog',
          component: roadDeviceStatusLog,
          meta: { allowedRoles: ['PUBLIC_MANAGER'] }
        },
        {
          path: 'controlLog',
          name: 'roadDeviceControlLog',
          component: roadDeviceControlLog,
          meta: { allowedRoles: ['PUBLIC_MANAGER'] }
        }
      ]
    },

    //지하주차장 라우터

    //로그인,회원가입
    {
      path: '/park/login',
      name: 'parkLogin',
      component: parkLogin
    },
    {
      path: '/park/find/id',
      name: 'parkFindId',
      component: parkFindId
    },
    {
      path: '/park/find/password',
      name: 'parkFindPassword',
      component: parkFindPassword
    },
    {
      path: '/park/find/password/reset',
      name: 'parkPasswordReset',
      component: parkPasswordReset
    },

    {
      path: '/park/join',
      name: 'parkJoin',
      component: parkJoin,
      children: [
        { path: '/agree', name: 'joinAgree', component: parkJoinAgree }
      ]
    },

    {
      path: '/auth/naver',
      name: 'NaverAuth',
      component: () =>
        import('@/undergroundParkingLot/views/member/naverSocialRedirect.vue') // 여기서 '@/views/NaverAuth.vue'는 실제 리다이렉트 후 렌더링할 컴포넌트 경로입니다.
      // beforeEnter: async (to, from, next) => {
      //   const code = to.query.code as string
      //   const state = to.query.state as string
      //   console.log(code)
      //   if (code && state) {
      //     try {
      //       const response = await http.post(`/login/oauth2/code/naver`, {
      //         code,
      //         state
      //       })
      //       console.log(code)
      //       console.log(response.data)
      //       if (response.data.type == 'join') {
      //         console.log('join')
      //         next('/social-join/extra')
      //       } else if (response.data.type == 'login') {
      //         next('/park/dash')
      //소셜로그인 dispatch 만들어서 쓰기. 혹은 소셜로그인 코드 여기에 쓰기 .

      // 이 부분에서 서버로부터 받은 토큰을 저장하거나 필요한 작업을 수행할 수 있습니다.
      // 그 후에 원하는 라우트로 리다이렉션합니다.
      // next('/success') // 성공 페이지로 이동
      //   } catch (err) {
      //     console.error(err)
      //     next('/error') // 에러 페이지로 이동
      //   }
      // } else {
      //   next('/error') // 에러 페이지로 이동
      // }
    },
    {
      path: '/auth/kakao',
      name: 'kakaoRediretPage',
      component: kakaoRedirectPage
    },
    {
      path: '/social-join/extra',
      name: 'socialJoinExtraInfo',
      component: socialJoinExtra
    },

    // 회원정보 조회(마이페이지)
    {
      path: '/mypage',
      name: 'parkMypage',
      component: parkMypage
    },
    // 회원정보 수정 시 비밀번호 확인 창
    {
      path: '/mypage/passwordCheck',
      name: 'parkPasswordCheck',
      component: parkPasswordCheck
    },
    // 회원정보 수정
    {
      path: '/mypage/update',
      name: 'parkMypageUpdate',
      component: parkMypageUpdate
    },

    {
      path: '/park/dash',
      name: 'ParkDash',
      component: ParkDash,
      meta: { allowedRoles: ['APART_MANAGER', 'APART_MEMBER'] }
    },
    // 지하주차장 신고접수
    {
      path: '/park/report',
      name: 'ParkReport',
      component: ParkReport,
      meta: { allowedRoles: ['APART_MANAGER', 'APART_MEMBER'] }
    },
    {
      path: '/park/report/create', // 신고접수 등록
      name: 'ParkReportCreate',
      component: parkReportCreate,
      meta: { allowedRoles: ['APART_MANAGER', 'APART_MEMBER'] }
    },
    {
      path: '/park/report/update/:report_id', // 신고접수 업데이트
      component: parkReportUpdate,
      meta: { allowedRoles: ['APART_MANAGER', 'APART_MEMBER'] }
    },
    {
      path: '/park/report/:report_id/detail', // 신고접수 상세
      component: parkReportItem,
      meta: { allowedRoles: ['APART_MANAGER', 'APART_MEMBER'] }
    },
    {
      path: '/park/systemlog',
      name: 'ParkSystemlog',
      component: ParkSystemlog,
      meta: { allowedRoles: ['APART_MANAGER'] },
      children: [
        {
          path: 'measureLog',
          name: 'parkMeasure',
          component: parkMeasureLog,
          meta: { allowedRoles: ['APART_MANAGER'] }
        },
        {
          path: 'alarmLog',
          name: 'parkAlarmLog',
          component: parkAlarmLog,
          meta: { allowedRoles: ['APART_MANAGER'] }
        },
        {
          path: 'statusLog',
          name: 'parkDeviceStatusLog',
          component: parkDeviceStatusLog,
          meta: { allowedRoles: ['APART_MANAGER'] }
        },
        {
          path: 'controlLog',
          name: 'parkDeviceControlLog',
          component: parkDeviceControlLog,
          meta: { allowedRoles: ['APART_MANAGER'] }
        }
      ]
    },
    {
      path: '/park/manage',
      name: 'ParkManage',
      component: ParkManage,
      meta: { allowedRoles: ['APART_MANAGER'] },
      children: [
        {
          path: 'custom',
          name: 'parkCustom',
          component: parkCustom,
          meta: { allowedRoles: ['APART_MANAGER'] }
        },
        {
          path: 'member',
          name: 'manageMember',
          component: parkManageMember,
          meta: { allowedRoles: ['APART_MANAGER'] }
        }
      ]
    },
    {
      path: '/park/control',
      name: 'ParkControl',
      component: ParkControl,
      meta: { allowedRoles: ['APART_MANAGER'] }
    },
    //알림함
    {
      path: '/alarm',
      name: 'Alarm',
      component: alarmBox
    },
    {
      path: '/alarm/detail/:alarm_id',
      name: 'AlarmDetail',
      component: alarmDetail
    }
  ]
})

// 뒤로가기로 페이지 이동했을 경우
// isMainPage 변수를 바꿔줌 -> 진입화면 / 서비스화면 렌더링에 활용
router.beforeEach((to, from, next) => {
  if (to.path === '/') {
    store.commit('setIsMainpage', true)
  } else {
    store.commit('setIsMainpage', false)
  }

  // 권한 검사
  const role = computed(() => store.getters['auth/role'])
  const allowedRoles = to.meta.allowedRoles as string[]
  if (allowedRoles && !allowedRoles.includes(role.value)) {
    // Role mismatch. Redirect to a different page or block the navigation.
    alert('잘못된 접근입니다.')
    next('/')
  }

  next()
})

export default router
