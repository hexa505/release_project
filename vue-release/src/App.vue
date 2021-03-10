<template>
  <div id="app">
    <div id="nav">
      <HeaderView/>
    </div>
    <router-view/>
  </div>
</template>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script>
import HeaderView from '@/views/HeaderView.vue'
export default {
  components: {
    HeaderView
  }
}
</script>

<style lang="stylus">
#app
  font-family Avenir, Helvetica, Arial, sans-serif
  -webkit-font-smoothing antialiased
  -moz-osx-font-smoothing grayscale
  text-align center
  color #2c3e50
  margin-top 60px
</style>
