package y2k.joyreactor.ui.post

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import org.json.JSONObject
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceInjector
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.http.HttpClient
import y2k.joyreactor.image.JoyGlide
import y2k.joyreactor.presenters.VideoPresenter
import y2k.joyreactor.ui.base.ToolBarActivity
import java.io.File

class VideoActivity : ToolBarActivity() {

    companion object {

        var BUNDLE_POST = "post"

        fun startActivity(activity: Activity?, post: Post) {
            val intent = Intent(activity, VideoActivity::class.java)
            intent.putExtra(BUNDLE_POST, post);
            activity!!.startActivity(intent)
        }
    }

    lateinit var mediaPlayer: MediaPlayer
    lateinit var videoPlayer: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val post = intent.getSerializableExtra(BUNDLE_POST) as Post?
        title = post?.title

        var progress = findViewById(R.id.progress)
        var image = findViewById(R.id.imageView) as  ImageView

        if (post?.image!!.isGif) {
            JoyGlide.load(image, post?.image)
        }

        if (post?.image!!.isCoub) {
            loadVideo(progress)
        }

        videoPlayer = findViewById(R.id.video) as VideoView
        videoPlayer.setOnPreparedListener {
            mp ->
            mp.isLooping = true
            progress.visibility = View.GONE
        }

        mediaPlayer = MediaPlayer();
        mediaPlayer.setOnPreparedListener {
            mp ->
            mp.isLooping = true
            mp.start()
        }

         ServiceInjector.inject(
                    object : VideoPresenter.View {

                        override fun showVideo(videoFile: File) {

                        }

                        override fun setBusy(isBusy: Boolean) {

                        }
                    }).loadVideo(post)
    }

    private fun loadVideo(progress: View) {
        var videoUrl = ""
        var audioUrl = ""

        progress.visibility = View.VISIBLE

        ioObservable {

            var jsonObject = JSONObject(HttpClient.instance.getRawString("https://coub.com/api/v2/coubs/5u5n1"))

            var imageUrl = jsonObject.getString("picture")
            var filesObject = jsonObject.getJSONObject("file_versions").getJSONObject("iphone")

            audioUrl = jsonObject.getJSONObject("file_versions").getJSONObject("mobile").getString("audio_url");
            videoUrl = filesObject.getString("url");
        }.subscribeOnMain {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepareAsync()
            videoPlayer.setVideoURI(Uri.parse(videoUrl))
            videoPlayer.start()
        }
    }

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()
    override val layoutId: Int
        get() = R.layout.activity_video

    override fun onStop() {
        super.onStop()
        if (mediaPlayer != null) {
            mediaPlayer.release()
            videoPlayer.stopPlayback()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        else
            super.onOptionsItemSelected(item)
        return true
    }
}