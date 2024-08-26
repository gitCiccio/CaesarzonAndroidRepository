package com.example.caesarzonapplication.model.utils

import androidx.room.TypeConverter
import com.example.caesarzonapplication.model.entities.userEntity.CityData
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromCityData(cityData: CityData?): String? {
        return cityData?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toCityData(cityDataString: String?): CityData? {
        return cityDataString?.let { Gson().fromJson(it, CityData::class.java) }
    }
}
