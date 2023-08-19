package com.example.hanadroid.model

import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.google.gson.annotations.SerializedName


/**
 * {
"domains": [
"marywood.edu"
],
"web_pages": [
"http://www.marywood.edu"
],
"state-province": null,
"name": "Marywood University",
"country": "United States",
"alpha_two_code": "US"
}
 */
data class University(
    @SerializedName("domains")
    val domains: List<String> = emptyList(),
    @SerializedName("web_pages")
    val webPages: List<String> = emptyList(),
    @SerializedName("state-province")
    val state: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("alpha_two_code")
    val alphaCode: String = ""
)

fun websiteUrl(university: University): Spanned {
    return HtmlCompat.fromHtml(university.webPages[0], HtmlCompat.FROM_HTML_MODE_LEGACY)
}

