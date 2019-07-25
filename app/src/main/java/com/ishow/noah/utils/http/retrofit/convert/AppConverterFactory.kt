package com.ishow.noah.utils.http.retrofit.convert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AppConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {


    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *> {

        val adapter = gson.getAdapter(TypeToken.get(type))
        return AppResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type,
                                      parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody> {

        val adapter = gson.getAdapter(TypeToken.get(type))
        return AppRequestBodyConverter(gson, adapter)
    }

    companion object {
        fun create(): AppConverterFactory {
            return create(Gson())
        }

        fun create(gson: Gson?): AppConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return AppConverterFactory(gson)
        }
    }
}