<template>
  <div class="container-fluid">
    <div class="columns is-centered">
      <div class="column">
        <i class="far fa-user"></i>&nbsp;User Data
        <br><br>
        <div class="card">
          <div class="card-content">
            <div class="media">
              <div class="media-left">
                <figure class="image is-48x48">
                  <img v-bind:src="image" v-bind:alt="name">
                </figure>
              </div>
              <div class="media-content">
                <p class="title is-4 unspaced">{{name}}</p>
                <span class="tag is-warning is-light">Registered User</span>
              </div>
            </div>

            <div class="content">
              
              <span class="tag is-warning">Chronos 1.0 Beta</span><br><br>

            </div>
          </div>
        </div>
      </div>
      
      <div class="column">
        <i class="fas fa-key"></i>  &nbsp;&nbsp; My Keys  
        <br><br>
        <div class="card">
          <div class="card-content">
            <div class="media">
              <div class="media-content">
                <p class="title is-4"><i class="fas fa-fingerprint"></i> &nbsp;&nbsp;Fingerprint of my key</p>
              </div>
            </div>            <br>

            <div class="content has-text-primary ">
                &nbsp;&nbsp;&nbsp;&nbsp; {{key}} <div class="tags has-addons margined"><span class="tag is-dark">Algorithm</span><span class="tag is-success is-light">AES-256</span>
</div>
            </div>
          </div>
        </div>
      </div>
      <div class="column">
      
  Future work<br><br>
        <nav class="panel">
          <p class="panel-heading">
            Coming soon
          </p>
          <a class="panel-block">
            New notifications
          </a>
          <a class="panel-block">
            Private events
          </a>
          <a class="panel-block">
            Share events
          </a>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import CryptoJS from "crypto-js";

  export default {
    components: {},
     data() {
      return {
        name: JSON.parse(localStorage.getItem('user')).name,
        image: JSON.parse(localStorage.getItem('user')).image_URL,
        key: CryptoJS.SHA3(JSON.parse(localStorage.getItem('user')).password, { outputLength: 128})
      }
    },
  }
</script>

<style scoped>
  .unspaced{
      margin: 0 !important;
      padding: 0;
  }

  .margined{
    margin-top: 4px;
  }

</style>