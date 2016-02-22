package y2k.joyreactor.services.requests

import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.services.MemoryBuffer

/**
 * Created by Oleg on 22.02.2016.
 */
class LikeDislikeService(private val requestFactory: LikeDislikeRequest,
                                 private val postRequest: PostRequest,
                                 private val postBuffer: MemoryBuffer) {

    fun like(postId: String): Observable<Unit> {
        return requestFactory
                .like(postId)
                .flatMap {
                    ioObservable {
                        postRequest.request(postId.toString());
                        postBuffer.updatePost(postRequest)
                    }
                }
    }

    fun dislike(postId: String): Observable<Unit> {
        return requestFactory
                .disLike(postId)
                .flatMap {
                    ioObservable {
                        postRequest.request(postId.toString());
                        postBuffer.updatePost(postRequest)
                    }
                }
    }
}