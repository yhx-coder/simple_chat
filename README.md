# simlpe_chat
这是一个简单的聊天室程序，由 Netty + Protobuf 实现。可以群聊和单聊。

命令行操作，界面很 low 。userName 目前没用，所有实现全靠 userID。

现有分支名 main 其实是 master 。初始化时写错了，将错就错吧。

采用 protobuf 主要因为不想自己写消息格式和编解码器。

**使用方法：**
先运行 sever 再运行 client。然后在 client 中按照菜单选择功能。

**下一步计划：**
设计一个 config 类，支持自定义 TCP 连接属性，和一些程序中的常量。
将登录功能做成与数据库交互。

由于本人开发水平有限，诚挚邀请各位大神帮助完善！学校开学，以后就随缘更新了。

注意：心跳检测中的时间是我瞎编的，没有实际测量过。
