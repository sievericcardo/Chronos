<template>
  <span>
    <b-datepicker inline v-model="date" :events="events" :indicators="indicators">
    </b-datepicker>
  </span>
</template>

<script>
  import axios from "axios"
  export default {
    computed: {
      indicators() {
        return this.bars ? 'bars' : 'dots'
      }
    },
    data() {
      return {
        date: new Date(),
        events: null,
        bars: false
      }
    },
    methods:{
      async setEventJoining(){
        const events = await this.getEventJoining();
        var ev = [];
        for(var i=0; i < events.data.length; i++){
          var event = {
            event_id: events.data[i].event_id,
            name: events.data[i].name,
            description: events.data[i].description,
            date: this.transfortDate(events.data[i].event_when),
            type: 'is-info',
          }
          ev.push(event);
        }
        this.events = ev;
      },


      transfortDate(UNIX_timestamp) {
        var a = new Date(UNIX_timestamp * 1000);
        return new Date(a.getFullYear(), a.getMonth(), a.getDate());
      },

      async getEventJoining(){
        let data = await axios.get("https://xelinion.servebeer.com/kp/getalljoiningsbyuserid", {
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
    },
      mounted(){
        this.setEventJoining();
    },
    watch: {
      date: function (val) {
        var eventsOfThatDay = []
        //e gli devo mandare anche tutti gli eventi di quella data
        this.events.forEach((element) => {
          if (val.toDateString() == element.date.toDateString()) {
            eventsOfThatDay.push(element)
          }
        });
        this.$emit('datePicked', {
          date: val,
          events: eventsOfThatDay
        })
      }
    }
  }
</script>

<style scoped>
  .control {
    text-align: center !important;
  }
</style>
