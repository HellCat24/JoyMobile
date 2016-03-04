package y2k.joyreactor.requests.factory

import org.jsoup.nodes.Document
import rx.Observable
import y2k.joyreactor.common.ioObservable
import y2k.joyreactor.enteties.Image
import y2k.joyreactor.enteties.Profile
import y2k.joyreactor.http.HttpClient
import y2k.joyreactor.requests.UserNameRequest
import java.util.regex.Pattern

/**
 * Created by y2k on 19/10/15.
 */
class ProfileRequestFactory {

    fun request(): Observable<Profile> {
        return UserNameRequest()
            .request()
            .flatMap { username ->
                ioObservable {
                    val page = HttpClient.instance.getDocument(getUrl(username))
                    ProfileParser(page).parse()
                }
            }
    }

    private fun getUrl(username: String?): String {
        if (username == null) throw RuntimeException()
        return "http://joyreactor.cc/user/" + username
    }

    private class ProfileParser(private val document: Document) {

        fun parse(): Profile {
            return Profile(
                    document.select("div.sidebarContent > div.user > span").text(),
                    Image(document.select("div.sidebarContent > div.user > img").attr("src"), 0, 0),
                    document.select("#rating-text > b").text().toFloat(),
                    document.select(".star-row-0 > .star-0").size,
                    getProgressToNewStar())
        }

        private fun getProgressToNewStar(): Float {
            val style = document.select("div.stars div.poll_res_bg_active").first().attr("style")
            val m = Pattern.compile("width:(\\d+)%;").matcher(style)
            if (!m.find()) throw IllegalStateException()
            return java.lang.Float.parseFloat(m.group(1))
        }
    }
}