package app.simple.inure.viewmodels.panels

import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.Typeface
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.simple.inure.R
import app.simple.inure.constants.Quotes
import app.simple.inure.util.ColorUtils.resolveAttrColor
import app.simple.inure.util.ColorUtils.toHexColor
import app.simple.inure.util.TTFHelper
import app.simple.inure.util.TextViewUtils.toHtmlSpanned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FontData(application: Application, val path: String, val applicationInfo: ApplicationInfo, val color: Int) : AndroidViewModel(application) {

    private val quote: MutableLiveData<Spanned> by lazy {
        MutableLiveData<Spanned>().also {
            setQuote()
        }
    }

    private val typeface: MutableLiveData<Typeface> by lazy {
        MutableLiveData<Typeface>().also {
            setFont()
        }
    }

    fun getQuote(): LiveData<Spanned> {
        return quote
    }

    fun getTypeFace(): LiveData<Typeface> {
        return typeface
    }

    private fun setQuote() {
        viewModelScope.launch(Dispatchers.Default) {
            val spanned = Quotes.quotes.random().replace(
                "%%%", color.toHexColor())
                    .toHtmlSpanned()

            quote.postValue(spanned)
        }
    }

    private fun setFont() {
        viewModelScope.launch(Dispatchers.IO) {
            val typeFace = TTFHelper.getTTFFile(
                path,
                applicationInfo, getApplication())

            this@FontData.typeface.postValue(typeFace)
        }
    }
}