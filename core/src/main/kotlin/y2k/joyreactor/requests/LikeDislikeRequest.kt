package y2k.joyreactor.requests

import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.http.HttpClient
import y2k.joyreactor.requests.const.URLConst
import java.util.regex.Pattern

/**
 * Created by Oleg on 22.02.2016.
 */
class LikeDislikeRequest {

    fun like(postId: String): Observable<Double> {
        return ioObservable {
            var doc = HttpClient.Companion.instance
                    .beginForm()
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get(URLConst.LIKE_REQUEST + postId + "/plus")
            doc.body().html().replace("[^0-9]".toRegex(), "").toFloat() / 10.0;
        }
    }

    fun disLike(postId: String): Observable<Double> {
        return ioObservable {
            var doc = HttpClient.Companion.instance
                    .beginForm()
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get(URLConst.LIKE_REQUEST + postId + "/minus")
            doc.body().html().replace("[^0-9]".toRegex(), "").toFloat() / 10.0;
        }
    }
}