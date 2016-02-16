package y2k.joyreactor.ui.profile

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import y2k.joyreactor.Profile
import y2k.joyreactor.R
import y2k.joyreactor.common.BaseFragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.common.isVisible
import y2k.joyreactor.presenters.ProfilePresenter
import y2k.joyreactor.ui.profile.message.DialogsActivity
import y2k.joyreactor.ui.profile.tags.TagsActivity
import y2k.joyreactor.view.WebImageView

class ProfileFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var navigationView = view.findViewById(R.id.navigation_view) as NavigationView

        val presenter = ServiceLocator.resolve(object : ProfilePresenter.View {

            override fun setProfile(profile: Profile) {
                (navigationView.findViewById(R.id.avatar) as WebImageView).setImage(profile.userImage)
                (navigationView.findViewById(R.id.rating) as TextView).text = "" + profile.rating
                (navigationView.findViewById(y2k.joyreactor.R.id.stars) as android.widget.RatingBar).rating = profile.stars.toFloat()
                (navigationView.findViewById(R.id.txt_profile_name) as TextView).text = "" + profile.userName
            }

            override fun setBusy(isBusy: Boolean) {
                view.findViewById(R.id.progress).isVisible = isBusy
            }
        })

        navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.isChecked)
                menuItem.isChecked = false
            else
                menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.tags -> {

                    true
                }
                R.id.messages -> {
                    startActivity(Intent(activity, DialogsActivity::class.java))
                    activity.overridePendingTransition(0, 0);
                    true
                }
                R.id.log_in -> {
                    //TODO Add Log Out
                    startActivity(Intent(activity, LoginActivity::class.java))
                    true
                }
                else -> {
                    Toast.makeText(activity, "Somethings Wrong", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }

        return view
    }
}