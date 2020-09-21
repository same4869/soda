## SODA --- 致力于快速开发，一系列轮子
> 在安卓日常开发过程中，尤其是从0到1的app搭建中，总是需要很多业务无关的工具（框架）支持，比如日志打印，网络请求，webview，UI框架等等，设计这些轮子网络需要花费很多时间，且对于多个app来说，明显是重复的工作。SODA专注于封装与集成这些轮子，致力于开发时的”拿来主义“，让开始的时候更加专注业务，提高开发效率。

### 1.能力总览

#### `sodaability`: 
- 通用下载模块
- 基于tinker的热修复方案及常用业务逻辑

#### `sodalibrary `: 
- MVP模式的规范架构框架SodaClean
- 通用线程池SodaExecutor
- 通用日志打印器SodaLog
- api mock组件（基于retrofit）
- 常用工具（文件工具类，sp工具类）
- native签名校验与获得字符串

#### `sodaui`:
- 通用轮播图SodaBanner
- 常规recyclerview与adapter的写法规范
- 通用下拉刷新SodaRefreshLayout
- 通用SodaTabTopLayout与SodaTabBottomLayout

### 2.项目结构

- `app`: 各种轮子的demo示例，尽可能全地演示各种库的使用方式。
- `sodaability`: 封装了一些具有独立业务能力的框架。
- `sodalibrary`: 封装了一些比较通用的非UI组件。
- `sodaui`: 封装了一些UI相关的常用控件 