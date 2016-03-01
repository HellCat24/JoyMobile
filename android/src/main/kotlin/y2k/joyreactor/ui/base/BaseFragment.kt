package y2k.joyreactor.ui.base

import android.support.v4.app.Fragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.services.BroadcastService
import y2k.joyreactor.services.LifeCycleService

/**
 * Created by y2k on 2/3/16.
 */
open class BaseFragment : Fragment() {

    val lifeCycleService = LifeCycleService(ServiceLocator.resolve(BroadcastService::class))

    override fun onResume() {
        super.onResume()
        lifeCycleService.activate()
    }

    override fun onPause() {
        super.onPause()
        lifeCycleService.deactivate()
    }
}