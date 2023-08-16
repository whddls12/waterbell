<template>
  <div>
    <h2 class="title">로그인</h2>

    <div>
      <div class="includeFinding">
        <div class="loginForm">
          <form @submit.prevent="login">
            <div class="login-user-box">
              <div id="idAndPssBox">
                <div id="idInput" class="id-input">
                  <label for="id" class="input-inner-label">아이디</label>
                  <input
                    class="input-inner-box"
                    type="text"
                    id="id"
                    :value="id"
                    @input="(e) => (id = e.target.value)"
                  />
                </div>
                <div id="passInput" class="id-input">
                  <label for="password" class="input-inner-label"
                    >비밀번호</label
                  >
                  <input
                    class="input-inner-box"
                    type="password"
                    id="password"
                    @input="(e) => (password = e.target.value)"
                    :value="password"
                  />
                </div>
              </div>
              <button type="submit" id="loginBtn" class="login-btn">
                로그인</button
              ><br />
              <div class="search-box">
                <div class="search-content">
                  <router-link to="/park/find/id">아이디 찾기</router-link>
                </div>
                <div>|</div>
                <div class="search-content">
                  <router-link to="/park/find/password"
                    >비밀번호 찾기</router-link
                  >
                </div>
              </div>
            </div>
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
        <div class="find-bt">
          <router-link to="/park/find/id">아이디 찾기</router-link>
          <router-link to="/park/find/password">비밀번호 찾기</router-link>
        </div>
      </div>
    </div>
    <div class="report-empty-box-bottom"></div>
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
      // const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=dIwg6T0yWa9t8y2yMsHJ&redirect_uri=http:/localhost:3000/auth/naver&state=WaterBell`

      //서버배포용
      const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=dIwg6T0yWa9t8y2yMsHJ&redirect_uri=https://i9b101.p.ssafy.io/auth/naver&state=WaterBell`

      window.location.href = url
    }
    const client_id_kakao = '333ed4acdf908937b3480366ff4b1d75'

    //로컬용
    // const redirect_uri = 'http//localhost:3000/auth/kakao'
    //서버 배포용
    const redirect_uri = 'https://i9b101.p.ssafy.io/auth/kakao'
    const kakaoLogin = () => {
      const url = `https://kauth.kakao.com/oauth/authorize?client_id=${client_id_kakao}&redirect_uri=${redirect_uri}&response_type=code`
      window.location.href = url
    }

    return {
      id,
      password,
      login, // login 함수를 템플릿에서 사용할 수 있도록 반환합니다.
      naverLogin,
      kakaoLogin
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
  max-width: 500px; /* Set a maximum width for the form */
  margin: 0 auto; /* Center align the form horizontally */
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

.find-bt {
  display: flex;
  gap: 10px;
}

.idAndPssBox {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 50px;
}

.id-input {
  display: flex;
  width: 500px;
  align-items: center;
  gap: 4px;
  margin-top: 30px;
  margin-bottom: 30px;
}
.input-inner-label {
  flex-grow: 0;
  display: flex;
  width: 90px;
  height: 30px;
  flex-direction: column;
  justify-content: center;
  flex-shrink: 0;
  color: var(--login-color, #666);
  text-align: center;
  /* Body2 */
  /* font-family: Roboto; */
  font-size: 20px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px; /* 100% */
  letter-spacing: 0.25px;
}

.input-inner-box {
  height: 50px;
  flex: 1 0 0;
  border-radius: 12px;
  border: 1px solid rgba(102, 102, 102, 0.35);
  /* width: 450px; */
}
.login-btn {
  display: flex;
  width: 511px;
  height: 54px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 40px;
  background: var(--2, #114cb1);
}
.login-user-box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  /* gap: 68px; */
}

.search-box {
  gap: 10px;
  margin: 10px 0px;
  display: flex;
}
.search-content {
  display: flex;
  width: 137px;
  height: 30px;
  flex-direction: column;
  justify-content: center;
  color: var(--login-color, #666);
  text-align: center;
  /* Body1 */
  font-family: Roboto;
  font-size: 16px;
  font-style: normal;
  font-weight: 400;
  line-height: 24px; /* 150% */
  letter-spacing: 0.5px;
}

.search-content a {
  color: var(--login-color, #666);
}

.login-btn {
  color: #fff;
  text-align: center;
  /* 로그인_btn */
  /* font-family: Roboto; */
  font-size: 20px;
  font-style: normal;
  font-weight: 600;
  line-height: 32px; /* 133.333% */
  letter-spacing: 7px;
}

.report-empty-box-bottom {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  height: 40px;
}
</style>
