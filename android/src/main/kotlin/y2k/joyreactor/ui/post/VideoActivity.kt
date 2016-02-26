package y2k.joyreactor.ui.post

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import pl.droidsonroids.gif.GifTextureView
import pl.droidsonroids.gif.InputSource
import y2k.joyreactor.Post
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.ToolBarActivity
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream

class VideoActivity : ToolBarActivity() {

    companion object {

        var BUNDLE_POST = "post"

        fun startActivity(activity: Activity?, post: Post) {
            val intent = Intent(activity, VideoActivity::class.java)
            intent.putExtra(BUNDLE_POST, post);
            activity!!.startActivity(intent)
        }
    }

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()
    override val layoutId: Int
        get() = R.layout.activity_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val post = intent.getSerializableExtra(BUNDLE_POST) as Post?
        title = post?.title

        val assManager = getAssets()
        var inputStream: InputStream? = null
        try {
            inputStream = assManager.open("tes_gif.gif")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val caInput = BufferedInputStream(inputStream)
        (findViewById(R.id.gif_view) as  GifTextureView).setInputSource(InputSource.InputStreamSource(caInput))

        /*  val videoView = findViewById(R.id.video) as VideoView
         videoView.setOnPreparedListener { mp -> mp.isLooping = true }

        //TODO Refactor
         ServiceLocator.resolve(
                 object : VideoPresenter.View {

                     override fun showVideo(videoFile: File) {
                         videoView.setVideoURI(Uri.parse("https://coubsecure-a.akamaihd.net/get/b85/p/coub/simple/cw_file/1ec34ad130f/af257fc7b0baeaf0fe73e/iphone_1434643353_iphone.mp4"))
                         videoView.start()
                     }

                     override fun setBusy(isBusy: Boolean) {
                         findViewById(R.id.progress).visibility = if (isBusy) View.VISIBLE else View.GONE
                     }
                 }).loadVideo(post)*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        else
            super.onOptionsItemSelected(item)
        return true
    }
}