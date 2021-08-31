<template>
<section class="hero is-medium" >
    <div class="hero-body">
      <div class="container padded-big">
        <h1 class="title">
          {{title}}
        </h1>
        <h2 class="subtitle">
          {{subtitle}}
        </h2>
      </div>
    </div>
  </section>
</template>

<script>
export default {
   name: 'TheHero',
   props: {
      title: {
        type: String,
        required: true
      },
      subtitle: {
        type: String,
        required: true
      },
    },
}
</script>

<style scoped>
.hero {
  background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('../assets/img/agenda.jpg');
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  text-align:center;
}
.hero-body {
  padding: 1rem;  
}
.padded-big{
  padding:2rem;
}

.title{
  color:white;
}

.subtitle{
  color:white;
}

</style>
