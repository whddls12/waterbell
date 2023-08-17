<template>
  <div>
    <div class="big-box">
      <div class="title">회원가입이 완료되었습니다.</div>
      <div>아래 버튼을 눌러 로그인을 진행하세요.</div>

      <div>
        <button class="imgBtn" type="button" @click="naverLogin">
          <img
            src="@/assets/Login&Signup/naver_login.png"
            class="socialLoginBtn"
          />
        </button>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
// import { useRouter } from 'vue-router'
// import store from '@/store/index'
import { useRoute, useRouter } from 'vue-router'
export default defineComponent({
  name: 'socialJoinEnd',

  setup() {
    //api 통신을 위한 axios
    const route = useRoute()
    const router = useRouter()

    const naverLogin = () => {
      //로컬 서버 연결용
      // const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=dIwg6T0yWa9t8y2yMsHJ&redirect_uri=http:/localhost:3000/auth/naver&state=WaterBell`

      //서버배포용
      const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=dIwg6T0yWa9t8y2yMsHJ&redirect_uri=https://i9b101.p.ssafy.io/auth/naver&state=WaterBell`

      window.location.href = url
    }

    return {
      naverLogin
    }
  }
})
</script>
<style scoped lang="css">
h3 {
  color: #000;
  /* 회원가입상자_제목 */
  font-family: score;
  font-size: 30px;
  font-style: normal;
  font-weight: 500;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
}

.big-box {
  display: flex;
  padding: 80px 0px;
  flex-direction: column;
  align-items: center;
  gap: 50px;
}
.p-msg {
  color: red;
  margin-top: 5px;
}
.p-msg.success {
  color: blue;
}
.formBox {
  display: flex;
  max-width: 500px;
  flex-direction: column;
  align-items: center;
  align-self: stretch;
  overflow: hidden;
}

.form {
  width: 100%;
  overflow: hidden;
  margin-bottom: 30px;
}
label,
.text {
  color: #666;
  /* Body2 */
  font-family: score;
  font-size: 20px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px; /* 100% */
  letter-spacing: 0.25px;
}
button {
  display: flex;
  padding: 11px 16px;
  width: 120px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  justify-self: stretch;
  align-self: stretch;
  border-radius: 10px;
  border: 0;
  background: #aaa9a9;
}
.labelBox {
  display: flex;
  width: 500px;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 30px;
  /* border: 1px solid red; */
}
.btnInput {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-self: stretch;

  /* border: 1px solid blue; */
}
.btnInput input {
  margin-right: 10px; /* 오른쪽 마진을 추가하여 button과의 간격을 조절합니다. */
}

.input {
  display: flex;
  height: 40px;
  padding: 16px 17px;
  justify-content: flex-end;
  align-items: center;
  align-self: stretch;

  flex: 1 0 0;
  border-radius: 12px;
  border: 1px solid rgba(102, 102, 102, 0.35);
}

.joinBtn {
  display: flex;
  width: 500px;
  height: 40px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.main-primary {
  border-radius: 40px;
  background: var(--2, #114cb1);
}

.joinBtn-text {
  color: #fff;
  text-align: center;
  /* 로그인_btn */
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 32px; /* 133.333% */
  letter-spacing: 7px;
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
