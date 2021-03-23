package com.example.mad_assignment.Customer.ChangeLanguage.Helper

import android.content.Context
import android.content.res.Configuration
import com.example.mad_assignment.Customer.ChangeLanguage.ChangeLanguageMainApplication
import java.util.*


object LocalHelper {

    fun setLocale(mContext: Context):Context {
        return if (ChangeLanguageMainApplication.instance!!.getLanguagePref() != null)
            updateResources(mContext, ChangeLanguageMainApplication.instance!!.getLanguagePref()!!)
        else
            mContext
    }

    fun setNewLocale(mContext: Context, language: String): Context {
        ChangeLanguageMainApplication.instance!!.setLanguagePref(language)
        return updateResources(mContext,language)
    }

    private fun updateResources(context: Context, language: String): Context {
        var localContext: Context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config =
            Configuration(res.configuration)
        config.setLocale(locale)
        localContext = context.createConfigurationContext(config)
        return localContext
    }
}