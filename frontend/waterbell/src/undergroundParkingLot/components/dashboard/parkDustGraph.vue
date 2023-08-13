<template>
  <div>
    <div class="container">
      <p>미세먼지 반도넛그래프 테스트</p>
      <canvas
        ref="dustChartCanvas"
        id="dustChartCanvas"
        width="400"
        height="200"
      ></canvas>
    </div>
  </div>
</template>
<script lang="ts">
import Chart from 'chart.js/auto'
import { ref, onMounted, computed, nextTick } from 'vue'
import { defineComponent } from 'vue'
import http from '@/types/http'
import store from '@/store'

export default defineComponent({
  name: 'parkDustGraph',
  setup() {
    const chartRef = ref(null)

    // 시설아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value
    const current_dust = ref(null) // 미세먼지 농도
    const left_dust = ref<number | null>() // 미세먼지 최대농도에서 현재 농도를 뺀 값

    async function getDustData() {
      try {
        const response = await http.get(`/dash/facilities/10/sensors`)
        current_dust.value = response.data.Dust
        left_dust.value = 500 - response.data.Dust

        return { current_dust }
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }
    // 차트를 화면에 그려주는 함수
    async function drawChart(dustChartCanvas: HTMLElement | null) {
      const canvas = document.getElementById(
        'dustChartCanvas'
      ) as HTMLCanvasElement
      const ctx = canvas.getContext('2d')

      if (!ctx) {
        return
      }

      // 차트 그리기
      new Chart(ctx, {
        type: 'doughnut', // 차트 타입
        data: {
          datasets: [
            {
              label: '미세먼지 그래프',
              data: [current_dust.value, left_dust.value],
              backgroundColor: ['rgb(255, 72, 72)', 'rgb(147, 147, 147)'],
              borderWidth: 0,
              borderRadius: 8,
              hoverBackgroundColor: ['rgb(255, 72, 72)', 'rgb(147, 147, 147)'],
              hoverOffset: 4
            }
          ]
        },
        options: {
          tooltips: {
            // 툴팁삭제
            enabled: false
          },
          legend: {
            // 범례삭제
            display: false
          },
          elements: {
            center: {
              text: current_dust,
              fontStyle: 'Helvetica'
            }
          },
          cutout: 70, // 파이 차트의 가운데 부분을 얼마나 자를 건지
          rotation: -90, // 호를 그릴 시작 각도
          circumference: 180 // 호를 그리는 각도
          // plugins: {
          //   AfterDraw: function (chart: Chart) {
          //     let ctx = chart.ctx
          //     let canvas = chart.canvas
          //     let currentDust = current_dust.value

          //     ctx.save()

          //     let centerX = (chart.chartArea.left + chart.chartArea.right) / 2
          //     let centerY = (chart.cahrtArea.top + chart.chartArea.bottom) / 1.3

          //     ctx.textBaseline = 'middle'
          //     ctx.textAlign = 'center'
          //     ctx.font = 'bold 14px Helvetica'
          //     ctx.fillStyle = 'black'
          //     ctx.fillText(currentDust, centerX, centerY)

          //     ctx.restore()
          //   }
          // }
        }
      })
    }

    onMounted(async () => {
      await getDustData()
      await nextTick()
      drawChart(chartRef.value)
    })

    return { chartRef, current_dust }
  }
})
</script>
<style>
#dash-cctv {
  height: 500px;
}
</style>
