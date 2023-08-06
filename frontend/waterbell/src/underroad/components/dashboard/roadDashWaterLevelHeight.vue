<template>
  <div class="container">
    <p>수위센서 그래프</p>
    <canvas
      ref="chartCanvas"
      id="chartCanvas"
      width="400"
      height="200"
    ></canvas>
  </div>
</template>
<script lang="ts">
import Chart from 'chart.js/auto'
import { ref, onMounted, computed, nextTick, defineComponent } from 'vue'
import http from '@/types/http'
import store from '@/store/index'

export default defineComponent({
  name: 'roadDashWaterLevelVue',
  setup() {
    const chartRef = ref(null)
    const timeArr = ref<string[]>([])
    const amountArr = ref<string[]>([])

    const makeData = (i: Record<string, any>) => {
      for (const key in i) {
        timeArr.value.push(key)
        amountArr.value.push(i[key])
      }
    }

    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value
    console.log(facility_id)

    // 수위 센서 데이터 가져오기
    async function getSensorData() {
      try {
        const response = await http.get(
          `/dash/facilities/${facility_id}/sensors/heightPerhour`
        )

        const apiData = response.data
        console.log(apiData)

        return { apiData }
      } catch (error) {
        console.log('수위센서 측정 정보 가져오기 실패:', error)
      }
    }

    async function drawChart(chartCanvas: HTMLElement | null) {
      const canvas = document.getElementById('chartCanvas') as HTMLCanvasElement
      const ctx = canvas.getContext('2d')
      //차트 그리기
      new Chart(ctx, {
        type: 'line',

        data: {
          labels: timeArr.value,
          datasets: [
            {
              label: '수위 측정 데이터',
              data: amountArr.value,
              backgroundColor: 'rgba(75, 192, 192, 0.2)',
              borderColor: 'rgba(75, 192, 192, 1)',
              borderWidth: 1
            }
          ]
        },
        options: {
          // 차트 옵션
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      })
    }

    onMounted(async () => {
      const apiData = await getSensorData()
      if (apiData) {
        makeData(apiData)
      }
      await nextTick()
      drawChart(chartRef.value)
    })

    return { chartRef, timeArr, amountArr, getSensorData }
  }
})
</script>
<style></style>
