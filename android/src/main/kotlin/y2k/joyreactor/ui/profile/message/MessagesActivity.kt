package y2k.joyreactor.ui.profile.message

import android.os.Bundle
import y2k.joyreactor.Message
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.services.BroadcastService
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by Oleg on 14.02.2016.
 */
class MessagesActivity() : ToolBarActivity() {

    private var currentDialog: Message? = null

    override val fragmentContentId: Int
        get() = R.id.container

    override val layoutId: Int
        get() = R.layout.activity_container

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDialog = intent.getSerializableExtra("dialog") as Message?
        replaceFragment(MessageFragment())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
    }

    override fun onPostResume() {
        super.onPostResume()
        ServiceLocator.resolve(BroadcastService::class).broadcast(BroadcastService.ThreadSelectedMessage(currentDialog!!))
    }
}