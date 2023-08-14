import axios from '@/types/apiClient'
import store from '@/store/index'
import { computed } from 'vue'
import router from '@/router/index'
import { connectWebSocket, closeWebSocket } from '@/types/webSocket_alarm'

export async function setAxios() {
  const apiClient = await axios.apiClient(store)
  const api = axios.api
  return { apiClient, api }
}
export async function memberLogin(loginId: string, password: string) {
  try {
    // console.log('실행되는가?')
    const apiClient = (await setAxios()).apiClient
    const api = (await setAxios()).api
    // const router = useRouter()
    const response = await apiClient.post('/member/login', {
      loginId,
      password
    })
    let user
    const member = response.data.member
    const accessToken = member.accessToken
    const refreshToken = member.refreshToken
    console.log(member.role)
    switch (member.role) {
      case 'APART_MANAGER':
      case 'PUBLIC_MANAGER': {
        // user = {
        //   id: member.id,
        //   loginId: member.loginId,
        //   role: member.role,
        //   phone: member.phone,
        //   facilityId: member.facilityId
        // }
        const error = new Error(`NOT_MEMBER`)
        // alert(error.message)
        throw error
      }
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
        store.commit('auth/setFacilityId', member.facilityId)
        break
    }
    //웹소켓 연결
    // 여기서 user 객체를 store에 저장하거나 다른 처리를 할 수 있습니다.
    await store.commit('auth/setTokens', { accessToken, refreshToken })
    await store.commit('auth/setIsLogin', true)
    await store.commit('auth/setRole', member.role)
    console.log(response.data)
    connectWebSocket()
    // 여기에서 auth state에 있는 loginUser를 getter로 가져오고 싶어.
    // 어떻게 해야해?
    const isLogin = computed(() => store.getters['auth/isLogin'])
    console.log(isLogin)
    if (isLogin.value) {
      router.push('/park/dash')
    }
  } catch (error: any) {
    console.log(error)
    // const { data, status, statusText } = error.response
    if (error.message === 'NOT_MEMBER') {
      alert('존재하지 않는 회원입니다.')
    } else if (error.response.data.message === 'fail') {
      switch (error.response.data.exception) {
        case '일치하는 회원이 없습니다.':
          alert('존재하지 않는 회원입니다.2')
          break
        case '비밀번호가 일치하지 않습니다.':
          alert('비밀번호가 일치하지 않습니다.')
          break
      }
    }

    return
  }
}

export async function managerLogin(loginId: string, password: string) {
  try {
    // console.log('실행되는가?')
    const apiClient = (await setAxios()).apiClient
    const api = (await setAxios()).api
    const response = await api.post('/member/login', {
      loginId,
      password
    })
    let user
    const member = response.data.member
    const accessToken = member.accessToken
    const refreshToken = member.refreshToken
    let result = null as null | string
    console.log(member.role)
    switch (member.role) {
      case 'APART_MANAGER': {
        user = {
          id: member.id,
          loginId: member.loginId,
          role: member.role,
          phone: member.phone,
          facilityId: member.facilityId
        }
        store.commit('auth/setFacilityId', member.facilityId)
        result = 'park'
        break
      }
      case 'APART_MEMBER': {
        const error = new Error(`NOT_MEMBER`)
        throw error
      }
      case 'PUBLIC_MANAGER': {
        user = {
          id: member.id,
          loginId: member.loginId,
          role: member.role,
          phone: member.phone,
          sidoId: member.sidoId,
          facilityId: member.facilityId || []
        }
        result = 'road'
        break
      }
    }
    //웹소켓 연결
    // 여기서 user 객체를 store에 저장하거나 다른 처리를 할 수 있습니다.
    // commit('setLoginUser', user)
    // commit('setIslogin', true); // Typo here: setIsLogin
    await store.commit('auth/setTokens', { accessToken, refreshToken })
    await store.commit('auth/setIsLogin', true)
    await store.commit('auth/setRole', member.role)
    console.log(response.data)
    connectWebSocket()
    // 여기에서 auth state에 있는 loginUser를 getter로 가져오고 싶어.
    // 어떻게 해야해?
    const isLogin = computed(() => store.getters['auth/isLogin'])
    // if (isLogin.value) {
    //   router.push('/park/dash')
    return result
  } catch (error: any) {
    console.log(error)
    // const { data, status, statusText } = error.response
    // if (error.message === 'NOT_MEMBER') {
    //   alert('존재하지 않는 회원입니다.')
    // } else if (error.response.data.message === 'fail') {
    //   switch (error.response.data.exception) {
    //     case '일치하는 회원이 없습니다.':
    //       alert('존재하지 않는 회원입니다.2')
    //       break
    //     case '비밀번호가 일치하지 않습니다.':
    //       alert('비밀번호가 일치하지 않습니다.')
    //       break
    //   }
    // }
    return 'fail'
  }
}

export async function logout() {
  try {
    const apiClient = (await setAxios()).apiClient
    const api = (await setAxios()).api
    await apiClient.post('/member/logout').then((res) => {
      // console.log(res.data)
      store.commit('auth/logout')
    })
    await closeWebSocket()
  } catch (error) {
    console.log(error.response)
  }
}
