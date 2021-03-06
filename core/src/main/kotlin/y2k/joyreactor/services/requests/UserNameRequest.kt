package y2k.joyreactor.services.requests

import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.http.HttpClient

/**
 * Created by y2k on 10/4/15.
 */
class UserNameRequest {

    fun request(): Observable<String?> {
        return ioObservable {
            val document = HttpClient.instance.getDocument("http://joyreactor.cc/donate")
            document.select("a#settings").first()?.text()
        }
    }
}