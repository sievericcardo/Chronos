<template>
    <div>
        <TheNavbar />
        <TheHero :title="title" :subtitle="subtitle"/>
        <br>
        <br>
        <div class="content has-text-centered">
            <h1>Search for events near you</h1>
        </div>
        <div class="container">
            <div class="field has-addons searchbar is-fullwidth is-inline-block">
                <div class="is-inline-flex is-fullwidth">
                    <div class="control has-icons-left is-fullwidth autocomplete">
                        <input type="text" id="searchbar-input" v-model="message" class="input is-info is-large is-fullwidth">
                        <span class="icon is-medium is-left">
                  <i class="fas fa-search-location"></i>
                </span>
                    </div>
                    <div class="control">
                        <a class="button is-info is-large">Search</a>
                    </div>
                </div>
                <div class="is-hidden" id="dropdown-menu" role="menu">
                    <div class="dropdown-content" v-for="city in suggests" :key="city.city" @click="setCity(city)">
                        <a class="dropdown-item">
                            <div class="box">
                                <article class="media">
                                    <div class="media-left">
                                        <figure class="image is-48x48">
                                            <img src="@/assets/img/skyline.png" alt="Image">
                                        </figure>
                                    </div>
                                    <div class="media-content">
                                        <div class="content">
                                            <p>
                                                <strong>{{ city.city }}</strong> <br>
                                                <small>{{ city.country }}</small>
                                            </p>
                                        </div>
                                    </div>
                                </article>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <br>
        <NearEventComponent />
        <TheFooter />
    </div>
</template>



<script>
import NearEventComponent from '@/components/NearEventComponent'
import TheNavbar from '@/components/TheNavbar'
import TheHero from '@/components/TheHero'
import TheFooter from '@/components/TheFooter'
import axios from "axios";

export default {
    name: 'homepage',
    components: {
        TheNavbar,
        TheHero,
        NearEventComponent,
        TheFooter
    },
    data() {
        return {
            message: '',
            suggests: null,
            title: "Chonos", 
            subtitle: "Your planning made simple !"
        };
    },
    methods: {
        setCity(city) {
            this.message = city.city;
            this.suggests = [];
        }
    },
    watch: {
        message: {
            handler: function(val) {
                if (val.length % 3 == 0 && this.suggests == null) {
                    var h = "https://photon.komoot.de/api/?q=" + val + "&limit=20&lang=en";
                    axios.get(h, {
                        headers: {
                            'Accept': 'application/json'
                        }
                    }).then((response) => {
                        var f = response.data.features
                        var s = []
                        f.forEach(element => {
                            var obj = {
                                city: element.properties.city,
                                country: element.properties.country
                            }
                            if (obj.city != null && obj.country != null)
                                s.push(obj)
                        });

                        s = s.filter((elem, index, self) => self.findIndex(
                            (t) => {
                                return (t.city === elem.city)
                            }) === index)
                        this.suggests = s
                        document.getElementById("dropdown-menu").classList.remove("is-hidden");
                    }).catch(function(error) {
                        console.log(error);
                    })
                }
                this.suggests = null;
            }

        }
    }

}
</script>


<style scoped>
.main-color {
    background-color: #d7ab2a !important;
    border-color: transparent;
    color: #fff;
}

.box {
    padding: 0.2rem;
    box-shadow: none;
}

.dropdown-item {
    padding: 0.2rem;
}

* {
    font-family: 'Open Sans', sans-serif !important;
}

.is-secondary {
    background-color: #CCCDC8 !important;
}

.searchbar {
    margin-top: 2rem;
    margin-bottom: 1.5rem;
}

.search {
    height: 3rem;
}

.is-fullwidth {
    width: 100%;
}

.fa,
.far,
.fas {
    font-family: "Font Awesome 5 Pro" !important;
}
</style>