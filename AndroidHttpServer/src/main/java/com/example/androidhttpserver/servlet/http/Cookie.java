package com.example.androidhttpserver.servlet.http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Cookie {
    private String n, v, e;

    public Cookie(String name, String value, String expires) {
        n = name;
        v = value;
        e = expires;
    }

    public Cookie(String name, String value) {
        this(name, value, 30);
    }

    public Cookie(String name, String value, int numDays) {
        n = name;
        v = value;
        e = getHTTPTime(numDays);
    }

    public String getHTTPHeader() {
        // expires表示的是有效时间; 如果不设置; 貌似是关闭浏览器就清空cookie
        String fmt = "%s=%s; expires=%s";
        return String.format(fmt, n, v, e);
    }

    public String getName() {
        return n;
    }

    public String getValue() {
        return v;
    }

    public static String getHTTPTime(int days) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(calendar.getTime());
    }
}
