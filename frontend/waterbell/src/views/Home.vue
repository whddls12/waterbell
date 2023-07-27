<template>
  <div class="home">
    <!-- <img alt="Vue logo" src="../assets/logo.png" /> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" /> -->
    <Map/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import Map from '@/underroad/components/dashboard/roadDashRainFallAmount.vue'
// import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src

export default defineComponent({
  name: "Home",
  components: {
    // HelloWorld,
    Map
  },
});
</script>
