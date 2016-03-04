package y2k.joyreactor.requests

import rx.Observable
import y2k.joyreactor.common.ioUnitObservable
import y2k.joyreactor.http.HttpClient
import y2k.joyreactor.requests.const.URLConst

/**
 * Created by y2k on 10/2/15.
 */
class SendMessageRequest(private val username: String) {

    fun request(message: String): Observable<Unit> {
        return ioUnitObservable {
            HttpClient.Companion.instance
                    .beginForm()
                    .put("username", username)
                    .put("text", message)
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", URLConst.MESSAGE_URL)
                    .send(URLConst.CREATE_MESSAGE_URL)
        }
    }
}