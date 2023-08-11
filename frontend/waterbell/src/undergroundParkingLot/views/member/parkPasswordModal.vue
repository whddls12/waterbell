<template>
  <div class="container">
    <h3>비밀번호 변경</h3>
    <div class="current-password">
      <label for="current-password">현재 비밀번호</label><br />
      <input type="password" label="current-password" id="current-password" />
      <!-- <p :class="{ 'p-msg': true, success: validate.password }">
        {{ passMsg }}
      </p> -->
    </div>
    <div class="new-password">
      <label for="new-password">새 비밀번호</label><br />
      <input type="password" label="new-password" id="new-password" />
      <!-- <p :class="{ 'p-msg': true, success: validate.password }">
        {{ passMsg }}
      </p> -->
    </div>
    <div class="new-password-confirm">
      <label for="new-password-confirm">새 비밀번호 확인</label><br />
      <input
        type="password"
        label="new-password-confirm"
        id="new-password-confirm"
      />
      <!-- <p :class="{ 'p-msg': true, success: validate.password }">
        {{ passMsg }}
      </p> -->
    </div>
    <p @click="$emit('close', false)">닫기</p>
  </div>
</template>
<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'parkPasswordModal'
})
</script>
<style></style>
