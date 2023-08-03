<template lang="">
  <div>
    <h2>로그인</h2>
    <div>
      <form @submit.prevent="login">
        <input
          type="text"
          id="id"
          :value="id"
          @input="(e) => (id = e.target.value)"
          placeholder="아이디"
          class="in"
        />
        <input
          type="password"
          id="password"
          placeholder="비밀번호"
          @input="(e) => (password = e.target.value)"
          :value="password"
          class="in"
        />
        <input type="submit" id="btn" value="로그인" @click="login()" /><br />
      </form>
      <a href="#none">비밀번호를 잊어버리셨나요?</a>
    </div>
  </div>
</template>
<script>
import { defineComponent, ref } from 'vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'parkLogin',
  setup() {
    const id = ref('')
    const password = ref('')
    const store = useStore() // 전역 스토어를 가져옵니다.
    const login = () => {
      store.dispatch('auth/login', {
        loginId: id.value,
        password: password.value
      })
    }

    return {
      id,
      password,
      login // login 함수를 템플릿에서 사용할 수 있도록 반환합니다.
    }
  }
})
</script>
<style scoped lang="css">
body {
  background-color: #1bbc9b;
}

div {
  margin: auto;
  width: 300px;
  background-color: #eeeff1;
  border-radius: 5px;
  text-align: center;
  padding: 20px;
}

input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border-radius: 5px;
  border: none;
}

.in {
  margin-bottom: 10px;
}

#btn {
  background-color: #1bbc9b;
  margin-bottom: 30px;
  color: white;
}

a {
  text-decoration: none;
  color: #9b9b9b;
  font-size: 12px;
}
</style>
