package joy.reactor.data.requests.factory

import joy.reactor.data.common.ioUnitObservable
import joy.reactor.data.http.HttpClient
import rx.Observable

/**
 * Created by y2k on 19/10/15.
 */
class CreateCommentRequestFactory {

    fun create(postId: String, commentText: String, parentId: Long?): Observable<Unit> {
        return ioUnitObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .put("parent_id", parentId.toString())
                    .put("post_id", postId)
                    .put("comment_text", commentText)
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/post/" + postId)
                    .send("http://joyreactor.cc/post_comment/create")
            doc.body();
        }
    }
}
