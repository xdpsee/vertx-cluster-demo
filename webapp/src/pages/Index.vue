<template>
  <div class="full-width">
    <Header login-text="登录" :on-login="onLogin"></Header>
  </div>
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
