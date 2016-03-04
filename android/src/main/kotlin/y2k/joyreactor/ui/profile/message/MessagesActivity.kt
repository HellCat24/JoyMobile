package y2k.joyreactor.ui.profile.message

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import y2k.joyreactor.enteties.Message
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceInjector
import y2k.joyreactor.services.BroadcastService
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by Oleg on 14.02.2016.
 */
class MessagesActivity() : ToolBarActivity() {

    companion object {

        var BUNDLE_DIALOG = "dialog"

        fun startActivity(activity: Activity?, dialog: Message) {
            val intent = Intent(activity, MessagesActivity::class.java)
            intent.putExtra(BUNDLE_DIALOG, dialog);
            activity!!.startActivity(intent)
        }
    }

    private var currentDialog: Message? = null

    override val fragmentContentId: Int
        get() = R.id.container

    override val layoutId: Int
        get() = R.layout.activity_container

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDialog = intent.getSerializableExtra(BUNDLE_DIALOG) as Message?
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
        replaceFragment(MessageFragment())
    }


    //TODO Refactore this
    override fun onPostResume() {
        super.onPostResume()
        ServiceInjector.resolve(BroadcastService::class).broadcast(BroadcastService.ThreadSelectedMessage(currentDialog!!))
    }
}