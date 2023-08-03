import { createStore } from 'vuex'
// import createPersistedState from 'vuex-persistedstate'
import http from '../types/http'
// import axios from 'axios'
export default createStore({
  state: {
    loginUser: null,
    role: 'none',
    underroadListByGugun: [],
    underroadList: [],
    nowUnderroad: {},
    location: { lon: '127', lat: '55' },
    tmplocation: {},
    isMainpage: true
  },
  getters: {
    underroadList(state) {
      return state.underroadList
    },
    underroadListByGugun(state) {
      return state.underroadListByGugun
    },
    tmplocation(state) {
      return state.tmplocation
    },
    location(state) {
      return state.location
    }
  },
  mutations: {
    setUnderroadbygugun(state, payload) {
      state.underroadListByGugun.push(payload)
    },
    //state에 tmplocation에 임시 위치 저장하기
    setTmplocation(state, payload) {
      state.tmplocation = payload
    },
    setIsMainpage(state, value) {
      state.isMainpage = value
    }
  },
  actions: {
    //아래 action을 페이지 로딩 되자마자 띄울 수 있도록 할 것.
    async fetchUnderroads(context, payload) {
      if (this.state.role == 'pmanager') {
        await http.get('/facilities/pmanager/roads').then((res) => {
          res.data.forEach((element) => {
            //구군에 따른 지하차도 리스트 세팅
            context.commit('setUnderroadbygugun', element)
            console.log('res.data')
            console.log(res.data)
          })
        })
      } else {
        await http.get('/facilities/roads').then((res) => {
          res.data.forEach((element) => {
            //구군에 따른 지하차도 리스트 세팅
            context.commit('setUnderroadbygugun', element)
            console.log('res.data')
            console.log(res.data)
          })
        })
      }
    }
  },
  modules: {}
})
//# sourceMappingURL=index.js.map
