package y2k.joyreactor.services.requests

import org.jsoup.Jsoup
import rx.Observable
import y2k.joyreactor.common.ioUnitObservable
import y2k.joyreactor.http.HttpClient
import java.util.regex.Pattern

/**
 * Created by y2k on 19/10/15.
 */class CreateCommentRequestFactory {

    private val commentId: String? = null

    fun create(postId: String, commentText: String): Observable<Unit> {
        return ioUnitObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .put("parent_id", commentId ?: "0")
                    .put("post_id", postId)
                    .put("comment_text", commentText)
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/post/" + postId)
                    .send("http://joyreactor.cc/post_comment/create")
            doc.body();
        }
    }
}
