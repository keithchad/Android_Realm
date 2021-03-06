package com.chad.gads2022_java_kotlin.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.gads2022_java_kotlin.R
import com.chad.gads2022_java_kotlin.adapters.DisplayAdapter.MyViewHolder
import com.chad.gads2022_java_kotlin.extensions.toast
import com.chad.gads2022_java_kotlin.models.Repository
import io.realm.Realm
import kotlinx.android.synthetic.main.header.view.txvName
import kotlinx.android.synthetic.main.list_item.view.*

class DisplayAdapter(val context: Context, private var list: List<Repository>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = list[position]
        holder.setData(current)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun swap(data: List<Repository>) {
        if (data.isEmpty()) context.toast("No Items Found")
        list = data
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var current: Repository? = null
        fun setData(current: Repository) {
            itemView.txvName.text = current.name
            itemView.txvLanguage.text = current.language
            itemView.txvForks.text = current.forks.toString()
            itemView.txvWatchers.text = current.watchers.toString()
            itemView.txvStars.text = current.stars.toString()
            this.current = current
        }

        private fun bookmarkRepository(current: Repository) {
            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransactionAsync(
                { realm -> realm.copyToRealmOrUpdate(current) },
                { context.toast("Bookmarked Successfully") })
            { context.toast("Error Occurred") }

        }

        init {
            itemView.img_bookmark.setOnClickListener { current?.let { it1 -> bookmarkRepository(it1) } }
            itemView.setOnClickListener {
                val url = current!!.htmlUrl
                val webpage = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private val TAG = DisplayAdapter::class.java.simpleName
    }
}