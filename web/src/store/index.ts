import { createStore } from 'vuex'

declare let SessionStorage: any
const USER = "USER"

const store = createStore({
  state: {
    //  || {} 是为了避免空指针异常
    user: SessionStorage.get(USER) || {}
  },
  mutations: {
    setUser (state, user) {
      state.user = user
      SessionStorage.set(USER, user)
    }
  },
  actions: {
  },
  modules: {
  }
})

export default store