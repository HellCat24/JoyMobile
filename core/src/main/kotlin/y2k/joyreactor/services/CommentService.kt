package y2k.joyreactor.services

import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.requests.factory.CreateCommentRequestFactory
import y2k.joyreactor.requests.PostRequest

/**
 * Created by y2k on 04/12/15.
 */
class CommentService(
        private val requestFactory: CreateCommentRequestFactory,
        private val postRequest: PostRequest,
        private val postBuffer: MemoryBuffer) {

    fun createComment(postId: String, commentText: String, parentId : Long?): Observable<Unit> {
        return requestFactory
                .create(postId, commentText, parentId)
                .flatMap {
                    ioObservable {
                        postRequest.request(postId.toString());
                        postBuffer.updatePost(postRequest)
                    }
                }
    }
}