package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.requests.LikeDislikeRequest

/**
 * Created by Oleg on 02.03.2016.
 */
class FavoriteService(private val requestFactory: FavoriteRequest) {

    fun addToFavorite(postId: String): Observable<Boolean> {
        return requestFactory.addToFavorite(postId)
    }

    fun deleteFromFavorite(postId: String): Observable<Boolean> {
        return requestFactory.deleteFromFavorite(postId)
    }
}