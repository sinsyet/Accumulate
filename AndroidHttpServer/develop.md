## develop note

#### 转发:

* 方法:

		IAndroidServletRequest#getRequestDispatcher#forward(req,resp);

* 说明: 假如是从Servlet1转发了Servlet2, 则Servlet1中写入流的数据不会传往浏览器

#### 重定向

* 方法:

	    IAndroidServletResponse#sendRedirect("path");

* 重定向: 返回头信息:

		Location:http://localhost:8080/Tester/servlet/AppServlet

* BaseUrl:

		 host -- 10.6.0.86:9999	// 客户端请求的主机信息(ip:端口或域名)
		 Chrome抓包显示: http://10.6.0.86:9999/; referer -- http://10.6.0.86:9999/
		 Firfox上抓出的包显示

#### include包含

* 方法:

		IRequestDispatcher#include(req,resp);

* 说明:

		包含也是通过RequestDispatcher来实现的, 不会触发二次请求;
		与转发不同的是:转发会把宿主servlet中的数据清除;而include只是把包含的servlet包含进宿主servlet中;

* 例:

		宿主servlet: MainServlet: writer.println("from main");
		转发到app servlet: forward(req,resp);
			网页显示: app servlet // 没有from main, 被擦除了

		包含app servlet: include(req,resp);
			网页显示: from main  app servlet; // 都显示

#### 加载html中的资源

* 概述:

    今天测试使用发现, html中引用的资源(例如背景图片)无法显示出来, 查询原因发现默认response的
    contet-type是text/html,
    实际如果response的是资源文件(例如js,img,css等), 不指定content-type也行;

    另外: request有一个头信息(Accept是表示期望接收的是什么形式的数据, 也可以从这里做判断)
        Accept: image/webp,image/apng,image/*,*/*;q=0.8