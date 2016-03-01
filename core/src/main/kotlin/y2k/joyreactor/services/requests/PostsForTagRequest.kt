package y2k.joyreactor.services.requests

import org.jsoup.nodes.Element
import rx.Observable
import rx.schedulers.Schedulers
import y2k.joyreactor.Image
import y2k.joyreactor.Post
import y2k.joyreactor.Tag
import y2k.joyreactor.http.HttpClient
import java.util.*
import java.util.regex.Pattern
import kotlin.properties.Delegates

/**
 * Created by y2k on 9/26/15.
 */
class PostsForTagRequest {

    fun requestAsync(tagId: Tag, pageId: String? = null, type: String): Observable<Data> {
        return Observable
                .fromCallable {
                    val url = UrlBuilder().build(tagId, pageId, type)
                    val doc = HttpClient.instance.getDocument(url)

                    val posts = ArrayList<Post>()
                    for (e in doc.select("div.postContainer"))
                        posts.add(newPost(e))

                    val next = doc.select("a.next").first()
                    Data(posts, next?.let { extractNumberFromEnd(next.attr("href")) })
                }
                .subscribeOn(Schedulers.io())
    }

    internal class PostParser(private val element: Element) {

        val commentCount: Int
            get() {
                val e = element.select("a.commentnum").first()
                val m = COMMENT_COUNT_REGEX.matcher(e.text())
                if (!m.find()) throw IllegalStateException()
                return Integer.parseInt(m.group())
            }

        val rating: Float
            get() {
                val e = element.select("span.post_rating > span").first()
                val m = RATING_REGEX.matcher(e.text())
                return if (m.find()) java.lang.Float.parseFloat(m.group()) else 0f
            }

        companion object {

            private val COMMENT_COUNT_REGEX = Pattern.compile("\\d+")
            private val RATING_REGEX = Pattern.compile("[\\d\\.]+")
        }
    }

    internal class ImageThumbnailParser(private val element: Element) {

        var imagesList by Delegates.notNull<MutableList<Image>>()

        fun getExpandedImages(): MutableList<Image> {
            return imagesList;
        }

        fun load(): Image? {
            imagesList = ArrayList<Image>();
            val imgList = element.select("div.post_content img")
            for (img in imgList) {
                if (img != null && img.hasAttr("width")) {
                    imagesList.add(Image(
                            if (hasFull(img))
                                img.parent().attr("href").replace("(/full/).+(-\\d+\\.)".toRegex(), "$1$2")
                            else
                                img.attr("src").replace("(/post/).+(-\\d+\\.)".toRegex(), "$1$2"),
                            Integer.parseInt(img.attr("width")),
                            Integer.parseInt(img.attr("height"))))
                }
            }

            if (imagesList.size > 0) {
                return imagesList[0];
            } else {
                return null;
            }
        }

        private fun hasFull(img: Element): Boolean {
            return "a" == img.parent().tagName()
        }
    }

    internal class YoutubeThumbnailParser(private val element: Element) {

        fun load(): Image? {
            val iframe = element.select("iframe.youtube-player").first() ?: return null
            val m = SRC_PATTERN.matcher(iframe.attr("src"))
            if (!m.find()) throw IllegalStateException(iframe.attr("src"))
            return Image(
                    "http://img.youtube.com/vi/" + m.group(1) + "/0.jpg",
                    Integer.parseInt(iframe.attr("width")),
                    Integer.parseInt(iframe.attr("height")))
        }

        companion object {

            private val SRC_PATTERN = Pattern.compile("/embed/([^\\?]+)")
        }
    }

    internal class CoubThumbnailParser(private val element: Element) {

        fun load(): Image? {
            val video = element.select("iframe").attr("src") ?: return null
            try {
                return Image(video, 0, 0)
            } catch (e: Exception) {
                println("ELEMENT | " + video)
                throw e
            }
        }
    }

    internal class GifThumbnailParser(private val element: Element) {

        fun load(): Image? {
            val video = element.select("video[poster]").first() ?: return null
            try {
                return Image(
                        element.select("span.video_gif_holder > a").first().attr("href").replace("(/post/).+(-)".toRegex(), "$1$2"),
                        Integer.parseInt(video.attr("width")),
                        Integer.parseInt(video.attr("height")))
            } catch (e: Exception) {
                println("ELEMENT | " + video)
                throw e
            }
        }
    }

    class Data(val posts: List<Post>, val nextPage: String?)

    companion object {

        internal fun newPost(element: Element): Post {
            val imageThumbnailParser = ImageThumbnailParser(element)
            var image = imageThumbnailParser.load()
            if (image == null) image = YoutubeThumbnailParser(element).load()
            if (image == null) image = GifThumbnailParser(element).load()
            if (image == null) image = CoubThumbnailParser(element).load()

            val parser = PostParser(element)

            return Post(
                    element.select("div.post_content").text(),
                    image,
                    imageThumbnailParser.getExpandedImages(),
                    element.select("div.uhead_nick > img").attr("src"),
                    element.select("div.uhead_nick > a").text(),
                    extractNumberFromEnd(element.id()),
                    parser.commentCount,
                    parser.rating)
        }

        private fun extractNumberFromEnd(text: String): String {
            val m = Pattern.compile("\\d+$").matcher(text)
            if (!m.find()) throw IllegalStateException()
            return m.group()
        }
    }
}