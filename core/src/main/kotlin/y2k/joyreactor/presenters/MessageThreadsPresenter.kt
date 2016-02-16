package y2k.joyreactor.presenters

import y2k.joyreactor.Message
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.services.BroadcastService
import y2k.joyreactor.services.UserMessagesService

/**
 * Created by y2k on 01/10/15.
 */
class MessageThreadsPresenter(
        private val view: MessageThreadsPresenter.View,
        private val broadcastService: BroadcastService,
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
        broadcastService.broadcast(BroadcastService.ThreadSelectedMessage(thread))
    }

    fun openMessages(dialog: Message) {
        navigation.openMessages(dialog)
    }

    interface View {

        fun setIsBusy(isBusy: Boolean)

        fun reloadData(threads: List<Message>)
    }
}