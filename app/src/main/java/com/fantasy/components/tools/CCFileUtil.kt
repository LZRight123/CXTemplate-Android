package com.fantasy.components.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.*
import java.util.UUID
import kotlin.uuid.Uuid

/**
 * Created on : June 18, 2016 Author : zetbaitsu Name : Zetra GitHub : https://github.com/zetbaitsu
 */
internal object CCFileUtil {
    private const val EOF = -1
    private const val DEFAULT_BUFFER_SIZE = 1024 * 4
    @Throws(IOException::class)
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = getFileName(context, uri)
        val splitName = splitFileName(fileName)
        val suffix = UUID.randomUUID().toString().takeLast(12)

        try {
            val tempFile = File.createTempFile(splitName[0], suffix)
            tempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out?.close()
            return tempFile
        } catch (e: Exception) {
            Log.e(
                    "CCFileUtil",
                    "Error creating temp file: ${e.message}, fileName=$fileName, prefix=${splitName[0]}, suffix=${splitName[1]}"
            )
            // 创建一个备用的临时文件
            val backupTempFile = File.createTempFile("backup", ".tmp")
            backupTempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(backupTempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out?.close()
            return backupTempFile
        }
    }

    private fun splitFileName(fileName: String?): Array<String?> {
        var name = fileName
        var extension: String? = ""
        val i = fileName!!.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }

        // 确保文件名前缀至少有3个字符，这是File.createTempFile的要求
        if (name.isNullOrEmpty() || name.length < 3) {
            name = "tmp" + (name ?: "")
        }

        return arrayOf(name, extension)
    }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun rename(file: File, newName: String?): File {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream?): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != input.read(buffer).also { n = it }) {
            output!!.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    fun createTmpFile(): File = File.createTempFile("book-", UUID.randomUUID().toString())
}
