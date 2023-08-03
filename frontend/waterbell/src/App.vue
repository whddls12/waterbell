<template>
  <div id="nav">
    <Home />
    <!-- <TheHeader /> -->
    <!-- <div class="router-view-container">
      <router-view></router-view>
    </div> -->
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
// import TheHeader from './components/TheHeader.vue'
import Home from './views/Home.vue'

export default defineComponent({
  name: 'App',
  components: {
    Home
    // TheHeader
  }
})
</script>

<style>
template {
  background-color: #f2f7ff;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  flex-basis: 100%;
  text-align: center;
  color: #2c3e50;
  background-color: #f2f7ff;
}

#nav {
  display: flex-inline;
  flex-direction: column;
  justify-content: center;
  align-content: flex-end;
}

* body {
  background-color: #f2f7ff;
  margin: 0;
  padding: 0;
}

div {
  margin: 0;
}

#nav {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  align-items: center;
  width: calc(100% - 320px);
  padding: 0;
}
</style>
