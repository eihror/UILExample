package com.eihror.uilexample

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView.ScaleType.CENTER_CROP
import com.nostra13.universalimageloader.core.display.BitmapDisplayer
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer
import java.util.Collections.synchronizedList
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import java.util.*

class ImageUtil {

  private var options: DisplayImageOptions? = null
  private var animationListener: AnimationListener = AnimationListener()

  data class DisplayerConfig(
    var strokeColor: Int = Color.WHITE,
    var strokeWidth: Float = 2f
  )

  private class AnimationListener : SimpleImageLoadingListener() {

    override fun onLoadingComplete(
      imageUri: String,
      view: View,
      loadedImage: Bitmap?
    ) {
      if (loadedImage != null) {
        val imageView = view as ImageView
        imageView.scaleType = CENTER_CROP
        val firstDisplay = !displayedImages.contains(imageUri)
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 500)
          displayedImages.add(imageUri)
        }
      }
    }

    companion object {
      internal val displayedImages = synchronizedList(LinkedList<String>())
    }
  }

  fun initImageLoader(context: Context) {
    val config = ImageLoaderConfiguration.Builder(context)
    config.threadPriority(Thread.NORM_PRIORITY - 2)
    config.denyCacheImageMultipleSizesInMemory()
    config.diskCacheFileNameGenerator(Md5FileNameGenerator())
    config.diskCacheSize(50 * 1024 * 1024) // 50 MiB
    config.tasksProcessingOrder(QueueProcessingType.LIFO)
    config.writeDebugLogs() // Remove for release app

    // Initialize ImageLoader with configuration.
    ImageLoader.getInstance()
        .init(config.build())
  }

  fun setData(
    imageOnLoading: Int?,
    imageForEmptyUri: Int?,
    imageOnFail: Int?
  ) {
    imageOnLoading?.let { setImageOnLoading(it) }
    imageForEmptyUri?.let { setImageForEmptyUri(it) }
    imageOnFail?.let { setImageOnFail(it) }
  }

  private fun initConfig(
    isCircularDisplayer: Boolean?,
    displayerConfig: DisplayerConfig?
  ) {

    displayerConfig?.let {
      setDisplayerConfig(it)
    }

    isCircularDisplayer?.let {
      if (it) {
        setBitmapDisplayer(
            CircleBitmapDisplayer(_displayerConfig.strokeColor, _displayerConfig.strokeWidth)
        )
      } else {
        setBitmapDisplayer(SimpleBitmapDisplayer())
      }
    }

    options = DisplayImageOptions.Builder()
        .showImageOnLoading(_imageOnLoading)
        .showImageForEmptyUri(_imageForEmptyUri)
        .showImageOnFail(_imageOnFail)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .displayer(_bitmapDisplayer as BitmapDisplayer)
        .build()
  }

  fun onClearUIL() {
    AnimationListener.displayedImages.clear()
  }

  private fun setImageOnLoading(imageOnLoading: Int) {
    _imageOnLoading = imageOnLoading
  }

  private fun setImageForEmptyUri(imageForEmptyUri: Int) {
    _imageForEmptyUri = imageForEmptyUri
  }

  private fun setImageOnFail(imageOnFail: Int) {
    _imageOnFail = imageOnFail
  }

  private fun setBitmapDisplayer(bitmapDisplayer: Any) {
    _bitmapDisplayer = bitmapDisplayer
  }

  private fun setDisplayerConfig(displayerConfig: DisplayerConfig) {
    _displayerConfig.strokeColor = displayerConfig.strokeColor
    _displayerConfig.strokeWidth = displayerConfig.strokeWidth
  }

  fun loadImage(
    imageUrl: String?,
    imageView: ImageView?,
    isCircularDisplayer: Boolean? = false,
    displayerConfig: DisplayerConfig? = DisplayerConfig()
  ) {
    this.initConfig(isCircularDisplayer, displayerConfig)

    ImageLoader.getInstance()
        .displayImage(imageUrl, imageView, options, animationListener)
  }

  companion object {
    private var _imageOnLoading: Int = -1
    private var _imageForEmptyUri: Int = -1
    private var _imageOnFail: Int = -1
    private var _displayerConfig: DisplayerConfig = DisplayerConfig()
    private var _bitmapDisplayer: Any? = null

  }
}