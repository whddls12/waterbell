<template lang="">
  <div class="main">
    <div class="controll1">사이렌 제어</div>
    <div class="warning1">작동 중</div>
    <div class="buttons">
      <button class="button1" @click="onAction">동작</button>
      <button class="button2" @click="onRelease">해제</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { useStore } from 'vuex'
import { mapGetters } from 'vuex'
import apiModule from '@/types/apiClient'

// import http from '@/types/http'

export default defineComponent({
  name: 'ParkControlWallCom',
  computed: {
    ...mapGetters('auth', [
      'loginUser',
      'isLogin',
      'role',
      'accessToken',
      'refreshToken'
    ])
  },
  setup() {
    const apiClient = apiModule.apiClient
    const store = useStore()
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    const onAction = () => {
      apiClient
        .post('/notification/apartManager/activation')
        .then((response) => {
          console.log(response.data)
        })
        .catch((error) => {
          console.error('Error sending the request:', error)
        })

      apiClient
        .post(`/control/manager/${facility_id}/ON`)
        .then((response) => {
          console.log(response.data)
        })
        .catch((error) => {
          console.log(error)
        })
    }

    const onRelease = () => {
      apiClient
        .post('/notification/apartManager/deactivation')
        .then((response) => {
          console.log(response.data)
        })
        .catch((error) => {
          console.error('Error sending the request:', error)
        })

      apiClient
        .post(`/control/manager/${facility_id}/OFF`)
        .then((response) => {
          console.log(response.data)
        })
        .catch((error) => {
          console.log(error)
        })
    }

    return {
      apiClient,
      store,
      onAction,
      onRelease
    }
  },

  methods: {}
})
</script>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 400px; /* 이전 크기의 절반 */
  height: 180.5px; /* 이전 크기의 절반 */
  background: #f2f7ff;
  box-shadow: 0px 2px 15px rgba(0, 0, 0, 0.17); /* 크기가 줄어드므로 그림자도 조절 */
  border-radius: 6px; /* 반경도 절반으로 줄임 */
  margin-right: 200px;
}

.controll1 {
  width: 368px;
  height: 59px;
  text-align: center;
  color: black;
  font-size: 15px;
  font-family: Roboto;
  font-weight: 500;
  line-height: 28px;
  letter-spacing: 0.25px;
  word-wrap: break-word;
}

.warning1 {
  width: 300px;
  height: 80px;
  text-align: center;
  color: black;
  font-size: 25px;
  font-family: Roboto;
  font-weight: 600;
  line-height: 48px;
  letter-spacing: 0.25px;
  word-wrap: break-word;
}

.buttons {
  display: flex;
  justify-content: center;
  width: 100%;
  padding: 0 10px; /* 이전 패딩의 절반 */
}

.button1 {
  display: flex;
  width: 120px;
  height: 40px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  border-radius: 10px;
  background: var(--unnamed, #f86262);
  margin-right: 10px;
  color: #fff;
  margin-bottom: 20px;
}

.button2 {
  display: flex;
  width: 120px;
  height: 40px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  border-radius: 10px;
  background: var(--1, #10316b);
  margin-left: 10px;
  color: #fff;
  margin-bottom: 20px;
}
</style>
