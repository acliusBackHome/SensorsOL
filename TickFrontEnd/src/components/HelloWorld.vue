<template>
<div id="chartCan">
  <vue-hightcharts :options="options" ref="splineCharts"></vue-hightcharts>
  <se-menu
    style="position:fixed;left:0;top:0;"
    :seset="menuSet"
    ></se-menu>
</div>
</template>

<script>
import vueHightcharts from 'vue2-highcharts';
import seMenu from './Menu.vue';
export default {
  name: 'HelloWorld',
  components: {
    vueHightcharts,
    seMenu
  },
  data () {
    return {
      menuSet: [],
      pool: [],
      socket: {
      },
      msg: 'Welcome to Your Vue.js App',
      options: {
        chart: {
          type: 'spline'
        },
        credits: {
          enabled: false
        },
        title: {
          text: 'sensors watcher'
        },
        subtitle: {
          text: '.tick'
        },
        xAxis: {
          reversed: false,
          title: {
            enabled: true,
            text: 'time'
          },
          labels: {
            formatter: function () {
              return this.value;
            }
          },
          maxPadding: 0.05,
          showLastLabel: true
        },
        yAxis: {
          title: {
            text: 'value'
          },
          labels: {
            formatter: function () {
              return this.value;
            }
          },
          lineWidth: 2
        },
        plotOptions: {
          spline: {
            marker: {
              radius: 4,
              lineColor: '#66ccff',
              lineWidth: 1
            }
          }
        },
        series: [[]]
      }
    }
  },
  methods: {
  },
  mounted: function () {
    this.$nextTick(function () {
      console.log(this.$route.params);
      // let ref = this.$refs.splineCharts;
      // ref.delegateMethod('showLoading', 'Loading...');
    })
  },
  sockets: {
    connect: function () {
      console.log('connect');
      this.$socket.emit('hi');
    },
    news: function (val) {
      console.log(val);
    },
    sensors: function (val) {
      val = JSON.parse(val);
      let ch = this.$refs.splineCharts;
      const selectSe = this.$route.params.se;
      const selectCom = this.$route.params.com;
      this.pool.push(val);
      let menuNode = {
        item: val.item,
        com: []
      };
      for (let i of val.data) {
        menuNode.com.push(i.name);
      }
      menuNode = JSON.stringify(menuNode);
      if (this.menuSet.indexOf(menuNode) === -1) this.menuSet.push(menuNode);
      if (val.item.toString() !== selectSe.toString()) return;
      const node = [
        val.time * 1,
        val.data.filter(function (v) {
          return v.name.toString() === selectCom;
        })[0].value * 1
      ];
      console.log(node);
      ch.getChart().series[0].addPoint(node);
    }
  },
  watch: {
    '$route': function () {
      this.$refs.splineCharts.getChart().series[0].setData([]);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
#chartCan {
  width: 100%;
}
</style>
