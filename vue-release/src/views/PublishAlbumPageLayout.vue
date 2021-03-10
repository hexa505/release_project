<template>
    <div class="PublishAlbumPageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <publish-album-form/>
  </div>
</template>

<script>

import PublishAlbumForm from '@/components/PublishAlbumForm.vue'
export default {
  name: 'PublishAlbumPageLayout',
  components: {
    PublishAlbumForm
  }
}
</script>

<style>
</style>
