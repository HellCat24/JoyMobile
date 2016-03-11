package joy.reactor.domain.services

import joy.reactor.data.common.concatAndRepeat
import joy.reactor.data.enteties.Message
import joy.reactor.data.repository.DataContext
import joy.reactor.domain.services.synchronizers.PrivateMessageFetcher
import rx.Observable

/**
 * Created by y2k on 12/8/15.
 */
class UserMessagesService(
        private val fetcher: PrivateMessageFetcher,
        private val entities: DataContext.Factory) {

    fun getThreads(): Observable<List<Message>> {
        return entities
                .applyUse {
                    Messages
                            .groupBy { it.userName }
                            .map { it.value.maxBy { it.date }!! }
                            .sortedByDescending { it.date }
                }
                .concatAndRepeat(fetcher.execute())
    }

    fun getMessages(username: String): Observable<List<Message>> {
        return entities.applyUse {
            Messages
                    .filter { it.userName == username }
                    .sortedByDescending { it.date }
        }
    }
}