# simlpe_chat
这是一个简单的聊天室程序，由 Netty + Protobuf 实现。可以群聊和单聊。

命令行操作，界面很 low 。

现有分支名 main 其实是 master 。初始化时写错了，将错就错吧。

采用 protobuf 主要因为不想自己写消息格式和编解码器。

**使用方法：**     
先运行 sever 再运行 client。然后在 client 中按照菜单选择功能。

**下一步计划：**     
异地登录同一账号时，旧客户端被挤下线功能。
针对未上线用户的消息加入 redis 缓存，等用户上线在推送。
将现有的 handler 重构，拆分成许多小 handler 。
试试添加注册功能。注册可以是网页版，用 springboot 写入数据库。

**现有测试用户：**

| 用户名 | 密码 |
| :------ | :---- |
| aaa|aaa|
|bbb|bbb|
|ccc|ccc|

**最后：** 由于本人开发水平有限，诚挚邀请各位大神帮助完善！学校开学，以后就随缘更新了。

注意：心跳检测中的时间是我瞎编的，没有实际测量过。     
碎碎念：1.日志一开始想在类路径下建文件夹输出，在 java 中设置 system property 的环境变量然后读取。后来觉得不合理。哪有在 jar 包里写日志的啊。后来决定在某一固定的路径记录。
