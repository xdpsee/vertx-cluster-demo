import Vue from 'vue'
import router from './router'
import App from './App'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import 'assets/css/app.styl'

Vue.use(iView)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
