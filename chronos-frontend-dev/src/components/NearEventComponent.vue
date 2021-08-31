<template>
    <div class="container">
        <p id="near-p">Upcoming events</p>
        <div class="columns is-8">

            <div class="column third">
                <div id = "eventCardComponents0">
                    <EventCardComponent v-for="event in eventCardComponents0" v-bind:key="event.name" :event="event"/>
                </div>
            </div>
            <div class="column third">
                <div id = "eventCardComponents1">
                    <EventCardComponent v-for="event in eventCardComponents1" v-bind:key="event.name" :event="event"/>
                </div>
            </div>
            <div class="column third">
                <div id = "eventCardComponents2">
                    <EventCardComponent v-for="event in eventCardComponents2" v-bind:key="event.name" :event="event"/>
                </div>
            </div>
            
        </div>
    </div>
</template>

<script>
import EventCardComponent from '@/components/EventCardComponent'
import axios from "axios";

export default {
    components: {
        EventCardComponent
    },
    data() {
      return {
        eventCardComponents0: null,
        eventCardComponents1: null,
        eventCardComponents2: null
      };
    },
    methods: {
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
        getEvent(){
        const t = this;
        axios.get("https://xelinion.servebeer.com/kp/getallevents", {
          headers: {
                'Accept': 'application/json'
            }
        }).then(function (response) {
            var eventi0 = [];
            var eventi1 = [];
            var eventi2 = [];
            
            for(var i=0; i<response.data.length; i++){
                response.data[i].user_avatar = "https://xelinion.servebeer.com/kp/" + response.data[i].user_avatar.substring(1, response.data[i].user_avatar.length);
                response.data[i].landing_image = "https://xelinion.servebeer.com/kp/" + response.data[i].landing_image.substring(1, response.data[i].landing_image.length);
                response.data[i].created_at = t.timeConverter(response.data[i].created_at);
                response.data[i].event_when = t.timeConverterWhen(response.data[i].event_when);
            if(i%3 == 0)
                eventi0.push(response.data[i]);
            else if(i%3 == 1)
                eventi1.push(response.data[i]);
            else
                eventi2.push(response.data[i]);
        }
            t.eventCardComponents0 = eventi0;
            t.eventCardComponents1 = eventi1;
            t.eventCardComponents2 = eventi2;
        }).catch(function (error) {
          console.log(error);
        });      
        },

    },
    mounted() {
        this.getEvent();
    },
}
</script>

<style>
@media (min-width: 1024px) {
    .third {
        padding: auto;
        max-width: 31vw;
    }
}

@media (min-width: 768px) and (max-width: 1023px) {
    .third {
        max-width: 47vw;
    }
}

@media (max-width: 767px) {
    .third {
        max-width: 80vw;
        margin: auto;
    }
}

.column {
    padding: 1rem;
}

.container {
    margin-top: 1rem;
}

#near-p {
    margin-bottom: 3rem;
    margin-left: 0.5rem;
    text-align: left;
    font-size: 2rem;
    font-weight: bold;
}
</style>