import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "axios";
import Vue from 'vue'

createApp(App).use(store).use(router).mount("#app");

Vue.createApp({}).config.globalProperties.$http = axios
Vue.createApp({}).config.globalProperties.$store = store
