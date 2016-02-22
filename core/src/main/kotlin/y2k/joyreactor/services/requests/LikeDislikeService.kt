package y2k.joyreactor.services.requests

import rx.Observable

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