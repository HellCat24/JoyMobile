package y2k.joyreactor.ui.utils

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

object PostUtils {
    fun loadCoub(v: WebView, url: String?) {
        val width = v.measuredWidth;
        val height = v.measuredWidth * 9 / 16
        val pageStart = "<!DOCTYPE HTML> \n" +
                "<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\" xmlns:og=\\\"http://opengraphprotocol.org/schema/\\\" xmlns:fb=\\\"http://www.facebook.com/2008/fbml\\\">\n" +
                "   <head></head>\n" +
                "   <body style=\\\"margin:0; padding:0;\\\">"
        val player = "<iframe src=\"$url\" allowfullscreen=\"true\" frameborder=\"0\" width=\"$width\" height=\"$height\"></iframe>"
        val pageEnd = "</body> </html>"
        val webViewSettings = v.settings
        webViewSettings.javaScriptCanOpenWindowsAutomatically = true
        webViewSettings.javaScriptEnabled = true
        webViewSettings.setSupportZoom(false)
        webViewSettings.pluginState = WebSettings.PluginState.ON
        v.isScrollbarFadingEnabled = false;
        v.loadData(pageStart + player + pageEnd, "text/html", "utf-8")
        v.isScrollContainer = false
        v.setWebViewClient(OnWebPageLoaded())
    }

    class OnWebPageLoaded : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.scrollTo(0, 0);
        }
    }
}