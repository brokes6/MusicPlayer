package com.example.musicplayerdome.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FilesUtil {
    //判断文件是否存在
    public static boolean fileIsExists(File f) {
        try
        {
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
    /**
     * 下载指定路径的文件，并写入到指定的位置
     *
     * @param dirName
     * @param urlStr
     * @return 返回0表示下载成功，返回1表示下载出错
     */
    public static int downloadFile(File dirName, String urlStr) {
        OutputStream output = null;
        InputStream input = null;
        try {
            //将字符串形式的path,转换成一个url
            URL url = new URL(urlStr);
            //得到url之后，将要开始连接网络，以为是连接网络的具体代码
            //首先，实例化一个HTTP连接对象conn
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //定义请求方式为GET，其中GET的大小写不要搞错了。
            conn.setRequestMethod("GET");
            //定义请求时间，在ANDROID中最好是不好超过10秒。否则将被系统回收。
            conn.setConnectTimeout(6 * 1000);
            //请求成功之后，服务器会返回一个响应码。如果是GET方式请求，服务器返回的响应码是200，post请求服务器返回的响应码是206（貌似）。
            if (conn.getResponseCode() == 200) {
                //返回码为真
                //从服务器传递过来数据，是一个输入的动作。定义一个输入流，获取从服务器返回的数据
                input = conn.getInputStream();
                File file = createFile(dirName);
                output = new FileOutputStream(file);
                //读取大文件
                byte[] buffer = new byte[1024];
                //记录读取内容
                int n = input.read(buffer);
                //写入文件
                output.write(buffer, 0, n);
                n = input.read(buffer);
            }
            output.flush();
            input.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

        }
        return 1;
    }
    /**
     * 在SD卡的指定目录上创建文件
     *
     */
    public static File createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
