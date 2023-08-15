<template>
  <div>
    <div class="container">
      <div id="warningmsgBox">
        <p id="message" :class="messageClass" v-text="message"></p>
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

    const status = ref('')

    const messageClass = computed(() => {
      return status.value == 'DEFAULT' ? 'blue-text' : 'red-text'
    })

    console.log(messageClass.value)
    const getStatus = () => {
      api.get(`/facilities/${facilityId.value}/status`).then((res) => {
        // console.log('status')
        // console.log(res)
        status.value = res.data

        console.log(status.value)
        if (status.value === 'FIRST') {
          message.value = '1차 경고 : 주차장 진입 자제 및 출차 권고'
        } else if (status.value === 'SECOND') {
          message.value = '2차 경고 : 주차장 진출입 금지'
        } else if (status.value === 'DEFAULT') {
          message.value = '정상 상태'
        } else {
          message.value = '차수판 동작 중 : 주차장 진출입 금지'
        }
      })
    }
    onMounted(() => {
      getStatus()
    })

    return { facilityId, getStatus, message, status, messageClass }
  }
})
</script>
<style scoped lang="css">
#warningmsgBox {
  display: flex;
  justify-content: center; /* 가로 중앙 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  height: 50px;
  padding-top: 10px;
}

#message {
  color: red;
  /* 경고문구 */
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 10px; /* 184.615% */
  letter-spacing: 7px;
  align-self: center;
  padding: 5px;
}
.blue-text {
  color: var(--blue, #f02f2f);
}
.red-text {
  color: red;
}

#warningmsgBox {
  display: flex;
}
</style>
