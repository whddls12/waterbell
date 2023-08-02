import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'

//지하차도 대시보드
import RoadDash from '@/underroad/views/roadDashboardView.vue'
import RoadReport from '@/underroad/views/roadReportView.vue'
import RoadSystemlog from '@/underroad/views/roadSystemLogView.vue'
import RoadManage from '@/underroad/views/roadManageView.vue'
import RoadControl from '@/underroad/views/roadControlView.vue'

//지하차도 신고접수
import roadReportItemVue from '../underroad/components/report/roadReportItem.vue'
import roadReportListVue from '../underroad/components/report/roadReportList.vue'
import roadReportCreateVue from '../underroad/components/report/roadReportCreate.vue'
import roadReportUpdateVue from '../underroad/components/report/roadReportUpdate.vue'

//지하주차장 로그인, 회원가입
import parkLogin from '../undergroundParkingLot/views/parkLoginView.vue'
import parkSignup from '../undergroundParkingLot/views/parkSignupView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/road/dash',
    name: 'RoadDash',
    component: RoadDash
  },
  {
    path: '/road/report',
    name: 'RoadReport',
    component: RoadReport,
    children: [
      {
        path: 'list',
        component: roadReportListVue
      },
      {
        path: 'create',
        component: roadReportCreateVue
      },
      {
        path: 'update',
        component: roadReportUpdateVue
      },
      {
        path: 'item',
        component: roadReportItemVue
      }
    ]
  },

  {
    path: '/road/control',
    name: 'RoadControl',
    component: RoadControl
  },
  {
    path: '/road/manage',
    name: 'RoadManage',
    component: RoadManage
  },
  {
    path: '/road/systemlog',
    name: 'RoadSystemlog',
    component: RoadSystemlog
  },

  //지하주차장 라우터
  //로그인,회원가입
  {
    path: '/park/login',
    name: 'parkLogin',
    component: parkLogin
  },

  {
    path: '/park/signup',
    name: 'parkSignup',
    component: parkSignup
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
