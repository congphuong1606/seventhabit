package com.discovertodo.phone.android.ebook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.fragment.EbookFragment;
import com.discovertodo.phone.android.global.DialogLoading;
import com.discovertodo.phone.android.util.StoreData;

public class EpubWebViewClient extends WebViewClient {

	private Activity activity;
	private EpubWebView webView;
	public static float widthWeb, heightWeb;
	public static int totalWidth;
	private int pageCount = 0;
	public static int[] arrMenu;
	public static int[] arrListTagP, arrListHeightTagP;
	public static int totalHeight;
	private int topTable = 0;
	private int heightTable = 0;
	StoreData data;

	public EpubWebViewClient(Activity activity, EpubWebView webView) {
		this.activity = activity;
		this.webView = webView;
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		MyJavaScriptInterface javaInterface = new MyJavaScriptInterface();
		webView.addJavascriptInterface(javaInterface, "HTMLOUT");
		data = new StoreData(activity);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(final WebView view, String url) {
		widthWeb = view.getMeasuredWidth()
				/ activity.getResources().getDisplayMetrics().density;
		heightWeb = view.getMeasuredHeight()
				/ activity.getResources().getDisplayMetrics().density;

		String varMySheet = "var mySheet = document.styleSheets[0];";
		String addCSSRule = "function addCSSRule(selector, newRule) {"
				+ "if (mySheet.addRule) {"
				+ "mySheet.addRule(selector, newRule);"
				+ "} else {"
				+ "ruleIndex = mySheet.cssRules.length;"
				+ "mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);"
				+ "}" + "}";
		String breakPage = "addCSSRule('p.div-pagebreak', '-webkit-column-break-after: always;')";
		String insertRule2 = "addCSSRule('p', 'text-align: justify;border-style: solid; padding-top:0px; padding-left:20px;  padding-right:15px; ')";
		String insertRule3 = "addCSSRule('table, th, td', 'margin:5px 20px 5px 20px;')";
		String insertRule4 = "addCSSRule('p.dmManh', 'padding-top:10px; padding-bottom:10px;')";
		String setTextSizeRule = "addCSSRule('body', 'font-size: 120%;line-height:1;')";
		String setHighlightColorRule = "addCSSRule('highlight', 'background-color: rgba(0,0,0,0);')";

		EpubWebView.loadJavascript(view, "javascript:" + varMySheet);
		EpubWebView.loadJavascript(view, "javascript:" + addCSSRule);
		EpubWebView.loadJavascript(view, "javascript:" + breakPage);
		EpubWebView.loadJavascript(view, "javascript:" + insertRule2);
		EpubWebView.loadJavascript(view, "javascript:" + insertRule3);
		EpubWebView.loadJavascript(view, "javascript:" + insertRule4);
		EpubWebView.loadJavascript(view, "javascript:" + setTextSizeRule);
		EpubWebView.loadJavascript(view, "javascript:" + setHighlightColorRule);
		if (!EpubWebView.isScroll) {
			final String insertRule1 = "addCSSRule('html', 'padding: 0px; height: "
					+ heightWeb
					+ "px; -webkit-column-gap: 0px; -webkit-column-width: "
					+ widthWeb + "px;')";
			view.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					EpubWebView.loadJavascript(view, "javascript:" + insertRule1);
				}
			}, 300);
		}

		if (EpubWebView.isScroll) {
			try {
				//int height = (int) (heightWeb - EpubWebViewClient.arrListTagP[1]);
				String reSizetab = "var image = document.getElementsByTagName('p')[0];"
						+ "image.setAttribute('style', 'margin-bottom: "
						+ 40 + "px;');";
				EpubWebView.loadJavascript(view, "javascript:" + reSizetab);
			} catch (Exception e) {
			}
		}

		String reSizeImage = "var image = document.getElementsByTagName('img')[0];"
				+ "image.setAttribute('style', 'margin:0 auto auto 0px; height:"
				+ (heightWeb - 20) + "px;width:auto;');";

		//EpubWebView.loadJavascript(view, "javascript:" + reSizeImage);
		final String[] listFont = activity.getResources().getStringArray(
				R.array.font);
		// final String[] listFont = EbookFragment.getFont();
		String strNameFont = "";
		if (data.getStringValue("font").equalsIgnoreCase("")) {
			strNameFont = listFont[0];
		} else {
			strNameFont = data.getStringValue("font");
		}
		// String strNameFont = "file:///android_asset/VNI-Lithos.ttf";
		// String strNameFont = "/system/fonts/RobotoCondensed-Bold.ttf";
		String strRunFont = "allp = document.getElementsByTagName('p');"
				+ "for (i = 0; i < allp.length; i++) { tagp = allp[i]; tagp.style.fontFamily = 'FontName';} ";

		final String strAddStyle = "var newStyle = document.createElement( 'style' );"
				+ "newStyle.appendChild( document.createTextNode( "
				+ "\"@font-face { "
				+ "font-family: 'FontName'; "
				+ "src: url('"
				+ strNameFont
				+ "') format('opentype'); "
				+ "} \") );"
				+ " document.head.appendChild( newStyle ); "
				+ strRunFont;
		String reSizeImage45 = "var array = document.getElementsByTagName('img');"
				+ "var image = document.getElementsByTagName('img')[array.length - 2];"
				+ "image.setAttribute('style','margin:0 auto auto 0px;height:"
				+ (heightWeb - 80) + "px;width:auto;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage45);
		String reSizeImage35 = "var image = document.getElementsByTagName('img')[35];"
				+ "image.setAttribute('style', 'margin: 0 auto;max-width: 80%;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage35);
		String reSizeImage29 = "var image = document.getElementsByTagName('img')[29];"
				+ "image.setAttribute('style', 'margin: 0 auto;max-width: 80%;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage29);
		String reSizeImage23 = "var image = document.getElementsByTagName('img')[23];"
				+ "image.setAttribute('style', 'margin: 0 auto;max-width: 80%;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage23);
		String reSizeImage25 = "var image = document.getElementsByTagName('img')[25];"
				+ "image.setAttribute('style', 'margin: 0 auto;max-width: 80%;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage25);
		String reSizeImage22 = "var image = document.getElementsByTagName('img')[22];"
				+ "image.setAttribute('style', 'margin: 0 auto;max-width: 80%;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage22);
		String reSizeImage32 = "var image = document.getElementsByTagName('img')[32];"
				+ "image.setAttribute('style', 'margin: 0 auto;max-width: 80%;');";
		EpubWebView.loadJavascript(view, "javascript:" + reSizeImage32);
		String goToOffsetFunc = " function pageScroll(xOffset){ window.scroll(xOffset,0); } ";
		EpubWebView.loadJavascript(view, "javascript:" + goToOffsetFunc);
		String strArrTagC = " var arr4 = document.getElementsByTagName(\"P\");"
				+ "for(i=1;i<arr4.length-1;i++){  arr4[i].setAttribute(\"style\",\"text-align: justify\");}";
		EpubWebView.loadJavascript(view, "javascript:" + strArrTagC);
		final String textSize1 = "document.body.style.fontSize = \""
				+ webView.getTextSize() + "em\"";

		EpubWebView.loadJavascript(view, "javascript:" + textSize1);
		EpubWebView.loadJavascript(view, "javascript:" + strAddStyle);

		String text = "function allIndexOf(str, toSearch) {"
				+ "var indices = [];"
				+ " for(var pos = str.indexOf(toSearch); pos !== -1; pos = str.indexOf(toSearch, pos + 1)) {"
				+ "indices.push(pos);}"
				+ "return indices;"
				+ "}"
				+ "function setTextSearch(text){"
				+ "for(var j=0;j<document.getElementsByTagName('p').length;j++){"
				+ "var obj = document.getElementsByTagName('p')[j];"
				+ "var content = obj.innerText;"
				+ "var keyWordLowcase = text.toLowerCase();"
				+ "var contentLowcase = obj.innerText.toLowerCase();"
				+ "var lengthContent = content.length;"
				+ "var lengthKeyWord = text.length;"
				+ "var countNumberIndex = allIndexOf(contentLowcase, keyWordLowcase);	"
				+ "var iCount = 0;" + "var textArray = [];"
				+ "var textTemp = '';" + "var s = obj.innerText;"
				+ "for(var k = countNumberIndex.length-1;k >=0;k-- ){"
				+ "s = s.substr(0,countNumberIndex[k])"
				+ "	+ \"<span class='dattien'>\""
				+ "	+ s.substr(countNumberIndex[k], lengthKeyWord)"
				+ "	+ \"</span>\" "
				+ "	+ s.substr(countNumberIndex[k]+lengthKeyWord,s.length);"
				+ "}" + "obj.innerHTML = s;" + " }" + "}  setTextSearch(\""
				+ "aaaaaaa" + "\");";
		// EpubWebView.loadJavascript(view, "javascript:" + text);
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				((EpubWebView) view).updateCountPage();
			}
		}, 500);

	}

	class MyJavaScriptInterface {

		@JavascriptInterface
		public void scrollWidth(String jsResult) {
			totalWidth = Integer.valueOf(jsResult);
			// Log.e("XXX scrollWidth", totalWidth + "");
		}

		@JavascriptInterface
		public void scrollHeight(String jsResult) {
			totalHeight = Integer.valueOf(jsResult);
			// Log.e("XXX scrollHeight", totalHeight + "");
			if (totalHeight > totalWidth) {
				pageCount = (int) ((float) totalHeight / heightWeb);
			} else {
				pageCount = (int) ((float) totalWidth / widthWeb);
			}

			webView.setTotalPageCount(pageCount);

			if (EpubWebView.isScroll) {
				webView.getEbookFragment().getSeekBarVertical()
						.setMax(pageCount - 1);
			} else
				webView.getEbookFragment().getSeekBar().setMax(pageCount - 1);

			// webView.goToPage(pageWebview());

			new Thread() {
				public void run() {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
					activity.runOnUiThread(new Runnable() {
						public void run() {
							webView.setVisibility(View.VISIBLE);
							DialogLoading.cancel();
						}
					});
				};
			}.start();

		}

		@JavascriptInterface
		public void getTop(int[] value) {
			arrMenu = value;
//			 for (int i = 0; i < arrMenu.length; i++) {
//			 Log.e("DAT", i + "::" + arrMenu[i] + ">>.");
//			 }
		}

		@JavascriptInterface
		public void getTopTagP(int[] value) {
			int count = 0;
			int dem = 0;
			for (int i = 213; i < 252; i++) {
				if (count == 3) {
					count = 0;
					dem++;
				}
				value[i] = value[i] + topTable + dem * (heightTable / 13);
				count++;
			}

			arrListTagP = value;
			// for (int i = 0; i < arrListTagP.length; i++) {
			// Log.e("DAT", i + "::" + arrListTagP[i] + ">>.");
			// }
		}

		@JavascriptInterface
		public void getHeightP(int[] value) {
			arrListHeightTagP = value;

			if (EbookFragment.ischeckScroll) {
				webView.goToPage(pageWebview());
				EbookFragment.ischeckScroll = false;
			} else {
				int newCurr = (int) (webView.getCurrentState() * pageCount);
				// if (!EpubWebView.isScroll) {
				webView.goToPage(newCurr);
				// } else {
				// webView.updateView();
				// }
			}
			webView.updateSeekBar();
			webView.dismisDialog();
		}

		@JavascriptInterface
		public void topTable(int value) {
			// Log.e("DAT", "top:" + value);
			topTable = value;
		}

		@JavascriptInterface
		public void heightTable(int value) {
			// Log.e("DAT", "height:" + value);
			heightTable = value;
		}

		@JavascriptInterface
		public void getSearch(int[] value) {
			// for (int i = 0; i < value.length; i++) {
			// Log.e("DAT", i + ":" + value[i] + ">>>");
			// }
		}

	}

	private int pageWebview() {
		int height = 0;
		if (EbookFragment.saveTabP == 0 && EbookFragment.saveIndex == 0)
			return 0;
		if (totalHeight > totalWidth) {
			height = (int) heightWeb;
		} else
			height = totalHeight;

		int page = arrListTagP[EbookFragment.saveTabP] / height;

		if (lenghtTabP() + arrListTagP[EbookFragment.saveTabP] >= height * page) {
			page = page + ((lenghtTabP() / height));
		}
		return page;
	}

	private int lenghtTabP() {
		return Math
				.round(((float) (EbookFragment.saveIndex + 3) / (float) EbookFragment.list
						.get(EbookFragment.saveTabP))
						* (float) arrListHeightTagP[EbookFragment.saveTabP]);
	}
}
