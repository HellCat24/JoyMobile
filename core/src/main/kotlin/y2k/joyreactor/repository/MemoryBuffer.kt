package y2k.joyreactor.repository

import y2k.joyreactor.enteties.*
import y2k.joyreactor.requests.PostRequest

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