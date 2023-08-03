import { Module } from 'vuex'
import { ApartManager, ApartMember, PublicManager } from '@/types/user' // Typo here: ApartManager
import apiClient from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router/index'

const auth: Module<any, any> = {
  namespaced: true,
  state: {
    isLogin: false,
    loginUser: null as null | ApartManager | ApartMember | PublicManager,
    role: null as null | string,
    accessToken: null as string | null,
    refreshToken: null as string | null
  },
  getters: {
    loginUser: (state) => state.loginUser,
    isLogin: (state) => state.isLogin,
    role: (state) => state.role,
    accessToken: (state) => state.accessToken,
    refreshToken: (state) => state.refreshToken
  },
  mutations: {
    setLoginUser(state, user) {
      state.loginUser = user
      state.role = user.role
      state.isLogin = true
    },
    setTokens(state, { accessToken, refreshToken }) {
      state.accessToken = accessToken
      state.refreshToken = refreshToken
    },
    setIsLogin(state, value) {
      state.isLogin = value
    },
    logout(state) {
      state.loginUser = null
      state.isLogin = false
      state.role = null
      state.accessToken = null
      state.refreshToken = null
    }
  },
  actions: {
    async login({ commit }, { loginId, password }) {
      try {
        console.log('실행되는가?')
        const response = await apiClient.post('/member/login', {
          loginId,
          password
        })

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
        // commit('setIslogin', true); // Typo here: setIsLogin
        commit('setTokens', { accessToken, refreshToken })
        commit('setIsLogin', true)

        // 여기에서 auth state에 있는 loginUser를 getter로 가져오고 싶어.
        // 어떻게 해야해?
        const isLogin = store.getters['auth/isLogin']
        if (isLogin) {
          router.push('/park/dash')
        }
      } catch (error) {
        const { data, status, statusText } = error.response
        if (error.response.data.message === 'fail') {
          switch (error.response.data.exception) {
            case '일치하는 회원이 없습니다.':
              alert('존재하지 않는 회원입니다.')
              break
            case '비밀번호가 일치하지 않습니다.':
              alert('비밀번호가 일치하지 않습니다.')
              break
          }
          return
        }
      }
    },

    async logout({ commit }) {
      apiClient.post('/member/logout').then((res) => {
        console.log(res.data)
      })
      commit('logout')
    }
  } // actions 객체의 닫는 중괄호
} // auth 모듈의 닫는 중괄호

export default auth
