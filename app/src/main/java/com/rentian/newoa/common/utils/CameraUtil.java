package com.rentian.newoa.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

public class CameraUtil {

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;// 图片的长宽不变

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;

    }

    /**
     * 根据路径获得图片并压缩返回bitmap
     *
     * @param filePath
     * @return BitmapFactory可以从一个指定文件中，利用decodeFile()解出Bitmap；也可以从自定义的图片资源中，
     * 利用decodeResource()解出Bitmap Options有以下几个属性，可以指定decode的选项
     * .inSampleSize 设置decode时的缩放比例；
     * .injustDecodeBounds如果设置为true，并不会把图像的数据完全解码，亦即decodeXyz()返回值为null，
     * 但是Options的outAbc中解出了图像的基本信息 .inPreferredConfig
     * 指定decode到内存中，手机中所采用的编码，可选值定义在Bitmap.Config中。 缺省值是ARGB_8888
     */
    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只读边，不读内容
        BitmapFactory.decodeFile(filePath, options);// 将图片转换为bitmap
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);

		/*
         * 先设置inJustDecodeBounds= true，调用decodeFile()得到图像的基本信息；
		 * 利用图像的宽度（或者高度，或综合）以及目标的宽度，得到inSampleSize值， 再设置inJustDecodeBounds=
		 * false，调用decodeFile()得到完整的图像数据。
		 * 先获取比例，再读入数据，如果欲读入大比例缩小的图，将显著的节约内容资源。有时候还会读入大量的缩略图，这效果就更明显了。
		 */
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */

    public static File getAlbumDir() {
        // Environment.getExternalStorageDirectory() 获得sd卡路径
        File dir = new File(Environment.getExternalStorageDirectory(),
                getAlbunName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存图片的文件夹名称
     *
     * @return
     */
    private static String getAlbunName() {

        return "wqpicture";
    }

}
