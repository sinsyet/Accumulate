#### 集成使用

* 初始化

	 	/**
	     * 初始化　
	     * @param ctx 上下文
	     * @param serverPort 监听的端口
	     * @param assetsWebInfoPath web配置文件路径,
	     *                          例如: "web/webinfo.xml", 指向的目录路径是
	     *                          module
	     *                              src
	     *                                  main
	     *                                      assets
	     *                                          web
	     *                                              webinfo.xml                                        
	     */
 		HttpServer.init(getApplicationContext(),9999,"web/webinfo.xml");

* 编写webinfo.xml

		<?xml version="1.0" encoding="UTF-8" ?>
		<web-server>
		    
		    <!--
		        android-servlet表示一个映射节点
		        一个android-servlet节点支持url和html-file或servlet-class的搭配方式;
		        如果两者都配置了, 那么服务器只会以html-file为准(即访问这个url的时候, 返回对应的html)
		        -->
		    <android-servlet>
		        <!--
		        url表示访问的url后缀, 
		        例如: 本机ip是10.6.0.184, 监听端口为9999
		        当访问: http://10.6.0.184:9999的时候, 
		        服务器返回的是web/html/index.html下的html文件给客户端
		        -->
		        <url>/</url>
		        <html-file>web/html/index.html</html-file>
		    </android-servlet>
		    <android-servlet>
		        <url>/login</url>
		        <servlet-class>com.example.zzw.servlets.LoginServlet</servlet-class>
		    </android-servlet>
		    <android-servlet>
		        <url>/main</url>
		        <servlet-class>com.example.zzw.servlets.MainServlet</servlet-class>
		    </android-servlet>
		</web-server>

* servlet
	* 编写一个继承AndroidHttpServlet的子类
		
			public class LoginServlet extends AndroidHttpServlet {
			
			    private static final String TAG = "LoginServlet";
			    @Override
			    public void doRequest(IAndroidServletRequest req, IAndroidServletResponse resp) {
			        String usn = req.getParamter("usn");
			        String psw = req.getParamter("psw");
			        Log.e(TAG, "doRequest: "+usn+" // "+psw);
			        if("admin".equals(usn) && "pass".equals(psw)){
			            resp.sendRedirect("./main");
			            resp.setStatus(HttpStatus.REDIRECT);
			        }else{
			            resp.sendRedirect("./web/html/login_fail.html");
			        }
			    }
			}