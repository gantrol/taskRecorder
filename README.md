# Land-Manager-App

TODO: Assign company
TODO: Assign excutor
TODO: 上传图片
    exif
    compress

TODO: 任务列表页根据状态设右边drawable
TODO: 登出时将“计时器”归零
.
.
https://stackoverflow.com/questions/39953457/how-to-upload-image-file-in-retrofit-2

> Of course we usually use Picasso to load image, but sometimes we really need use Retrofit to load a special image (like fetch a captcha image)
>
> Retrofit is a REST library, you can use Retrofit only to get image URL but for displaying Image you should use Picasso: http://square.github.io/picasso/
>
> https://stackoverflow.com/questions/25462523/retrofit-api-to-retrieve-a-png-image
>
> https://androidclarified.com/android-image-upload-example/
>
> http://www.kotlintpoint.com/upload-image-camera-gallery-retrofit-android/

<https://cn.bing.com/search?q=andriod+login&go=Submit&qs=n&FORM=BESBTB&sp=-1&pq=123&sc=8-3&sk=&cvid=05E667B2DD474600AB19DA5B3268F48F&ensearch=1>

<http://www.cnblogs.com/dumuqiao/archive/2012/03/19/2405430.html>

<https://material.io/design/foundation-overview/#addition>

```
TODO: di of dagger2 https://www.jianshu.com/p/39d1df6c877d
TODO: 测试<https://developer.android.com/training/testing/>

Given -> When -> then

https://developer.android.com/studio/test
https://developer.android.com/training/testing/unit-testing/local-unit-tests#android-builders


例子？



https://developer.android.com/topic/libraries/data-binding/generated-binding

butterknife 中 list 控制用户访问的方式安全么？
https://github.com/JakeWharton/butterknife
https://www.journaldev.com/10439/android-butterknife-example
https://www.androidhive.info/2017/10/android-working-with-butterknife-viewbinding-library/

UI选择
https://stackoverflow.com/questions/40692214/changing-background-color-of-selected-item-in-recyclerview
https://material.io/develop/android/components/bottom-navigation-view/

          https://material.io/design/components/navigation-drawer.html#
          https://material.io/develop/android/components/navigation-view/
          https://material.io/develop/android/components/bottom-navigation-view/
          https://material.io/design/components/tabs.html#
          https://material.io/develop/android/components/tab-layout/
          https://developer.android.com/training/implementing-navigation/lateral.html#horizontal-paging
          UI
          http://jakewharton.github.io/butterknife/index.html
          https://material.io/design/components/bottom-navigation.html#specs

          https://material.io/develop/android/components/tab-layout/
          subtitle: https://stackoverflow.com/questions/37936323/navigation-drawer-menu-item-with-titles-and-sub-titles

          UI: https://stackoverflow.com/questions/36542343/navigation-drawer-add-titles-to-groups-not-items

          Fragment: https://developer.android.com/reference/android/app/Fragment
TODO: 每天学一些JAVA。。。
    http://interactivepython.org/runestone/static/JavaReview/index.html
```


- 需求分析
- 画一个产品原型，然后给出UI设计
- 模块划分、架构评审
- 利用API开发
- 测试

## 符号说明

### 优先级

- $^1:$ Must Have
- $^2:$ Should Have
- $^3:$ Could Have
- $^4:$ Won't Have

## 资源

- API

  - 土地管理系统 API<https://land.bigkeer.cn/swagger/index.html?tdsourcetag=s_pctim_aiomsg>
  - 开源中国<https://www.oschina.net/openapi>
    - <https://www.oschina.net/openapi/docs/oauth2_authorize>
- 远程仓库<https://code.lotlab.org/GDUT-Graduation/>
  - 后端<https://code.lotlab.org/GDUT-Graduation/Land-Manager-Backend>
  - 需求<https://code.lotlab.org/GDUT-Graduation/Land_Manage_Intro>
  - APP<https://code.lotlab.org/GDUT-Graduation/Land-Manager-App>
- 代码参考

  - android手册
  - android书
  - https://sourcey.com/articles/beautiful-android-login-and-signup-screens-with-material-design
  - <https://mobdevgroup.com/platform/android/project>
