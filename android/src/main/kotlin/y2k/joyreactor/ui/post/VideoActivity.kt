package y2k.joyreactor.ui.post

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.VideoView
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.presenters.VideoPresenter
import y2k.joyreactor.ui.base.ToolBarActivity

import java.io.File

class VideoActivity : ToolBarActivity() {

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()
    override val layoutId: Int
        get() = R.layout.activity_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val videoView = findViewById(R.id.video) as VideoView
        videoView.setOnPreparedListener { mp -> mp.isLooping = true }

        ServiceLocator.resolve(
                object : VideoPresenter.View {

                    override fun showVideo(videoFile: File) {
                        videoView.setVideoPath(videoFile.absolutePath)
                        videoView.start()
                    }

                    override fun setBusy(isBusy: Boolean) {
                        findViewById(R.id.progress).visibility = if (isBusy) View.VISIBLE else View.GONE
                    }
                })
    }
}