package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Message
import joy.reactor.data.requests.SendMessageRequest
import joy.reactor.domain.services.UserMessagesService

/**
 * Created by y2k on 01/10/15.
 */
class MessagesPresenter(
        private val view: MessagesPresenter.View,
        private val service: UserMessagesService) {

    private var currentUsername: String? = null

    init {
        reloadMessages();
    }

    fun reply(message: String) {
        view.setBusy(true)
        view.clearMessage()
        currentUsername?.let {
            SendMessageRequest(it)
                    .request(message)
                    .subscribeOnMain(
                            { reloadMessages() },
                            { view.setBusy(false) })
        }
    }

    private fun reloadMessages() {
        currentUsername?.let {
            view.setBusy(true)
            service
                    .getMessages(it)
                    .subscribeOnMain({
                        view.updateMessages(it)
                        view.setBusy(false)
                    }, {
                        view.setBusy(false)
                    })
        }
    }

    interface View {

        fun updateMessages(messages: List<Message>)

        fun setBusy(isBusy: Boolean)

        fun clearMessage()
    }
}