- UI参考

  - adobe

    - <https://helpx.adobe.com/cn/xd/how-to/what-is-xd.html>

      <https://helpx.adobe.com/cn/support/xd.html?promoid=3SH1B97W&mv=other>

      <https://www.adobe.com/cn/products/xd/resources.html>

      <https://www.materialpalette.com/icons>

  - android设计<https://developer.android.com/training/appbar>

    - <https://github.com/JakeWharton/butterknife>

  - 颜色

    - <https://material.io/design/color/applying-color-to-ui.html#top-bottom-app-bars>
    - <https://material.io/design/color/the-color-system.html#tools-for-picking-colors>

  - [页面设计网站](https://org.modao.cc/)

    - <https://www.mockplus.cn/blog/post/999>

  - 侧滑菜单<https://github.com/baoyongzhang/SwipeMenuListView>

  - 

## 用户管理

### 政府账号$^3$

### 公司账号$^1$

政府账号后台制作？

### 员工（普通）账号$^1$

## View：页面

### 学习UI$^1$

<https://helpx.adobe.com/cn/xd/how-to/what-is-xd.html>

<https://helpx.adobe.com/cn/support/xd.html?promoid=3SH1B97W&mv=other>

<https://www.adobe.com/cn/products/xd/resources.html>

<https://www.materialpalette.com/icons>

### icon

<https://github.com/domenic/svg2png>

```
svg2png ic_icon.svg -o ic_launcher.png --width=192 --height=192
svg2png ic_icon.svg -o ic_launcher.png --width=144 --height=144
svg2png ic_icon.svg -o ic_launcher.png --width=96 --height=96
svg2png ic_icon.svg -o ic_launcher.png --width=72 --height=72
svg2png ic_icon.svg -o ic_launcher.png --width=48 --height=48
```

```
svg2png round-bkg.svg -o ic_launcher_round.png --width=192 --height=192
svg2png round-bkg.svg -o ic_launcher_round.png --width=144 --height=144
svg2png round-bkg.svg -o ic_launcher_round.png --width=96 --height=96
svg2png round-bkg.svg -o ic_launcher_round.png --width=72 --height=72
svg2png round-bkg.svg -o ic_launcher_round.png --width=48 --height=48
```



[页面设计网站](https://org.modao.cc/)

### 登录页面

### 任务列表页

### 任务详情页

### 横屏功能？$^3$

现只做竖屏，横屏可以添加，但很麻烦，先不弄。

## 功能拆解

### 登录$^1$——登录页面

选这个，<http://codelightstudios.github.io/Android-Smart-Login/>

相关文件已经下载到`D:\app\Envir-monitor`

<https://sourcey.com/articles/beautiful-android-login-and-signup-screens-with-material-design>

### 任务管理$^1$——任务列表页|详情页

#### 任务内容$^1$

##### 任务


- 任务名称
- 负责公司
- 负责员工（多个？）
- 任务类型：三个场景
- 任务结果
- 任务地点

  - 初步调查：要调查的公司名称
  - ？
  - ？
- 采样点坐标【采样】

##### 任务结果

###### 初步调查

- 场地中心坐标
- 巡查路线记录(坐标, 时间戳, 人？)[==地图：保存路线，手机方向错？==]
- 拍照（分项，多张）
  - 访谈人
  - 座谈会议
  - 敏感对象
  - 地块现场

###### 采样布点之初布点

  - 设置初布点坐标
  - 初布点四方拍照记录（踏勘初布点情况）
    - 四方拍照
    - 实际坐标

###### 采样布点之采样

  - 采样点点四方拍照记录
      - 四方拍照
      - 实际坐标

详见<https://code.lotlab.org/GDUT-Graduation/Land_Manage_Intro/src/branch/master/README.md>

#### 任务操作

##### 政府$^3$

###### 发放

###### 接收

###### 驳回



##### 公司$^1$

###### 确认

###### 分配

###### 修改

###### 驳回

###### 完成



##### 员工$^1$

###### 确认

###### 填写

###### 完成



### 名字图标$^2$

名字候选：

- 调查快照？
- 毕业设计-土地管理系统APP