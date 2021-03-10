<template>
    <div class="EditAlbumPageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <publish-album-form>
  </div>
</template>

<script>
import PublishAlbumForm from '@/components/PublishAlbumForm.vue'
export default {
  name: 'EditAlbumPageLayout',
  components: {
    PublishAlbumForm
  }
}
</script>

<style>
</style>
