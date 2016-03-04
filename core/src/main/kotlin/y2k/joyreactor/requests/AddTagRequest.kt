package y2k.joyreactor.requests

import rx.Observable
import y2k.joyreactor.common.ioUnitObservable
import y2k.joyreactor.http.HttpClient
import y2k.joyreactor.requests.const.URLConst
import java.net.URLEncoder

/**
 * Created by y2k on 19/10/15.
 */
class AddTagRequest(private val tagName: String) {

    fun request(): Observable<Unit> {
        return ioUnitObservable {
            val tagUrl = URLConst.TAG_REQUEST + URLEncoder.encode(tagName)
            val tagPage = HttpClient.Companion.instance.getDocument(tagUrl)
            val addTagLink = tagPage.select("a.change_favorite_link").first().absUrl("href")
            HttpClient.Companion.instance.getText(addTagLink)
        }
    }
}