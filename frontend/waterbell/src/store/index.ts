import { createStore } from 'vuex'
// import createPersistedState from 'vuex-persistedstate'
import http from '../types/http'
import { Underroad } from '@/types/underroad'
// import axios from 'axios'
export default createStore({
  state: {
    loginUser: null, //로그인 유저 정보
    isLogin: false, //로그인 상태 true/false
    role: 'none', //로그인 유저의 권한
    underroadListByGugun: [] as any, //지하차도 리스트(구군에 따라 리스트로 되어 있음)
    underroadList: [] as Underroad[], //지하차도 전부 리스트 //map에서 뿌릴 때 썼음
    nowUnderroad: {} as {
      id: string
      name: string
      lat: string
      lon: string
      status: string
      firstMsg: string
      secondMsg: string
      releaseMsg: string
    },
    location: { lon: '127', lat: '55' }, //현재 위치(gps)
    tmpUnderroad: {} as { id: string }, //임시 선택 지하차도
    isMainpage: true
  },
  getters: {
    underroadList(state) {
      return state.underroadList
    },
    underroadListByGugun(state) {
      return state.underroadListByGugun
    },
    tmpUnderroad(state) {
      return state.tmpUnderroad
    },
    location(state) {
      return state.location
    },

    nowUnderroad(state) {
      return state.nowUnderroad
    },
    loginUser(state) {
      return state.loginUser
    },
    isLogin(state) {
      return state.isLogin
    }
  },
  mutations: {
    setUnderroadbygugun(state, payload) {
      state.underroadListByGugun.push(payload)
    },
    //state에 tmplocation에 임시 위치 저장하기
    // setTmplocation(state, payload) {
    //   state.tmplocation = payload
    // },
    setIsMainpage(state, value) {
      state.isMainpage = value
    },
    setTmpUnderroad(state, payload) {
      state.tmpUnderroad = payload
    }
  },
  actions: {
    //아래 action을 페이지 로딩 되자마자 띄울 수 있도록 할 것.
    async fetchUnderroads(context: any, payload) {
      if (this.state.role == 'pmanager') {
        await http
          .get('/facilities/pmanager/roads')
          .then((res: { data: any }) => {
            res.data.forEach((element: any) => {
              //구군에 따른 지하차도 리스트 세팅
              context.commit('setUnderroadbygugun', element)
            })
          })
      } else {
        await http.get('/facilities/roads').then((res: { data: any }) => {
          res.data.forEach((element: any) => {
            //구군에 따른 지하차도 리스트 세팅

            context.commit('setUnderroadbygugun', element)
          })
        })
      }
    }
  },

  modules: {}
})
