<template>
  <article class="media">
  <figure class="media-left">
    <p class="image is-64x64">
      <img class="is-rounded" v-bind:src="comment.user_avatar">
    </p>
  </figure>
  <div class="media-content">
    <div class="content">
      <p>
        <strong>{{comment.username}}</strong> <small>{{comment.created_at}}</small>
        <br>
        {{comment.body}}
      </p>
    </div>
    <nav class="level is-mobile">
      <div class="level-left">
        <a class="level-item">
          <span class="icon is-small"><i class="fas fa-reply"></i></span>
        </a>
        <a class="level-item">
          <span class="icon is-small"><i class="fas fa-retweet"></i></span>
        </a>
        <a class="level-item">
          <span class="icon is-small"><i class="fas fa-heart"></i></span>
        </a>
      </div>
    </nav>
  </div>
</article>
</template>

<script>
export default {
  props: {
      comment: {
        type: Object,
        required: true
      }
    },
}
</script>

<style scoped>

.card{
    border-radius: 1rem;
    margin:1rem;
}

.is-rounded{
    object-fit: cover;
      object-position: center center;
      height: 100%;
      width: 100%;
      border-radius: 250983px;
}

      

</style>
