package com.example.liqian.newproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by lq
 * on 2021/7/7
 */
public class FileUtils {



/* public File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }*/



    /**
     * 把位图数据保存到指定路径的图片文件
     *
     * @param path
     * @param bitmap
     */
    public static void saveImage(String path, Bitmap bitmap) {
        try {
            //根据指定文件路径构建缓存输出对象
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            //把位图数据压缩到缓存输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //完成缓存输出流的写入动作
            outputStream.flush();
            //关闭缓存输出流
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从指定路径的图片文件中读取位图数据
     * @param path
     * @return
     */
    public static Bitmap openImage(String path) {
        Bitmap bitmap = null;
        try {
            //根据指定文件路径构建缓存输入流对象
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
            //从缓存输入流中解码位图数据
            bitmap = BitmapFactory.decodeStream(inputStream);
            //关闭输入流
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回文件中的位图数据
        return bitmap;
    }
}
