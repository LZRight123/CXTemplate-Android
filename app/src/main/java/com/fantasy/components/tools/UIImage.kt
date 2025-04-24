package com.fantasy.components.tools

import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Parcelable
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toFile
import com.blankj.utilcode.util.FileUtils
import com.fantasy.components.extension.randomString
import com.fantasy.components.widget.CCCoilImage
import github.leavesczy.matisse.CaptureStrategy
import github.leavesczy.matisse.ImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseCapture
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.SmartCaptureStrategy
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.Compression
import id.zelory.compressor.constraint.default
import java.io.File
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import androidx.core.net.toUri
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@Parcelize
class MatisseColiImageEngine : ImageEngine {
    @Composable
    override fun Image(mediaResource: MediaResource) {
        CCCoilImage(model = { mediaResource.uri }, modifier = Modifier.fillMaxSize())
    }

    @Composable
    override fun Thumbnail(mediaResource: MediaResource) {
        CCCoilImage(model = { mediaResource.uri }, modifier = Modifier.fillMaxSize())
    }
}

suspend fun Uri.compress(config: Compression.() -> Unit = { default(width = 720, height = 960) }): File {
    val context = getContext
    try {
        val tmpFile = CCFileUtil.from(context, this)
        val compressFile = Compressor.compress(context, tmpFile) { config() }
        if (isDebugBuilder) {
            try {
                val originSize = getImageSizeFromUri(this)
                val newSize = getImageSizeFromUri(compressFile.toUri())
                cclog(
                    """
            图片压缩 原始图片大小： ${FileUtils.getSize(tmpFile)} 压缩后图片大小： ${FileUtils.getSize(compressFile)}
            originImageSize: 宽: ${originSize?.first}, 高: ${originSize?.second}
            newImageSize: 宽: ${newSize?.first}, 高: ${newSize?.second}
            """.trimIndent()
                )
            } catch (e: Exception) {

            }
        }

        FileUtils.delete(tmpFile) // 删掉浪费内存
        return compressFile
    } catch (e: Exception) {
        cclog("图片压缩失败: ${e.message}")
        // 创建一个备用的临时文件，避免应用崩溃
        val backupFile = File.createTempFile("backup_image", ".jpg")
        return backupFile
    }
}

fun Uri.toByteArray() = when(scheme) {
    "file" -> {
        cclog("uriToByteArray scheme: $scheme")
        toFile().readBytes()
    }
    else -> {
        // 对于 content:// 或其他类型的 Uri 使用通用方法
        getContext.contentResolver.openInputStream(this)?.use { inputStream ->
            inputStream?.readBytes()
        } ?: ByteArray(0) // 返回一个空的字节数组，避免应用崩溃
    }
}
@OptIn(ExperimentalEncodingApi::class)
suspend fun Uri.toBase64() = try {
    Base64.encode(toByteArray())
} catch (e: Exception) {
    cclog("转换Base64失败: ${e.message}")
    // 返回一个空的Base64字符串，避免应用崩溃
    ""
}

fun MediaResource.toUIImage(unique: Boolean = false) =
    UIImage(
        id = if (unique) UUID.randomUUID().toString() else uri.toString(),
        uriString = uri.toString(),
        name = name
    )

@Parcelize
@Serializable
data class UIImage(
    val id: String = UUID.randomUUID().toString(),
    val uriString: String = "",
    val name: String = "",
) : Parcelable, java.io.Serializable {
    val uri
        get() = uriString.toUri()

    val byteArray get() = uri.toByteArray()
    val path
        get() = uri.path ?: ""

    suspend fun compress(
        config: Compression.() -> Unit = { default(width = 540, height = 720)  }
    ) = uri.compress(config).toUri()


    val takenTime: LocalDateTime?  get() {
        return try {
            // 获取输入流
            val inputStream = getContext.contentResolver.openInputStream(uri)
            inputStream?.use {
                val exif = ExifInterface(it)
                // 获取原始拍摄时间
                val dateTimeString = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                cclog("getImageTakenTime dateTime: $dateTimeString")
                dateTimeString?.let {
                    LocalDateTime.parse(it, LocalDateTime.Format {
                        byUnicodePattern("yyyy:MM:dd HH:mm:ss")
                    })
                }
            }
        } catch (e: Exception) {
            cclog("getImageTakenTime error: $e")
            e.printStackTrace()
            null
        }
    }

    companion object {
        fun build(uri: Uri) =
            UIImage(
                uriString = uri.toString(),
                name = uri.lastPathSegment ?: "",
            )

        val mock
            get() = build(Uri.parse(randomString(3)))
    }
}

@Composable
fun rememberLauncherUIImage(
    unique: Boolean = false, // unique 为 true 可以反复选取同一张照片
    onResult: (List<UIImage>) -> Unit
): ManagedActivityResultLauncher<Matisse, List<MediaResource>?> =
    rememberLauncherForActivityResult(contract = MatisseContract()) { items ->
        val result = items?.map { item -> item.toUIImage(unique) } ?: emptyList()
        onResult(result)
    }

fun ManagedActivityResultLauncher<Matisse, List<MediaResource>?>.openAlbum(
    maxSelectable: Int = 1,
) {
    PermissionManager.checkPhotoAlbum {
        if (it) {
            launch(ccMatisse(maxSelectable = maxSelectable))
        }
    }
}

fun ManagedActivityResultLauncher<MatisseCapture, List<MediaResource>?>.openCamera() =
    launch(ccMatisseCapture())

private fun ccMatisse(
    maxSelectable: Int = 1,
    singleMediaType: Boolean = true,
    captureStrategy: CaptureStrategy? = null,
) =
    Matisse(
        maxSelectable = maxSelectable,
        imageEngine = MatisseColiImageEngine(),
        fastSelect = maxSelectable == 1,
        singleMediaType = singleMediaType,
        captureStrategy = captureStrategy,
    )

private fun ccMatisseCapture(
    captureStrategy: CaptureStrategy =
        SmartCaptureStrategy("${appName}希望访问您的相册和相机用来选择图片来更改您的头像及记录您的日常"),
) =
    MatisseCapture(
        captureStrategy = captureStrategy,
    )

// 根据 uri 计算大小
fun getImageSizeFromUri(uri: Uri?): Pair<Int, Int>? {
    if (uri == null) return null
    return try {
        val inputStream = getContext.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()
        Pair(options.outWidth, options.outHeight)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}