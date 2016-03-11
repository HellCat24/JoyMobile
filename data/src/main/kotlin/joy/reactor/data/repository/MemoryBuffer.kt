package joy.reactor.data.repository

import joy.reactor.data.enteties.Attachment
import joy.reactor.data.enteties.Comment
import joy.reactor.data.enteties.Message
import joy.reactor.data.enteties.Post
import joy.reactor.data.requests.PostRequest

/**
 * Created by y2k on 12/23/15.
 */
object MemoryBuffer {

    private var request: PostRequest? = null

    var messages: List<Message> = emptyList()

    val post: Post get() {
        return request!!.post!!
    }

    val comments: List<Comment> get() {
        return request?.comments ?: emptyList()
    }

    val attachments: List<Attachment> get() {
        return request?.getAttachments() ?: emptyList()
    }

    fun updatePost(request: PostRequest) {
        this.request = request
    }
}