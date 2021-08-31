import Vue from 'vue'
import App from './App.vue'
import router from './router/router'
import VModal from 'vue-js-modal'
import Vuelidate from 'vuelidate'
import Buefy from 'buefy'
import 'buefy/dist/buefy.css'
import VueAWN from "vue-awesome-notifications"
import GoogleLogin from 'vue-google-login';
import UUID from 'vue-uuid';

Vue.use(UUID);
Vue.use(GoogleLogin)

Vue.use(VModal)
Vue.use(Vuelidate)
Vue.use(Buefy)
Vue.use(VueAWN)
new Vue({
  router,
  VModal,
  Vuelidate,
  validations: {},
  render: h => h(App)
}).$mount('#app')
