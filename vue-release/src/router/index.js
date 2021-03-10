import Vue from 'vue'
import VueRouter from 'vue-router'
import HomePageLayout from '../views/HomePageLayout.vue'
import SearchPageLayout from '../views/SearchPageLayout.vue'
import NotificationPageLayout from '../views/NotificationPageLayout.vue'
import SignUpPageLayout from '../views/SignUpPageLayout.vue'
import UserPageLayout from '../views/UserPageLayout.vue'
import PublishAlbumPageLayout from '../views/PublishAlbumPageLayout.vue'
import ProfilePageLayout from '../views/ProfilePageLayout.vue'
import EditAlbumPageLayout from '../views/EditAlbumPageLayout.vue'
Vue.use(VueRouter)
const routes = [
  {
    path: '/',
    name: 'HomePageLayout',
    component: HomePageLayout
  },
  {
    path: '/search',
    name: 'SearchPageLayout',
    component: SearchPageLayout
  },
  {
    path: '/notifications',
    name: 'NotificationPageLayout',
    component: NotificationPageLayout
  },
  {
    path: '/signup',
    name: 'SignUpPageLayout',
    component: SignUpPageLayout
  },
  {
    path: '/member/@:username',
    name: 'UserPageLayout',
    component: UserPageLayout
  },
  {
    path: '/publish',
    name: 'PublishAlbumPageLayout',
    component: PublishAlbumPageLayout
  },
  {
    path: '/edit/:albumid',
    name: 'EditAlbumPageLayout',
    component: EditAlbumPageLayout
  },
  {
    path: '/profile',
    name: 'ProfilePageLayout',
    component: ProfilePageLayout
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
