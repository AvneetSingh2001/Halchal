package com.avicodes.halchalin.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

fun FragmentActivity.openDialog(dialog: DialogFragment, tag: String) {
    if (!this.isFinishing && null == supportFragmentManager.findFragmentByTag(tag)) {
        try {
            val ft: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            ft.add(dialog, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun FragmentActivity.dismissDialog(tag: String) {
    try {
        if (null != supportFragmentManager.findFragmentByTag(tag))
            (supportFragmentManager.findFragmentByTag(tag) as DialogFragment).dismissAllowingStateLoss()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}