

```js
store：组件与vuex通信的一个桥梁对象
state:
getters:
dispatch(): 
	调用actions时使用 例如  this.$store.dispatch('xxx')
	
commit()：
	调用mutations时使用  例如this.$store.commit('xxx')
```



store的配置文件

```typescript
/**
 * vuex最核心的管理对象store
 */
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

//相当于data对象
const state = {
    count: 1, // 初始化状态数据
}
/**
 * 包含n个用于直接更新状态数据的方法的对象
 * 在mutations中最好不要有逻辑或异步代码
 */
const mutations = {
    INCREMENT(state) {
        state.count++
    },
    DECREMENT(state) {
        state.count--
    }
}

/**
 * 调用mutaions的地方
 * 在action中可以包含逻辑或异步代码
 */
const actions = {
    increment(context) {
        // commit给mutations
        context.commit('INCREMENT')
    },
    decrement({ commit, state }) {
        console.log('状态数据---》', state)
        commit('DECREMENT')
    }
}
/**
 * 包含n个基于state数据的getter计算属性的方法
 */
const getters = {
    evenOrOdd: state => state.count%2==1?'奇数':'区苏'
    
}
export default new Vuex.Store({
    state,
    mutations,
    actions,
    getters
})
```



vuex使用

```vue
<template>
  <div>
    <div>{{$store.state.count}}</div>
    <div>{{$store.getters.evenOrOdd}}</div>
    <button @click="addOne">add 1</button>
    <button @click="subOne">sub 1</button>
  </div>
</template>

<script>
export default {
  mounted () {
    console.log(this.$store)
  },
  methods: {
    addOne() {
      this.$store.dispatch('increment')
    },
    subOne() {
      this.$store.dispatch('decrement')
    }
  }
};
</script>

<style scoped>
</style>
```

