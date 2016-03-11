package joy.reactor.data.requests

import joy.reactor.data.common.ioUnitObservable
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.const.URLConst
import rx.Observable
import java.net.URLEncoder

/**
 * Created by y2k on 19/10/15.
 */
class AddTagRequest(private val tagName: String) {

    fun request(): Observable<Unit> {
        return ioUnitObservable {
            val tagUrl = URLConst.Companion.TAG_REQUEST + URLEncoder.encode(tagName)
            val tagPage = HttpClient.Companion.instance.getDocument(tagUrl)
            val addTagLink = tagPage.select("a.change_favorite_link").first().absUrl("href")
            HttpClient.Companion.instance.getText(addTagLink)
        }
    }
}