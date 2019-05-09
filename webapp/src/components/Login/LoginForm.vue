<template>
  <div class="login-panel">
    <Form ref="form" :model="form" :rules="rule">
      <FormItem class="center-content">
        <Avatar icon="ios-person" size="large"></Avatar>
      </FormItem>
      <FormItem prop="user">
        <Input type="text" v-model="form.user" size="large" placeholder="Username">
          <Icon type="md-person" slot="prepend"/>
        </Input>
      </FormItem>
      <FormItem prop="password">
        <Input type="password" v-model="form.password" size="large" placeholder="Password">
          <Icon type="md-key" slot="prepend"/>
        </Input>
      </FormItem>
      <FormItem>
        <Button type="success" @click="handleSubmit('form')" long>Signin</Button>
      </FormItem>
      <Divider />
      <FormItem class="center-content">
        <a>Forget password?</a>&nbsp;or&nbsp;<a>create an account</a>
      </FormItem>
    </Form>
  </div>
</template>

<script>
import store from '../../store/index'
import router from '../../router/index'

export default {
  data () {
    return {
      form: {
        user: '',
        password: ''
      },
      rule: {
        user: [
          {
            required: true,
            message: 'Please fill in the user name',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: 'Please fill in the password.',
            trigger: 'blur'
          },
          {
            type: 'string',
            min: 6,
            message: 'The password length cannot be less than 6 bits',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    handleSubmit (name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.$Message.success('Success!')
          store.commit('LOGIN', '123456789012345678901234567890')
          router.replace({
            path: '/home'
          })
        } else {
          this.$Message.error('Fail!')
        }
      })
    }
  }
}
</script>

<style lang='stylus' scoped>
.login-panel {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 360px;
  padding: 30px;
  box-shadow: 2px 2px 5px #cdcdcd;
}
</style>
