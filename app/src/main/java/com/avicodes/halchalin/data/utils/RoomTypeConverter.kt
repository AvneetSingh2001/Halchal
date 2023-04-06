package com.avicodes.halchalin.data.utils

import androidx.room.TypeConverter
import com.avicodes.halchalin.data.models.ResUrls
import com.google.gson.Gson

class RoomTypeConverters{
    @TypeConverter
    fun convertInvoiceListToJSONString(resUrls: ResUrls): String = Gson().toJson(resUrls)
    @TypeConverter
    fun convertJSONStringToInvoiceList(jsonString: String): ResUrls = Gson().fromJson(jsonString,ResUrls::class.java)

}