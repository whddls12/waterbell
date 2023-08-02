<template>
  <div class="header-container">
    <img
      src="../assets/images/waterbell-logo.png"
      alt="waterbell-logo"
      width="200"
      height="50"
      @click="goToMain"
    />
    <!-- 로그아웃 상태일 때 보여줄 메뉴 -->
    <button>로그인</button>
    <button>회원가입</button>
    <!-- 로그인 상태일 때 보여줄 메뉴 -->
    <button>알림함</button>
    <p id="hello-msg">@@님 어서오세요!</p>
    <button>마이페이지</button>
    <button>로그아웃</button>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'TheHeader',
  components: {},
  setup() {
    const store = useStore()
    const isMainPage = computed(() => store.state.isMainpage)
    const router = useRouter()

    function goToMain() {
      store.commit('setIsMainpage', true)
      router.push({ path: '/' })
    }

    return {
      isMainPage,
      goToMain
    }
  }
})
</script>

<style></style>
