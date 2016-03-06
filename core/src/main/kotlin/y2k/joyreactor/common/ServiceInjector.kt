package y2k.joyreactor.common

import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.presenters.*
import y2k.joyreactor.repository.DataContext
import y2k.joyreactor.repository.MemoryBuffer
import y2k.joyreactor.requests.*
import y2k.joyreactor.requests.factory.CreateCommentRequestFactory
import y2k.joyreactor.requests.factory.LoginRequestFactory
import y2k.joyreactor.requests.factory.OriginalImageRequestFactory
import y2k.joyreactor.requests.factory.ProfileRequestFactory
import y2k.joyreactor.services.*
import y2k.joyreactor.services.synchronizers.MyTagFetcher
import y2k.joyreactor.services.synchronizers.PostMerger
import y2k.joyreactor.services.synchronizers.PrivateMessageFetcher
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by y2k on 07/12/15.
 */
object ServiceInjector {

    private val map = HashMap <KClass<*>, () -> Any>()

    init {
        add(MessageListRequest::class) { MessageListRequest(inject(UserImageRequest::class)) }
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

    fun inject(lifeCycleService: LifeCycleService, view: TagListPresenter.View): TagListPresenter {
        return TagListPresenter(view,
                inject(TagListService::class),
                inject(BroadcastService::class),
                lifeCycleService)
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

    fun inject(lifeCycleService: LifeCycleService, view: MessagesPresenter.View): MessagesPresenter {
        return MessagesPresenter(view, inject(UserMessagesService::class), lifeCycleService)
    }

    fun inject(view: MessageThreadsPresenter.View): MessageThreadsPresenter {
        return MessageThreadsPresenter(view, inject(BroadcastService::class), inject(UserMessagesService::class), Navigation.instance)
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