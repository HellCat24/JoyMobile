package joy.reactor.data.requests

import joy.reactor.data.common.ioObservable
import joy.reactor.data.http.HttpClient
import rx.Observable

/**
 * Created by y2k on 10/4/15.
 */
class UserNameRequest {

    fun request(): Observable<String?> {
        return ioObservable {
            val document = HttpClient.Companion.instance.getDocument("http://joyreactor.cc/donate")
            document.select("a#settings").first()?.text()
        }
    }
}