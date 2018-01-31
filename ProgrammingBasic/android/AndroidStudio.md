## 配置git
### 一、配置git
 - 1、setting–>Version Control–>Git–>Path to Git executable中选择git.exe的位置，这个Stutio一般会默认配置好。  
 - 2、配置完路径后点击后面的Test按钮。

### 二、将项目分享到github
 - 1、设置github账号密码：打开Setting–>Version Control–>GitHub，填写完账号密码后，点击Test测试。
 - 2、VCS -> Import into Version Control -> Share Project on GitHub

### 三、ADD and COMMIT
 - 项目右键->Add / Repository(pull, push)

参考：http://blog.csdn.net/xmxkf/article/details/51595096

## 关于“no serial number”的解决方案
 - adb usb
 - adb devices
 - adb tcpip 5555
 - adb connect IP_ADDR (settings->About->Status->IP)
 - adb devices
 - adb disconnect IP_ADDR



