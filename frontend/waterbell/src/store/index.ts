import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
// import http from '../types/http'
import auth from './auth/auth'
// import axios from '@/types/apiClient'
// import store from '@/store/index'

// const apiClient = axios.apiClient(store)
// const api = axios.api
export default createStore({
  state: {
    location: { lon: '127', lat: '55' }, //현재 위치(gps)
    tmpUnderroad: {} as { id: string }, //임시 선택 지하차도
    isMainpage: true,
    isPark: true,
    camClient1: null as null | string,
    camClient2: null as null | string,
    actionTriggered: false,
    UactionTriggered: false
  },
  getters: {
    tmpUnderroad(state) {
      return state.tmpUnderroad
    },
    location(state) {
      return state.location
    },

    camClient1(state) {
      return state.camClient1
    },
    camClient2(state) {
      return state.camClient2
    }
  },
  mutations: {
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
    setIspark(state, value) {
      state.isPark = value
    },
    setCamClient1(state, value) {
      state.camClient1 = value
    },

    setCamClient2(state, value) {
      state.camClient2 = value
    },

    SET_ACTION_TRIGGERED(state, payload) {
      state.actionTriggered = payload
    },

    SET_ACTION_UTRIGGERED(state, payload) {
      state.UactionTriggered = payload
    }
  },
  actions: {
    //아래 action을 페이지 로딩 되자마자 띄울 수 있도록 할 것.
<<<<<<< HEAD
    // async fetchUnderroads(context: any, payload) {
    //   // try {
    //   //로그인된 유저의 자격이 지하차도 매니저이면 지하차도 목록을 list로 생성할 것.
    //   //지하차도 리스트가 새로고침돼도 개수 유지되도록 리셋
    //   await context.commit('auth/resetList')
    //   const isLogin = context.rootGetters['auth/isLogin'] //로그인 여부
    //   const role = context.rootGetters['auth/role'] //유저의 역할
    //   //pmanager일 때
    //   if (isLogin && role == 'PUBLIC_MANAGER') {
    //     let member = null as any //멤버 if문 내 전역 잡기
    //     await apiClient //현재 로그인된 토큰으로 멤버 정보 가져오기
    //       .get('/member/findMember/token')
    //       .then((res) => {
    //         // console.log(res.data.member)의 결과
    //         //{
    //         //     "id": 7,
    //         //     "loginId": "pManager1",
    //         //     "role": "PUBLIC_MANAGER",
    //         //     "phone": "01088742165",
    //         //     "accessToken": "",
    //         //     "refreshToken": null,
    //         //     "sidoId": 7,
    //         //     "facilityId": []
    //         // }
    //         member = res.data.member
    //         console.log('여기는여?')
    //       })
    //       .then(() => {
    //         http.get('/facilities/roads').then((res: { data: any }) => {
    //           res.data.forEach(async (element: any) => {
    //             // console.log(element)
    //             //결과
    //             //   {
    //             //     "gugunName": "대덕구",
    //             //     "sido": {
    //             //         "sidoName": "대전",
    //             //         "id": 3
    //             //     },
    //             //     "gugunId": 3,
    //             //     "underroads": [
    //             //         {
    //             //             "id": 10,
    //             //             "gugun": {
    //             //                 "id": 3,
    //             //                 "gugunName": "대덕구",
    //             //                 "sido": {
    //             //                     "sidoName": "대전",
    //             //                     "id": 3
    //             //                 }
    //             //             },
    //             //             "firstFloodMessage": "침수경고 현재수위:15mm 이상",
    //             //             "activation_message": "진입금지",
    //             //             "deactivation_message": "진입금지 해제",
    //             //             "firstAlarmValue": 15,
    //             //             "secondAlarmValue": 30,
    //             //             "hubIp": "172.20.10.8",
    //             //             "status": "SECOND",
    //             //             "undergroundRoadName": "대전IC지하차도",
    //             //             "latitude": 36.3599119,
    //             //             "longitude": 127.4480964,
    //             //             "apart": false
    //             //         }
    //             //     ]
    //             // }
    //             //element의 sido.id와 member의 sidoId가 일치하는 경우만
    //             console.log(element.sido.id)
    //             console.log(member.sidoId)
    //             if (element.sido.id == member.sidoId) {
    //               console.log('여기 들어오냐')
    //               context.commit('auth/setUnderroadbygugun', element)
    //               for (const underroad of element.underroads) {
    //                 console.log(underroad)
    //                 context.commit('auth/setUnderroadList', underroad) //지하차도 세팅하는거 로그인 때도 넣기
    //               }
    //             }
    //           })
    //         })
    //       })
    //     //아니라면(비회원) 지하차도 전부 리스트로 받아오기(시군구별 + 전체)
    //   } else {
    //     await http.get('/facilities/roads').then((res: { data: any }) => {
    //       res.data.forEach((element: any) => {
    //         if (element.underroads.length != 0) {
    //           context.commit('auth/setUnderroadbygugun', element)
    //           for (const e of element.underroads) {
    //             context.commit('auth/setUnderroadList', e)
    //           }
    //         }
    //       })
    //     })
    //   }
    // }
=======

    async fetchUnderroads(context: any, payload) {
      // try {
      //로그인된 유저의 자격이 지하차도 매니저이면 지하차도 목록을 list로 생성할 것.
      //지하차도 리스트가 새로고침돼도 개수 유지되도록 리셋
      await context.commit('auth/resetList')

      const isLogin = context.rootGetters['auth/isLogin'] //로그인 여부
      const role = context.rootGetters['auth/role'] //유저의 역할

      //pmanager일 때
      if (isLogin && role == 'PUBLIC_MANAGER') {
        let member = null as any //멤버 if문 내 전역 잡기
        await apiClient //현재 로그인된 토큰으로 멤버 정보 가져오기
          .get('/member/findMember/token')
          .then((res) => {
            // console.log(res.data.member)의 결과
            //{
            //     "id": 7,
            //     "loginId": "pManager1",
            //     "role": "PUBLIC_MANAGER",
            //     "phone": "01088742165",
            //     "accessToken": "",
            //     "refreshToken": null,
            //     "sidoId": 7,
            //     "facilityId": []
            // }

            member = res.data.member

            console.log('여기는여?')
          })
          .then(() => {
            http.get('/facilities/roads').then((res: { data: any }) => {
              res.data.forEach(async (element: any) => {
                // console.log(element)
                //결과
                //   {
                //     "gugunName": "대덕구",
                //     "sido": {
                //         "sidoName": "대전",
                //         "id": 3
                //     },
                //     "gugunId": 3,
                //     "underroads": [
                //         {
                //             "id": 10,
                //             "gugun": {
                //                 "id": 3,
                //                 "gugunName": "대덕구",
                //                 "sido": {
                //                     "sidoName": "대전",
                //                     "id": 3
                //                 }
                //             },
                //             "firstFloodMessage": "침수경고 현재수위:15mm 이상",
                //             "activation_message": "진입금지",
                //             "deactivation_message": "진입금지 해제",
                //             "firstAlarmValue": 15,
                //             "secondAlarmValue": 30,
                //             "hubIp": "172.20.10.8",
                //             "status": "SECOND",
                //             "undergroundRoadName": "대전IC지하차도",
                //             "latitude": 36.3599119,
                //             "longitude": 127.4480964,
                //             "apart": false
                //         }
                //     ]
                // }

                //element의 sido.id와 member의 sidoId가 일치하는 경우만
                console.log(element.sido.id)
                console.log(member.sidoId)
                if (element.sido.id == member.sidoId) {
                  console.log('여기 들어오냐')
                  context.commit('auth/setUnderroadbygugun', element)
                  for (const underroad of element.underroads) {
                    console.log(underroad)
                    context.commit('auth/setUnderroadList', underroad) //지하차도 세팅하는거 로그인 때도 넣기
                  }
                }
              })
            })
          })

        //아니라면(비회원) 지하차도 전부 리스트로 받아오기(시군구별 + 전체)
      } else {
        await http.get('/facilities/roads').then((res: { data: any }) => {
          res.data.forEach((element: any) => {
            if (element.underroads.length != 0) {
              context.commit('auth/setUnderroadbygugun', element)
              for (const e of element.underroads) {
                context.commit('auth/setUnderroadList', e)
              }
            }
          })
        })
      }
    },
    triggerAction({ commit }) {
      commit('SET_ACTION_TRIGGERED', true)
    },
    resetActionTrigger({ commit }) {
      commit('SET_ACTION_TRIGGERED', false)
    },
    UtriggerAction({ commit }) {
      commit('SET_ACTION_UTRIGGERED', true)
    },
    UresetActionTrigger({ commit }) {
      commit('SET_ACTION_UTRIGGERED', false)
    }
>>>>>>> 7b90db06ec8c1b5beb596b5857e8fb86a9476fc3
  },

  modules: {
    auth
  },
  plugins: [createPersistedState()]
})
