<template>
  <div class="HomePageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <album-list/>
  </div>
</template>

<script>
// @ is an alias to /src
import AlbumList from '../components/AlbumList.vue'
export default {
  name: 'HomePageLayout',
  components: {
    AlbumList
  }
}
</script>
