# session_controller
<h1>简介 - Introduction</h1>
<p>会话控制器是模拟了会话定时地向服务器发送请求，包括连接请求（Start），断开请求（Stop）。具有以下特性：</p>
<ul>
<li>会话可以定时向服务器发送POST请求，并把会话信息以XML形式存储在POST的body里面；</li>
<li>支持多线程同时发送会话信息；</li>
</ul>
<h2>Class介绍以及UML</h2>
<ul>
<li>DeliverySessionCreation: 会话原始类，封装了一个会话的信息；</li>
<li>DeliverySession: 为DeliverySessionCreation提供定时器；</li>
<li>SessionRequest: 作为会话的定时任务，将DeliverySessionCreation的信息发送给Server；</li>
</ul>
<h1>客户端 - Session Client</h1>

<h1>服务器端 - Session Server</h1>
<p></p>
<>
<h1>测试 - Test</h1>
