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
    companion object {

        internal fun newBlog(element: Element): Blog {
            var image = ImageThumbnailParser(element).load()

            val links = element.select("a[href]")

            val url = links[0].attr("href").replace("/tag/", "")
            val title = links[0].attr("title")

            val data = element.select("small")

            val rating = data[0].text()
            val subscriberCount = data[1].text()

            return Blog(title,
                    image,
                    rating,
                    subscriberCount,
                    url
            )
        }
    }
}