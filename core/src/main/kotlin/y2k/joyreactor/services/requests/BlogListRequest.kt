package y2k.joyreactor.services.requests

import org.jsoup.nodes.Element
import rx.Observable
import rx.schedulers.Schedulers
import y2k.joyreactor.Blog
import y2k.joyreactor.Image
import y2k.joyreactor.http.HttpClient
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogListRequest {

    fun requestAsync(url : String): Observable<List<Blog>> {
        return Observable
                .fromCallable {
                    val doc = HttpClient.instance.getDocument(url)

                    val blogs = ArrayList<Blog>()
                    for (e in doc.select("div.blog_list_item"))
                        blogs.add(newBlog(e))

                    blogs.toList();
                }
                .subscribeOn(Schedulers.io())
    }


    internal class ImageThumbnailParser(private val element: Element) {

        fun load(): Image? {
            val imgList = element.select("div.blog_list_avatar img")
            for (img in imgList) {
                if (img != null) {
                    return Image(
                            img.attr("src").replace("(/post/).+(-\\d+\\.)".toRegex(), "$1$2"), 0, 0)
                }
            }

            return null;
        }
    }

    internal class BlogParse(private val element: Element) {

        val title: String
            get() {

                return ""
            }

        val rating: String
            get() {
                return ""
            }

        val subscriberCount: String
            get() {
                return ""
            }

        companion object {

            private val COMMENT_COUNT_REGEX = Pattern.compile("\\d+")
            private val RATING_REGEX = Pattern.compile("[\\d\\.]+")
        }
    }

    companion object {

        internal fun newBlog(element: Element): Blog {
            var image = ImageThumbnailParser(element).load()

            val parser = BlogParse(element)

            return Blog(element.text(),
                    image,
                    parser.rating,
                    parser.subscriberCount
            )
        }

        private fun extractNumberFromEnd(text: String): String {
            val m = Pattern.compile("\\d+$").matcher(text)
            if (!m.find()) throw IllegalStateException()
            return m.group()
        }

        private val BLOG_DATA = Pattern.compile("<small>(\\S+)</small>")
    }
}