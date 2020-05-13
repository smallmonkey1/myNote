#### 渲染进程报错

![image-20200425150039032](E:\Desktop\note\Electron\error.assets\image-20200425150039032.png)



```js
app.on("ready", () => {
    mainWindow = new browserWindow({ 
        width: 800, 
        height: 800, 
        webPreferences: { nodeIntegration: true } 
    })
    mainWindow.loadFile("index.html") // 加载 html 页面
    mainWindow.on("close", () => {
        mainWindow = null;
    });
});
```

原因：`webPreferences`  写成了 `webPrefences` ：拼写问题

---

### 加载 `electron` 模块的问题

错误代码

```js
var {menu} = require("electron");
....
var m = menu.buildFromTemplate(template);
menu.setApplicationMenu(m);
```

对照了一下网上的写法和源码之后，发现是对象名 `menu` 的问题

获取 electron 模块里面的模块的 方法目前我知道两个

```js
# 1.通过 .对象的方法，获取
# 这里的源码暂时不懂怎么看
var electron = require("electron");
var menu = electron.Menu;

// *********************************************************************

# 2.通过类似于 构造器 这种方法，集体返回一系列对象
# 但是获取到的对象的名字很有讲究，
const {app, Menu, BrowserWindow} = require("electron");

//**********************************************************************

# 至于这种方式返回的对象名怎么写，可以查看
# electron 源码 15 行--当前版本 ：v8.2.3
interface CommonInterface {
     app: App;
    autoUpdater: AutoUpdater;
    ......
    BrowserView: typeof BrowserView;
}
# 这里面列举了获取模块的标准名字，虽然他这里叫 接口，但是我怎么觉得有点构造器的味道
```

Label：加载报错，方法未找到，Menu，加载模块，模块的方法

---

### 顺序导致报错

```html
<script src="../render/demo3.js"></script>
<button id="childWindow"> open child Window! </button>
```

就因为 `script` 比 `html` 元素提前执行，导致在 `js` 中，可能是接收不到 `childWindow` 这个元素

























































































































































