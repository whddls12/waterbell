<template>
  <div class="container">
    <p>기온: {{ current_temp }}</p>
    <p>습도: {{ current_humid }}</p>
  </div>
</template>
<script lang="ts">
import { ref, onMounted, computed, defineComponent } from 'vue'
import store from '@/store/index'
import http from '@/types/http'

export default defineComponent({
  name: 'parkDashTempAndHumid',
  setup() {
    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    const current_temp = ref(null)
    const current_humid = ref(null)
    async function getDustData() {
      try {
        const response = await http.get(`/dash/facilities/10/sensors`)

        current_temp.value = response.data.HEIGHT
        current_humid.value = response.data.HUMIDITY
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }
    onMounted(async () => {
      await getDustData()
    })

    return { current_temp, current_humid, getDustData }
  }
})
</script>
<style></style>
