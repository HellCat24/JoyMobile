package joy.reactor.data.requests

import joy.reactor.data.common.ioObservable
import joy.reactor.data.enteties.Image
import joy.reactor.data.enteties.Tag
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.const.URLConst
import rx.Observable
import java.util.*

/**
 * Created by y2k on 19/10/15.
 */
class TagsForUserRequest(private val username: String) {

    fun request(): Observable<List<Tag>> {
        return ioObservable {
            val document = HttpClient.instance.getDocument(URLConst.USERNAME_URL + username)
            val tags = ArrayList<Tag>()
            for (h in document.select(".sideheader")) {
                if ("Читает" == h.text()) {
                    for (a in h.parent().select("a")) {
                        val name = a.text()
                        tags.add(Tag(name, name, false, Image()))
                    }
                    break
                }
            }
            tags
        }
    }
}