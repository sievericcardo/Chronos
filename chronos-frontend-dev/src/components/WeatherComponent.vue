<template>
    <div class="content is-medium weather" v-if="showMeteo">
        <h3>Meteo</h3>
            <div id = "meteo">
                <p>Previsioni: {{meteo}}</p>
                <img v-bind:src="icon_meteo"> 
                <p>Minima: {{minima_meteo}}</p>
                <p>Massima: {{massima_meteo}}</p>
            </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
    name: 'WeatherComponent',
    data() {
      return {
        meteo: null,
        minima_meteo: null,
        massima_meteo: null,
        icon_meteo: null,
        showMeteo:true
      };

    },
    props: {
      event: {
        type: Object,
        required: true
      }
    },
    methods: {
      setMeteo() {
        var months = {'Jan':0,'Feb':1,'Mar':2,'Apr':3,'May':4,'Jun':5,'Jul':6,'Aug':7,'Sep':8,'Oct':9,'Nov':10,'Dec':11};
        var date = new Date(this.event.when[2], months[this.event.when[1]], this.event.when[0]);
        var diff = Math.round(Math.abs(date - new Date())/1000/60/60/24);
        console.log(diff);
        if(diff<16){
          var res;
          var h = "https://api.weatherbit.io/v2.0/forecast/daily?&lat="+ this.event.lat+"&lon="+ this.event.lon+"&key=b6e8d477d55c44a3bc717c604cb12f93&lang=it";
                axios.get(h, {
                  headers: {
                    'Accept': 'application/json'
                  }
                }).then((response) => {
                  //l'indice adesso è casuale, poi sarà quello relativo al girno dell'evento
                      res = response.data.data[diff]
                      this.meteo =  res.weather.description
                      this.minima_meteo = res.min_temp + "°C"
                      this.massima_meteo = res.max_temp + "°C"
                      this.icon_meteo = "https://www.weatherbit.io/static/img/icons/" + res.weather.icon + ".png"
                      localStorage.setItem('prova', "ciao");
                  
                }).catch(function (error) {
                    console.log(error);
                })
        }else{
          this.showMeteo = false;
        }
      }

    },
    mounted() {
      this.setMeteo();
    },
}
</script>


<style scoped>
  #meteo img{
    float: left;
    margin-right: 1em;
  }

  @media screen and (min-width: 768px) {
    .weather {
      margin-top: 10rem;
    }
  }
</style>