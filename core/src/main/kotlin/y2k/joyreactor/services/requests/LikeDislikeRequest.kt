package y2k.joyreactor.services.requests

import org.jsoup.Jsoup
import rx.Observable
import y2k.joyreactor.common.ioUnitObservable
import y2k.joyreactor.http.HttpClient
import java.util.regex.Pattern

/**
 * Created by Oleg on 22.02.2016.
 */
class LikeDislikeRequest {

    private val postId: String? = null

    fun like(postId: String): Observable<Unit> {
        return ioUnitObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .put("token", getToken())
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get("http://joyreactor.cc/post_vote/add/" + postId + "/plus")
            doc.body();
        }
    }

    fun disLike(postId: String): Observable<Unit> {
        return ioUnitObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .put("token", getToken())
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get("http://joyreactor.cc/post_vote/add/" + postId + "/minus")
            doc.body();
        }
    }

    private fun getToken(): String {
        var doc = Jsoup.connect("http://joyreactor.cc/donate").get().toString();
        val m = TOKEN_REGEX.matcher(doc)
        if (!m.find()) throw IllegalStateException()
        return m.group(1)
    }

    companion object {

        val TOKEN_REGEX = Pattern.compile("var token = '(.+?)'")
    }
}