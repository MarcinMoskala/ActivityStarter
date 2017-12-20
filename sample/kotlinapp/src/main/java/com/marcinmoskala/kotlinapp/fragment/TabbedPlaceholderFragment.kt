package com.marcinmoskala.kotlinapp.fragment

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.marcinmoskala.activitystarter.argExtra
import com.marcinmoskala.kotlinapp.R

class TabbedPlaceholderFragment : Fragment() {

    @get:Arg var sectionNumber: Int by argExtra()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_tabbed, container, false)
        ActivityStarter.fill(this)
        val textView = rootView.findViewById(R.id.section_label) as TextView
        textView.setOnClickListener {
            sectionNumber++
            textView.text = "$sectionNumber"
        }
        textView.text = "$sectionNumber"
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        ActivityStarter.save(this, outState)
    }
}