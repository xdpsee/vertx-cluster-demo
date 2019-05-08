import Vue from 'vue'
import Router from 'vue-router'

import Home from 'pages/Home'
import Login from 'pages/Login'

Vue.use(Router)

const routes = [
  {
    path: '/',
    component: Home
  },
  {
    path: '/login',
    component: Login
  }
]

export default new Router({
  routes
})
