package y2k.joyreactor.ui.profile.message

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.ocpsoft.prettytime.PrettyTime
import y2k.joyreactor.Message
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.BaseFragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.presenters.MessageThreadsPresenter
import y2k.joyreactor.ui.base.BaseFragmentActivity
import y2k.joyreactor.ui.profile.tags.TagsActivity
import y2k.joyreactor.view.WebImageView

/**
 * Created by y2k on 11/13/15.
 */
class DialogsFragment : BaseFragment() {

    private var presenter: MessageThreadsPresenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_threads, container, false)

        val list = view.findViewById(R.id.list) as RecyclerView
        list.layoutManager = LinearLayoutManager(context)
        val adapter = ThreadAdapter()
        list.adapter = adapter

        val progress = view.findViewById(R.id.progress)

        presenter = ServiceLocator.resolve(
            object : MessageThreadsPresenter.View {

                override fun setIsBusy(isBusy: Boolean) {
                    progress.visibility = if (isBusy) View.VISIBLE else View.GONE
                }

                override fun reloadData(threads: List<Message>) {
                    adapter.updateData(threads)
                }
            })

        return view
    }

    internal inner class ThreadAdapter : RecyclerView.Adapter<ThreadAdapter.ViewHolder>() {

        private var threads: List<Message> = emptyList()
        private val prettyTime = PrettyTime()

        fun updateData(threads: List<Message>) {
            this.threads = threads
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_message_thread, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val t = threads[position]
            holder.userImage.setImage(t.getUserImageObject().toImage())
            holder.userName.text = t.userName
            holder.lastMessage.text = t.text
            holder.time.text = prettyTime.format(t.date)
        }

        override fun getItemCount(): Int {
            return threads.size
        }

        internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var userImage: WebImageView
            var userName: TextView
            var lastMessage: TextView
            var time: TextView

            init {
                userImage = view.findViewById(R.id.userImage) as WebImageView
                userName = view.findViewById(R.id.userName) as TextView
                lastMessage = view.findViewById(R.id.lastMessage) as TextView
                time = view.findViewById(R.id.time) as TextView

                view.findViewById(R.id.button).setOnClickListener {
                   presenter!!.openMessages(threads[adapterPosition])
                }
            }
        }
    }
}