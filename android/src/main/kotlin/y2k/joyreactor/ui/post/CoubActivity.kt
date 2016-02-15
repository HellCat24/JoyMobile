package y2k.joyreactor.ui.post

/**
 * Created by Oleg on 14.02.2016.
 */

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import y2k.joyreactor.R
import y2k.joyreactor.ui.utils.PostUtils

class CoubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coub)

        val coubPlayer = findViewById(R.id.video) as WebView

        val display = windowManager.getDefaultDisplay()
        val size = Point()
        display.getSize(size)

        //TODO Finish this
        coubPlayer.visibility = View.VISIBLE
        coubPlayer.layoutParams.height = size.x * 9 / 16 - 20
        coubPlayer.requestLayout()
        //TODO Move this to the service
        PostUtils.loadCoub(coubPlayer, "COUB_URL_HERE")
    }
}
