package joy.reactor.data.requests

import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.const.URLConst
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by Oleg on 02.03.2016.
 */
class FavoriteRequest {

    fun addToFavorite(postId: String): Observable<Boolean> {
        return Observable
                .fromCallable {
                    val rand = 1000 + Random().nextInt(9000)
                    var doc = HttpClient.instance
                            .beginForm()
                            .putHeader("X-Requested-With", "XMLHttpRequest")
                            .putHeader("Referer", "http://joyreactor.cc/")
                            .put("rand", rand.toString())
                            .get(URLConst.ADD_FAVORITE_REQUEST + postId)
                    doc.text().equals("Ok");
                }.subscribeOn(Schedulers.io())
    }

    fun deleteFromFavorite(postId: String): Observable<Boolean> {
        return Observable
                .fromCallable {
                    val rand = 4000 + Random().nextInt(5000)
                    var doc = HttpClient.instance
                            .beginForm()
                            .putHeader("X-Requested-With", "XMLHttpRequest")
                            .putHeader("Referer", "http://joyreactor.cc/")
                            .put("rand", rand.toString())
                            .get(URLConst.DELETE_FAVORITE_REQUEST + postId)
                    doc.text().equals("Ok");
                }.subscribeOn(Schedulers.io())
    }
}