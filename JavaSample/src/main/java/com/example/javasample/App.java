package com.example.javasample;

import java.io.IOException;
import java.util.Locale;

public class App {
    public static void main(String[] args) throws IOException {
       new UdpServer(12200).start();
    }
}
