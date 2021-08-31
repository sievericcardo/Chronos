import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '@/pages/HomePage'
import EventPage from '@/pages/EventPage'
import ProfilePage from '@/pages/ProfilePage'

Vue.use(Router)

const router = new Router({
    routes: [{
        path: '/',
        component: HomePage
    },
    {
        path: '/event',
        name: 'event',
        component: EventPage,
        props: true
    },
    {
        path: '/user',
        component: ProfilePage
    }
    ]
});

export default router