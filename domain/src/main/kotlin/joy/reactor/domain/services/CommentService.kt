package joy.reactor.domain.services

import joy.reactor.data.common.ioObservable
import joy.reactor.data.repository.MemoryBuffer
import joy.reactor.data.requests.PostRequest
import joy.reactor.data.requests.factory.CreateCommentRequestFactory
import rx.Observable

/**
 * Created by y2k on 04/12/15.
 */
class CommentService(
        private val requestFactory: CreateCommentRequestFactory,
        private val postRequest: PostRequest,
        private val postBuffer: MemoryBuffer) {

    fun createComment(postId: String, commentText: String, parentId: Long?): Observable<Unit> {
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