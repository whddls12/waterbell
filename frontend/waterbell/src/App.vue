<template>
  <div>
    <Home />
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import Home from './views/Home.vue'

export default defineComponent({
  name: 'App',
  components: {
    Home
  }
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.service-select {
  display: flex;
  align-content: center;
  justify-content: center;
}

.select-box {
  border: 1px solid #939393;
  width: 500px;
  height: 300px;

  margin: 20px;
}

.header {
  display: flex;
}

#hello-msg {
  display: inline-block;
}
</style>
