import { createStore } from 'vuex'
import http from '../types/http'
import { Underroad } from '@/types/underroad'
// import axios from 'axios'
export default createStore({
  state: {
    loginUser: null,
    role: 'none',
    underroadListByGugun: [],
    underroadList: [] as Underroad[],
    nowUnderroad: {},
    location: { lon: '127', lat: '55' }
  },
  getters: {
    underroadList(state) {
      return state.underroadList
    }
  },
  mutations: {
    addUnderroad(state, payload) {
      state.underroadList.push(payload)
    }
  },
  actions: {
    async fetchUnderroads(context: any, payload) {
      if (this.state.role == 'pmanager') {
        await http
          .get('/facilities/pmanager/roads')
          .then((res: { data: any }) => {
            res.data.forEach((element: any) => {
              context.commit('addUnderroad', element)
              console.log(res.data)
            })
          })
      } else {
        await http.get('/facilities/roads').then((res: { data: any }) => {
          res.data.forEach((element: any) => {
            context.commit('addUnderroad', element)
          })
        })
      }
    }
  },

  modules: {}
})
