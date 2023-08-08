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
                <button class="imgBtn" type="button">
                  <img
                    src="@/assets/Login&Signup/naver_login.png"
                    @click="handleNaverCallback"
                    class="socialLoginBtn"
                  />
                </button>
              </div>
              <div>
                <button class="imgBtn" type="button">
                  <img
                    src="@/assets/Login&Signup/kakao_login.png"
                    class="socialLoginBtn"
                    @click="kakaoLogin"
                  />
                </button>
              </div>
            </div>
          </form>
        </div>

        <a href="#none">비밀번호를 잊어버리셨나요?</a>
      </div>
    </div>
  </div>
</template>
<script>
import { defineComponent, ref } from 'vue'
import { useStore } from 'vuex'
import apiModule from '@/types/apiClient'
// import router from '@/router/index'
// import axios from 'axios'
export default defineComponent({
  name: 'parkLogin',
  setup() {
    const api = apiModule.api
    const id = ref('')
    const password = ref('')
    const store = useStore() // 전역 스토어를 가져옵니다.
    const login = () => {
      store.dispatch('auth/memberLogin', {
        loginId: id.value,
        password: password.value
      })
    }

    const uri = '/login/oauth2/code'

    const handleNaverCallback = async () => {
      // URL에서 코드를 가져옵니다.
      api.post(`${uri}/naver`).then((res) => {
        console.log(res.data)
      })

      //   if (!code || !state) return

      //   try {
      //     // 백엔드 서버에 코드를 전달하고 토큰을 요청합니다.
      //     const response = await api
      //       .post('http://localhost:8080/login/oauth2/code/naver', {
      //         code: code,
      //         state: state
      //       })
      //       .then((res) => {
      //         if (res.data.type == 'join') {
      //           router.push('/park/join')
      //         }
      //       })

      //     console.log(response)

      //     // 응답을 처리합니다. 예를 들어 JWT 토큰을 local storage에 저장하는 등의 작업을 수행할 수 있습니다.
      //     // ...
      //   } catch (error) {
      //     console.error('네이버 로그인 처리 중 오류가 발생했습니다:', error)
      //   }
      // }

      // 네이버 콜백 처리를 실행합니다.
      // handleNaverCallback()
      return {
        id,
        password,
        login, // login 함수를 템플릿에서 사용할 수 있도록 반환합니다.
        handleNaverCallback
      }
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
