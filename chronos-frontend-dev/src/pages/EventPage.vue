<template>
    <div>
        <TheNavbar />
        <TheHero :title="title" :subtitle="subtitle" :style="{ backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('${event.landing_image}')` }"/>
        <br>
        <div class="container-fluid">
            <div class="columns">
                <div class="column">
                    <div>
                        <p class="title">Quando:</p>
                        <div class="container-fluid">
                            <div class="level-item is-pulled-left">
                                <div class="notification">
                                    <p class="month">{{event.event_when[0]}} {{event.event_when[1]}} {{event.event_when[2]}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="content is-medium">
                        <br>
                        <br>
                        <br>
                        <p class="title">Dove:</p>
                        <p>{{event.location}}</p>
                    </div>
                    <div id="map">
                        <l-map :zoom="zoom" :center="center">
                            <l-tile-layer :url="url" :attribution="attribution"></l-tile-layer>
                            <l-marker :lat-lng="marker"></l-marker>
                        </l-map>
                    </div>
                    
                </div>
                <div class="column">
                    <div class="content is-medium">
                        <h2>Descrizione evento: </h2>
                        <p>{{event.description}}</p>
                        <WeatherComponent :event="eventForWeather"/>
                    </div>
                </div>
                <div class="column">
                    <ParticipantsComponent :eventId="String(event.event_id)"/>
                </div>
            </div>
            <div class="container-fluid">
                <div class="columns is-centered">
                    <div class="column is-four-fifths">
                        <h1 class="title">Thread</h1>
                        <div class="chat">
                            <div class="feed">
                                <SingleMessageComponent v-for="comment in Comments" v-bind:key="comment.user" :comment="comment"/>
                            </div>
                        </div>
                        <article class="media" v-if="image!=''">
                            <figure class="media-left">
                                <p class="image is-64x64">
                                    <img class="is-rounded" v-bind:src="image">
                                </p>
                            </figure>
                            <div class="media-content">
                                <div class="field">
                                    <p class="control">
                                        <textarea class="textarea" v-model="textComment" placeholder="Add a comment..."></textarea>
                                    </p>
                                </div>
                                <nav class="level">
                                    <div class="level-left">
                                        <div class="level-item">
                                            <a class="button is-info" @click="submit()">Submit</a>
                                        </div>
                                    </div>
                                    
                                </nav>
                            </div>
                        </article>
                    </div>
                </div>
            </div>
        </div>
        <TheFooter />
    </div>

</template>



<script>
    import TheNavbar from '@/components/TheNavbar'
    import TheHero from '@/components/TheHero'
    import TheFooter from '@/components/TheFooter'
    import SingleMessageComponent from '@/components/SingleMessageComponent'
    import WeatherComponent from '@/components/WeatherComponent'
    import ParticipantsComponent from '@/components/ParticipantsComponent'
    import L from 'leaflet';
    import axios from "axios";

    import {
        LMap,
        LTileLayer,
        LMarker
    } from 'vue2-leaflet';

    delete L.Icon.Default.prototype._getIconUrl

    L.Icon.Default.mergeOptions({
        iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
        iconUrl: require('leaflet/dist/images/marker-icon.png'),
        shadowUrl: require('leaflet/dist/images/marker-shadow.png')
    })

    export default {
        name: 'homepage',
        components: {
            TheNavbar,
            TheHero,
            TheFooter,
            LMap,
            LTileLayer,
            LMarker,
            WeatherComponent,
            ParticipantsComponent,
            SingleMessageComponent
        },   
        data() {
            return {
                zoom: 5,
                url: 'https://{s}.tile.osm.org/{z}/{x}/{y}.png',
                attribution: '&copy; <a href="https://osm.org/copyright">OpenStreetMap</a> contributors',
                textComment: "",
                event: this.$attrs.event,
                center: L.latLng(this.$attrs.event.latitude, this.$attrs.event.longitude),
                marker: L.latLng(this.$attrs.event.latitude, this.$attrs.event.longitude),
                title: this.$attrs.event.name, 
                subtitle: "",
                Comments: null,
                eventForWeather: {lat:this.$attrs.event.latitude, lon: this.$attrs.event.longitude, when: this.$attrs.event.event_when},
                get image() {
                    if(localStorage.getItem('user')!=null)
                        return JSON.parse(localStorage.getItem('user')).image_URL;
                    else return "";
                },
            };
        },
        methods: {
            getComment(){
                const t = this;
                axios.get("https://xelinion.servebeer.com/kp/getallcommentsbyeventid?event_id=" + t.event.event_id,{
                    headers: {
                        'Accept': 'application/json'
                    }
                })
                .then(function (response) {
                        for(var i=0; i<response.data.length; i++){
                            if(response.data[i].user_avatar[0]=='.'){
                                response.data[i].user_avatar = "https://xelinion.servebeer.com/kp" + response.data[i].user_avatar.substring(1, response.data[i].user_avatar.length);
                            }
                            response.data[i].created_at = t.timeConverter(response.data[i].created_at);
                        } 
                        t.Comments = response.data;          
                    }).catch(function (error) {
                        console.log(error);
                    });
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
            submit(){
                if(this.textComment.length != 0){
                    const t = this;
                    axios.post("https://xelinion.servebeer.com/kp/newcomment", {
                        body: this.textComment,
                        event_id: this.event.event_id,
                        created_at: Math.round(Date.now()/1000)
                    }, {
                        headers: {
                            "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
                        }
                    })
                    .then(function (response) {
                        console.log(response); 
                        var comment = {
                            username: JSON.parse(localStorage.getItem('user')).name,
                            user_avatar: JSON.parse(localStorage.getItem('user')).image_URL,
                            body: t.textComment,
                            created_at: t.timeConverter(Math.round(Date.now()/1000))
                        };
                        t.Comments.push(comment);  
                        t.textComment = "";

                    }).catch(function (error) {
                        console.log(error);
                    });
                }
            },
            onStorageUpdate() {
                if(localStorage.getItem('user')!=null){
                    this.image = JSON.parse(localStorage.getItem('user')).image_URL;
                }
            },

        },
        mounted() {
            this.getComment();
        },
        watch: {
            image (){
                if(localStorage.getItem('user')==null)
                    this.image = "";
                else
                    this.image = JSON.parse(localStorage.getItem('user')).image_URL;
            }
        }
    }
</script>


<style scoped>
@import '~leaflet/dist/leaflet.css';
    
    /* .hero {
        background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('../assets/img/fuji.jpg');
    } */

    .col-6 {
        padding: 1rem;
    }

    .main-color {
        background-color: #d7ab2a !important;
        border-color: transparent;
        color: #fff;
    }

    #map {
        width: 100%;
        height: 20rem;
        margin-bottom: 2rem;
        
    }

    .vue2leaflet-map{
        z-index: 1;
    }

    * {
        font-family: 'Open Sans', sans-serif !important;
    }

    .chat {
        padding: 1rem;
    }

    .is-secondary {
        background-color: #CCCDC8 !important;
    }

    .control {
        margin: 0 auto;
    }

    .column {
        padding: 3rem;
    }

    .is-rounded {
        object-fit: cover;
        object-position: center center;
        height: 100%;
        width: 100%;
        border-radius: 250983px;
    }

    #meteo img {
        float: left;
        margin-right: 1em;
    }

</style>