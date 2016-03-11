package y2k.joyreactor.ui

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import joy.reactor.data.common.subscribeOnMain
import y2k.joyreactor.platform.UpdateService
import y2k.joyreactor.ui.profile.tags.AddTagDialogFragment

/**
 * Created by y2k on 05/02/16.
 */
class UpdateFragment(c: Context) : ProgressDialog(c) {

    lateinit var service: UpdateService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = UpdateService(context.applicationContext)
        setMessage("Checking for updates")
        service.checkHasUpdates().subscribeOnMain {
            service.update().subscribeOnMain({ dismiss() })
        }
    }

    companion object {

        private val TAG_ID = "updates_tag"

        fun show(context: Context) {
            UpdateFragment(context).show()
        }

        fun dismiss(activity: AppCompatActivity) {
            val dialog = activity.supportFragmentManager.findFragmentByTag(TAG_ID) as AddTagDialogFragment
            dialog.dismiss()
        }
    }
}