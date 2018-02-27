package com.example.androidhttpserver.servlet.http;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Http协议中一个完整的Cookie格式如下;
 *
 * 服务端 -> 浏览器; 通过Chrome开发者工具抓包显示的;
 * Set-Cookie:lastTime=1519737333546; Expires=Tue, 27-Feb-2018 14:15:33 GMT; Path=/Day11/rem
 * Set-Cookie:key2=value2; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/Day11/fu
 *
 * 浏览器 -> 服务器; 不携带expire和path
 *
 * cookie: lastTime=1519737333546
 * cookie: key2=value2
 *
 * ----------------------------------------------------------------------------------------------
 *
 * 服务端向客户端写出cookie的时候;
 *
 *  pw.print("Set-Cookie" + ": " + cookieStr + "\r\n");
 *
 * ----------------------------------------------------------------------------------------------
 *
 * 如何删除浏览器端的cookie;
 * 将cookie的expire置为过期时间即可删除;
 * 例如: test1=testV1;
 * test1=-delete-; expire=过期的时间;
 * 
 * @author YGX
 */
public class Cookie {

    // key
    private String name;
    // value
    private String value;
    // 有效期; 不设置则当浏览器关闭时清除cookie
    private String expire;
    // 请求路径
    private String path;

    private SimpleDateFormat expireFormater =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.CHINA);

    public Cookie(String name, String value, String expires) {
        this.name = name;
        this.value = value;
        this.expire = expires;
    }

    public Cookie(String name, String value, long secondExpire){
        this.name = name;
        this.value = value;
        this.expire = getExpireString(System.currentTimeMillis() + secondExpire * 1000);
    }

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Cookie(String name, String value, int numDays) {
        this.name = name;
        this.value = value;
        expire = getHTTPTime(numDays);
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setMaxAge(int secondExpire){
        expire = getExpireString(System.currentTimeMillis()+secondExpire * 1000);
    }

    public String getHTTPHeader() {
        // expires表示的是有效时间; 如果不设置; 貌似是关闭浏览器就清空cookie
        String fmt = "%s=%s; expires=%s";
        return String.format(fmt, name, value, expire);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static String getHTTPTime(int days) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(calendar.getTime());
    }

    public String getExpireString(long millis){
        return expireFormater.format(new Date(millis));
    }

    public String toCookieString(){
        StringBuilder sBuf = new StringBuilder();
        sBuf.append(name).append("=").append(value);
        if(!TextUtils.isEmpty(expire)){
            sBuf.append("; ").append("expires=").append(expire);
        }

        if(!TextUtils.isEmpty(path)){
            sBuf.append("; ").append("path=").append(path);
        }
        return sBuf.toString();
    }
}
