#### AndroidHttpServer

* 支持功能
	* Cookie
	* 获取表单提交信息
	* 支持上传
	* 支持文件下载
	* 支持重定向
	* 支持转发
	* 支持session

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