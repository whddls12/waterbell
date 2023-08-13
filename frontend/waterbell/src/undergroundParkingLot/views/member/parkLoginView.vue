div
<template lang="">
  <div>
    <h2>로그인</h2>

    <div>
      <div class="includeFinding">
        <div class="loginForm">
          <form @submit.prevent="login">
            <div id="idAndPssBox">
              <div id="idInput">
                <label for="id">아이디</label>
                <input
                  type="text"
                  id="id"
                  :value="id"
                  @input="(e) => (id = e.target.value)"
                  class="in"
                />
              </div>
              <div id="passInput">
                <label for="password">비밀번호</label>
                <input
                  type="password"
                  id="password"
                  @input="(e) => (password = e.target.value)"
                  :value="password"
                  class="in"
                />
              </div>
            </div>
            <button type="submit" id="loginBtn">로그인</button><br />
            <div class="socialBox">
              <img src="@/assets/Login&Signup/OR.png" />
              <div>
                <button class="imgBtn" type="button" @click="naverLogin">
                  <img
                    src="@/assets/Login&Signup/naver_login.png"
                    class="socialLoginBtn"
                  />
                </button>
              </div>
              <div>
                <button class="imgBtn" type="button" @click="kakaoLogin">
                  <img
                    src="@/assets/Login&Signup/kakao_login.png"
                    class="socialLoginBtn"
                  />
                </button>
              </div>
            </div>
          </form>
        </div>

        <a href="#none">비밀번호 찾기?</a>
      </div>
    </div>
  </div>
</template>
<script>
import { defineComponent, ref } from 'vue'
import { useStore } from 'vuex'

// import { useRouter, useRoute } from 'vue-router'
// import http from '@/types/http'

import { memberLogin } from '@/types/authFunctionModule'

export default defineComponent({
  name: 'parkLogin',
  setup() {
    // const api = apiModule.api
    const id = ref('')
    const password = ref('')
    const store = useStore() // 전역 스토어를 가져옵니다.
    // const router = useRouter()
    // const route = useRoute()

    const login = () => {
      memberLogin(id.value, password.value)
    }

    const naverLogin = () => {
      //로컬 서버 연결용
      const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=dIwg6T0yWa9t8y2yMsHJ&redirect_uri=http://localhost:8081/#/auth/naver&state=WaterBell`

      //운영용
      // const url= `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=dIwg6T0yWa9t8y2yMsHJ&redirect_uri=http://i9b101.p.ssafy.io:8080/login/oauth2/code/naver&state=WaterBell`
      console.log('가고 있냐 ')
      window.location.href = url
    }
    const client_id_kakao = '333ed4acdf908937b3480366ff4b1d75'
    const redirect_uri = 'http://localhost:8081/#/auth/kakao'
    const kakaoLogin = () => {
      const url = `https://kauth.kakao.com/oauth/authorize?client_id=${client_id_kakao}&redirect_uri=${redirect_uri}&response_type=code`
      window.location.href = url
    }

    // const nvalidateMember = async (code, state) => {
    //   http
    //     .get('/login/oauth2/code/naver', {
    //       params: {
    //         code: this.$route.query.code,
    //         state: this.$route.query.state
    //       }
    //     })
    //     .then((response) => {
    //       if (response.data.message === 'success') {
    //         if (response.data.type === 'join') {
    //           router.push('/park/join')
    //           // 회원 가입 페이지로 리다이렉션하거나 알림 표시
    //         } else {
    //           // 로그인 처리
    //           router.push('/')
    //         }
    //       } else {
    //         console.error(response.data.exception) // 실패 시 서버에서 보낸 예외 메시지 출력
    //       }
    //     })

    //   // console.log(response.data)
    //   //  = { ...response.data }
    // }
    // const invalidateMember = async () => {
    //   alert('로그인 실패')
    // }

    // const nLogin = async (code, state) => {
    //   try {
    //     await nvalidateMember(code, state)
    //     alert('네이버 로그인 성공')
    //     router.push({ name: 'Home' })
    //   } catch (err) {
    //     alert('네이버 로그인 실패')
    //     console.error(err)
    //   }
    // }

    // // 쿼리스트링으로 code를 받게되면 이를 통해 서버에 요청을 보내는 로직을 수행한다.
    // if (route.query.code && route.query.state) {
    //   nLogin(route.query.code, route.query.state)
    // }

    return {
      id,
      password,
      login, // login 함수를 템플릿에서 사용할 수 있도록 반환합니다.
      naverLogin,
      kakaoLogin
      // nvalidateMember,
      // nLogin,
      // invalidateMember
    }
  }
})
</script>
<style scoped lang="css">
body {
  background-color: #1bbc9b;
}

/* div {
  display: flex;
  width: 1000px;
  padding: 80px 0px;
  flex-direction: column;
  align-items: center;
  gap: 1px;
} */

input {
  height: 30px;
  flex: 1 0 0;
  border-radius: 10px;
  border: 1px solid rgba(102, 102, 102, 0.35);
}

label {
  flex-grow: 1;
}

.in {
  margin-bottom: 10px;
}

#loginBtn {
  border-radius: 40px;
  background: #114cb1;
}
#idAndPassBox {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 50px;
}
/* a {
  text-decoration: none;
  color: #9b9b9b;
  font-size: 12px;
} */

.loginForm {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 68px;
}

.includeFinding {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 26px;
}

.socialBox {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 40px;
}

/* .socialBox button {
  text-align: center;
} */

button img {
  display: block;
  margin: auto;
}

.imgBtn {
  padding: 0;
  border: none;
  background: none;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
