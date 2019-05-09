import Vue from 'vue'
import Router from 'vue-router'
import store from '../store/index'
import Home from 'pages/Home'
import Login from 'pages/Login'
import Index from 'pages/Index'

Vue.use(Router)

const routes = [
  {
    path: '/login',
    component: Login
  },
  {
    path: '/',
    component: Index,
    meta: {
      requireAuth: false
    }
  },
  {
    path: '/home',
    component: Home,
    meta: {
      requireAuth: true
    }
  }
]

if (localStorage.getItem('token')) {
  store.commit('LOGIN', localStorage.getItem('token'))
}

const router = new Router({
  routes
})

router.beforeEach((to, from, next) => {
  console.log(from)
  console.log(to)
  if (to.meta.requireAuth) {
    if (store.state.token) {
      next()
    } else {
      next({
        path: '/login',
        query: {
          redirect: to.fullPath
        }
      })
    }
  } else {
    if (store.state.token) {
      next({
        path: '/home'
      })
    } else {
      next()
    }
  }
})

export default router
