<template>
    <div>
        <TheNavbar />
        <section class="hero">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">
                        {{name}}
                    </h1>
                    <h2>Personal Profile</h2>
                </div>
            </div>
        </section>
        <br>
        <div class="container">
            <div class="tabs is-centered">
                <ul>
                    <li v-bind:class="{ 'is-active': userprofiledata }" @click="makeUserProfileActive()"><a>User Profile</a></li>
                    <li v-bind:class="{ 'is-active': userevents }" @click="makeUserEventsActive()"><a>My Events</a></li>
                    <li v-bind:class="{ 'is-active': usernotes }" @click="makeUserNotesActive()"> <a>My Notes</a></li>
                </ul>
            </div>
            <UserProfileComponent v-if="userprofiledata" />
            <UserProfileEventsComponent v-if="userevents" />
            <UserProfileMyNotesComponent v-if="usernotes" />
        </div>
        <br>
        <TheFooter />
    </div>
</template>

<script>
import TheNavbar from '@/components/TheNavbar'
import TheFooter from '@/components/TheFooter'
import UserProfileComponent from '@/components/UserProfileComponent'
import UserProfileEventsComponent from '@/components/UserProfileEventsComponent'
import UserProfileMyNotesComponent from '@/components/UserProfileMyNotesComponent'


export default {
    name: 'profilepage',
    components: {
        TheNavbar,
        TheFooter,
        UserProfileComponent,
        UserProfileEventsComponent,
        UserProfileMyNotesComponent
    },
    data() {
        return {
            userprofiledata: true,
            userevents: false,
            usernotes: false,
            name: JSON.parse(localStorage.getItem('user')).name
        };
    },
    methods: {
        makeUserProfileActive() {
            this.userprofiledata = true
            this.userevents = false
            this.usernotes = false
        },
        makeUserEventsActive() {
            this.userprofiledata = false
            this.userevents = true
            this.usernotes = false
        },
        makeUserNotesActive() {
            this.userprofiledata = false
            this.userevents = false
            this.usernotes = true
        },
    }
}
</script>

<style scoped>
* {
    font-family: 'Open Sans', sans-serif !important;
}

.hero {
    padding: 1rem;
    background-color: #D8AE32 !important;
    text-align: center;
}

#main-title1 {
    font-size: 2rem;
    color: #D8AE32 !important;
}

#main-title2 {
    font-size: 2rem;
    color: #202625 !important;
}

.brand-logo {
    text-decoration: none;
    color: black !important;
}

#fcolor {
    font-size: 2rem;
    color: #D8AE32 !important;
}

.simple-link:hover {
    color: #D8AE32 !important;
}

#scolor {
    font-size: 2rem;
    color: #202625 !important;
}

.main-color {
    background-color: #d7ab2a !important;
    border-color: transparent;
    color: #fff;
}

.img-logo {
    vertical-align: middle;
    max-width: 3rem;
    max-height: 3rem;
}

.is-secondary {
    background-color: #CCCDC8 !important;
}

.title {
    color: white;
}

.subtitle {
    color: white;
}

.footer {
    padding: 2rem;
}

.is-three-quarters {
    margin: 0 auto;
}

.padded {
    padding: 1rem;
}
</style>