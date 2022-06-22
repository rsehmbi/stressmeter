package com.example.stressmeter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.stressmeter.ui.image.IMAGES
import com.example.stressmeter.ui.image.STRESS_SCORES
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SubmitResult : AppCompatActivity() {

    var CURRENT_PAGE = -1
    var CURRENT_IMAGE = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_result)

        val imagePageID = intent.getIntExtra("IMAGE_PAGE_ID",-1)
        val imageID = intent.getIntExtra("IMAGE_PICTURE_ID",-1)

        setImagetoImageView(imagePageID, imageID)
        CURRENT_PAGE = imagePageID
        CURRENT_IMAGE = imageID
    }

    private fun setImagetoImageView(page:Int, image:Int){
        val image_id = IMAGES[page][image]
        val selectedImage: ImageView = findViewById(R.id.result_image_view_id)
        selectedImage.setImageResource(image_id)
    }

    fun CloseSubmitScreen(view: View) {
        this.finish()
    }

    fun currentTime(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSSS")
        return current.format(formatter).toString().replace(":","")
    }

    fun writeResults(){
        var filename = "stress_timestamp.csv"
        var csvFile = File(getExternalFilesDir(null) , filename)

        val fileContent = "${currentTime()},${STRESS_SCORES[CURRENT_PAGE][CURRENT_IMAGE]}"
        if (!csvFile.exists()){
            csvFile.createNewFile()
        }
        try {
            val fileOutPutStream = FileOutputStream(csvFile, true)
            fileOutPutStream.write(fileContent.toByteArray())
            fileOutPutStream.write("\n".toByteArray())
            fileOutPutStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
        else{
            writeResults()
            this.finishAffinity();
        }
    }
    fun SaveResults(view: View) {
        checkPermissions()
    }
}