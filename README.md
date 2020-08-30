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
<p>若未安装Python，可在官网下载Python： <a href="https://www.python.org/downloads/" target="_blank">下载Python</a>。根据电脑的Python版本执行server.py(以Python3为例):</p>
<pre>
<code>$ python3 server.py</code>
</pre>
<h1>测试 - Test</h1>
<h2>单元测试</h2>
<p>本项目测试了：</p>
<ol>
  <li>单线程发送会话</li>
  <li>多线程发送会话，使用了线程池进行多线程并发发送会话</li>
</ol>
<h2>代码覆盖率</h2>
<p>本项目代码覆盖率：<strong>74%</strong>。</p>
<p>本项目使用了JaCoCo-Maven插件进行代码覆盖率检测。第一步需要电脑安装Maven，第二步在pox.xml中声明Jacoco插件。</p>
<h3>安装Maven（Mac OS版本）</h3>
<p>从官网下载Maven：<a href="https://maven.apache.org/download.cgi" target="_blank">下载Maven</a></p>
<p>配置环境：</p>
<pre>
<code>vi ~/.bash_profile</code>
</pre>
<p>添加路径</p>
<pre>
<code>
export MVN_HOME=/...(Maven解压的路径).../apache-maven-3.6.3
export PATH=$PATH:$MVN_HOME/bin
<code>
</pre>
<p>重新加载配置以及</p>
<pre>
<code>source ~/.bash_profile</code>
</pre>
<p>兼容zsh</p>
<pre><code>vim ~/.zshrc
# 在.zshrc最后添加
source ~/.bash_profile </code></pre>
<p>测试Maven</p>
<pre><code>mvn -v</code></pre>
<p>安装Maven on linux可参考：<a href="https://blog.csdn.net/qq_38270106/article/details/97764483" target="_blank">Linux安装Maven</a></p>
<h3>在pox.xml配置JaCoCo插件(本项目的pox.xml中已配置)</h3>
<pre><code><plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.2</version>
  <executions>
    <execution>
      <goals>
        <goal>prepare-agent</goal>
      </goals>
     </execution>
     <!-- attached to Maven test phase -->
     <execution>
      <id>report</id>
      <phase>test</phase>
      <goals>
        <goal>report</goal>
      </goals>
     </execution>
  </executions>
</plugin></code></pre>

