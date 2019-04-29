# Car-eye-JTT1077-796-platform

Car-eye-JTT1077-796-platform 开发的基于JTT808/1078 JTT1077，JT/T796协议的平台架构，兼容JT/T905设备接入。包括了基于Java和mysql的数据管理系统，也包含了通信平台，视频服务器，客户端程序。是一个功能完善的车辆管理平台

# 平台具有哪些优势
1. 采用最新的layUI+mysql数据库构建web服务器。
2. 采用最稳定的socket底层构建JT1078视频服务器，视频转发到RTMP服务器，支持http，RTMP,HLS各种输出。
3. 基于http，MQ等多种通信方式。
4. 多平台支持，支持linux和windows双操作系统。
5. 客户端SDK支持linux，android，windows等各种应用。
6. 设备SDK支持多通道推流，支持推送文件，高版本的android系统支持Epoll数据传送方式。
7. 分布式架构web和视频服务器可以运行在不同硬件上。

# 体系架构



# 我们为什么要开发这个平台

不同的设备厂家，使用相同的平台架构，既符合了国家交通部的要求，又具备更好的兼容性

# 哪些设备适用于该平台

平台是基于国家交通部的JT808/JT1078打造的，所有符合部标协议的机器都可以接入到该平台

# 平台客户端运行展示


![](https://github.com/Car-eye-team/Car-eye-JTT808-1078-platform/blob/master/Car-eye.png)




# 体验平台登录方式

web登录地址：www.liveoss.com:8088    
账号：test      
密码：123456     

客户端的登录账号     
账号：test    
密码：123456   
服务器：www.liveoss.com     

通信平台的IP:39.108.246.45
端口：9999
登陆平台后选一个不在线的车，把设备上的终端号设置成一样，就可以连接上平台。

