package com.example.test2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {

    private var groceryList: ArrayList<String>? = null
    private var groceryListRecyclerViewAdapter: MyGroceryList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        groceryListRecyclerViewAdapter = MyGroceryList(groceryList,applicationContext)
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = groceryListRecyclerViewAdapter
        initList()

    }

    private fun initList() {
        val items = arrayOf("Milk","Tomtato","Orange","Carrot","Baking-Soda","Bread","Potatos","Banan","WaterMelon","Strwberry")
        groceryList = ArrayList()
        for(item in items)
            groceryList?.add(item)


        groceryListRecyclerViewAdapter!!.setGroceryValues(groceryList!!)
    }

}