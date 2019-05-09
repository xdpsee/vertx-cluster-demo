import axios from 'axios'
import store from './store/index'
import router from './router/index'

axios.defaults.timeout = 5000
axios.defaults.baseURL = 'https://api.github.com'

axios.interceptors.request.use(
  config => {
    if (store.state.token) {
      config.headers.Authorization = `token ${store.state.token}`
    }
    return config
  },
  err => {
    return Promise.reject(err)
  }
)

axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401: {
          store.commit('LOGOUT')
          if (router.currentRoute.path !== 'login') {
            router.replace({
              path: 'login',
              query: {
                redirect: router.currentRoute.path
              }
            })
          }
        }
      }
    }
    return Promise.reject(error.response.data)
  }
)

export default axios
