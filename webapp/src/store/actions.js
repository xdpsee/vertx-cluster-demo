const actions = {
  UserLogin ({ commit }, token) {
    commit('LOGIN', token)
  },
  UserLogout ({ commit }) {
    commit('LOGOUT')
  },
  UserName ({ commit }, username) {
    commit('USERNAME', username)
  }
}

export default actions
