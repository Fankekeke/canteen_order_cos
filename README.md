### 基于SpringBoot + Vue点餐配送系统.

#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok


> 管理员端功能模块：
1. 商家管理模块：商家信息的查询、增加、修改、删除等
2. 用户管理模块：用户信息的查询、增加、修改、删除等
3. 个人中心模块：当天日期时间，日历、登录用户名等
4. 退出登录：退出到登录页面
5. 销售与统计模块：统计系统所有商品销售数量，当天营业额
6. 用户地址管理
7. 公告信息管理
8. 菜品信息管理
9. 订单信息与订单评价
10. 积分物品管理
11. 商家信息管理
12. 员工信息管理
13. 注册用户管理

> 商家端功能模块：
1. 注册、登录模块：商家进行注册、登录到商家端口。
2. 菜系信息管理模块：菜系信息查询、添加、修改、删除等。
3. 菜品评论管理模块：查看用户对相应菜品的评价
4. 订单管理模块：订单处理、订单记录查询、查看顾客消费历史记录等。
5. 营养分析模块：根据所选菜品，自动计算热量、蛋白质、脂肪等营养成分，帮助顾客更加健康地饮食。（热量 蛋白质 脂肪等放在菜品信息的营养里，加入购物车或者结算的时候 在能量计算里累计 超出当日摄入量时 提示）
6. 会员管理模块：商品对会员进行增、删、改、查
7. 个人中心模块：当天日期时间，日历、登录用户名等
8. 退出登录：退出到登录页面
9. 销售与统计模块：统计本家商品销售数量，当天营业额
10. 店家会员积分调整
11. 员工信息管理：处理本家员工信息

> 用户端功能模块：
1. 注册、登录模块：用户进行注册、登录到用户端口
2. 首页模块：轮播图推荐菜品、商户查询、菜系查询、菜品查询
3. 购物车模块：加入购物车、购物车结算（支付宝沙箱）、购物车管理、清空购物车等。
4. 评价模块：对已消费的菜品进行评价，为其他用户参考。
5. 个人中心模块：当天日期时间，日历、登录用户名
6. 退出登录：退出到登录页面
7. 积分兑换模块：通过购买商品获取对应的积分，一定的积分可以兑换相应的商品。
8. 会员折扣模块：对支付的商品根据会员等级进行相应的折扣
9. 配送收货地址管理
10. 订单支付支付宝沙盒支付，支付后邮件通知
11. 积分物品兑换记录管理



#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 后端启动方式

1.首先启动redis，进入redis目录终端。输入redis-server回车
2.导入sql文件，修改数据库与redis连接配置
3.idea中启动后端项目

#### 默认后台账户密码
[管理员]
admin
1234qwer

[用户]
fank
1234qwer

[商家]
shangjia
1234qwer


#### 项目截图

|  |  |
|---------------------|---------------------|
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576692558.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576893522.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576678760.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576884557.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576661415.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576867495.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576641695.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576838962.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576628521.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576825600.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576611164.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576799407.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701577197448.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576747695.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576964791.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576725562.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576913590.jpg) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576713210.jpg) |
|![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1701576760956.jpg) |


#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.黑奴价格

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源

[2024年答辩顺利通过](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年答辩顺利通过](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年答辩通过率100%](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)


#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)
