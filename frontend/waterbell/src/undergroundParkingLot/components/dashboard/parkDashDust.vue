<template>
  <div class="container">
    <p>미세먼지</p>
  </div>
</template>
<script lang="ts">
import { onMounted, computed, defineComponent } from 'vue'
import store from '@/store'
import http from '@/types/http'

export default defineComponent({
  name: 'parkDashDust',
  setup() {
    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    async function getDustData() {
      try {
        const response = await http.get(
          `/dash/facilities/${facility_id}/sensors/dust`
        )

        const apiData = response.data
        console.log(apiData)

        return { apiData }
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }
    onMounted(async () => {
      await getDustData()
    })

    return { getDustData }
  }
})
</script>
<style></style>
