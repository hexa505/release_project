<template>
  <div class="HeaderView">
      <router-link to="/">Home</router-link> |
      <router-link to="/search">search</router-link> |
      <router-link to="/notifications">notifications</router-link> |
      <router-link to="/signup">signup</router-link> |
      <router-link to="/profile">profile</router-link> |
      <router-link :to="{ name: 'UserPageLayout', params: {username: 'vivi'}}">User page</router-link>
  </div>
</template>

<script>
export default {
  name: 'HeaderView'
}
</script>
<style>

</style>
