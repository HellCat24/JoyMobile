package joy.reactor.data.requests.factory

import joy.reactor.data.common.ioObservable
import joy.reactor.data.http.HttpClient
import rx.Observable

/**
 * Created by y2k on 9/30/15.
 */
class LoginRequestFactory {

    fun request(username: String, password: String): Observable<String> {
        return ioObservable {
            getCsrf(username, password)
        }
    }

    private fun getCsrf(username: String, password: String): String {
        val document = HttpClient.instance.getDocument("http://joyreactor.cc/login")
        val token = document.getElementById("signin__csrf_token").attr("value")
        HttpClient.instance
                .beginForm()
                .put("signin[username]", username)
                .put("signin[password]", password)
                .put("signin[remember]", "on")
                .put("signin[_csrf_token]", token)
                .send("http://joyreactor.cc/login")
        return token
    }
}