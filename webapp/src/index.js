import Vue from 'vue'
import router from './router'
import App from './App'
import iView from 'iview'
import store from './store/index'
import axios from './http'

import 'iview/dist/styles/iview.css'
import 'assets/css/app.styl'

Vue.use(iView)
Vue.prototype.axios = axios

new Vue({
  axios,
  store,
  router,
  render: h => h(App)
}).$mount('#app')
