package joy.reactor.data.requests

import joy.reactor.data.enteties.Message
import joy.reactor.data.http.HttpClient
import joy.reactor.data.requests.const.URLConst
import java.util.*

/**
 * Created by y2k on 11/17/15.
 */
class MessageListRequest() {

    fun getMessages(page: String?): Pair<List<Message>, String?> {
        val messages = ArrayList<Message>()

        val url = page ?: URLConst.MESSAGE_URL
        val document = HttpClient.instance.getDocument(url)

        //TODO FIX IMAGES
        for (s in document.select("div.messages_wr > div.article")) {
            val username = s.select("div.mess_from > a").text()
            messages.add(
                    Message(
                            s.select("div.mess_text").text(),
                            Date(1000 * java.lang.Long.parseLong(s.select("span[data-time]").attr("data-time"))),
                            s.select("div.mess_reply").isEmpty(),
                            username,
                            ""))
        }

        val nextNode = document.select("a.next").first()
        val nextPage = nextNode?.absUrl("href")

        return messages to nextPage
    }
}