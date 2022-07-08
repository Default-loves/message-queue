

### docker启动

`docker run -it -d --name rabbitmq-my -p 5672:5672 -p 15672:15672 rabbitmq:3-management`

**管理界面**

- 地址：http://172.16.60.223:15672		
- guest/guest



### 子模块simple-producer

简单的消息发送者

### 子模块simple-consumer

简单的消息消费者

### 子模块many-comsumer

某个服务A有多个节点实例，比如两个，都绑定队列A，实现队列A的消息，只会被服务A的实例节点群消费一次，如图所示：

![image-20220622112631817](G:\GithubMy\my\message-queue\pic\image-20220622112631817.png)

### 子模块fanout-exchange

有两个服务：服务A和服务B，每个服务都有两个实例，总共4个实例，实现一条消息，会被服务A的某个实例和服务B的某个实例消费，同一条消息总共被消费两次

![image-20220622113544428](G:\GithubMy\my\message-queue\pic\image-20220622113544428.png)

### 子模块dead-letter

死信的处理

当消费者处理消息的时候报错了，没有给消息队列发送完成消息，那么消息队列会将消息重新添加到队列中，然后被消费者处理的时候报错又放入队列，周而复始，这种消息称为“死信”

死信会严重影响正常的消息处理过程，rabbitMQ的做法是消息处理报错的时候会重试几次，仍然有问题后将消息放到特殊的队列——死信队列，由其他程序消费死信队列，避免对正常队列的影响。

![img](G:\GithubMy\my\message-queue\pic\40f0cf14933178fd07690372199e8428.png)

### 子模块dlx

除了**子模块dead-letter**说的对死信的处理，RabbitMQ 2.8.0 后支持的死信交换器 DLX 也可以实现类似功能

RabbitMQ 的DLX 死信交换器和普通交换器没有什么区别，只不过它有一个特点是，可以把其它队列关联到这个 DLX 交换器上，然后消息过期后自动会转发到 DLX 交换器。那么，我们就可以利用这个特点来实现延迟消息重投递，经过一定次数之后还是处理失败则作为死信处理。

![img](G:\GithubMy\my\message-queue\pic\4139d9cbefdabbc793340ddec182a936.png)

WORKER 作为 DLX 用于处理消息，BUFFER 用于临时存放需要延迟重试的消息，WORKER 和 BUFFER 绑定在一起。

DEAD 用于存放超过重试次数的死信。在这里 WORKER 其实是一个 DLX，我们把它绑定到 BUFFER 实现延迟重试。

通过 RabbitMQ 实现具有延迟重试功能的消息重试以及最后进入死信队列的整个流程如下：

1. 客户端发送记录到 WORKER；
2. Handler 收到消息后处理失败；
3. 第一次重试，发送消息到 BUFFER；
4. 3 秒后消息过期，自动转发到 WORKER；
5. Handler 再次收到消息后处理失败；
6. 第二次重试，发送消息到 BUFFER；
7. 3 秒后消息过期，还是自动转发到 WORKER；
8. Handler 再次收到消息后处理失败，达到最大重试次数；
9. 发送消息到 DEAD（作为死信消息）；
10. DeadHandler 收到死信处理（比如进行人工处理）。