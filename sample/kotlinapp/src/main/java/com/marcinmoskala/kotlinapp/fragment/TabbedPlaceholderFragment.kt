package com.marcinmoskala.kotlinapp.fragment

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.marcinmoskala.activitystarter.argExtra
import com.marcinmoskala.kotlinapp.R

class TabbedPlaceholderFragment : Fragment() {

    @get:Arg var sectionNumber: Int by argExtra()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView = inflater!!.inflate(R.layout.fragment_tabbed, container, false)
        ActivityStarter.fill(this, savedInstanceState)
        val textView = rootView.findViewById<TextView>(R.id.section_label)
        textView.setOnClickListener {
            sectionNumber++
            textView.text = "$sectionNumber"
        }
        textView.text = "$sectionNumber"
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        ActivityStarter.save(this, outState)
    }
}
