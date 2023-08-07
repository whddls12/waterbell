<template>
  <div class="container">
    <p>미세먼지</p>
  </div>
</template>
<script lang="ts">
import { ref, onMounted, computed, defineComponent } from 'vue'
import store from '@/store/index'
import http from '@/types/http'

export default defineComponent({
  name: 'parkDashDust',
  setup() {
    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value
    const current_dust = ref(null)

    async function getDustData() {
      try {
        const response = await http.get(`/dash/facilities/10/sensors/DUST`)

        return { current_dust }
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }
    onMounted(async () => {
      // await getDustData()
    })

    return { current_dust, getDustData }
  }
})
</script>
<style></style>
