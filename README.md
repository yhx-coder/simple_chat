# simlpe_chat
这是一个简单的聊天室程序，由 Netty + Protobuf 实现。可以群聊和单聊。

命令行操作，界面很 low 。

github 的默认分支名改为了 main 。

采用 protobuf 主要因为不想自己写消息格式和编解码器。

半年后发现一个惨重教训：不好好写注释，看不懂了（捂脸）！还有 netty 不用快忘光了。

***大消息传输处理有大佬帮帮忙不，我想学下怎么处理好。***

**使用方法：**     
先运行 sever 再运行 client。然后在 client 中按照菜单选择功能。

**下一步计划：**     
异地登录同一账号时，旧客户端被挤下线功能。 目前有点影响用户体验。 

加入文件传输机制，让聊天双方可以发送文件。目前的消息都是短字符串，也需要借此增加大消息的传输机制。
  设想逻辑：文件接收方在线，则让发送方直接发送给接收方。若接收方不在线，则将文件存入 redis，设置过期时间。等待接收方上线后进行推送文件摘要（不直接推文件，怕接收文件太多太大撑爆客户端）。接收方确认下载后，两种情景：文件未过期则从服务器拉取文件；文件过期就向接收方发送文件过期消息。对于大消息的处理目前看了两种，一是客户端自己分片发送，服务器接收到文件后发送偏移量并和之前收到的文件进行组装，全收到后发送特殊字符。一种是利用chunkedwritehandler。需要针对此种情景重新设计消息体格式。

试着用 swing 给程序加一个图形界面，将消息显示与消息发送分开。

将现有的 handler 重构，拆分成许多小 handler 。  

**现有测试用户：**

| 用户名 | 密码 |
| :------ | :---- |
| aaa|aaa|
|bbb|bbb|
|ccc|ccc|

**最后：** 由于本人开发水平有限，诚挚邀请各位大神帮助完善！学校开学，以后就随缘更新了。

注意：心跳检测中的时间是我瞎编的，没有实际测量过。     
碎碎念：

1.日志一开始想在类路径下建文件夹输出，在 java 中设置 system property 的环境变量然后读取。后来觉得不合理。哪有在 jar 包里写日志的啊。后来决定在某一固定的路径记录。

2.消息缓存的实现方案： protobuf 对象转换成 json 存在 redis 服务器上，key 为接收方 id，value 为消息体。

3.使用 redis 做消息缓存不知道合不合适，感觉在内存中存储不太合理，毕竟聊天中也有可能发送大文件，吃掉很多服务器内存，有点浪费。以后考虑给消息加上发送时间。
