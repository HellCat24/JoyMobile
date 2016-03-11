package joy.reactor.data.requests.factory

import joy.reactor.data.common.ioObservable
import joy.reactor.data.enteties.Image
import joy.reactor.data.enteties.Profile
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.UserNameRequest
import org.jsoup.nodes.Document
import rx.Observable
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