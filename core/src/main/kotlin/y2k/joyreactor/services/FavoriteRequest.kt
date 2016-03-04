package y2k.joyreactor.services

import rx.Observable
import rx.schedulers.Schedulers
import y2k.joyreactor.http.HttpClient
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
                            .get("http://joyreactor.cc/favorite/create/" + postId)
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
                            .get("http://joyreactor.cc/favorite/delete/" + postId)
                    doc.text().equals("Ok");
                }.subscribeOn(Schedulers.io())
    }
}