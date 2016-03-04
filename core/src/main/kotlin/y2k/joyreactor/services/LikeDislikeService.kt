package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.requests.LikeDislikeRequest

/**
 * Created by Oleg on 22.02.2016.
 */
class LikeDislikeService(private val requestFactory: LikeDislikeRequest) {

    fun like(postId: String): Observable<Float> {
        return requestFactory
                .like(postId)
                .map { it -> it.toFloat() }
    }

    fun dislike(postId: String): Observable<Float> {
        return requestFactory
                .disLike(postId)
                .map { it -> it.toFloat() }
    }
}