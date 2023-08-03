import { createStore } from 'vuex'

import { ApartManagaer, ApartMember, PublicManager } from '@/types/user'
import apiClient from '@/types/apiClient'
// import axios from 'axios'
export default createStore({
  state: {
    isLogin: false, //로그인됨 :  true 로그인 안됨 : false
    loginUser: null as null | ApartManagaer | ApartMember | PublicManager, //로그인된 유저 정보
    role: null as null | string,
    accessToken: null as string | null,
    refreshToken: null as string | null
  },
  getters: {
    loginUser(state) {
      return state.loginUser
    },
    isLogin(state) {
      return state.isLogin
    },
    role(state) {
      return state.role
    },
    accessToken(state) {
      return state.accessToken
    },
    refreshToken(state) {
      return state.refreshToken
    }
  },
  mutations: {
    setLoginUser(state, user) {
      state.loginUser = user
      state.role = user.role
      state.isLogin = true
    },
    set_Tokens(state, { accessToken, refreshToken }) {
      state.accessToken = accessToken
      state.refreshToken = refreshToken
    }
  },
  actions: {
    async login({ commit }, { loginId, password }) {
      try {
        const response = await apiClient.post('/member/login', {
          loginId,
          password
        })

        if (response.data.message === 'fail') {
          switch (response.data.exception) {
            case '일치하는 회원이 없습니다.':
              alert('존재하지 않는 회원입니다.')
              break
            case '비밀번호가 일치하지 않습니다.':
              alert('비밀번호가 일치하지 않습니다.')
              break
          }
          return
        }
        let user
        const member = response.data.member
        const accessToken = member.accessToken
        const refreshToken = member.refreshToken

        switch (member.role) {
          case 'APART_MANAGER':
            user = {
              id: member.id,
              loginId: member.loginId,
              role: member.role,
              phone: member.phone,
              facilityId: member.facilityId
            }
            break
          case 'APART_MEMBER':
            user = {
              id: member.id,
              loginId: member.loginId,
              role: member.role,
              phone: member.phone,
              facilityId: member.facilityId,
              name: member.name,
              addressNumber: member.addressNumber
            }
            break
          case 'PUBLIC_MANAGER':
            user = {
              id: member.id,
              loginId: member.loginId,
              role: member.role,
              phone: member.phone,
              sidoId: member.sidoId,
              facilityId: member.facilityId || []
            }
            break
        }

        // 여기서 user 객체를 store에 저장하거나 다른 처리를 할 수 있습니다.
        commit('setLoginUser', user)
        commit('setTokens', { accessToken, refreshToken })
      } catch (error) {
        console.error(error)
      }
    }
  }
})
