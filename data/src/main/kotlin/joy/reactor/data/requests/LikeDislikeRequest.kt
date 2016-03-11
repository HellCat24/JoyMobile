package joy.reactor.data.requests

import joy.reactor.data.common.ioObservable
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.const.URLConst
import rx.Observable

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
                    .get(URLConst.LIKE_REQUEST + postId + "/plus")
            doc.body().html().replace("[^0-9]".toRegex(), "").toFloat() / 10.0;
        }
    }

    fun disLike(postId: String): Observable<Double> {
        return ioObservable {
            var doc = HttpClient.instance
                    .beginForm()
                    .putHeader("X-Requested-With", "XMLHttpRequest")
                    .putHeader("Referer", "http://joyreactor.cc/")
                    .get(URLConst.LIKE_REQUEST + postId + "/minus")
            doc.body().html().replace("[^0-9]".toRegex(), "").toFloat() / 10.0;
        }
    }
}