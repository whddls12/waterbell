<template>
  <div>
    <div class="container">차수판 동작여부 및 경고메시지</div>
  </div>
</template>
<script>
import { defineComponent, onMounted, computed } from 'vue'
import apiModule from '@/types/apiClient'
import store from '@/store/index'
export default defineComponent({
  name: 'parkDashWarningMsgVue',
  setup() {
    const api = apiModule.api

    // let warningMsg = ref()
    let facilityId = computed(() => store.getters['auth/facilityId'])
    console.log('facilityId')
    console.log(facilityId.value)
    const getStatus = () => {
      api.get(`/facilities/${facilityId.value}/status`).then((res) => {
        console.log('status')
        console.log(res)
      })
    }
    onMounted(() => {
      getStatus()
    })

    return { facilityId, getStatus }
  }
})
</script>
<style lang=""></style>
