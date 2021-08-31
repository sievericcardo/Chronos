<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <router-link class="is-flex" to="/">
        <img id="logo" src="@/assets/img/chronos-logo.png" />
      </router-link>

      <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false"
        data-target="navbarBasicExample">
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div id="navbarBasicExample" class="navbar-menu">
      <div class="navbar-start">
        <a class="navbar-item" href="https://xelinion.servebeer.com/wiki/index?title=Kernel_Panic_Event_Planner">
          Documentation
        </a>
      </div>

      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons">
            <button class="button is-primary" @click="$modal.show('login-box')" v-if="name==''">
              Login
            </button>
        <template>
          <div class="dropdown is-right" v-bind:class="{ 'is-active': isDropdownActive }" v-on-clickaway="away" v-if="name!=''">
            <div class="dropdown-trigger" @click="isDropdownActive=!isDropdownActive">
              <a aria-haspopup="true" aria-controls="{ 'dropdown-menu'}">
                <span>{{name}}
                  <img class="is-rounded" v-bind:src="image">
                </span>
                <span class="icon is-small">
                  <i class="fa fa-angle-down" aria-hidden="true"></i>
                </span>
              </a>
            </div>
            <div class="dropdown-menu" id="dropdown-menu" role="menu">
              <div class="dropdown-content">
                <router-link to="/user" class="dropdown-item has-text-centered">
                  <i class="far fa-user-circle"></i> &emsp;&emsp;<span>My Profile</span>
                </router-link>
                <a href="#" class="dropdown-item has-text-centered" @click="logout()">
                  <span @click="logout()"><i class="fas fa-sign-out-alt"></i> &emsp; Logout</span></a>
              </div>
            </div>
          </div>
        </template>
            <LoginComponent></LoginComponent>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
  import LoginComponent from '@/components/LoginComponent'
  //import LoginNotificationComponent from '@/components/LoginNotificationComponent'
  import { mixin as clickaway } from 'vue-clickaway';

  document.addEventListener('DOMContentLoaded', () => {

    // Get all "navbar-burger" elements
    const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

    // Check if there are any navbar burgers
    if ($navbarBurgers.length > 0) {

      // Add a click event on each of them
      $navbarBurgers.forEach(el => {
        el.addEventListener('click', () => {

          // Get the target from the "data-target" attribute
          const target = el.dataset.target;
          const $target = document.getElementById(target);

          // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
          el.classList.toggle('is-active');
          $target.classList.toggle('is-active');

        });
      });
    }

  });

  export default {
    props: ['id'],
    mixins: [ clickaway ],
    name: 'TheNavbar',
    components: {
    LoginComponent,
    //LoginNotificationComponent,
    },
    data() {
      return {
        showNav: false,
        canBeShown: false,
        showNotification: false,
        action: [
            {key: 1, name: "User profile"},
            {key: 2, name: "Logout"}
        ],
        selectedAction: "",
        isDropdownActive: false,
        name: "",
        image: "",
      }
    },
    created() {
      setInterval(() => {
        this.canBeShown = !this.canBeShown
      }, 5000)
    },
    methods: {
      notification(){
        this.showNotification = true;
        setTimeout(() => this.showNotification = false, 1000)
      },

      conditionalShow() {
        this.$modal.show('login-box', {
          show: this.canBeShown
        });
      },
      dialogEvent(eventName) {
        // eslint-disable-next-line no-console
        console.log('Dialog event: ' + eventName)
      },
      away() {
        this.isDropdownActive = false;
      },
      logout() {
        this.name = "";
        localStorage.clear();
        this.$router.push('/');
        this.$awn.success('See you soon !')
      },
      

    },
    mounted() {
      if(localStorage.getItem('user')!=null){
        this.name = JSON.parse(localStorage.getItem('user')).name;
        this.image = JSON.parse(localStorage.getItem('user')).image_URL;
      }
    },
    watch: {
      canBeShown: function() {
        if(localStorage.getItem('user')!=null){
          this.name = JSON.parse(localStorage.getItem('user')).name;
          this.image = JSON.parse(localStorage.getItem('user')).image_URL;
        }
      },
    }
  }
</script>

<style scoped>
@import '~vue-awesome-notifications/dist/styles/style.css';
  .main-color {
    background-color: #d7ab2a !important;
    border-color: transparent;
    color: #fff;
  }

  #main-title1 {
    font-size: 2rem;
    color: #D8AE32 !important;
  }

  #main-title2 {
    font-size: 2rem;
    color: #202625 !important;
  }

  .img-logo {
    vertical-align: middle;
    max-width: 3rem;
    max-height: 3rem;
  }

  .brand-logo {
    text-decoration: none;
    color: black !important;
  }

  #fcolor {
    font-size: 2rem;
    color: #D8AE32 !important;
  }

  .simple-link:hover {
    color: #D8AE32 !important;
  }

  #scolor {
    font-size: 2rem;
    color: #202625 !important;
  }

  .navbar {
    padding: 0.5rem;
  }

  .vue-google-signin-button {
    display: inline-block;
    padding: 4px 8px;
    border-radius: 3px;
    background-color: #3c82f7;
    color: #fff;
    box-shadow: 0 3px 0 #0f69ff;
  }

  .is-rounded {
    object-fit: cover;
    object-position: center center;
    height: 30px;
    width: 30px;
    border-radius: 250983px;
  }

</style>