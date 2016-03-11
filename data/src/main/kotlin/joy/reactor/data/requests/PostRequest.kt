package joy.reactor.data.requests

import joy.reactor.data.enteties.Attachment
import joy.reactor.data.enteties.Comment
import joy.reactor.data.enteties.Image
import joy.reactor.data.enteties.Post
import joy.reactor.data.http.HttpClient
import java.util.*
import java.util.regex.Pattern

/**
 * Created by y2k on 11/21/15.
 */
class PostRequest {

    var post: Post? = null
    private val commentsRequest = PostCommentsRequest()
    private val attachments = ArrayList<Attachment>()

    val comments: List<Comment>
        get() = commentsRequest.comments

    fun request(postId: String) {
        val page = HttpClient.instance.getDocument(getPostUrl(postId))

        val postNode = page.select("div.postContainer").first()
        post = PostsListRequest.newPost(postNode) // TODO:

        commentsRequest.request(page)

        val imgElement = postNode.select("div.image > img")
        if (imgElement.size > 1)
            for (e in imgElement.subList(1, imgElement.size - 1)) {
                val a = Attachment(Image(e.absUrl("src"),
                        Integer.parseInt(e.attr("width")),
                        Integer.parseInt(e.attr("height"))))
                attachments.add(a)
            }
    }

    private fun getPostId(href: String): String {
        val m = POST_ID.matcher(href)
        if (!m.find()) throw IllegalStateException()
        return m.group(1)
    }

    private fun getPostUrl(postId: String): String {
        return "http://anime.reactor.cc/post/" + postId // TODO:
    }

    fun getAttachments(): List<Attachment> {
        return attachments
    }

    companion object {

        private val POST_ID = Pattern.compile("/post/(\\d+)")
    }
}