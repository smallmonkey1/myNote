向路由组件传递参数的方式

```js
https://router.vuejs.org/zh/guide/essentials/passing-props.html

```



路由组件缓存

```html
<!--
这个结构的意思是缓存router-view内管理的所有组件
可选参数
include: 可以设置只缓存某些组件，通过组件的名字来标识。多个组件逗号隔开
exclude: 同理
-->
<keep-alive>
	<router-view/>
</keep-alive>
```

