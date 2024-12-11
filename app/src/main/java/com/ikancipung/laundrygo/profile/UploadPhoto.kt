package com.ikancipung.laundrygo.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID


fun UploadImageToGitHub(imageUri: Uri, githubToken: String, context: Context) {
    val scope = CoroutineScope(Dispatchers.Main)

    scope.launch {
        withContext(Dispatchers.IO) {
            try {
                val contentResolver: ContentResolver = context.contentResolver
                val inputStream: InputStream = contentResolver.openInputStream(imageUri)!!

                // Ambil nama file dari URI (atau buat nama unik)
                val fileName =
                    getFileNameFromUri(imageUri, context) ?: "profile_${UUID.randomUUID()}.jpg"

                // Simpan file sementara
                val tempFile = File(context.cacheDir, fileName)
                val outputStream = FileOutputStream(tempFile)
                inputStream.copyTo(outputStream)

                outputStream.flush()
                outputStream.close()

                val byteArray = tempFile.readBytes()
                val base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP)

                val url =
                    URL("https://api.github.com/repos/idhamma/LaundryGO-image-storage/contents/$fileName")
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
                    val rawImageUrl =
                        "https://raw.githubusercontent.com/idhamma/LaundryGO-image-storage/starter/$fileName"
                    updateProfileImageUrlInFirebase(rawImageUrl, context)
                } else {
                    val responseCode = connection.responseCode
                    val responseMessage = connection.responseMessage

                    Log.d("GitHubAPI", "Response Code: $responseCode")
                    Log.d("GitHubAPI", "Response Message: $responseMessage")

                    val errorStream = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.e("GitHubAPI", "Error Body: $errorStream")

                    Log.e("GitHubUpload", "Failed: $responseCode - $responseMessage")
                    withContext(Dispatchers.Main) {

                        if (responseMessage == "Unprocessable Entity") {
                            val rawImageUrl =
                                "https://raw.githubusercontent.com/idhamma/LaundryGO-image-storage/starter/$fileName"
                            updateProfileImageUrlInFirebase(rawImageUrl, context)
                        } else {
                            Toast.makeText(
                                context, "Gagal upload: $responseMessage", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GitHubUpload", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

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

fun updateProfileImageUrlInFirebase(imageUrl: String, context: Context) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid!!)

    // Perbarui URL gambar di Firebase
    databaseRef.child("photoUrl").setValue(imageUrl).addOnSuccessListener {
        Toast.makeText(context, "Foto profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener {
        Toast.makeText(context, "Gagal memperbarui foto profil", Toast.LENGTH_SHORT).show()
    }
}


