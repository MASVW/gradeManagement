package com.smp5.app
import android.os.Build
import android.provider.MediaStore
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.*

class ExportActivity : ComponentActivity() {

    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:3000/api/v1/csv"

    private val exportItems = listOf(
        ExportItem("Students", "$baseUrl/students"),
        ExportItem("Subjects", "$baseUrl/subject"),
        ExportItem("Grades", "$baseUrl/grades")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewExport)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExportAdapter(exportItems) { item ->
            downloadCsv(item)
        }
    }

    private fun downloadCsv(item: ExportItem) {
        val request = Request.Builder().url(item.url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ExportActivity, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val fileName = "${item.title.lowercase()}.csv"
                    val contentResolver = contentResolver
                    val values = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS) // Public download folder
                    }

                    val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
                    } else {
                        // For older versions of Android, you can use Environment.getExternalStoragePublicDirectory
                        val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                        Uri.fromFile(downloadDir)
                    }

                    uri?.let {
                        try {
                            val outputStream = contentResolver.openOutputStream(it)
                            val inputStream = body.byteStream()

                            outputStream?.use { os ->
                                inputStream.copyTo(os)
                                runOnUiThread {
                                    Toast.makeText(this@ExportActivity, "${item.title} saved to Downloads", Toast.LENGTH_LONG).show()
                                }
                            }
                        } catch (e: IOException) {
                            runOnUiThread {
                                Toast.makeText(this@ExportActivity, "Failed to save file: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })
    }
}

