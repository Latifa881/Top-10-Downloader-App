package com.example.top10downloaderapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialogue_view.view.*
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.item_row.view.tvTitle


class RecyclerViewAdapter ( val details: ArrayList<Details>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = details[position]

        holder.itemView.apply {
            tvTitle.text = data.title
            tvTitle.setOnClickListener{
                val builder = AlertDialog.Builder(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_view, null)
                builder.setView(dialogView)
                dialogView.tvTitle.text=data.title

                dialogView.tvSummary.text= data.summary
                dialogView.tvSummary.visibility= View.GONE
                dialogView.scrollable.visibility= View.GONE
                dialogView.ivSummary.setOnClickListener {
                    if(dialogView.tvSummary.visibility== View.VISIBLE){
                        dialogView.tvSummary.visibility= View.GONE
                        dialogView.scrollable.visibility= View.GONE
                        Log.d("MY TAG",data.summary.toString())
                    }else{
                        dialogView.tvSummary.visibility= View.VISIBLE
                        dialogView.scrollable.visibility= View.VISIBLE
                    }
                }
                try {
                    dialogView.ivLink.setOnClickListener {
                        var  uri = Uri.parse(data.ID)
                        val intent =  Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent)
                    }
                }catch (e:Exception){

                }


                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
                dialogView.ivClose.setOnClickListener{
                    alertDialog.dismiss()
                }
            }
        }
    }

    override fun getItemCount()=details.size
}