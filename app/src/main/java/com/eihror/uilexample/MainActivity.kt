package com.eihror.uilexample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eihror.uilexample.ImageUtil.DisplayerConfig
import kotlinx.android.synthetic.main.activity_main.image_one
import kotlinx.android.synthetic.main.activity_main.image_two

class MainActivity : AppCompatActivity() {

  var imageUtil: ImageUtil = ImageUtil()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    imageUtil.initImageLoader(applicationContext)

    imageUtil.setData(
        imageOnLoading = R.drawable.ic_launcher_foreground,
        imageForEmptyUri = R.drawable.ic_launcher_foreground,
        imageOnFail = R.drawable.ic_launcher_foreground
    )

    imageUtil.loadImage(
        imageUrl = "https://static.quizur.com/i/b/5b5a275ed85d16.952723135b5a275ec94bf4.02056781.jpg",
        imageView = image_one,
        isCircularDisplayer = false,
        displayerConfig = null
    )

    imageUtil.loadImage(
        imageUrl = "https://static.quizur.com/i/b/5b5a275ed85d16.952723135b5a275ec94bf4.02056781.jpg",
        imageView = image_two,
        isCircularDisplayer = true,
        displayerConfig = DisplayerConfig(Color.BLACK, 16f)
    )

  }

  override fun onStop() {
    super.onStop()
    imageUtil.onClearUIL()
  }
}
