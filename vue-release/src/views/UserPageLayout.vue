<template>
    <div class="UserPageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <profile-view/>
    <tag-list/>
    <album-list/>
  </div>
</template>

<script>
import TagList from '../components/TagList.vue'
import AlbumList from '../components/AlbumList.vue'
import ProfileView from '../components/ProfileView.vue'
export default {
  components: { TagList, AlbumList, ProfileView },
  name: 'UserPageLayout'
}
</script>

<style>
</style>
