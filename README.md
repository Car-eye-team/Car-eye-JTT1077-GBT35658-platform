
# Car-eye-1077-GBT35658-platform 

Car-eye-1077-GBT35658-platform  开发的基于JTT808/1078 JTT1077，GBT35658协议的平台架构，兼容JT/T905设备，GB28181设备，GB32960设备接入。
包括了基于Java和mysql的数据管理系统，也包含了通信平台，视频服务器，客户端程序。是一个功能完善的车辆管理平台

# 平台具有哪些优势
1. 采用最新的layUI+mysql数据库构建web服务器，前后端分离。
2. 采用最稳定的socket底层构建JT1078视频服务器，视频转发到RTMP服务器，支持http-flv，RTMP,WS-flv各种输出。
3. 基于http，MQ等多种通信方式。
4. web运行于最常见的centos操作系统，视频服务器支持linux和windows双操作系统。
5. 客户端SDK支持linux，android，windows等各种应用。
6. 设备SDK支持多通道推流，支持推送文件，高版本的android系统支持Epoll数据传送方式。
7. 分布式架构web和视频服务器可以运行在不同硬件上。
8. 后端采用最新的spring-boot，kafaka，MQ，netty等架构。
9. 支持语音双向对讲，平台录像。

# 我们为什么要开发这个平台

不同的设备厂家，使用相同的平台架构，既符合了国家交通部的要求，又具备更好的兼容性

# 哪些设备适用于该平台

平台是基于国家交通部的JT808/JT1078打造的，所有符合部标协议的机器都可以接入到该平台


# 平台客户端运行展示


![](https://gitee.com/careye_open_source_platform_group/Car-eye-JTT1077-JT796-platform/raw/master/Car-eye.png)




# 体验平台登录方式

web登录地址：https://liveoss.com  
账号：test      
密码：123456     

客户端的登录账号     
账号：test    
密码：123456   
服务器：www.liveoss.com     

通信平台的IP:39.108.246.45
端口：9999
登陆平台后选一个不在线的车，把设备上的终端号设置成一样，就可以连接上平台。


# 接口文档

车辆管理平台提供了丰富的接口方便客户进行二次开发，其中包含了两部分：
1. web前后端的API接口文档，方便用户进行二次开发，相关接口说明请参考swagger文档输出：
http://www.liveoss.com:8088/doc.html

2. PC和移动端API接口
提供HTTP访问平台资源,远程接口基本满足客户的一般需求。相关文档请参考：CMS客户端API接口文档V1.0.3.doc，android, IOS客户端和微信小程序可以从平台的
体验的网址下载进行体验。

# 快速部署

平台提供了快速部署功能，相关访问的网址是  http://www.liveoss.com:8086/
只需要将目标服务器和用户密码填写好，就可以将平台快速部署到远程服务器，相关操作请参考部署文档:Car-eye 车辆管理平台部署手册(V1.0).docx。


# 微信小程序客户端   

平台提供微信小程序客户端用来实现对平台车辆的快速管理，欢迎扫码关注我们：    
![](https://gitee.com/careye_open_source_platform_group/Car-eye-JTT1077-JT796-platform/raw/master/weixin.jpg)


# 联系我们

car-eye 开源官方网址：www.car-eye.cn    
car-eye 车辆管理平台网址：www.liveoss.com  
car-eye GB28181管理平台网址 ：www.streaming-vip.com:10088     
car-eye 技术官方邮箱: support@car-eye.cn  
car-eye 车辆管理平台技术交流QQ群:590411159   
car-eye 视频服务和管理平台QQ群：713522732     
![](https://gitee.com/careye_open_source_platform_group/car-eye-jtt1078-media-server/raw/master/QQ/QQ.jpg)   

CopyRight©  car-eye 开源团队 2018-2020


