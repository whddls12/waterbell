<template>
<<<<<<< frontend/waterbell/src/App.vue
  <div id="nav">
    <div>
    <Home />
    </div>
    <TheHeader />
    <div class="router-view-container">
      <router-view></router-view>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import TheHeader from './components/TheHeader.vue'

export default  defineComponent({
 
   name: 'App',
  components: {
    Home,
    TheHeader
  }
})

 

<style>
/* template {
  background-color: #f2f7ff;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* flex-basis: 100%;
  text-align: center;

  color: #2c3e50;
  background-color: #f2f7ff;
}

#nav {
  display: flex-inline;
  flex-direction: column;
  justify-content: center;
  align-content: flex-end;

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

#nav a.router-link-exact-active {
  color: #42b983;
} */

body {
  background-color: #f2f7ff;
  margin: 0;
  padding: 0;
}
div {
  margin: 0;
}
#app {
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;

  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 80vw; /* set width to 80vw to give 10% padding on each side */
  height: 100vh;
  /* display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f2f7ff;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 100vw;
  height: 100vh;
  margin-left: 10%;
  margin-right: 10%; */
}
.router-view-container {
  padding: 10px 20px;
  display: flex;
  justify-content: center;
  width: 100%;
  height: 100%;
  overflow: auto; /* prevent components from going out of bounds */
  background-color: white;
}

router-view {
  flex-flow: 1;
}

#nav {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  align-items: center;
  width: calc(100% - 320px);
  padding: 0;

#hello-msg {
  display: inline-block;
}
</style>
