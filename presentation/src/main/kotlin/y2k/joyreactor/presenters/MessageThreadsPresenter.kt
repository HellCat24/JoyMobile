package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Message
import joy.reactor.domain.services.UserMessagesService
import y2k.joyreactor.platform.Navigation

/**
 * Created by y2k on 01/10/15.
 */
class MessageThreadsPresenter(
        private val view: MessageThreadsPresenter.View,
        private val service: UserMessagesService,
        private val navigation: Navigation) {

    init {
        view.setIsBusy(true)
        service.getThreads()
                .subscribeOnMain {
                    view.setIsBusy(false)
                    view.reloadData(it)
                }
    }

    fun selectThread(thread: Message) {
        //TODO
    }

    fun openMessages(dialog: Message) {
        navigation.openMessages(dialog)
    }

    interface View {

        fun setIsBusy(isBusy: Boolean)

        fun reloadData(threads: List<Message>)
    }
}