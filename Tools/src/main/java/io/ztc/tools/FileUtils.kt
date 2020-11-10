package io.ztc.tools

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import java.io.*
import java.net.HttpCookie
import java.util.*

/**
 * 文件管理工具
 */
object FileUtils {

    /**
     * 获取assets目录路径
     */
    fun getAssetsCacheFile(context: Context, fileName: String): String? {
        val cacheFile = File(context.cacheDir, fileName)
        try {
            val inputStream = context.assets.open(fileName)
            inputStream.use { _ ->
                val outputStream = FileOutputStream(cacheFile)
                outputStream.use { _->
                    val buf = ByteArray(1024)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) {
                        outputStream.write(buf, 0, len)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cacheFile.absolutePath
    }

    fun getCustomStyleFilePath( context:Context,  customStyleFileName:String):String? {

        var  outputStream:FileOutputStream? = null
        var  inputStream:InputStream? = null
        var parentPath:String? = null
        try {
            inputStream = context.assets.open("customConfigDir/$customStyleFileName");
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer);
            parentPath = context.filesDir.absolutePath;
            val customStyleFile = File("$parentPath/$customStyleFileName");
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();
            outputStream = FileOutputStream (customStyleFile);
            outputStream.write(buffer);
        } catch ( e:IOException) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e);
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch ( e:IOException) {
                Log.e("CustomMapDemo", "Close stream failed", e);
                return null;
            }
        }
        return "$parentPath/$customStyleFileName";
    }

    private const val SAVE_DIR = "appUpdate"
    private const val BITMAP_DIR = "Boohee"
    private const val AUDIO_DIR = "SoundRecorder"

