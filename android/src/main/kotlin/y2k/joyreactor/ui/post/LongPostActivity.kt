package y2k.joyreactor.ui.post

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import y2k.joyreactor.R
import y2k.joyreactor.common.find
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.ui.adapter.ImageListAdapter
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by Oleg on 06.03.2016.
 */
class LongPostActivity : ToolBarActivity() {

    lateinit var adapter: ImageListAdapter

    companion object {

        var BUNDLE_POST = "post"

        fun startActivity(activity: Activity?, post: Post) {
            val intent = Intent(activity, LongPostActivity::class.java)
            intent.putExtra(BUNDLE_POST, post);
            activity!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val post = intent.getSerializableExtra(VideoActivity.BUNDLE_POST) as Post?

        val list = find<RecyclerView>(R.id.list)

        adapter = ImageListAdapter()
        adapter.setPostImages(post!!.images)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    override val layoutId: Int
        get() = R.layout.fragment_list
}