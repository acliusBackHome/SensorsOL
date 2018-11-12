<template>
  <div id="menu-box">
    <v-navigation-drawer
      v-model="drawer"
      absolute
      temporary
      >
      <v-list>
      <v-list-group
        no-action
        sub-group
        v-for="(item, index) in seset"
        :key="index"
        >
        <v-list-tile
          slot="activator"
          >
          <v-list-tile-title>{{JSON.parse(item).item}}</v-list-tile-title>
        </v-list-tile>
        <v-list-tile
          v-for="(it, id) in JSON.parse(item).com"
          :key="id"
          @click="c(JSON.parse(item).item, it)">
          <v-list-tile-title>
            {{it}}
          </v-list-tile-title>
          <v-list-tile-action>
            <v-icon v-text="'settings'"></v-icon>
          </v-list-tile-action>
        </v-list-tile>
      </v-list-group>
      </v-list>
    </v-navigation-drawer>
    <v-btn
      color="pink"
      @click.stop="drawer = !drawer"
      style="position:fixed;right:2em;bottom:2em;"
      >
      Menu
    </v-btn>
  </div>
</template>
<script>
export default {
  name: 'seMenu',
  props: ['seset'],
  data () {
    return {
      drawer: false
    };
  },
  methods: {
    c (a, b) {
      console.log(a, b);
      this.$router.push({path: '/' + a + '/' + b});
    }
  }
}
</script>
<style>
#menu-box {
  width: 100%;
  height: 100%;
}
</style>
