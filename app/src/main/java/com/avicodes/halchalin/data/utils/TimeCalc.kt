package com.avicodes.halchalin.data.utils

import com.avicodes.halchalin.R
import com.blankj.utilcode.util.StringUtils.getString

class TimeCalc {

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun getTimeAgo(time: Long): String? {
            val now: Long = System.currentTimeMillis()
            if (time > now || time <= 0) {
                return null
            }
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                getString(R.string.just_now_text)
            } else if (diff < 2 * MINUTE_MILLIS) {
                getString(R.string.minute_ago_text)
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " " + getString(R.string.minutes_ago_text)

            } else if (diff < 90 * MINUTE_MILLIS) {
                getString(R.string.hour_ago_text)

            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " " + getString(R.string.hours_ago_text)

            } else if (diff < 48 * HOUR_MILLIS) {
                getString(R.string.yesterday_text)
            } else {
                (diff / DAY_MILLIS).toString() + " " + getString(R.string.day_ago_text)
            }
        }


    }
}