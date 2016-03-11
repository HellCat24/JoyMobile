package y2k.joyreactor.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import y2k.joyreactor.R
import y2k.joyreactor.ServiceInjector
import y2k.joyreactor.presenters.LoginPresenter
import y2k.joyreactor.ui.base.ToolBarActivity

class LoginActivity : ToolBarActivity(), LoginPresenter.View {

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val presenter = ServiceInjector.inject(this)

        findViewById(R.id.login).setOnClickListener {
            val username = (findViewById(R.id.username) as TextView).text.toString()
            val password = (findViewById(R.id.password) as TextView).text.toString()

            presenter.login(username, password)
        }
    }

    override fun setBusy(isBusy: Boolean) {
        findViewById(R.id.progress).visibility = if (isBusy) View.VISIBLE else View.GONE
    }

    override fun showError() {
        Toast.makeText(this, R.string.unknown_error_occurred, Toast.LENGTH_LONG).show()
    }

    override fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}