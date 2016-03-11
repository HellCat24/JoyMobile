package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Tag
import joy.reactor.domain.services.TagListService

/**
 * Created by y2k on 9/26/15.
 */
class TagListPresenter(
        private val view: TagListPresenter.View,
        private val service: TagListService) {

    init {
        service
                .getMyTags()
                .subscribeOnMain { view.reloadData(it) }
    }

    fun selectTag(tag: Tag) {
        //TODO
    }

    interface View {

        fun reloadData(tags: List<Tag>)
    }
}