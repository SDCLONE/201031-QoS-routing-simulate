### ubuntu上需要配置的选项

#### 配置启动root登录
1. 目前在启动界面无法直接用root登录，需要增加配置 
2. 配置远程终端（XShell）使用root连接：https://blog.csdn.net/weixin_30838873/article/details/99035786  （需要在ubuntu端安装ssh）
3. 配置ubuntu登录图形界面可以使用root登录：https://blog.csdn.net/dengjin20104042056/article/details/80461749
4. 登录之后原始账户baobao所有信息均在 /home/baobao    /home/baobao/DeskTop  目录下

#### 利用XShell将工程包jar文件发送到虚拟机上（或者直接拖拽）
1. ifconfig获取虚拟机ip，随后使用XShell连接，用户名root  密码123456
2. 进入目录  cd ...
3. 直接将电脑上文件拖拽到XShell黑色框中 (可能提示未安装rz sz，按照要求安装即可)
直接拖拽就忽略上述三步


#### 在虚拟机端使用java -jar命令执行工程
java -jar <filename>.jar
