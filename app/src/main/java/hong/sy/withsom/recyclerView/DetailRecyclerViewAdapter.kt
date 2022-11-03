package hong.sy.withsom.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hong.sy.withsom.data.DetailData
import hong.sy.withsom.R

class DetailRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>() {
    var datas = mutableListOf<DetailData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.class_recycler_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_detail_title)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_detail_content)

        fun bind(item : DetailData) {
            tvTitle.text = item.title
            tvContent.text = item.content
        }
    }
}