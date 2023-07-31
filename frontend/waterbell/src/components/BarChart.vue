<template>
  <div>
    <canvas
      ref="MyChart"/>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js'
import { defineComponent } from 'vue'
Chart.register(...registerables)

export default defineComponent({
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  data: () => ({
    type: 'bar',
    data: {
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      datasets: [{
        label: '# of Votes',
        data: [12, 19, 3, 5, 2, 3],
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)'
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  }),
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  mounted() {
    this.createChart()
  },
  methods: {
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    createChart() {
      // eslint-disable-next-line no-new
      new Chart(this.$refs.MyChart, {
        type: 'bar',
        data: this.data,
        options: this.options
      })
    }
  }

})
</script>

<style>

</style>
