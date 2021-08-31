<template>
  <div class="container-fluid">
    <div class="is-fullwidth">
      <button class="button is-success" @click="activeModal()">New Event</button>
    </div>
    <div class="columns is-centered">
      <div class="column has-text-centered">
        <h2>Select a date</h2>
        <DatePickerComponent @datePicked="onChildUpdate"></DatePickerComponent>
      </div>
      <div class="column">
        <h1 class="has-text-centered">Event Detail</h1>
        <article :key="event.description" v-for="event in events" class="message">
          <div class="message-header">
            <a v-on:click="goToEvent(event.event_id)">{{event.name}} - {{event.date.toLocaleDateString()}}</a>
            <button class="delete" aria-label="delete"></button>
          </div>
          <div class="message-body">
            {{event.description}}
          </div>
        </article>
      </div> 
    </div>
    <NewEventComponent />
  </div>
</template>


<script>
  import DatePickerComponent from '@/components/DatePickerComponent'
  import NewEventComponent from '@/components/NewEventComponent'
  import axios from "axios"

  export default {
    components: {
      DatePickerComponent,
      NewEventComponent
    },
    data() {
      return {
        date: null,
        events: null
      }
    },
    methods: {
      onChildUpdate(newValue) {
        this.date = newValue.date.toLocaleDateString()
        this.events = newValue.events
      },
      activeModal(){
        var modal = document.querySelector('.modal'); // assuming you have only 1
        modal.classList.add('is-active');
        modal.querySelector('.modal-background').addEventListener('click', function (e) {
          e.preventDefault();
          modal.classList.remove('is-active');
        });
      },
      async getEvent(eventId){
        let data = axios.get("https://xelinion.servebeer.com/kp/getevent?event_id=" + eventId, {
          headers: {
                'Accept': 'application/json'
            }
        }).then(function (response) {
          return response.data;
        }).catch(function (error) {
          console.log(error);
        });

        return await data;
      },
      async goToEvent(eventId){
        let event = await this.getEvent(eventId);
        event.landing_image = "https://xelinion.servebeer.com/kp/" + event.landing_image.substring(1, event.landing_image.length);
                event.created_at = this.timeConverter(event.created_at);
                event.event_when = this.timeConverterWhen(event.event_when);
        this.$router.push({ name: 'event', params: { event: event } });
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
      timeConverterWhen(UNIX_timestamp){
        var a = new Date(UNIX_timestamp * 1000);
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var date = a.getDate();
        var time = [date, month, year];
        return time;
      },
    },

  }
</script>

<style scoped>
  .is-fullwidth {
    margin: 0 auto;
    width: max-content;
  }
</style>