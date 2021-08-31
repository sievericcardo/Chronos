<template>
  <div class="content is-medium">
    <h2>Partecipanti</h2>
    <div id="tutto">

      <div class ="arrow">
        <button v-on:click="back()" v-if="existsBack"></button>
      </div>

      <div id = "partecipants" class="ciao">
        <img class="is-rounded" v-for="user in partecipants" :title="user.username" :key="user.username" v-bind:src="user.avatar"> 
        <div v-if="more"><p>...</p></div>
      </div>
      
      <div class ="arrow">
        <button v-on:click="next()" v-if="existsNext"></button>
      </div>

    </div>
        <div class="level-item" v-if="login!=null">
          <a class="button is-primary" @click="joining()">Participate</a>
        </div>
        <br>
        <div class="level-item" v-if="login!=null">
          <a class="button is-primary" @click="deleteJoining()">Cancel participation</a>
        </div>

          <div class="box-part" id="bp-right">
          <div class="box-messages"></div>
  </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
    name: 'ParticipantsComponent',
    data() {
      return {
        partecipants: null,
        more: false,
        existsBack: false,
        existsNext: false,
        partecipanti_data: [],
        numPag: 1,
        login: JSON.parse(localStorage.getItem('user'))
      };

    },
    props: {
      eventId: {
        type: String,
        required: true
      }
    },
    methods: {
      async setPartecipanti() {
        const partecipantii = await this.getPartecipanti();
        this.partecipanti_data = partecipantii
   
        var size = 0;
        if(this.partecipanti_data.data.length <= 14){
          size = this.partecipanti_data.data.length;
          this.more = false;
          this.existsNext = false;
          this.existsBack = false;
        }
        else{
          size = 14;
        }
        
        var partecipanti = [];
        for(var i=0; i < size; i++){
          if(this.partecipanti_data.data[i].avatar[0]=='.'){
            this.partecipanti_data.data[i].avatar = "https://xelinion.servebeer.com/kp/" + this.partecipanti_data.data[i].avatar.substring(1, this.partecipanti_data.data[i].avatar.length);
          }
          partecipanti.push(this.partecipanti_data.data[i]);
        }
        this.partecipants = partecipanti;  
      },

      next() {
        this.partecipants = null;
        var partecipanti = [];
        var size;

        this.partecipants = partecipanti;
        this.numPag = this.numPag + 1;

        this.existsBack = true;
        if((this.numPag*14) > this.partecipanti_data.data.length){
          this.existsNext = false;
          size = this.partecipanti_data.data.length;
          this.more = false;
        } else
          size = this.numPag*14;

        for(var i=((this.numPag-1)*14); i < size; i++){
           if(this.partecipanti_data.data[i].avatar[0]=='.'){
            this.partecipanti_data.data[i].avatar = "https://xelinion.servebeer.com/kp/" + this.partecipanti_data.data[i].avatar.substring(1, this.partecipanti_data.data[i].avatar.length);
          }
          partecipanti.push(this.partecipanti_data.data[i]);
        }
        this.partecipants = partecipanti;
      },

      back() {
        this.partecipants = null;
        var partecipanti = [];
        var size;

        this.more = true;
        if(this.numPag>=2)
          this.existsNext = true;
        if(this.numPag-1 ==1)
          this.existsBack = false;

        this.partecipants = partecipanti;
        this.numPag = this.numPag - 1;

        size = this.numPag*14;

        for(var i=size-14; i < size; i++){
          if(this.partecipanti_data.data[i].avatar[0]=='.'){
            this.partecipanti_data.data[i].avatar = "https://xelinion.servebeer.com/kp/" + this.partecipanti_data.data[i].avatar.substring(1, this.partecipanti_data.data[i].avatar.length);
          }
          partecipanti.push(this.partecipanti_data.data[i]);
        }
        this.partecipants = partecipanti;
      },

      async getPartecipanti(){
        let data = await axios.get("https://xelinion.servebeer.com/kp/getalljoiningsbyeventid?event_id=" + this.eventId)
        .then(function (data) {       
          return data;
        })
        .catch(function (error) {
          console.log(error);
        });
        return await data;

      },



      joining(){
        const t = this;
        axios.post("https://xelinion.servebeer.com/kp/newjoining", this.eventId, {
          headers: {
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
          }
        })
        .then(function (response) { 
          if(response.data.message == "Success"){
            var user = {
              username: JSON.parse(localStorage.getItem('user')).name,
              avatar: JSON.parse(localStorage.getItem('user')).image_URL
            }
            console.log(t.partecipanti_data.data);
            t.partecipanti_data.data.unshift(user);
            console.log(t.partecipanti_data.data);
            var size = 0;
            if(t.partecipanti_data.data.length <= 14){
              size = t.partecipanti_data.data.length;
              t.more = false;
              t.existsNext = false;
              t.existsBack = false;
            }
            else{
              size = 14;
            }
            var partecipanti = [];
            for(var i=0; i < size; i++){
              partecipanti.push(t.partecipanti_data.data[i]);
            }
            t.partecipants = partecipanti;

            t.$modal.hide('login-box');
            t.$awn.success('Now join the event !')
          } else{
            t.$awn.alert(response.data.message)
          }     
        })
        .catch(function (error) {
          t.$awn.error(error)
        });
      },

      deleteJoining(){
        const t = this;
        axios.post("https://xelinion.servebeer.com/kp/deletejoining", this.eventId, {
          headers: {
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
          }
        })
        .then(function (response) {
          if(response.data.message == "Success"){
            t.setPartecipanti();
            t.$modal.hide('login-box');
            t.$awn.success('Your participation is canceled !')
          } else{
            t.$awn.alert(response.data.message)
          }     

        })
        .catch(function (error) {
          t.$awn.error(error);
        });



      },




    },
    mounted() {
      this.setPartecipanti();

    },
}

</script>

<style scoped>
  #partecipants{
    display: flex;
    flex-wrap: wrap;
    text-align: center;
    justify-content: center;
  }

  #partecipants > img{
    width: 70px;
    height: 70px;
    margin: 5px;
    text-align: center;
    float: left;
  }

  #partecipants > div{
    width: 50px;
    height: 70px;
    margin: 5px;
    position: relative;
    float: left;
    text-align: center;
  }

  #partecipants > div p{
    position: absolute;
    bottom: 0;
    left: 20px;
    font-size: 30px;
  }

  .arrow{
    width: 20px;
    margin: 5px;
    align-self: flex-end;
  }

  .arrow > button{
    width: 20px;
    height: 20px;
    background-image: url("../assets/img/next.png");
    background-size: 20px 20px;
    border: none;
    background-color: none;
    border-radius: 10px;
    color:none;
  }

  .arrow > button:focus {
    color: #f1f1f1;
  }

  .arrow:nth-child(1) > button{
    background-image: url("../assets/img/back.png");
  }

  #tutto{
    display: flex;
    text-align: center;
    margin-bottom: 1rem;
  }

  .is-rounded {
        object-fit: cover;
        object-position: center center;
        height: 100%;
        width: 100%;
        border-radius: 250983px;
    }
 
</style>