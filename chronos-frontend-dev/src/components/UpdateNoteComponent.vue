<template>
  <div id="modalUpdate">
  <div class="modal" name="note-box" ref="notemodal" transition="pop-out" :adaptive="true">
    <div class="modal-background"></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">
          <span class="icon is-small is-left is-transparent">
            <i class="fas fa-sticky-note fa-w-14"></i>
          </span><strong id="new-note-title">Update Note</strong></p>
        <button class="delete" aria-label="close" @click="closeModal()"></button>
      </header>
      <section class="modal-card-body">
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
    name: 'UpdateNoteComponent',
    data() {
      return {
        note: "",
        color: "",
        result: false
      }
    },
    
    methods: {
      closeModal() {
        this.$refs.notemodal.classList.toggle("is-active");
      },
      saveChanges() {
        const t = this;
        if(this.note.length!=0 & this.color.length!=0){
          console.log(this.$parent.note.id);
           console.log(this.$parent.note.date);

          var data = this.note;
          var key  = CryptoJS.enc.Utf8.parse(JSON.parse(localStorage.getItem('user')).password);
          var random = uuid.v1().substring(0, 16);
          var iv   = CryptoJS.enc.Utf8.parse(random);
          var encrypted = CryptoJS.AES.encrypt(data, key,
          {iv:iv,mode:CryptoJS.mode.CBC, padding:CryptoJS.pad.ZeroPadding
          });
          encrypted = random + encrypted;
          
          axios.post("https://xelinion.servebeer.com/kp/updatenote", {
            id: this.$parent.note.id,
            title: this.$parent.note.title,
            created_at: Math.round(Date.now()/1000),
            body: encrypted,
            color: this.color
            }, {
            headers: {
              "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
            }
          })
          .then(function (response) { 
            if(response.data.message == "Success"){
              t.closeModal()
              t.$awn.success('Note update successfully')
              t.$parent.note.body = data;
              t.$parent.note.color = t.color
            }

            
          })
          .catch(function (error) {
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

  #modalUpdate{
    text-align: left;
  }
</style>