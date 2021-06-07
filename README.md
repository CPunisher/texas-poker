<p align="center">
    <h2 align="center">Texas Poker</h2>
    <p align="center">北京航空航天大学软件学院2019级Java课程大作业</p>
    <p align="center">
        <img src="https://img.shields.io/badge/maven-v3.6.3-blue" alt="maven"/>
        <img src="https://img.shields.io/badge/java-9%2B-blue" alt="java"/>
        <img src="https://img.shields.io/badge/contributor-3-green" alt="contributor"/>
    </p>
</p>

> Texas Poker 是基于 Java 语言开发的采用 C/S 模式的多人在线德州扑克游戏，
> 客户端 Gui 使用 Java Swing 绘制，客户端和服务器之间的通信基于 Netty NIO，
> 并在此基础上设计了独特的 Packet 的传输协议。项目整体采用 Log4j2 控制日志的输出。

## 特性
- 基于``Netty NIO``实现的网络架构
- 整体采用MVC设计模式，实现模型、视图、逻辑分离
- 可以很方便地修改为其他回合制卡牌游戏

## 环境要求
JDK 9以上

## 运行
### 运行服务端
```shell
java -jar texas-poker-server-jar-with-dependencies.jar
```
### 运行客户端
双击 `texas-poker-client-jar-with-dependencies.jar` 即可

## 依赖
```
netty-all: "~> 4.1.48.Final"
log4j-core: "~> 2.14.1"
log4j-api: "~> 2.14.1"
```