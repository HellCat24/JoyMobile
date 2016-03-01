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

    fun openSecretActivity() {
        navigation.openPostListForBlog("http://joyreactor.cc/tag/%25D1%2581%25D0%25B5%25D0%25BA%25D1%2580%25D0%25B5%25D1%2582%25D0%25BD%25D1%258B%25D0%25B5%2B%25D1%2580%25D0%25B0%25D0%25B7%25D0%25B4%25D0%25B5%25D0%25BB%25D1%258B/rating")
    }

    fun openTags() {
        navigation.openTags()
    }

    fun openFavorites(username : String) {
        navigation.openUserPosts(username + "/favorite")
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