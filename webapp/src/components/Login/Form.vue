<template>
  <div class="login-form-wrapper">
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-flex align-center justify-center layout text-xs-center>
        <v-avatar :size="70" color="green lighten-4">
          <img src="https://vuetifyjs.com/apple-touch-icon-180x180.png" alt="avatar">
        </v-avatar>
      </v-flex>
      <v-text-field v-model="username" :rules="[rules.required]" label="用户名" required></v-text-field>
      <v-text-field v-model="password"
        :append-icon="show1 ? 'visibility' : 'visibility_off'"
        :rules="[rules.required, rules.min]"
        :type="show1 ? 'text' : 'password'"
        label="密码"
        @click:append="show1 = !show1"
      ></v-text-field>
      <v-btn :disabled="!valid" block color="success" @click="validate">登 录</v-btn>
      <v-flex align-center justify-center layout text-xs-center>
        <v-btn flat color="primary">忘记密码？</v-btn>
        <v-btn flat color="primary">注册新用户</v-btn>
      </v-flex>
    </v-form>
  </div>
</template>

<script>
export default {
  data: () => ({
    valid: false,
    username: '',
    show1: false,
    password: '',
    rules: {
      required: value => !!value || '该项不能为空，请填写.',
      min: v => v.length >= 8 || '至少8个字符'
    }
  }),

  methods: {
    validate () {
      if (this.$refs.form.validate()) {
        this.snackbar = true
      }
    }
  }
}
</script>

<style lang='stylus' scoped>
.login-form-wrapper {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
  width: 400px;
  padding: 30px;
  box-shadow: 2px 2px 5px #cdcdcd;
}
</style>
