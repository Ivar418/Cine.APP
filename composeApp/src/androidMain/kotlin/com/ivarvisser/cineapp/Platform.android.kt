package com.ivarvisser.cineapp

import android.content.Intent
import android.os.Build
import androidx.core.content.FileProvider
import net.codinux.log.Log
import net.codinux.log.android.AndroidContext
import java.io.File

class AndroidPlatform() : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isMobile: Boolean = true
    override val isAndroid: Boolean = true

    override fun openFile(bytes: ByteArray, fileName: String) {
        val context = AndroidContext.applicationContext ?: return
        try {
            // 1. Save the bytes to a file in the cache directory
            val file = File(context.cacheDir, fileName)
            file.writeBytes(bytes)

            // 2. Get a URI for the file using FileProvider
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            // 3. Create and start an Intent to view the PDF
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.error(e) { "Failed to open PDF file: $fileName" }
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()