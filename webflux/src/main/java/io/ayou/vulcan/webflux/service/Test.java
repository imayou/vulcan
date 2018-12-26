package io.ayou.vulcan.webflux.service;

import java.io.*;
import java.net.Socket;

/**
 * @ClassName Test
 */
public class Test {
    public static void main(String[] args) {
        if (true) {
            h1();
        } else {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {

                for (int i = 0; i < 100; i++) {
                    //建立基站获取链接地址及端口号
                    socket = new Socket("localhost", 9011);
                    //获取服务器发过来的字节流
                    inputStream = socket.getInputStream();
                    //开始解析字节流
                    byte[] b = new byte[1024];
                    String str = "";
                    int length = -1;
                    while ((length = inputStream.read(b, 0, b.length)) != -1) {
                        str += new String(b, 0, length);
                    }
                    System.out.println(str);

                    outputStream = socket.getOutputStream();
                    outputStream.write("hello my server".getBytes());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //最后关闭
                try {
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static void h1() {
        try {
            //1.建立客户端socket连接，指定服务器位置及端口
            Socket socket = new Socket("localhost", 9011);
            //2.得到socket读写流
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            //输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //3.利用流按照一定的操作，对socket进行读写操作
            String info = "用户名：Tom,用户密码：123456";
            pw.write(info);
            pw.flush();
            socket.shutdownOutput();
            //接收服务器的相应
            String reply = null;
            while (!((reply = br.readLine()) == null)) {
                System.out.println("接收服务器的信息：" + reply);
            }
            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
