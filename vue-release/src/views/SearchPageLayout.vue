<template>
    <div class="SearchPageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <album-list/>
  </div>
</template>

<script>
import AlbumList from '@/components/AlbumList.vue'
export default {
  name: 'SearchPageLayout',
  components: {
    AlbumList
  }
}
</script>

<style>
</style>
