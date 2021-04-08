# 1.vue 使用 vue-echarts插件报错

```java
vue getboundingclientrect()无效
```

对着图表滚轮时报错，并且修改option内容图表没变化

解决：

给他加个监听器

```js

mounted () {
    window.addEventListener('scroll', this.handleScroll, true)
  },
```



2. # `computed` 定义过的属性不需要在 `data` 中再次定义



```java

data() {
    return {
        todos: [
            {id: 1, title: 'A', complete: false},
            {id: 2, title: 'B', complete: false},
            {id: 3, title: 'C', complete: true},
            {id: 4, title: 'D', complete: false},
        ],
        // 这个不能定义，要不然会出问题
        // isComplete: 0,
    };
},
computed: {
    isComplete() {
        let count = 0;
        for (let i = 0; i < this.todos.length; i++) {
            console.log('是否完成图', this.todos[i].complete)
                if (this.todos[i].complete) count+=1;
        }
        return count;
    }
},

```

