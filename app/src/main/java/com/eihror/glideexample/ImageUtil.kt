package com.eihror.glideexample

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
import java.util.Collections.synchronizedList
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import java.util.*

class ImageUtil {

    private var options: DisplayImageOptions? = null
    private var animationListener: AnimationListener? = null

    var _imageOnLoading: Int = -1
        set(value) {
            if (value != -1) {
                field = value
            }
        }

    var _imageForEmptyUri: Int = -1
        set(value) {
            if (value != -1) {
                field = value
            }
        }

    var _imageOnFail: Int = -1
        set(value) {
            if (value != -1) {
                field = value
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
        ImageLoader.getInstance().init(config.build())
    }

    private fun initConfig(): DisplayImageOptions? {
        options = DisplayImageOptions.Builder()
            .showImageOnLoading(_imageOnLoading)
            .showImageForEmptyUri(_imageForEmptyUri)
            .showImageOnFail(_imageOnFail)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(CircleBitmapDisplayer(Color.WHITE, 0f))
            .build()

        return options
    }

    fun loadImage(imageUrl: String, imageView: ImageView, hasAnimation: Boolean) {

        this.initConfig()

        animationListener = if (hasAnimation) {
            AnimationListener()
        } else {
            null
        }

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options, animationListener)

    }

    fun onClearUIL() {
        AnimationListener.displayedImages.clear()
    }

    private class AnimationListener : SimpleImageLoadingListener() {

        override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap?) {
            if (loadedImage != null) {
                val imageView = view as ImageView
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
}