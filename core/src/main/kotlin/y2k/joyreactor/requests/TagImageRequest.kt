package y2k.joyreactor.requests

import y2k.joyreactor.common.PersistentMap
import y2k.joyreactor.http.HttpClient
import y2k.joyreactor.repository.IconStorage
import java.net.URLEncoder

/**
 * Created by y2k on 10/18/15.
 */
class TagImageRequest {

    private val cache = PersistentMap("tag-images.1.dat")

    fun request(tag: String): String {
        TagImageRequest.Companion.sStorage = IconStorage.get(TagImageRequest.Companion.sStorage, "tag.names", "tag.icons")

        val clearTag = tag.toLowerCase()
        var imageId = getImageId(clearTag)

        if (imageId == null) imageId = cache.get(clearTag)
        if (imageId == null) {
            imageId = getFromWeb(clearTag)
            cache.put(clearTag, imageId).flush()
        }
        return imageId
    }

    private fun getImageId(clearTag: String): String? {
        val id = TagImageRequest.Companion.sStorage!!.getImageId(clearTag)
        return if (id == null) null else "http://img0.reactor.cc/pics/avatar/tag/" + id
    }

    private fun getFromWeb(tag: String): String {
        val doc = HttpClient.instance.getDocument("http://joyreactor.cc/tag/" + URLEncoder.encode(tag))
        val result = doc.select("img.blog_avatar").first().attr("src")
        println("Not found in cache | $tag | $result")
        return result
    }

    companion object {

        private var sStorage: IconStorage? = null
    }
}