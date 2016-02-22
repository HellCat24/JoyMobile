package y2k.joyreactor.services.requests

import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.http.HttpClient
import java.util.regex.Pattern

/**
 * Created by Oleg on 22.02.2016.
 */
class LikeDislikeRequest {

    fun like(postId: String): Observable<Double> {
        return ioObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get("http://joyreactor.cc/post_vote/add/" + postId + "/plus")
            doc.body().html().replace("[^0-9]".toRegex(), "").toFloat()/10.0;
        }
    }

    fun disLike(postId: String): Observable<Double> {
        return ioObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get("http://joyreactor.cc/post_vote/add/" + postId + "/minus")
            doc.body().html().replace("[^0-9]".toRegex(), "").toFloat()/10.0;
        }
    }
}