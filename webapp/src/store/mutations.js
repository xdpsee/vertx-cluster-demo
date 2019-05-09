const mutations = {
  LOGIN: (state, token) => {
    localStorage.setItem('token', token)
    state.token = token
  },
  LOGOUT: (state) => {
    localStorage.removeItem('token')
    state.token = null
  },
  USERNAME: (state, username) => {
    localStorage.setItem('username', username)
    state.username = username
  }
}

export default mutations
