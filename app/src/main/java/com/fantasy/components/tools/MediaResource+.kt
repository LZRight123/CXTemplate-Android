package com.fantasy.components.tools

import android.net.Uri
import android.os.Parcelable
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.blankj.utilcode.util.FileUtils
import com.fantasy.components.extension.randomString
import com.fantasy.components.widget.CXCoilImage
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
import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.UUID


@Parcelize
class MatisseColiImageEngine : ImageEngine {
    @Composable
    override fun Image(mediaResource: MediaResource) {
        CXCoilImage(
            model = { mediaResource.uri },
            modifier = Modifier.fillMaxSize()
        )
    }

    @Composable
    override fun Thumbnail(mediaResource: MediaResource) {
        CXCoilImage(
            model = { mediaResource.uri },
            modifier = Modifier.fillMaxSize()
        )
    }

}

suspend fun Uri.compress(
    config: Compression.() -> Unit = { default() }
): File {
    val context = getContext
    val tmpFile = CXFileUtil.from(context, this)
    val compressFile = Compressor.compress(
        context,
        tmpFile
    ) {
        config()
    }
    cxlog(
        "图片压缩 原始图片大小： ${FileUtils.getSize(tmpFile)} 压缩后图片大小： ${
            FileUtils.getSize(
                compressFile
            )
        } "
    )
    FileUtils.delete(tmpFile) //删掉浪费内存
    return compressFile
}


fun MediaResource.toUIImage() = UIImage(origin = this)

@Parcelize
data class UIImage(
    val id: String = UUID.randomUUID().toString(),
    val origin: MediaResource,
    val finalImageUrl: String? = null,
) : Parcelable {
    val uri get() = origin.uri
    val path get() = origin.path ?: ""
    val name get() = origin.name ?: ""

    suspend fun compress(
        config: Compression.() -> Unit = { default() }
    ) = uri.compress(config)

    companion object {
        fun build(uri: Uri) = UIImage(
            origin = MediaResource(
                id = 999,
                bucketId = "",
                bucketName = "",
                uri = uri,
                path = uri.path ?: "",
                name = uri.lastPathSegment ?: "",
                mimeType = "image/jpeg"
            )
        )

        val mock get() = build(Uri.parse(randomString(3)))
    }
}

@Composable
fun rememberLauncherUIImage(
    unique: Boolean = false, // unique 为 true 可以反复选取同一张照片
    onResult: (List<UIImage>) -> Unit
): ManagedActivityResultLauncher<Matisse, List<MediaResource>?> = rememberLauncherForActivityResult(
    contract = MatisseContract()
) { items ->
    val result = items?.map { item ->
        UIImage(
            id = if (unique) UUID.randomUUID().toString() else item.uri.toString(),
            origin = item
        )
    } ?: emptyList()
    onResult(result)
}

fun ManagedActivityResultLauncher<Matisse, List<MediaResource>?>.openAlbum(
    maxSelectable: Int = 1,
) {
    PermissionManager.checkPhotoAlbum {
        if (it) {
            launch(cxMatisse(maxSelectable = maxSelectable))
        }
    }
}

fun ManagedActivityResultLauncher<MatisseCapture, List<MediaResource>?>.openCamera() =
    launch(cxMatisseCapture())

private fun cxMatisse(
    maxSelectable: Int = 1,
    singleMediaType: Boolean = true,
    captureStrategy: CaptureStrategy? = null,
) = Matisse(
    maxSelectable = maxSelectable,
    imageEngine = MatisseColiImageEngine(),
    fastSelect = maxSelectable == 1,
    singleMediaType = singleMediaType,
    captureStrategy = captureStrategy,
)

private fun cxMatisseCapture(
    captureStrategy: CaptureStrategy = SmartCaptureStrategy("${appName}希望访问您的相册和相机用来选择图片来更改您的头像及记录您的日常"),
) = MatisseCapture(
    captureStrategy = captureStrategy,
)