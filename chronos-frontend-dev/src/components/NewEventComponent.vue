<template>
  <div class="modal" name="note-box" ref="eventmodal" transition="pop-out" :adaptive="true">
    <div class="modal-background"></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">
          <span class="icon is-small is-left is-transparent">
            <i class="fas fa-calendar-plus fa-w-14"></i>
          </span><strong id="new-note-title">New Event</strong></p>
        <button class="delete" aria-label="close" @click="closeModal()"></button>
      </header>
      <section class="modal-card-body">
        <div class="field">
          <label class="label">Title</label>
          <div class="control has-icons-left has-icons-right">
            <input class="input" type="text" placeholder="Text input" v-model="title">
            <span class="icon is-small is-left">
              <i class="fas fa-heading fa-w-16"></i>
            </span>
            <span class="icon is-small is-right">
              <i class="fas fa-check"></i>
            </span>
          </div>
          <p class="help is-success"></p>
        </div>

        <div class="field">
          <label class="label">Location</label>
          <div class="control has-icons-left has-icons-right">
            <input class="input" type="text" placeholder="Text input" v-model="location">
            <span class="icon is-small is-left">
              <i class="fas fa-compass fa-w-16"></i>
            </span>
            <span class="icon is-small is-right">
              <i class="fas fa-check"></i>
            </span>
          </div>
          <p class="help is-success"></p>
        </div>

        <div class="field">
          <label class="label">Description</label>
          <div class="control">
            <textarea class="textarea" placeholder="Textarea" v-model="description"></textarea>
          </div>
        </div>



        
        <div class="field">
          <label class="label">When</label>
          <div class="control has-icons-left has-icons-right">
            <input class="input" type="date" v-model="date">
            <span class="icon is-small is-left">
              <i class="fas fa-calendar"></i>
            </span>
            <span class="icon is-small is-right">
              <i class="fas fa-check"></i>
            </span>
          </div>
          <p class="help is-success"></p>
        </div>
        <div class="field">
          <label class="label">Landing Image</label>
          <div class="file has-name">
            <label class="file-label">
              <input class="file-input" type="file" name="resume" @change="fileHandler()">
              <span class="file-cta">
                <span class="file-icon">
                  <i class="fas fa-upload"></i>
                </span>
                <span class="file-label">
                  Normal file…
                </span>
              </span>
              <span class="file-name">
                file
              </span>
            </label>
          </div>
        </div>
      </section>
      <footer class="modal-card-foot">
        <button class="button is-success" @click="saveChanges()">Save changes</button>
        <button class="button" @click="closeModal()">Cancel</button>
      </footer>
    </div>
  </div>
</template>

<script>
  import axios from "axios";

  export default {
    name: 'NewEventComponent',
    data() {
      return {
        title: null,
        location: null,
        description: null,
        file: 0,
        date: null,
        result: false
    }
    },
    created() {

    },
    methods: {
      closeModal() {
        this.$refs.eventmodal.classList.toggle("is-active");
      },
      async saveChanges() {
        const t = this;
       if(this.title.length!=0 & this.location.length!=0 & this.description.length!=0 & this.date.length!=0){
          var formData = new FormData();
          const fileInput = document.querySelector("input[type=file]");
          formData.append("name", this.title);
          formData.append("location", this.location);
          formData.append("description", this.description);
          formData.append("file", fileInput.files[0]);
          formData.append("event_when", Math.round(new Date(this.date).getTime() / 1000));

          const latlon = await this.getLanLong(this.location);
        
          formData.append("latitude", latlon.data[0].lat);
          formData.append("longitude", latlon.data[0].lon);
          formData.append("longitude", latlon.data[0].lon);
          
          axios.post("https://xelinion.servebeer.com/kp/newevent", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
              "Authorization": "Bearer " + JSON.parse(localStorage.getItem('user')).jwt
          }
              })
          .then(function (response) {
           if(response.data.message == "Success"){
              t.closeModal()
              t.$awn.success('New event added successfully')
            }
          })
          .catch(function (error) {
             t.closeModal()
            t.$awn.error(error)
          });
        }
      }, 
      async getLanLong(location){
        let data = await axios.get("https://nominatim.openstreetmap.org/search/" + location +"?format=json&addressdetails=1&limit=1&polygon_svg=1")
          .then(function (data) {
            return data;            
          })
          .catch(function (error) {
            console.log(error);
          });
        return await data;
      },

      fileHandler(){
        const fileInput = document.querySelector("input[type=file]");
        if (fileInput.files.length > 0) {
          const fileName = document.querySelector(".file-name");
          fileName.textContent = fileInput.files[0].name;
          let ext = fileInput.files[0].name.split(".").pop();
          let check =
                     fileInput.files[0].size < 800000 &&
                    fileInput.files[0].size > 100000 &&
                    (ext == "png" || ext == "jpg");
          if (check) {
            this.file = 1;
          } else {
            this.file = 2;
          }
        }
      },
    }
  }
</script>

<style scoped>
@import '~vue-awesome-notifications/dist/styles/style.css';
  .modal-background {
    background-color: none;
    opacity: 0.4;
  }

  #new-note-title {
    margin-left: 1rem;
  }

  .is-transparent {
    opacity: 0.2;
  }


</style>