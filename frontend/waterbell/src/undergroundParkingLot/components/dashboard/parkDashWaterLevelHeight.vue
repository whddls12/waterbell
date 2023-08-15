<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <i class="fas fa-chart-line"></i>
        <h3>수위센서 그래프</h3>
      </div>
      <div class="dash-box-content">
        <canvas
          ref="chartCanvas"
          id="chartCanvas"
          width="400"
          height="200"
        ></canvas>
      </div>
    </div>
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
        console.log(key)
        timeArr.value.push(key)
        amountArr.value.push(i[key])
      }
    }
    console.log(timeArr.value)
    console.log(amountArr.value)

    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    // 수위 센서 데이터 가져오기
    async function getSensorData() {
      try {
        const response = await http.get(
          `/dash/facilities/10/sensors/heightPerhour`
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
              backgroundColor: 'rgba(151, 143, 237, 0.2)',
              borderColor: 'rgba(151, 143, 237, 1)',
              borderWidth: 1
            }
          ]
        },
        options: {
          elements: {
            line: {
              fill: true
            }
          },
          // 차트 옵션
          scales: {
            x: {
              grid: {
                display: false
              },
              title: {
                display: true,
                text: '시각(시:분)'
              }
            },
            y: {
              beginAtZero: true,
              grid: {
                display: false
              },
              title: {
                display: true,
                text: '지하주차장 수위(cm)'
              }
            }
          },
          plugins: {
            legend: {
              display: false // 범례 제거
            }
          }
        }
      })
    }

    onMounted(async () => {
      const apiData = await getSensorData()
      if (apiData) {
        makeData(apiData.apiData)
      }
      await nextTick()
      drawChart(chartRef.value)
    })

    return { chartRef, timeArr, amountArr, getSensorData }
  }
})
</script>
<style></style>
