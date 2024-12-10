package com.ikancipung.laundrygo.profile

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ikancipung.laundrygo.R
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.UUID

fun UploadImageToGitHubold(imageUri: Uri, githubToken: String,context: Context) {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream = contentResolver.openInputStream(imageUri)!!

    // Gambar ke file sementara
    val tempFile = File(context.cacheDir, "profile.jpg")
    val outputStream = FileOutputStream(tempFile)

    // Menyalin InputStream ke OutputStream
    inputStream.copyTo(outputStream)

    outputStream.flush()
    outputStream.close()

    val byteArray = tempFile.readBytes()
    val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

    val url = URL("https://api.github.com/repos/idhamma/LaundryGO-image-storage/contents/profile-pic/${tempFile.name}")
    val connection = url.openConnection() as HttpURLConnection
    connection.apply {
        requestMethod = "PUT"
        setRequestProperty("Authorization", "Bearer $githubToken")
        setRequestProperty("Content-Type", "application/json")
        doOutput = true
    }

    val jsonData = """
        {
            "message": "Upload profile image",
            "content": "$base64Image",
            "branch": "main",
            "path": "profile-pic/${tempFile.name}"
        }
    """

    connection.outputStream.write(jsonData.toByteArray())

    connection.inputStream.bufferedReader().use {
        val response = it.readText()
        val photoPath = context.getString(R.string.path_profile_pic)
        // Parse response and handle success/failure
        if (connection.responseCode == HttpURLConnection.HTTP_CREATED) {
            // Ambil URL gambar dari response dan simpan di Firebase
            val rawImageUrl = "${photoPath}/${tempFile.name}"
            updateProfileImageUrlInFirebase(rawImageUrl, context)
        } else {
            // Tangani kesalahan upload
            Log.e("GitHubUpload", "Upload failed: $response")
        }
    }
}

fun UploadImageToGitHub(imageUri: Uri, githubToken: String, context: Context) {
    val scope = CoroutineScope(Dispatchers.Main)

    scope.launch {
        withContext(Dispatchers.IO) {
            try {
                val contentResolver: ContentResolver = context.contentResolver
                val inputStream: InputStream = contentResolver.openInputStream(imageUri)!!

                // Ambil nama file dari URI (atau buat nama unik)
                val fileName = getFileNameFromUri(imageUri, context) ?: "profile_${UUID.randomUUID()}.jpg"

                // Simpan file sementara
                val tempFile = File(context.cacheDir, fileName)
                val outputStream = FileOutputStream(tempFile)
                inputStream.copyTo(outputStream)

                outputStream.flush()
                outputStream.close()

                val byteArray = tempFile.readBytes()
                val base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP)

                val url = URL("https://api.github.com/repos/idhamma/LaundryGO-image-storage/contents/$fileName")
                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    requestMethod = "PUT"
                    setRequestProperty("Authorization", "Bearer $githubToken")
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }

                val jsonData = """
                    {
                        "message": "Upload profile image",
                        "content": "$base64Image",
                        "branch": "starter"
                    }
                """.trimIndent()

                connection.outputStream.write(jsonData.toByteArray())

                val responseCode = connection.responseCode
                val responseMessage = connection.responseMessage

                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // URL raw untuk file
                    val rawImageUrl = "https://raw.githubusercontent.com/idhamma/LaundryGO-image-storage/starter/$fileName"
                    updateProfileImageUrlInFirebase(rawImageUrl, context)
                } else {
                    Log.e("GitHubUpload", "Failed: $responseCode - $responseMessage")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Gagal upload: $responseMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("GitHubUpload", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

//fun UploadImageToGitHub(imageUri: Uri, token: String, context: Context) {
//    val uid = FirebaseAuth.getInstance().currentUser?.uid
//    if (uid == null) {
//        Toast.makeText(context, "User tidak terautentikasi", Toast.LENGTH_SHORT).show()
//        return
//    }
//
//    // Path untuk penyimpanan di GitHub
//    val fileName = "$uid.jpg"
//    val repository = "LaundryGO-image-storage"
//    val branch = "starter"
//    val filePath = "profile-pic/$fileName"
//    val rawImageUrl = "https://raw.githubusercontent.com/idhamma/$repository/$branch/$filePath"
//
//    try {
//        val file = File(imageUri.toFile().path)
//        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
//        val multipartBody = MultipartBody.Part.createFormData("content", fileName, requestBody)
//
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://api.github.com/repos/idhamma/$repository/contents/$filePath")
//            .header("Authorization", "Bearer $token")
//            .put(
//                MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart(
//                        "message", "Update profile picture for $uid"
//                    )
//                    .addFormDataPart("content", multipartBody.body.contentLength().toString())
//                    .build()
//            )
//            .build()
//
//        val response = client.newCall(request).execute()
//        if (response.isSuccessful) {
//            Log.d("UploadImageToGitHub", "Gambar berhasil diunggah ke GitHub")
//
//            // Perbarui URL di Firebase Realtime Database
//            FirebaseDatabase.getInstance().getReference("users").child(uid)
//                .child("photoUrl")
//                .setValue(rawImageUrl)
//                .addOnSuccessListener {
//                    Toast.makeText(context, "Foto profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(context, "Gagal memperbarui URL di database", Toast.LENGTH_SHORT).show()
//                }
//        } else {
//            Log.e("UploadImageToGitHub", "Gagal mengunggah gambar: ${response.message}")
//        }
//    } catch (e: Exception) {
//        Log.e("UploadImageToGitHub", "Terjadi kesalahan: ${e.message}")
//    }
//}

// Fungsi untuk mendapatkan nama file dari URI
private fun getFileNameFromUri(uri: Uri, context: Context): String? {
    var fileName: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow("_display_name"))
            }
        }
    }
    if (fileName == null) {
        fileName = uri.path?.substringAfterLast('/')
    }
    return fileName
}

fun updateProfileImageUrlInFirebase(imageUrl: String,context: Context) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid!!)

    // Perbarui URL gambar di Firebase
    databaseRef.child("photoUrl").setValue(imageUrl)
        .addOnSuccessListener {
            Toast.makeText(context, "Foto profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Gagal memperbarui foto profil", Toast.LENGTH_SHORT).show()
        }
}


