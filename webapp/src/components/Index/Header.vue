<template>
  <div class="full-width">
    <div class="top-bar">
      <div class="brand">
        <img src="static/img/logo.png"/>
      </div>
      <div class="login-layout">
        <div>
          <Button type="info" icon="ios-man" shape="circle" @click="onLogin"></Button>
          <a @click="onLogin">{{loginText}}</a>
        </div>
        <Divider type="vertical" />
        <Icon type="md-more" size="24"></Icon>
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
    justify-content: space-between;
    align-items: center;
    margin-right: 10px;
    a {
      font-size: 14px;
    }
  }
}

</style>
