package y2k.joyreactor

import joy.reactor.data.repository.DataContext
import joy.reactor.data.repository.MemoryBuffer
import joy.reactor.data.requests.*
import joy.reactor.data.requests.factory.CreateCommentRequestFactory
import joy.reactor.data.requests.factory.LoginRequestFactory
import joy.reactor.data.requests.factory.OriginalImageRequestFactory
import joy.reactor.data.requests.factory.ProfileRequestFactory
import joy.reactor.domain.services.*
import joy.reactor.domain.services.synchronizers.MyTagFetcher
import joy.reactor.domain.services.synchronizers.PostMerger
import joy.reactor.domain.services.synchronizers.PrivateMessageFetcher
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.presenters.*
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by y2k on 07/12/15.
 */
object ServiceInjector {

    private val map = HashMap <KClass<*>, () -> Any>()

    init {
        add(MessageListRequest::class) { MessageListRequest() }
        add(PostMerger::class) { PostMerger(inject(DataContext.Factory::class)) }
        add(MemoryBuffer::class) { MemoryBuffer }
        add(MyTagFetcher::class) { MyTagFetcher(inject(DataContext.Factory::class)) }
        add(PrivateMessageFetcher::class) { PrivateMessageFetcher(inject(MessageListRequest::class), inject(DataContext.Factory::class)) }
        add(PostService::class) {
            PostService(
                    inject(OriginalImageRequestFactory::class),
                    inject(PostRequest::class),
                    inject(MemoryBuffer::class),
                    inject(DataContext.Factory::class))
        }
        add(PostListService::class) {
            PostListService(
                    inject(DataContext.Factory::class),
                    inject(PostsListRequest::class),
                    inject(LikeDislikeRequest::class),
                    inject(FavoriteRequest::class),
                    inject(PostMerger::class))
        }
        add(TagListService::class) {
            TagListService(
                    inject(DataContext.Factory::class),
                    inject(UserNameRequest::class),
                    inject(MyTagFetcher::class))
        }
        add(BlogService::class) {
            BlogService(inject(BlogListRequest::class))
        }
        add(ProfileService::class) {
            ProfileService(inject(ProfileRequestFactory::class), inject(LoginRequestFactory::class))
        }
        add(UserMessagesService::class) {
            UserMessagesService(inject(PrivateMessageFetcher::class), inject(DataContext.Factory::class))
        }
        add(CommentService::class) {
            CommentService(
                    inject(CreateCommentRequestFactory::class),
                    inject(PostRequest::class),
                    inject(MemoryBuffer::class))
        }
    }

    // ==========================================
    // Presenters
    // ==========================================

    fun inject(view: PostListPresenter.View): PostListPresenter {
        return PostListPresenter(view, inject(PostListService::class), inject(ProfileService::class), inject(PostMerger::class))
    }

    fun inject(view: PostPresenter.View): PostPresenter {
        return PostPresenter(view, inject(PostService::class), inject(ProfileService::class), Navigation.instance)
    }

    fun inject(view: BlogListPresenter.View): BlogListPresenter {
        return BlogListPresenter(view, inject(BlogListRequest::class), Navigation.instance)
    }

    fun inject(view: TagListPresenter.View): TagListPresenter {
        return TagListPresenter(view, inject(TagListService::class))
    }

    fun inject(view: ProfilePresenter.View): ProfilePresenter {
        return ProfilePresenter(view, inject(ProfileService::class), Navigation.instance)
    }

    fun inject(view: CreateCommentPresenter.View): CreateCommentPresenter {
        return CreateCommentPresenter(view, inject(ProfileService::class), inject(CommentService::class))
    }

    fun inject(view: LoginPresenter.View): LoginPresenter {
        return LoginPresenter(view, inject(ProfileService::class))
    }

    fun inject(view: AddTagPresenter.View): AddTagPresenter {
        return AddTagPresenter(view, inject(TagListService::class))
    }

    fun inject(view: MessagesPresenter.View): MessagesPresenter {
        return MessagesPresenter(view, inject(UserMessagesService::class))
    }

    fun inject(view: MessageThreadsPresenter.View): MessageThreadsPresenter {
        return MessageThreadsPresenter(view, inject(UserMessagesService::class), Navigation.instance)
    }

    fun inject(view: ImagePresenter.View): ImagePresenter {
        return ImagePresenter(view, inject(PostService::class))
    }

    fun inject(view: VideoPresenter.View): VideoPresenter {
        return VideoPresenter(view, inject(PostService::class))
    }

    // ==========================================
    // Private methods
    // ==========================================

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> inject(type: KClass<T>): T {
        return map[type]?.let { it() as T } ?: type.java.newInstance()
    }

    private fun <T : Any> add(type: KClass<T>, factory: () -> T) {
        map[type] = factory
    }
}