<template>
  <div class="full-width">
    <div class="top-bar">
      <div class="brand">
        <img src="static/img/logo.png"/>
      </div>
      <div class="login-layout">
        <div>
          <v-btn flat icon @click="onLogin">
            <v-icon>account_circle</v-icon>
          </v-btn>
          <a @click="onLogin">{{loginText}}</a>
        </div>
        <v-divider type="vertical" />
        <v-btn flat icon>
          <v-icon>more_vert</v-icon>
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  props: {
    loginText: {
      type: String,
      default: 'Signin'
    },
    onLogin: {
      type: Function
    }
  }
}

</script>

<style lang="stylus" scoped>
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
  padding: 0 10px 0 10px;
  box-shadow: 2px 2px 5px #cccccc;
  background: #fefefe;
  .brand {
    img {
      height: 70px;
    }
  }
  .login-layout {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin-right: 10px;
    a {
      font-size: 14px;
    }
  }
}

</style>
