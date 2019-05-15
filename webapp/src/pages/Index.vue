<template>
  <v-app>
    <Header login-text="登录" :on-login="onLogin"></Header>
  </v-app>
</template>

<script>
import Header from '../components/Index/Header'
import router from '../router/index'

export default {
  components: {
    Header
  },
  methods: {
    onLogin () {
      router.push({
        path: 'login',
        query: {
          redirect: router.currentRoute.path
        }
      })
    }
  }
}
</script>
