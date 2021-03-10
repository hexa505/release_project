<template>
    <div class="NotificationPageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <notification-list/>
  </div>
</template>

<script>
import NotificationList from '../components/NotificationList.vue'
export default {
  components: { NotificationList },
  name: 'NotificationPageLayout'
}
</script>

<style>
</style>
