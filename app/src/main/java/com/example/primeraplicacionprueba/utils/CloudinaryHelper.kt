package com.example.primeraplicacionprueba.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryHelper {

    private var isInitialized = false

    fun init(context: Context, cloudName: String, apiKey: String, apiSecret: String) {
        if (!isInitialized) {
            val config = mapOf(
                "cloud_name" to cloudName,
                "api_key" to apiKey,
                "api_secret" to apiSecret,
                "secure" to true
            )
            MediaManager.init(context, config)
            isInitialized = true
        }
    }

    suspend fun uploadImage(
        imageUri: Uri,
        folder: String = "places"
    ): String = suspendCancellableCoroutine { continuation ->

        val requestId = MediaManager.get().upload(imageUri)
            .option("folder", folder)
            .option("resource_type", "image")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val secureUrl = resultData["secure_url"] as? String
                    if (secureUrl != null) {
                        continuation.resume(secureUrl)
                    } else {
                        continuation.resumeWithException(
                            Exception("Failed to get secure URL from upload result")
                        )
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(
                        Exception("Upload failed: ${error.description}")
                    )
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()

        continuation.invokeOnCancellation {
            MediaManager.get().cancelRequest(requestId)
        }
    }

    suspend fun uploadImages(
        imageUris: List<Uri>,
        folder: String = "places"
    ): List<String> {
        return imageUris.map { uri ->
            uploadImage(uri, folder)
        }
    }
}
