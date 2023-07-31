<template>
  <div id="nav">

    <!-- <RoadDash /> -->
    <roadDashRainAmountVue />
  </div>
</template>

<script setup>
// import RoadDash from "./underroad/views/roadDashboardView.vue";
import roadDashRainAmountVue from "./underroad/components/dashboard/roadDashRainFallAmount.vue";
</script>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
