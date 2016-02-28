package y2k.joyreactor.presenters

import y2k.joyreactor.Message
import y2k.joyreactor.Profile
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.services.ProfileService

/**
 * Created by y2k on 9/30/15.
 */
class ProfilePresenter(
    private val view: ProfilePresenter.View,
    private val service: ProfileService,
    private val navigation: Navigation) {

    fun loadProfile() {
        view.setBusy(true)
        service.getProfile()
                .subscribeOnMain({
                    view.setProfile(it)
                    view.setBusy(false)
                }, {
                    view.setBusy(false)
                    view.hideProfileMenu()
                })
    }

    fun logout() {
        service.logout().subscribeOnMain { Navigation.instance.switchProfileToLogin() }
    }

    fun openTags() {
        navigation.openTags()
    }

    fun openDialogs() {
        navigation.openDialogs()
    }

    fun openLogin() {
        navigation.openLogin()
    }

    interface View {

        fun setProfile(profile: Profile)

        fun setBusy(isBusy: Boolean)

        fun hideProfileMenu()
    }
}