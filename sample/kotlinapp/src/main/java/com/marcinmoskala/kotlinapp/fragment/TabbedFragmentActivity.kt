package com.marcinmoskala.kotlinapp.fragment

import activitystarter.ActivityStarter
import activitystarter.MakeActivityStarter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.marcinmoskala.kotlinapp.R

@MakeActivityStarter
class TabbedFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed_fragment)
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager.adapter = mSectionsPagerAdapter
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = TabbedPlaceholderFragmentStarter.newInstance(position + 1)

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? = when (position) {
            0 -> "SECTION 1"
            1 -> "SECTION 2"
            2 -> "SECTION 3"
            else -> null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ActivityStarter.save(this)
    }
}
