package com.example.test2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//only when opening dialog need to send acticity,childFragmentManager

class MyGroceryList(
    groceryValues: ArrayList<String>?,
    context: Context?,
) : RecyclerView.Adapter<MyGroceryList.ViewHolder>() {

    private var groceryValues : ArrayList<String>? = groceryValues
    private var context : Context = context!!

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyGroceryList.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grocery_list, parent, false)



        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item: String = groceryValues?.get(position)!! // each item postion

        holder.mItem = item

        holder.list_item.setText(item)
    }

    override fun getItemCount(): Int {
       return groceryValues?.size!!
    }


    fun setGroceryValues(mValues: ArrayList<String>) {

        this.groceryValues = mValues

        notifyDataSetChanged() // notifying android that we changed the list,refresh the list that was empty at first.
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var list_item: TextView = view.findViewById(R.id.grocery_item)
        lateinit var mItem: String


        override fun toString(): String {
            return super.toString()
        }
    }


}