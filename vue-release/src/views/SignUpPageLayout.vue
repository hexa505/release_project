<template>
    <div class="SignUpPageLayout">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome!"/>
    <sign-up-form/>
  </div>
</template>

<script>

import SignUpForm from '@/components/SignUpForm.vue'
export default {
  name: 'SignUpPageLayout',
  components: {
    SignUpForm
  }
}
</script>

<style>
</style>
