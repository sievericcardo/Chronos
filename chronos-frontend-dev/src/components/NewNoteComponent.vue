<template>
  <div id="modalNew">
  <div class="modal" name="note-box" ref="notemodal" transition="pop-out" :adaptive="true">
    <div class="modal-background"></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">
          <span class="icon is-small is-left is-transparent">
            <i class="fas fa-sticky-note fa-w-14"></i>
          </span><strong id="new-note-title">New Note</strong></p>
        <button class="delete" aria-label="close" @click="closeModal()"></button>
      </header>
      <section class="modal-card-body">
        <div class="field">
          <label class="label">Title</label>
          <div class="control has-icons-left has-icons-right">
            <input class="input" type="text" placeholder="Text input" v-model="title">
            <span class="icon is-small is-left">
              <i class="fas fa-heading fa-w-16"></i>
            </span>
            <span class="icon is-small is-right">
              <i class="fas fa-check"></i>
            </span>
          </div>
          <p class="help is-success"></p>
        </div>

        <div class="field">
          <label class="label">Body</label>
          <div class="control">
            <textarea class="textarea" placeholder="Textarea" v-model="note"></textarea>
          </div>
        </div>

        <div class="field">
          <label class="label">Color</label>
          <div class="control has-icons-left">
            <div class="select">
              <select v-model="color">
                <option value="0">Yellow</option>
                <option value="1">Light Blue</option>
                <option value="2">Green</option>
                <option value="3">Purple</option>
              </select>
              <span class="icon is-small is-left">
                  <i class="fas fa-palette fa-w-16"></i>
                </span>
            </div>
          </div>
        </div>
      </section>
      <footer class="modal-card-foot">
        <button class="button is-success" @click="saveChanges()">Save changes</button>
        <button class="button" @click="closeModal()">Cancel</button>
      </footer>
    </div>
  </div>
</div>
</template>

<script>
import axios from "axios";
import CryptoJS from "crypto-js";
import { uuid } from 'vue-uuid';


  export default {
    name: 'NewNoteComponent',
    data() {
      return {
        title: null,
        note: null,
        color: null,
        result: false
      }
    },
    
    methods: {
      closeModal() {
        this.$refs.notemodal.classList.toggle("is-active");
      },
      saveChanges() {
        const t = this;
        if(this.title.length!=0 & this.note.length!=0 & this.color.length!=0){
          var data = this.note;
          //if(JSON.parse(localStorage.getItem('user')).password==)
          var key  = CryptoJS.enc.Utf8.parse(JSON.parse(localStorage.getItem('user')).password);
          var random = uuid.v1().substring(0, 16);
          var iv   = CryptoJS.enc.Utf8.parse(random);
          var encrypted = CryptoJS.AES.encrypt(data, key,
          {iv:iv,mode:CryptoJS.mode.CBC, padding:CryptoJS.pad.ZeroPadding
          });
          encrypted = random + encrypted;

          var date = Math.round(Date.now()/1000);
          var note = {
            id: -1,
            title: this.title,
            body: data,
            created_at: date,
            color: this.color
            }
          
          axios.post("https://xelinion.servebeer.com/kp/newnote", {
            title: this.title,
            body: encrypted,
            created_at: date,
            color: this.color
            }, {
            headers: {
              "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
            }
          })
          .then(function (response) { 
            if(response.data.message == "Success"){
              t.closeModal()
              t.$awn.success('New note added successfully')
              note.id = response.data.note_id
              if(t.$parent.noteComponents1.length > t.$parent.noteComponents0.length){
                t.$parent.noteComponents0.push(note)
              }else if (t.$parent.noteComponents1.length == t.$parent.noteComponents0.length){
                t.$parent.noteComponents0.push(note)
              }else{
                t.$parent.noteComponents1.push(note)
              }
            }
            
          })
          .catch(function (error) {
            t.closeModal()
            t.$awn.error(error)
          });
        }
      }
    }
  }
</script>

<style scoped>
@import '~vue-awesome-notifications/dist/styles/style.css';
  .modal-background {
    background-color: none;
    opacity: 0.4;
  }

  #new-note-title{
    margin-left:1rem;
  }

  .is-transparent{
    opacity: 0.3;
  }
</style>