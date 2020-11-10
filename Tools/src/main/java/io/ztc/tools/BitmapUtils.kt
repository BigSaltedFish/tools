package io.ztc.tools

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.*
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.ImageColumns
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Base64
import android.util.Log
import top.zibin.luban.Luban
import java.io.*
import java.lang.ref.WeakReference

object BitmapUtils {
    //通过路径将图片转化为Bitmap
    fun getBitmap(path: String?): Bitmap? {
        try {
            val opts = BitmapFactory.Options()
            opts.inJustDecodeBounds = true
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeFile(path, opts)
            val width = opts.outWidth
            val height = opts.outHeight
            val scaleWidth = 0f
            val scaleHeight = 0f
            opts.inJustDecodeBounds = false
            val scale = Math.max(scaleWidth, scaleHeight)
            opts.inSampleSize = scale.toInt()
            val weak = WeakReference(
                BitmapFactory.decodeFile(path, opts)
            )
            return Bitmap.createScaledBitmap(weak.get()!!, width, height, true)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getURLByURI(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme: String = uri.scheme!!
        var data: String? = null
        if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor: Cursor? = context.contentResolver
                .query(uri, arrayOf(ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndex(ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * 质量压缩方法
     * @param image bitmap
     * @return 压缩后的bitmap
     */
    fun compressImageTo(image: Bitmap, size: Int): Bitmap? {
        return try {
            val baos = ByteArrayOutputStream()
            image.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                baos
            ) //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            var options = 100
            while (baos.toByteArray().size / 1024 > size) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset() //重置baos 即清空baos
                //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差 ，第三个参数：保存压缩后的数据的流
                try {
                    image.compress(
                        Bitmap.CompressFormat.JPEG,
                        options,
                        baos
                    ) //这里压缩options%，把压缩后的数据存放到baos中
                } catch (e: Exception) {
                    //回压
                    options += 10
                    baos.reset()
                    image.compress(Bitmap.CompressFormat.JPEG, options, baos)
                    val isBm = ByteArrayInputStream(baos.toByteArray())
                    return BitmapFactory.decodeStream(isBm, null, null)
                }
                options -= 10 //每次都减少10
                if (options < 1) {
                    break;
                }
            }
            val isBm =
                ByteArrayInputStream(baos.toByteArray()) //把压缩后的数据baos存放到ByteArrayInputStream中
            BitmapFactory.decodeStream(isBm, null, null)
        } catch (e: Exception) {
            image
        }
    }

    fun compressImage(
        context: Context,
        imagePath: String,
        size: Int,
    ): Bitmap? {
        // 同步方法直接返回压缩后的文件
        return try {
            val file = Luban.with(context).load(listOf(imagePath)).ignoreBy(size).get()[0]
            getBitmapByPath(file.path)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 质量压缩方法 质量归零
     * @param image bitmap
     * @return 压缩后的bitmap
     */
    fun compressImage(bitmap: Bitmap): Bitmap? {
        return try {
            val showBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
            Log.i(
                "缩率图压缩", "压缩后图片的大小" + (showBitmap.byteCount / 1024) + "KB宽度为"
                        + showBitmap.width + "高度为" + showBitmap.height
            )
            showBitmap
        } catch (e: java.lang.Exception) {
            null
        }
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    fun rotateBitmapByDegree(context: Context, bm: Bitmap, degree: Int): Bitmap? {
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        try {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
        } catch (e: Exception) {
            returnBm?.recycle()
            return bm
        }
        if (returnBm == null) {
            returnBm = bm
        }
        if (bm != returnBm) {
            bm.recycle()
        }
        return returnBm
    }


    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation: Int = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    fun rottingImageView(angle: Int, bitmap: Bitmap): Bitmap? {
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: OutOfMemoryError) {
        }
        if (returnBm == null) {
            returnBm = bitmap
        }
        if (bitmap !== returnBm) {
            bitmap.recycle()
        }
        return returnBm
    }


    /**
     * 通过路径将图片转化为Bitmap
     * @param path 文件路径
     * @return bitmap
     */
    fun getBitmapByPath(path: String?): Bitmap? {
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeFile(path, opts)
        val width = opts.outWidth
        val height = opts.outHeight
        val scaleWidth = 0f
        val scaleHeight = 0f
        opts.inJustDecodeBounds = false
        val scale = scaleWidth.coerceAtLeast(scaleHeight)
        opts.inSampleSize = scale.toInt()
        val weak = WeakReference(BitmapFactory.decodeFile(path, opts))
        return try {
            Bitmap.createScaledBitmap(weak.get()!!, width, height, true)
        } catch (e: Exception) {
            try {
                BitmapFactory.decodeFile(path, opts)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    }

    /**
     * 文字水印添加
     */
    fun addTextWatermark(
        bitmap: Bitmap,
        content: String?,
        textSize: Int,
        color: Int,
        x: Float,
        positionFlag: Boolean,
        recycle: Boolean
    ): Bitmap? {
        if (ImgUtils.isEmptyBitmap(bitmap) || content == null) return null
        val ret = bitmap.copy(bitmap.config, true)
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas(ret)
        textPaint.color = color
        textPaint.textSize = textSize.toFloat()
        textPaint.isAntiAlias = true;
        val bounds = Rect()

        textPaint.getTextBounds(content, 0, content.length, bounds)
        //canvas.drawText(content, bitmap.width - x - bounds.width() - bounds.left, bitmap.height - bounds.height() - bounds.top - y, textpaint)
        if (positionFlag) {
            canvas.translate(x, (bitmap.height - (6 * textSize)).toFloat())
            val textL = StaticLayout(
                content,
                textPaint,
                bitmap.width,
                Layout.Alignment.ALIGN_NORMAL,
                1.0F,
                1.0F,
                true
            )
            textL.draw(canvas)
            //canvas.drawText(content, x, bitmap.height - bounds.height() - bounds.top - y, textpaint)
        } else {
            canvas.translate(0F, (bitmap.height - (6 * textSize)).toFloat())
            val textL = StaticLayout(
                content,
                textPaint,
                bitmap.width, Layout.Alignment.ALIGN_NORMAL,
                1.0F,
                1.0F,
                true
            )
            textL.draw(canvas)
            //canvas.drawText(content, bitmap.width - x - bounds.width() - bounds.left, bitmap.height - bounds.height() - bounds.top - y, textpaint)

        }
        if (recycle && !bitmap.isRecycled) bitmap.recycle()
        return ret
    }


    /**
     * bitmap转为base64
     * @param bitmap bitmap
     * @return base64字符串
     */
    fun bitmapToBase64(context: Context?, bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }


    val RGB_8888: Bitmap.Config = Bitmap.Config.ARGB_8888
    val RGB_565: Bitmap.Config = Bitmap.Config.RGB_565

    const val HDMaxWidth: Int = 1080
    const val HDMaxHeight: Int = 1920

    /**
     * 读取一个缩放后的图片，限定图片大小，避免OO
     * @param uri       图片uri，支持“file://”、“content://”
     * @param maxWidth  最大允许宽度
     * @param maxHeight 最大允许高度
     * @return  返回一个缩放后的Bitmap，失败则返回null
     */
    fun decodeUri(
        context: Context,
        uri: Uri?,
        maxWidth: Int,
        maxHeight: Int,
        level: Bitmap.Config
    ): Bitmap? {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //只读取图片尺寸
        resolveUri(context, uri, options)

        //计算实际缩放比例
        var scale = 1
        for (i in 0 until Int.MAX_VALUE) {
            if ((options.outWidth / scale > maxWidth && options.outWidth / scale < maxWidth * 1.4) ||
                (options.outHeight / scale > maxHeight && options.outHeight / scale < maxHeight * 1.4)
            ) {
                scale++
            } else {
                break
            }
        }
        options.inSampleSize = scale
        options.inJustDecodeBounds = false //读取图片内容
        options.inPreferredConfig = level //根据情况进行修改

        var bitmap: Bitmap? = null
        try {
            bitmap = resolveUriForBitmap(context, uri, options)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return bitmap
    }


    /**
     * 解析URI
     */
    private fun resolveUri(context: Context, uri: Uri?, options: BitmapFactory.Options) {
        if (uri == null) {
            return
        }
        val scheme = uri.scheme
        if (ContentResolver.SCHEME_CONTENT == scheme || ContentResolver.SCHEME_FILE == scheme) {
            var stream: InputStream? = null
            try {
                stream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(stream, null, options)
            } catch (e: java.lang.Exception) {
                Log.w("resolveUri", "无法打开内容: $uri", e)
            } finally {
                if (stream != null) {
                    try {
                        stream.close()
                    } catch (e: IOException) {
                        Log.w("resolveUri", "无法关闭内容: $uri", e)
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE == scheme) {
            Log.e("resolveUri", "无法关闭内容: $uri")
        } else {
            Log.e("resolveUri", "无法关闭内容: $uri")
        }
    }


    private fun resolveUriForBitmap(
        context: Context,
        uri: Uri?,
        options: BitmapFactory.Options
    ): Bitmap? {
        if (uri == null) {
            return null
        }
        var bitmap: Bitmap? = null
        val scheme = uri.scheme
        if (ContentResolver.SCHEME_CONTENT == scheme || ContentResolver.SCHEME_FILE == scheme) {
            var stream: InputStream? = null
            try {
                stream = context.contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(stream, null, options)
            } catch (e: java.lang.Exception) {
                Log.w("resolveUriForBitmap", "Unable to open content: $uri", e)
            } finally {
                if (stream != null) {
                    try {
                        stream.close()
                    } catch (e: IOException) {
                        Log.w("resolveUriForBitmap", "Unable to close content: $uri", e)
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE == scheme) {
            Log.w("resolveUriForBitmap", "Unable to close content: $uri")
        } else {
            Log.w("resolveUriForBitmap", "Unable to close content: $uri")
        }
        return bitmap
    }

    /**
     * 综合压缩算法
     */
    fun compressPicture(
        context: Context,
        uri: Uri?,
        cameraSavePath: File,
        maxWidth: Int = HDMaxWidth,
        maxHeight: Int = HDMaxHeight,
        level: Bitmap.Config = Bitmap.Config.RGB_565
    ):Bitmap? {
        return try {
            val path:String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) cameraSavePath.toString() else uri!!.encodedPath
            Log.e("path", path!!)
            compressImage(context, path, 2048)
        }catch (e: Exception){
            e.printStackTrace()
            decodeUri(context, uri, maxWidth, maxHeight, level)
        }
    }
}