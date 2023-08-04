import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import http from '../types/http'
import { Underroad } from '@/types/underroad'
import auth from './auth/auth'
// import axios from 'axios'

export default createStore({
  state: {
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
    },
    setUnderroadList(state, payload) {
      state.underroadList.push(payload)
    },
    resetList(state) {
      state.underroadList = []
      state.underroadListByGugun = []
    }
  },
  actions: {
    //아래 action을 페이지 로딩 되자마자 띄울 수 있도록 할 것.

    async fetchUnderroads(context: any, payload) {
      try {
        await context.commit('resetList')
        const authGetters = context.rootGetters['auth/loginUser']
        const isLogin = context.rootGetters['auth/isLogin']
        const role = context.rootGetters['auth/role']
        const user = context.rootGetters['auth/loginUser']
        if (isLogin && role == 'PUBLIC_MANAGER') {
          context.commit('setUnderroadbygugun', user.facilities) //지하차도 세팅하는거 로그인 때도 넣기
        } else {
          await http.get('/facilities/roads').then((res: { data: any }) => {
            res.data.forEach((element: any) => {
              //구군에 따른 지하차도 리스트 세팅
              context.commit('setUnderroadbygugun', element.underroads)
              for (const el of element.underroads) {
                context.commit('setUnderroadList', el)
                console.log(el)
              }
            })
          })
        }
      } catch (error) {
        console.log('지하차도를 가져오는데 에러가 발생했습니다.')
      }
    }
  },

  modules: {
    auth
  },
  plugins: [createPersistedState()]
})
