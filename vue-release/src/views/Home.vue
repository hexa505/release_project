<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <UploadTest/>
    <HelloWorld msg="Welcome!"/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import UploadTest from '@/components/UploadTest.vue'
export default {
  name: 'Home',
  components: {
    HelloWorld,
    UploadTest
  }
}
</script>
