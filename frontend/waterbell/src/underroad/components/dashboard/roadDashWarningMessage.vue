<template lang="">
  <div class="container">
    <div>
      <p id="message" :class="messageClass" v-text="message"></p>
    </div>
  </div>
</template>
<script>
import { defineComponent, onMounted, computed, ref } from 'vue'
import apiModule from '@/types/apiClient'
import store from '@/store/index'
export default defineComponent({
  name: 'roadDashWarnMsgVue',
  setup() {
    const api = apiModule.api
    let message = ref('')
    // let warningMsg = ref()
    let facilityId = computed(() => store.getters['auth/facilityId'])
    let nowUnderroad = ref(store.getters['auth/nowUnderroad'])

    let name = ref(nowUnderroad.value.undergroundRoadName)
    // console.log(nowUnderroad.value)
    const status = ref('')
    const messageClass = computed(() => {
      return status.value == 'DEFAULT' ? 'blue-text' : 'red-text'
    })

    console.log(messageClass)
    const getStatus = () => {
      api.get(`/facilities/${facilityId.value}/status`).then((res) => {
        // console.log('status')
        // console.log(res)
        status.value = res.data

        console.log(status.value)
        if (status.value === 'FIRST') {
          message.value = name.value + ' :  침수 위기 1차 경고 발령 '
        } else if (status.value === 'SECOND') {
          message.value = name.value + ' :  2차 경고 도로 진입에 주의하세요'
        } else if (status.value === 'DEFAULT') {
          message.value = name.value + ' :  정상 상태'
        } else {
          message.value = name.value + ' :  진입 금지'
        }
      })
    }
    onMounted(() => {
      getStatus()
    })

    return {
      facilityId,
      getStatus,
      message,
      status,
      messageClass,
      nowUnderroad,
      name
    }
  }
})
</script>
<style scoped lang="css">
#message {
  /* 경고문구 */
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 10px; /* 184.615% */
  letter-spacing: 7px;
}
.blue-text {
  color: #114cb1;
}

.red-text {
  color: red;
}
</style>
