package y2k.joyreactor.services.requests

import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.http.HttpClient

/**
 * Created by y2k on 9/30/15.
 */
class LoginRequestFactory {

    fun request(username: String, password: String): Observable<String> {
        return ioObservable {
            getCsrf(username, password)
            //            System.out.println(doc.html());

            // FIXME:
            //            if (doc.getElementById("logout") == null)
            //                throw new IllegalStateException();
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