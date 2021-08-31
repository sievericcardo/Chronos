<template>
  <div class="card">
    <div class="card-image">
      <figure class="image is-4by3">
        <img v-bind:src="event.landing_image" alt="Placeholder image" :event="event">
      </figure>
    </div>
    <div class="card-content">
      <p class="is-size-5 event-title">
         <router-link :to="{name: 'event', params: {event: event} }">{{event.name}}</router-link>   
      </p>
      <br>
      <div class="columns">
        <div class="col-7">

          <div class="content justified ">
            <p>{{description}}</p>
          </div>

          <div class="container is-inline-flex centered">
              <img v-bind:src="event.user_avatar" class="is-rounded" alt="Placeholder image">
            <div>
              <p class="is-bold">{{event.username}}</p>
              <time datetime="2016-1-1">{{event.created_at}}</time>
            </div>
          </div>

        </div>
        <div class="col-3">
          <div>
            <div class="level-item has-text-centered">
              <div class="notification">
                <p class="day">{{event.event_when[0]}}</p>
                <p class="month">{{event.event_when[1]}}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    props: {
      event: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        description: ""
      }
    },
    methods: {
      getDesctiption(){
        var description;
        if(this.event.description.length >= 80)
          description = this.event.description.substring(0, 80) + "...";
        else{
          description = this.event.description;
        }
        this.description = description;
      },
      
    },
    mounted() {
        this.getDesctiption();
      },
}
</script>

<style>
  img {
    vertical-align: middle;
  }

  p {
    vertical-align: middle;
  }

  .day {
    font-size: 2.75rem;
    color: #202625 !important;
  }

  .month {
    font-size: 1.5rem;
    color: #202625 !important;
  }

  .col-7 {
    padding-right: 1rem;
  }

  .col-3 {
    padding-left: 1rem;
  }

  .notification {
    padding: 1rem !important;
  }

  .card {
    margin-bottom: 1rem !important;
  }

  .card-content {
    padding: 1rem !important;
  }

  .columns {
    padding: 1rem;
  }

  .centered {
    align-items: center;
  }

  .event-title {
    font-weight: bold;
  }

  .is-bold {
    font-weight: bold;
  }

  .justified {
    text-align: justify;
    text-justify: inter-character !important;
  }

  .is-rounded{
    margin-right: 10px;
        object-fit: cover;
    object-position: center center;
    height: 48px;
    width: 48px;
    border-radius: 250983px;
  }
</style>