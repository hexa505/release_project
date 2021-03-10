<template>
  <div class="ProfileView">
      <router-link to="/publish">Publish Album</router-link> |
      <router-link :to="{ name: 'EditAlbumPageLayout', params: { albumid: 123 }}">Edit Album</router-link> |
      <router-link to="/profile">profile</router-link>
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>
