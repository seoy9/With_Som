package hong.sy.withsom.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hong.sy.withsom.NoticeDetailActivity
import hong.sy.withsom.R
import hong.sy.withsom.data.NoticeData
import java.io.Serializable

class NoticeRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<NoticeRecyclerViewAdapter.NoticeViewHolder>() {
    var notices = mutableListOf<NoticeData>()

    inner class NoticeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tv_title : TextView = itemView.findViewById(R.id.tv_title_notice)
        private val tv_date : TextView = itemView.findViewById(R.id.tv_date_notice)

        fun bind(notice: NoticeData) {
            tv_title.text = notice.title
            tv_date.text = notice.date

            itemView.setOnClickListener {
                val intent = Intent(context, NoticeDetailActivity::class.java)
                intent.putExtra("notice", notice as Serializable)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_recycler_item, parent, false)
        return NoticeViewHolder(view)
    }

    override fun getItemCount(): Int = notices.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(notices[notices.size - position - 1])
    }
}