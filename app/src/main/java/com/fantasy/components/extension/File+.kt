import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import com.fantasy.components.tools.CCFileUtil
import java.io.File
import java.io.FileOutputStream

/**
 * https://github.com/jeziellago/image-minifier file image bitmap处理
 * https://juejin.cn/post/6959840823261265957 - 压缩
 */

/**
 * 压缩
 */
fun Bitmap.matrixScale(scale: Float): Bitmap {
    val matrix = Matrix()
    matrix.setScale(scale, scale)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun File.asRgb565Bitmap(): Bitmap {
    val options = BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.RGB_565
    return BitmapFactory.decodeFile(this.absolutePath, options)
}

fun File.asBitmap(
    shouldAdjustRotation: Boolean = false,
    options: BitmapFactory.Options? = null
): Bitmap {
    val bitmap = BitmapFactory.decodeFile(this.absolutePath, options)

    return if (shouldAdjustRotation) {
        val exif = ExifInterface(this.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        val matrix = Matrix().apply {
            when (orientation) {
                6 -> postRotate(90f)
                3 -> postRotate(180f)
                8 -> postRotate(270f)
            }
        }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}

fun Bitmap.toFile(): File? {
    try {
        // 将 Bitmap 写入临时文件
        val file = CCFileUtil.createTmpFile()
        val fileOutputStream = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        return file
    } catch (e: Exception) {
        e.printStackTrace()
        // 处理异常
        return null
    }
}