<template>
  <div class="container">
    <div class="is-fullwidth">
      <button class="button is-success" @click="activeModal()">New Note</button>
    </div>
    <div class="columns is-centered">
      <div class="column has-text-centered">
        <div class="columns is-centered">
          <div class="column half">
            <div id = "noteComponents0">
              <NoteComponent v-for="note in noteComponents0" v-bind:key="note.id" :note="note"/>
            </div>
          </div>
          <div class="column half">
            <div id = "noteComponents1">
              <NoteComponent v-for="note in noteComponents1" v-bind:key="note.id" :note="note"/>
            </div>
          </div>

        </div>
      </div>
    </div>
    <NewNoteComponent/>
  </div>
</template>


<script>
  import NoteComponent from '@/components/NoteComponent'
  import NewNoteComponent from '@/components/NewNoteComponent'
  import axios from "axios"
  import CryptoJS from "crypto-js"

  export default {
    components: {
      NoteComponent,
      NewNoteComponent
    },
    data() {
      return {
        noteComponents0: null,
        noteComponents1: null
      };
    },
    methods: {
      activeModal() { 
        var modall = document.getElementById("modalNew");
        var modal = modall.querySelector('.modal')
        //var modal = document.querySelector('.modal'); // assuming you have only 1
        modal.classList.add('is-active');
        modal.querySelector('.modal-background').addEventListener('click', function (e) {
          e.preventDefault();
          modal.classList.remove('is-active');
        });
      },
      async setNote(){
        const note = await this.getNote();
        var note0 = [];
        var note1 = [];
        for(var i=0; i < note.data.length; i++){
          note.data[i].created_at = this.timeConverter(note.data[i].created_at);
          var iv = CryptoJS.enc.Utf8.parse(note.data[i].body.substring(0, 16));
          var key =  CryptoJS.enc.Utf8.parse(JSON.parse(localStorage.getItem('user')).password);
          var decrypt= CryptoJS.AES.decrypt(note.data[i].body.substring(16, note.data[i].body.length),key,{iv:iv,padding:CryptoJS.pad.ZeroPadding});
          note.data[i].body = decrypt.toString(CryptoJS.enc.Utf8);
          if((i%2) == 0){
            note0.push(note.data[i]);
          } else{
            note1.push(note.data[i]);
          }
        }

        this.noteComponents0 = note0;
        this.noteComponents1 = note1;
      },

      async getNote(){
        let data = await axios.get("https://xelinion.servebeer.com/kp/getallnotes", {
          headers: {
            "Accept": 'application/json',
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
          }
          })
          .then(function (data) {
            return data;            
          })
          .catch(function (error) {
            console.log(error);
          });
        return await data;
      },


      timeConverter(UNIX_timestamp){
            var a = new Date(UNIX_timestamp * 1000);
            var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
            var year = a.getFullYear();
            var month = months[a.getMonth()];
            var date = a.getDate();
            var hour = a.getHours();
            var min = a.getMinutes();
            var time = hour + ':' + min + " - " + date + ' ' + month + ' ' + year;
            return time;
      },
    },
    mounted() {
      this.setNote();
      //this.$refs.child.$on('note', this.newNote());
    },
  }
</script>

<style scoped>
  .is-fullwidth {
    margin: 0 auto;
    width: max-content;
  }
  @media (min-width: 1024px) {
    .half {
      padding: auto;
      max-width: 45vw;
    }
  }

  @media (min-width: 768px) and (max-width: 1023px) {
    .half {
      max-width: 47vw;
    }
  }

  @media (max-width: 767px) {
    .half {
      max-width: 80vw;
      margin: auto;
    }
  }
</style>