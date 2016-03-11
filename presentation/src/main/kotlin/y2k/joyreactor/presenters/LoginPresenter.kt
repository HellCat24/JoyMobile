package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.domain.services.ProfileService
import y2k.joyreactor.platform.Navigation

/**
 * Created by y2k on 9/29/15.
 */
class LoginPresenter(
        private val view: LoginPresenter.View,
        private val service: ProfileService) {

    fun login(username: String, password: String) {
        view.setBusy(true)
        service
                .login(username, password)
                .subscribeOnMain({
                    view.setBusy(false)
                    Navigation.instance.switchLoginToProfile()
                }, {
                    it.printStackTrace()
                    view.setBusy(false)
                    view.showError()
                })
    }

    fun register() {
        view.openUrl("http://joyreactor.cc/register")
    }

    interface View {

        fun setBusy(isBusy: Boolean)

        fun showError()

        fun openUrl(url: String)
    }
}