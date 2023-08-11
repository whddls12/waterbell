<template>
  <div>
    <div class="container">
      <div>
        <p id="message" v-text="message"></p>
        {{ message.value }}
      </div>
    </div>
  </div>
</template>
<script>
import { defineComponent, onMounted, computed, ref } from 'vue'
import apiModule from '@/types/apiClient'
import store from '@/store/index'
export default defineComponent({
  name: 'parkDashWarningMsgVue',
  setup() {
    const api = apiModule.api
    let message = ref('')
    // let warningMsg = ref()
    let facilityId = computed(() => store.getters['auth/facilityId'])
    console.log('facilityId')
    console.log(facilityId.value)
    const getStatus = () => {
      api.get(`/facilities/${facilityId.value}/status`).then((res) => {
        console.log('status')
        console.log(res)
        const status = res.data
        console.log(status)
        if (status === 'FIRST') {
          message.value = '1차 경고 : 주차장 진입 자제 및 출차 권고'
        } else if (status === 'SECOND') {
          message.value = '2차 경고 : 주차장 진출입 금지'
          console.log(message.value)
        } else if (status === 'DEFAULT') {
          message.value = '정상 상태'
        } else {
          message.value = '차수판 동작 중 : 주차장 진출입 금지'
        }
      })
    }
    onMounted(() => {
      getStatus()
    })

    return { facilityId, getStatus, message }
  }
})
</script>
<style scoped lang="css">
#message {
  color: var(--red, #f02f2f);

  /* 경고문구 */
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 10px; /* 184.615% */
  letter-spacing: 7px;
}
</style>
