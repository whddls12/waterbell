<template>
  <div>Redirecting...</div>
</template>

<script>
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'naverSocialRedirect'

  // created() {
  //     // const urlParams = new URLSearchParams(window.location.search);
  //     // const code = urlParams.get('code');
  //     // const state = urlParams.get('state');

  //     if (code && state) {
  //       // 서버에 인증 코드와 상태를 보내 액세스 토큰을 얻는다.
  //       axios.post(`/login/oauth2/code/naver`, { code, state })
  //         .then(res => {
  //           console.log(res.data);
  //           // 성공적으로 토큰을 받았다면, 이 토큰을 사용하여 사용자 정보를 얻거나 다른 작업을 수행할 수 있다.
  //           // 필요에 따라 다른 페이지로 리다이렉트할 수도 있다.
  //         })
  //         .catch(err => console.error(err));
  //     }
  // }
})
</script>

<style></style>
