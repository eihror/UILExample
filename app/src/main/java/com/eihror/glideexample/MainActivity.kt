package com.eihror.glideexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var imageUtil: ImageUtil = ImageUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageUtil.initImageLoader(applicationContext)

        imageUtil._imageOnLoading = R.drawable.ic_launcher_foreground
        imageUtil._imageForEmptyUri = R.drawable.ic_launcher_foreground
        imageUtil._imageOnFail = R.drawable.ic_launcher_foreground


    }
}
