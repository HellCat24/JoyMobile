package joy.reactor.data.requests

import joy.reactor.data.common.ioUnitObservable
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.const.URLConst
import rx.Observable

/**
 * Created by y2k on 10/2/15.
 */
class SendMessageRequest(private val username: String) {

    fun request(message: String): Observable<Unit> {
        return ioUnitObservable {
            HttpClient.instance
                    .beginForm()
                    .put("username", username)
                    .put("text", message)
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", URLConst.MESSAGE_URL)
                    .send(URLConst.CREATE_MESSAGE_URL)
        }
    }
}