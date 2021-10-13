# StateX


<img src="https://tva1.sinaimg.cn/large/008i3skNly1gvdyqjncaxj60vy0jqq3n02.jpg" alt="image-20211013203710758" width="700" />


[![](https://jitpack.io/v/Petterpx/StateX.svg)](https://jitpack.io/#Petterpx/StateX) [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) 

简洁易用的 Android 状态页处理库🍃。
<div align=center>
  <img src="https://github.com/Petterpx/StateX/blob/main/statex_simple.gif" width="200"/>
</div>

## 功能

- 支持compose与View,分层设计
- 完善的Api支持
- 支持全局/局部配置
- 支持常见的状态缺省页
- 支持配置全局点击重试

## 依赖方式

### 添加jitpack仓库

#### build.gradle

**Gradle7.0以下**

```groovy
allprojects {
		repositories {
			// ...
			maven { url 'https://jitpack.io' }
		}
}
```

> AndroidStudio-Arctic Fox && Gradle7.0+,并且已经对依赖方式进行过调整，则可能需要添加到如下位置：
>
> **settings.gradle**
>
> dependencyResolutionManagement {
>     repositories {
>
> ​        // ...
> ​        maven { url 'https://jitpack.io' }
> ​    }
> }

### Gradle

```groovy
implementation 'com.github.Petterpx:StateX:1.0-beta03'
```

> 如果您的项目中暂时只用到了View或者是一个完全使用Compose重写的项目，可以参照下述方式引入，即可减少不必要的导入。

##### View中单独引入

```groovy
implementation 'com.github.Petterpx.StateX:statex:1.0-beta03'
implementation 'com.github.Petterpx.StateX:statex-view:1.0-beta03'
```

##### compose中单独引入

```groovy
implementation 'com.github.Petterpx.StateX:statex:1.0-beta03'
implementation 'com.github.Petterpx.StateX:statex-compose:1.0-beta03'
```

#### 注意

StateX 不会引入 `任何` 第三方库,即意味着您需要手动导入kotlin库，如果是在compose中使用，您需要导入compose相关组件，这样可以避免不必要的冲突。

```groovy
// kotlin
implementation 'androidx.core:core-ktx:1.6.0'

// compose
implementation "androidx.compose.ui:ui:1.0.2"
implementation "androidx.compose.material:material:1.0.2"
implementation "androidx.compose.runtime:runtime:1.0.2"
implementation "androidx.compose.ui:ui-tooling-preview:1.0.2"
```

#### 混淆

一般情况下，框架内部无需混淆。

