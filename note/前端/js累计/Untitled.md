##### 通过 `id` 获取元素

```js
var btn = document.querySelector("#btn");
var btn2 = this.document.querySelector("#btn");
```

##### 页面加载事件

```js
window.onload = () => {
	// 处理
}
window.onload=function(){
    
}
```

##### div内赋值

```js
var div = this.document.querySelector("#div");
 div.innerHTML = data;
```

##### 获取标签内元素的值 

比如 `id,class,href`等等

获取 a 标签的 `href` 

获取 `href`

```js
# getAttribute()
var cla=document.getElementById("btn").getAttribute("class");
```

###### 获取input的值

```js
document.querySelector("#input").value;
```

