<template>
  <!-- 회원정보 수정하기 전 -->
  <!-- 비밀번호 확인 창 -->
  <div class="password-check">
    <div class="password-check-title">
      <h1>회원 정보 수정</h1>
    </div>
    <div class="password-check-content">
      <!-- 비밀번호 -->
      <div class="password-check-content-box password">
        <label for="currentPW">비밀번호</label>
        <input type="password" id="currentPW" v-model="currentPW" />
      </div>
      <!-- 버튼 -->
      <div class="password-check-bt">
        <button id="check" @click="isEqual">확인</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent } from 'vue'
import router from '@/router'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkMypage',
  setup() {
    const apiClient = axios.apiClient(store)
    // const api = axios.api
    const currentPW = ref('')

    // 입력을 다 하고 엔터키로 비밀번호 확인이 가능하게끔
    let input = document.getElementById('currentPW')

    input?.addEventListener('keyup', function (event: any) {
      if (event.keyCode === 13) {
        event.preventDefault()
        document.getElementById('check')?.click()
      }
    })

    // 현재 비밀번호를 잘 입력했는지 검증
    function isEqual() {
      apiClient
        .post(`/member/verificationPW`, {
          password: currentPW.value
        })
        .then((res) => {
          router.push({ path: '/park/mypage/update' }) // 비밀번호가 맞으면 수정화면으로
        })
        .catch((err) => {
          console.log(err)
          alert('비밀번호가 일치하지 않습니다.') // alert 창보다는 밑에 메시지를 띄워주는게 낫나..?
        })
    }
    return { currentPW, isEqual }
  }
})
</script>

<style scoped>
.password-check {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.password-check-content-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 20px 0px;
  font-size: 1.5rem;
  gap: 5px;
}

input {
  border-radius: 8px;

  width: 660px;
  height: 60px;
  font-size: 1.5rem;
  padding-left: 10px;
}

.password-check-bt {
  flex-direction: column;
  gap: 40px;
}

#check {
  display: flex;
  width: 660px;
  height: 60px;
  justify-content: center;
  align-items: center;

  border: none;
  border-radius: 40px;
  color: white;
  background-color: #114cb1;
  font-weight: 600;
  font-size: 1.5rem;
  letter-spacing: 7px;
}

#check:hover {
  cursor: pointer;
}
</style>
