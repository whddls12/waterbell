import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'

// 날짜 포맷
// import { formatDate } from './util/filters'
// Vue.filter('formatDate', formatDate)

// import 'bootstrap/dist/css/bootstrap.min.css'
// import Geolocation from 'vue-geolocation-api'

axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)
app.config.globalProperties.$http = axios
app.config.globalProperties.$store = store
app.use(store).use(router)

// fetchUnderroads 액션을 실행하고 완료될 때까지 기다립니다.
store
  .dispatch('fetchUnderroads')
  .then(() => {
    // 액션이 완료되면 앱을 마운트합니다.
    app.mount('#app')
  })
  .catch((error) => {
    console.error('Failed to fetch underroads: ', error)
  })
