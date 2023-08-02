<template lang="">
  <div>
    <h2>Login</h2>
    <div>
      <form action="">
        <input type="text" id="id" placeholder="아이디" class="in" />
        <input
          type="password"
          id="password"
          placeholder="비밀번호"
          class="in"
        />
        <input type="submit" id="btn" value="로그인" /><br />
      </form>
      <a href="#none">비밀번호를 잊어버리셨나요?</a>
    </div>
  </div>
</template>
<script>
import { defineComponent, onMounted, ref } from 'vue'
export default defineComponent({
  name: 'parkLogin'
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
