## demo 介绍

- 包名`pool`：JDK标准线程池`ThreadPoolExecutor`的演示demo。
- 包名`pooltask`：`Spring`包装的`ThreadPoolExecutor`的演示demo。
- 包名`forkjoin`：JDK标准工作窃取线程池`ForkJoinPool`的演示demo。
- 包名`cf`：`CompletableFuture`异步任务的编排框架的用法。

## Spring 在为 @Async 寻找默认执行器时遵循一个明确的查找链

- 第一优先级：AsyncConfigurer.getAsyncExecutor() 方法的返回值。这是 Spring 首先、也是最明确寻找的地方。
- 第二优先级：查找容器中是否存在名为 taskExecutor 的 Executor 类型 Bean。
- 第三优先级：查找容器中是否存在带有 @Primary 注解的 Executor 类型 Bean。
- 第四优先级：查找任意一个 Executor 类型的 Bean（如果存在多个则会报错）。
- 最终回退：如果以上都未找到，则创建一个简单的 SimpleAsyncTaskExecutor。


## 关于 @EnableAsync 注解用法的深入解析

如果我们将@EnableAsync放在主启动类上，那么它会作用于整个Spring上下文，即整个应用都可以使用@Async注解。

如果我们将@EnableAsync放在某个配置类上，那么它只会作用于该配置类所在的上下文。但是，在Spring Boot应用中，通常我们只有一个Spring上下文，所以放在配置类上和放在主启动类上效果是一样的。

如果我们在多个配置类上使用了@EnableAsync，或者在主启动类和配置类上都使用了@EnableAsync。Spring允许我们在多个配置类上使用@EnableAsync，但是它们会合并到同一个Spring上下文中，因此不会有什么问题。

> 建议：
> - 如果没有配置类，直接使用yaml配置使用，注解放主启动类上。
> - 大型微服务项目（推荐放在主启动类），可以一眼明确模块启用了哪些核心功能。
> - 简单小程序（推荐放在主启动类），简化配置，一目了然。
> - 想要模块化配置类，放配置类上，模块化，易于维护。
> - 框架/库开发，放配置类上，便于集成和条件化。

## 三种常用线程池的详细对比

| 特性     | ThreadPoolExecutor | ThreadPoolTaskExecutor      | ForkJoinPool      |
|--------|--------------------|-----------------------------|-------------------|
| 底层实现   | JDK标准线程池           | Spring包装的ThreadPoolExecutor | 工作窃取线程池           |
| 队列机制   | 共享阻塞队列             | 共享阻塞队列                      | 每个线程有自己的双端队列      |
| 任务调度   | FIFO               | FIFO                        | LIFO（本地）/FIFO（窃取） |
| 适用场景   | 独立任务、IO密集型         | Spring集成、业务异步               | 递归分治、CPU密集型       |
| 线程间协作  | 无协作                | 无协作                         | 工作窃取              |
| 任务依赖处理 | 手动处理               | 手动处理                        | 内置fork/join机制     |
| 默认并行度  | 可配置                | 可配置                         | CPU核心数（common池）   |


## 核心架构区别

- ThreadPoolExecutor：共享任务队列 + 工作线程竞争

- ForkJoinPool：工作窃取(work-stealing) + 每个线程有自己的双端队列。**每个线程优先处理自己的队列，空闲时从其他线程尾部"偷"任务**


## 实战建议

#### 什么时候用 ForkJoinPool？

- Java 8+ 并行流：parallelStream() 默认使用它
- 递归算法：树遍历、分治排序、动态规划
- CPU密集型计算：数学计算、图像处理、科学计算
- 任务可均匀分割：数组/集合的并行处理

#### 什么时候用 ThreadPoolExecutor？

- IO密集型任务：网络请求、文件读写、数据库操作
- 独立短任务：Web请求处理、消息消费
- 需要特殊队列：优先级队列、延迟队列
- 需要精确控制：自定义拒绝策略、线程工厂

#### 什么时候用 ThreadPoolTaskExecutor？

- Spring Boot 项目：简单配置，快速集成
- @Async 注解：Spring管理的异步方法
- 配置外部化：通过yml文件管理
- 需要Actuator监控：集成Spring Boot监控

## 总结建议

#### ForkJoinPool：为"分治递归"而生，工作窃取算法，CPU缓存友好

- 建议用：✅ 递归任务、并行计算、CPU密集型
- 不建议：❌ IO阻塞、任务太小、非递归场景

#### ThreadPoolExecutor：通用任务处理，灵活配置

- 建议用：✅ IO密集型、独立任务、特殊队列需求
- 不建议：❌ 递归任务（需要手动分割）

#### ThreadPoolTaskExecutor：Spring包装版，方便集成

- 建议用：✅ Spring生态、快速开发、配置管理
- 不建议：❌ 需要精细控制、非Spring环境
