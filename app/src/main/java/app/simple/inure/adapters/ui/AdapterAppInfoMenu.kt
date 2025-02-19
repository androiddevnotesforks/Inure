package app.simple.inure.adapters.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.simple.inure.R
import app.simple.inure.decorations.ripple.DynamicRippleLinearLayoutWithFactor
import app.simple.inure.decorations.views.TypeFaceTextView

class AdapterAppInfoMenu(private val list: List<Pair<Int, String>>) : RecyclerView.Adapter<AdapterAppInfoMenu.Holder>() {

    private lateinit var appInfoMenuCallbacks: AppInfoMenuCallbacks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_app_info_menu, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.icon.transitionName = list[position].second
        holder.icon.setImageResource(list[position].first)
        holder.text.text = list[position].second
        holder.container.setOnClickListener {
            appInfoMenuCallbacks.onAppInfoMenuClicked(list[position].second, holder.icon)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.adapter_app_info_menu_icon)
        val text: TypeFaceTextView = itemView.findViewById(R.id.adapter_app_info_menu_text)
        val container: DynamicRippleLinearLayoutWithFactor = itemView.findViewById(R.id.adapter_app_info_menu_container)

        init {
            text.isSelected = true
        }
    }

    fun setOnAppInfoMenuCallback(appInfoMenuCallbacks: AppInfoMenuCallbacks) {
        this.appInfoMenuCallbacks = appInfoMenuCallbacks
    }

    interface AppInfoMenuCallbacks {
        fun onAppInfoMenuClicked(source: String, icon: ImageView)
    }
}