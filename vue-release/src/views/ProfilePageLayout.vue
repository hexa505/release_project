<template>
    <div class="ProfilePageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <profile-form/>
    <notification-list/>
  </div>
</template>

<script>
import ProfileForm from '../components/ProfileForm.vue'
export default {
  components: {
    ProfileForm
  },
  name: 'ProfilePageLayout'
}
</script>

<style>
</style>
