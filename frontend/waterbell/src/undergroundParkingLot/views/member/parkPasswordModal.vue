<template>
  <div class="container">
    <h3>비밀번호 변경</h3>
    <div class="old-password">
      <label for="oldPassword">현재 비밀번호</label><br />
      <input
        type="password"
        label="oldPassword"
        id="oldPassword"
        v-model="oldPassword"
      />
    </div>
    <div class="new-password">
      <label for="newPassword">새 비밀번호</label><br />
      <input
        type="password"
        label="newPassword"
        id="newPassword"
        v-model="newPassword"
      />
      <!-- <p :class="{ 'p-msg': true, success: validate.password }">
        {{ passMsg }}
      </p> -->
    </div>
    <div class="new-password-confirm">
      <label for="newPasswordConfirm">새 비밀번호 확인</label><br />
      <input
        type="password"
        label="newPasswordConfirm"
        id="newPasswordConfirm"
        v-model="newPasswordConfirm"
      />
      <!-- <p :class="{ 'p-msg': true, success: validate.password }">
        {{ passMsg }}
      </p> -->
    </div>
    <div>
      <p @click="$emit('close', false)">취소</p>
      <p @click="changePassword">저장</p>
    </div>
  </div>
</template>
<script>
import { ref, defineComponent } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkPasswordModal',
  setup() {
    const apiClient = axios.apiClient(store)
    // input 입력 값
    const oldPassword = ref('')
    const newPassword = ref('')
    const newPasswordConfirm = ref('')

    // function closePop() {
    //   this.$emit('close')
    // }

    function changePassword() {
      // 1. 현재 비밀번호를 잘 입력했는지 검증
      apiClient
        .post(`/member/verificationPW`, {
          password: oldPassword.value
        })
        .then((res) => {
          console.log('현재 비밀번호 일치')
          console.log(res)
          // 2. 새 비밀번호와 다시 입력한 값이 맞다면
          console.log(newPassword.value)
          console.log(newPasswordConfirm.value)
          if (
            newPassword.value != '' &&
            newPassword.value == newPasswordConfirm.value
          ) {
            console.log('새 비밀번호 확인값 일치')
            // 3. 비밀번호 재설정
            apiClient
              .post(`/member/reset/password`, {
                oldPw: oldPassword.value,
                newPw: newPassword.value
              })
              .then((res) => {
                alert('비밀번호가 변경되었습니다.')
                // 비밀번호가 변경되고나면 모달창이 사라지는 함수 어떻게하지
                // closePop()
                console.log(res)
              })
              .catch((err) => {
                console.log(err)
              })
          }
        })
        .catch((err) => {
          console.log(err)
          alert('현재 비밀번호가 일치하지 않습니다.')
          return
        })
    }
    return {
      oldPassword,
      newPassword,
      newPasswordConfirm,
      // closePop,
      changePassword
    }
  }
})
</script>
<style></style>
