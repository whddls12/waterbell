import { createApp } from 'vue'
import Vuex from 'vuex'
import App from './App.vue'
import router from './router'
import createPersistedState from 'vuex-persistedstate'
import auth from './store/auth/auth'
import store from './store'
import axios from 'axios'
// import Geolocation from 'vue-geolocation-api'

axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)
app.config.globalProperties.$http = axios
app.config.globalProperties.$store = store
app.use(store).use(router)

// export default new Vuex.Store({
//   modules: {
//     auth: auth
//   },

//   plugins: [
//     createPersistedState({
//       //주목! : 여기에 쓴 모듈만 저장됩니다.
//       paths: ['auth']
//     })
//   ]
// })

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

// import { createApp } from 'vue'
// import App from './App.vue'
// import router from './router'
// import store from './store'
// import axios from 'axios'

// axios.defaults.baseURL = 'http://localhost:8080'

// const app = createApp(App)
// app.config.globalProperties.$http = axios
// app.config.globalProperties.$store = store
// app.use(store).use(router).mount('#app')

// store.dispatch('fetchUnderroads')
