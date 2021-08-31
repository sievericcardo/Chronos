<template>
  <div class="card" v-bind:class="'color'+note.color" ref="maincomp">
    <header class="card-header">
      <p class="card-header-title">
        Note
      </p>
      <a href="#" class="card-header-icon" aria-label="more options" @click="update()">
        <span class="icon">
          <i class="fas fa fa-edit fa-w-18" aria-hidden="true"></i>
        </span>
      </a>
      <a href="#" class="card-header-icon" aria-label="more options" @click="cancel()">
        <span class="icon">
          <i class="fas fa-trash-alt fa-w-14" aria-hidden="true"></i>
        </span>
      </a>
    </header>
    <div class="card-content">
      <div class="note-text">
        {{note.body}}
        <br>
        <time datetime="2016-1-1">{{note.created_at}}</time>
      </div>
    </div>
    <UpdateNoteComponent/>
  </div>
</template>

<script>
import UpdateNoteComponent from '@/components/UpdateNoteComponent'
import axios from "axios";

  export default {
    components: {
      UpdateNoteComponent
    },
    data() {
      return{
        exists: true
       }
    },
    props: {
      note: {
        type: Object,
        required: true
      }
    },
    methods: {
      activeModal() { 
        var modall = document.getElementById("modalUpdate");
        var modal = modall.querySelector('.modal');
        modal.classList.add('is-active');
        modal.querySelector('.modal-background').addEventListener('click', function (e) {
          e.preventDefault();
          modal.classList.remove('is-active');
        });
      },
      cancel: function() {
        const t = this
        var mykey = this.$vnode.key
        axios
            .post("https://xelinion.servebeer.com/kp/deletenote", mykey, {
            headers: {
              "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
            }
          })
            .then(function (response) {
                if(response.data.message == "Success"){
                  var notes0 = [];
                  var notes1 = [];
                  for(var i=0; i < t.$parent.noteComponents0.length; i++){
                    if(t.$parent.noteComponents0[i].id != mykey)
                      notes0.push(t.$parent.noteComponents0[i]);
                  }
                  for(var j=0; j < t.$parent.noteComponents1.length; j++){
                    if(t.$parent.noteComponents1[j].id != mykey)
                      notes1.push(t.$parent.noteComponents1[j]);
                  }
                  if(notes0.length==0){
                    notes0 = notes1;
                    notes1 = [];
                  }
                  if(notes0.length < notes1.length){
                    var n = notes1[0];
                    notes1.shift();
                    notes0.unshift(n);
                  }
                  t.$parent.noteComponents0 = notes0;
                  t.$parent.noteComponents1 = notes1;
                  t.$awn.success('Note deleted correctly !')
                }
            })
            .catch(function (error) {
                console.log(error);
            });
      },
      update: function() {
        this.activeModal();
      }
    }
  }
</script>

<style scoped>
  @font-face {
    font-family: "JandaQuickNoteW00-Regular";
    src: url("//db.onlinewebfonts.com/t/e841147604be84f58db124cc68aad191.eot");
    src: url("//db.onlinewebfonts.com/t/e841147604be84f58db124cc68aad191.eot?#iefix") format("embedded-opentype"), url("//db.onlinewebfonts.com/t/e841147604be84f58db124cc68aad191.woff2") format("woff2"), url("//db.onlinewebfonts.com/t/e841147604be84f58db124cc68aad191.woff") format("woff"), url("//db.onlinewebfonts.com/t/e841147604be84f58db124cc68aad191.ttf") format("truetype"), url("//db.onlinewebfonts.com/t/e841147604be84f58db124cc68aad191.svg#JandaQuickNoteW00-Regular") format("svg");
  }

  .card {
    margin: 1rem;
  }

  .is-rounded {
    object-fit: cover;
    object-position: center center;
    height: 100%;
    width: 100%;
    border-radius: 250983px;
  }

  .note-text {
    font-family: "JandaQuickNoteW00-Regular" !important;
  }

  /* Yellow */
  .color0 {
    background-color: #ffc;
  }

  /* Cyan */
  .color1 {
    background-color: #cff;
  }

  /* Green */
  .color2 {
    background-color: #e1ffe0;
  }

  /* Magenta */
  .color3 {
    background-color: #fcf;
  }
</style>