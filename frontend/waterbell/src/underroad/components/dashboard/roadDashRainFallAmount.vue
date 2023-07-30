<template>
  <div>
    <p>지하주차장 대시보드 강수량 그래프</p>
    <canvas ref="chartCanvas" width="400" height="200"></canvas>
  </div>
</template>
<script>
import Chart from "chart.js/auto";
import { ref, onMounted } from "vue";
import { defineComponent } from "vue";
import axios from "axios";

export default defineComponent({
  name: "roadDashRainAmountVue",
  setup() {
    const chartRef = ref(null);

    onMounted(() => {
      // 차트 그리기
      drawChart(chartRef.value);
    });

    return { chartRef };
  },
});

async function drawChart(chartCanvas) {
  try {
    // API 데이터 가져오기 (예시를 위해 랜덤 데이터 사용)
    const response = await axios.get("https://api.example.com/data");
    const apiData = response.data;

    // 차트 생성을 위한 데이터 가공
    const labels = apiData.map((data) => data.label);
    const values = apiData.map((data) => data.value);

    // 차트 그리기
    new Chart(chartCanvas, {
      type: "bar", // 차트 타입 (bar, line 등)
      data: {
        labels: labels,
        datasets: [
          {
            label: "API 데이터",
            data: values,
            backgroundColor: "rgba(75, 192, 192, 0.2)",
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1,
          },
        ],
      },
      options: {
        // 차트 옵션 설정 (생략 가능)
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  } catch (error) {
    console.error("API 데이터 가져오기 실패:", error);
  }
}
</script>
<style lang=""></style>
