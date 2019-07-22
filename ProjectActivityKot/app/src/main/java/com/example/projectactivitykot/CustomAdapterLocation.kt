package com.example.projectactivitykot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapterLocation(context: Context, textViewResourceId: Int, objects: List<Location>) : ArrayAdapter<Location>(context, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getViewOptimize(position, convertView, parent)
    }

    fun getViewOptimize(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.row_layout, null)
            viewHolder = ViewHolder()
            viewHolder.name = convertView!!.findViewById<View>(R.id.textViewName) as TextView

            convertView.tag = viewHolder

        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val location = getItem(position)
        viewHolder.name!!.setText(location!!.name)

        return convertView
    }


    private inner class ViewHolder {

        var name: TextView? = null
    }

}