    /**
     * 获取照片文件路径
     */
    fun getSaveDir(context: Context?):String{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            context!!.getExternalFilesDir(SAVE_DIR)!!.absolutePath
        }else{
            Environment.getExternalStorageDirectory().absolutePath
        }
    }

    /**
     * 存储bitmap
     */
    fun saveBitmap(context: Context?, bmp: Bitmap): String? {
        val appDir = File(getSaveDir(context), BITMAP_DIR)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            return fileName
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取音频文件
     */
    fun getAudioFileByUrl(context: Context?, fileName: String?): File? {
        var savePath: String = getSaveDir(context)
        savePath += "/$AUDIO_DIR/"
        return File(File(savePath), fileName!!)
    }

    /**
     * 获取音频文件路径
     */
    @JvmStatic
    fun getAudioFilePathByUrl(context: Context?, fileName: String?): String? {
        var savePath = getSaveDir(context)
        savePath += "/$AUDIO_DIR/"
        return savePath+fileName
    }

    /**
     * 获取bitmap根据文件名
     */
    fun getBitmapByUrl(context: Context?, fileName: String?): Bitmap? {
        val file = File(File(getSaveDir(context), BITMAP_DIR), fileName!!)
        return try {
            val bitmap = BitmapUtils.getBitmapByPath(file.canonicalPath)
            bitmap
        }catch (e: java.lang.Exception){
            null
        }
    }

    /**
     * 获取bitmap文件根据文件名
     */
    fun getBitmapFileByUrl(context: Context?, fileName: String?): File {
        return File(File(getSaveDir(context), BITMAP_DIR), fileName!!)
    }

    /**
     * 返回bitmap文件抽象路径
     */
    fun getBitmapPathByUrl(context: Context?, fileName: String?): String {
        return File(File(getSaveDir(context), BITMAP_DIR), fileName!!).path
    }

    /**
     * 返回bitmap文件绝对路径
     */
    fun getBitmapAbsolutePathByUrl(context: Context?, fileName: String?): String {
        return File(File( getSaveDir(context), BITMAP_DIR), fileName!!).absolutePath
    }

    /**
     * 删除图片文件
     */
    fun delBitmapFileByUrl(context: Context?, fileName: String?) {


        val file = File(File(getSaveDir(context), BITMAP_DIR), fileName!!)
        try {
            file.delete()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    /**
     * 删除音频文件
     */
    fun delAudioFileByUrl(context: Context?, fileName: String?) {
        var mFilePath: String = getSaveDir(context)
        mFilePath += "/$AUDIO_DIR/"

        val file = File(File(mFilePath), fileName!!)
        try {
            if (file.exists()){
                file.delete()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    /**
     * 文件转BASE64
     */
    fun fileToBase64(path: String?): String? {
        return try {
            val file = File(path!!)
            val inputFile = FileInputStream(file)
            val buffer = ByteArray(file.length().toInt())
            inputFile.read(buffer)
            inputFile.close()
            Base64.encodeToString(buffer, Base64.NO_WRAP)
        }catch (e: java.lang.Exception){
            null
        }
    }

    /**
     * 判断文件夹是否存在,如果不存在则创建文件夹
     */
    fun isExistAndMake(path: String) {
        val file = File(path);

        if (!file.exists()) {
            file.mkdir();
        }
    }

    @SuppressLint("NewApi")
    @JvmStatic
    fun getLengthByURL(context: Context, url: String, cookies:List<HttpCookie>):Int{
        val mediaPlayer = MediaPlayer()
        try {
            //mediaPlayer.setDataSource(url)
            mediaPlayer.setDataSource(context, Uri.parse(url), null, cookies)
            var duration:Int= 0
            mediaPlayer.setOnPreparedListener {
                duration = mediaPlayer.duration
            }
            mediaPlayer.prepareAsync()
            if (0 != duration) {
                return duration
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }finally {
            mediaPlayer.release()
        }
        return 0;
    }


    @JvmStatic
    fun getLengthByURI(path: String):Int{
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            val duration = mediaPlayer.duration
            if (0 != duration) {
                return duration
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }finally {
            mediaPlayer.release()
        }
        return 0;
    }

    /**
     * 文件是否存在
     * @param strFile 文件路径
     * @return
     */
    fun fileIsExists(strFile: String?): Boolean {
        try {
            val f = File(strFile!!)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * 删除单个文件
     * @param fileName 删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    fun deleteFile(fileName: String): Boolean {
        val file = File(fileName)
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        return if (file.exists() && file.isFile) {
            if (file.delete()) {
                println("删除单个文件" + fileName + "成功！")
                true
            } else {
                println("删除单个文件" + fileName + "失败！")
                false
            }
        } else {
            println("删除单个文件失败：" + fileName + "不存在！")
            false
        }
    }

    /**
     * 删除某个文件
     */
    private fun deleteFile(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children!!.indices) {
                val success = deleteFile(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        }
        return dir?.delete() ?: false
    }

    /**
     * 删除目录及目录下的文件
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    fun deleteDirectory(dir: String): Boolean {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        var dir = dir
        if (!dir.endsWith(File.separator)) dir += File.separator
        val dirFile = File(dir)
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory) {
            println("删除目录失败：" + dir + "不存在！")
            return false
        }
        var flag = true
        // 删除文件夹中的所有文件包括子目录
        val files = dirFile.listFiles()!!
        for (file in files) {
            // 删除子文件
            if (file.isFile) {
                flag = deleteFile(file.absolutePath)
                if (!flag) break
            } else if (file.isDirectory) {
                flag = deleteDirectory(file
                    .absolutePath)
                if (!flag) break
            }
        }
        if (!flag) {
            println("删除目录失败！")
            return false
        }
        // 删除当前目录
        return if (dirFile.delete()) {
            println("删除目录" + dir + "成功！")
            true
        } else {
            false
        }
    }

    /**
     * 删除数据库
     * @param context 上下文环境
     */
    fun deleteSql(context: Context) {
        val path = context.filesDir.parent
        //清空数据库目录；
        val fileDB = File("$path/databases")
        if (fileDB.exists()) {
            val files = fileDB.listFiles()!!
            for (file in files) {
                file.delete()
            }
        }
        Log.d("数据库", "清空")
    }

    /**
     * 删除某个文件夹下的文件
     */
    private fun deleteDirIn(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()!!
            for (child in children) {
                val success = deleteFile(File(dir, child))
                if (!success) {
                    return false
                }
            }
            true
        } else {
            false
        }
    }


    /**
     * 获取一个指定的文件路径
     */
    fun getSavePath(context: Context, dirName: String): String {
        var mFilePath: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Objects.requireNonNull<File>(
                context.getExternalFilesDir(null)
            ).absolutePath
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        mFilePath += "/$dirName/"
        return  mFilePath
    }

    /**
     * 清除指定文件下的所有文件
     */
    fun clearAudioCache(context: Context,dirName:String):Boolean {
        var savePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.getExternalFilesDir(null)!!.absolutePath
        } else {
            Environment.getExternalStorageDirectory().absolutePath
        }
        savePath += "/dirName/"
        val file = File(savePath)
        return if (file.exists()) {
            deleteDirIn(File(savePath))
        }else{
            false
        }
    }

}