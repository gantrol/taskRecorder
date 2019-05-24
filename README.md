# Task Recorder: Tool for Environmental Site Investigation

## Background

一个为实现了土壤环境调查的部分过程的安卓应用，目前已初步实现初步调查部分。

## Base

- [android develop](https://developer.android.com/)
  - [Guide](https://developer.android.com/guide)
- android and java, book or website
  - https://forums.bignerdranch.com/c/android-programming-3rd-edition
  - [Java Review](<https://runestone.academy/runestone/static/JavaReview/index.html>)
  - [Other Tutorials]
    - [vogella](https://www.vogella.com/tutorials/android.html)
    - [Android Pub](https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a)
- Resource
  - [StackOverFlow](https://stackoverflow.com/)(hint: It may better to search by Bing or Google with "\<keyword\> site: stackoverflow.com/", instead of searching in stackoverflow directly .)
  - [github](github.com)(世界最大的同性交友平台，拥有的资源不是盖的)
    - [gson](https://github.com/google/gson)
    - [square](https://github.com/square)
    - [Google architectural tools and patterns sample on Github](https://github.com/googlesamples/android-architecture)
  - [mobdevgroup](https://mobdevgroup.com/platform/android/project)
  - API
    - 开源中国
      - <https://www.oschina.net/openapi>
      - <https://www.oschina.net/openapi/docs/oauth2_authorize>
    - ...
- tool
  - Adobe XD
  - Translator for Chinese and English(下列两款在短语翻译方面做得不错)
    - [腾讯翻译君](https://fanyi.qq.com/translateapi)
    - [Transmart](https://transmart.qq.com/index)


## Tech

### 流程

- 需求分析
- 画一个产品原型，然后给出UI设计
- 模块划分、架构评审
- 利用API开发
- 测试

### UI

- Application:
  - [Adobe XD](<https://www.adobe.com/products/xd.html>): free and easy to [learn](https://letsxd.com/);



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

### google arch

以[google arch](https://developer.android.com/jetpack/docs/guide)为主题，选取~自己能理解的~部分：

1. retrofit2
2. dao
3. data-binding

#### data-binding

https://developer.android.com/topic/libraries/data-binding/generated-binding

butterknife 中 list 控制用户访问的方式安全么？
https://github.com/JakeWharton/butterknife
https://www.journaldev.com/10439/android-butterknife-example
https://www.androidhive.info/2017/10/android-working-with-butterknife-viewbinding-library/

### TODO

TODO: di of dagger2 https://www.jianshu.com/p/39d1df6c877d
TODO: 测试<https://developer.android.com/training/testing/>
TODO: 每天学一些JAVA。。。
    http://interactivepython.org/runestone/static/JavaReview/index.html

Given -> When -> then

https://developer.android.com/studio/test
https://developer.android.com/training/testing/unit-testing/local-unit-tests#android-builders


## 具体问题

### 图片



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
