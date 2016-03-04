package y2k.joyreactor.enteties

/**
 * Created by y2k on 07/12/15.
 */
interface CommentGroup {

    operator fun get(position: Int): Comment

    fun size(): Int

    fun isChild(position: Int): Boolean

    fun getId(position: Int): Long

    fun add(comment: Comment)

    class OneLevel(private val parent: Comment?, private val children: MutableList<Comment>) : CommentGroup {

        constructor(children: MutableList<Comment>) : this(null, children) {
        }

        override operator fun get(position: Int): Comment {
            if (parent == null) return children[position]
            if (position == 0) return parent
            return children[position - 1]
        }

        override fun size(): Int {
            return children.size + if (parent == null) 0 else 1
        }

        override fun isChild(position: Int): Boolean {
            // is need divider
            return parent != null && position > 0
        }

        override fun getId(position: Int): Long {
            if (parent == null) return children[position].id
            if (position == 0) return parent.parentId
            return children[position - 1].id
        }

        override fun add(comment: Comment) {
            children.add(comment)
        }
    }

    class TwoLevel(private val comments: MutableList<Comment>) : CommentGroup {

        override fun add(comment: Comment) {
            for(comment in comments){
                if(comment.id == comment.parentId){
                    //comment.
                }
            }
        }

        override fun isChild(position: Int): Boolean {
            return comments[position].parentId != 0L
        }

        override operator fun get(position: Int): Comment {
            return comments[position]
        }

        override fun size(): Int {
            return comments.size
        }

        override fun getId(position: Int): Long {
            return comments[position].id
        }
    }
}