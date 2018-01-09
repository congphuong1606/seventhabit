package com.discovertodo.phone.android.ebook;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.ShowImageActivity;
import com.discovertodo.phone.android.fragment.EbookFragment;
import com.discovertodo.phone.android.util.ProgressHUD;
import com.discovertodo.phone.android.util.StoreData;

import java.io.IOException;
import java.io.StringReader;

@SuppressLint("SetJavaScriptEnabled")
public class EpubWebView extends WebView {
	private int totalPageCount;
	private int currentPage;
	private int topP = 0;
	private float currentState;
	private EbookFragment ebookFragment;
	private float textSize = (float) 1.0, MAX_TEXT_SIZE = (float) 2.0,
			MIN_TEXT_SIZE = (float) 0.6;
	public static Boolean isScroll = false;
	public Context cont;

	public static interface Callback {
		public void update();
	};

	Callback call;

	public EpubWebView(Context context) {
		super(context);
		cont = context;
		initWebView();
	}

	public EpubWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		cont = context;
		initWebView();
	}

	public EpubWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		cont = context;
		initWebView();
	}

	public void initWebView() {
		this.totalPageCount = 200;
		this.currentPage = 0;
		this.currentState = 0;
		// this.getSettings().setDefaultFontSize(textSize);
		this.getSettings().setUseWideViewPort(false);
		this.getSettings().setBuiltInZoomControls(false);
		this.getSettings().setDisplayZoomControls(false);
		this.getSettings().setJavaScriptEnabled(true);
		// this.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// this.setOnLongClickListener(new OnLongClickListener() {
		// @Override
		// public boolean onLongClick(View v) {
		// return true;
		// }
		// });
		// this.setLongClickable(false);
	}

	public void goToPage(int pageNumber) {
		this.currentPage = pageNumber;
		this.updateCurrentState();
		if (!isScroll)
			this.scrollTo((currentPage) * this.getWidth(), 0);
		else
			this.scrollTo(0, (currentPage) * this.getHeight());
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public EbookFragment getEbookFragment() {
		return ebookFragment;
	}

	public void setEbookFragment(EbookFragment ebookFragment) {
		this.ebookFragment = ebookFragment;
	}

	public float getCurrentState() {
		return this.currentState;
	}

	public void updateCurrentState() {
		this.currentState = (float) (currentPage + 0.5) / (totalPageCount);
	}

	public void updateSeekBar() {
		if (EpubWebView.isScroll) {
			this.ebookFragment.getSeekBarVertical().setProgress(currentPage);
		} else
			this.ebookFragment.getSeekBar().setProgress(currentPage);
	}

	public boolean setTextSizeIncrease() {
		textSize += 0.2;
		String textSize1 = "document.body.style.fontSize = \"" + textSize
				+ "em\"";
		loadJavascript(this, "javascript:" + textSize1);
		ProgressHUD.show(cont, "", false);
		final String[] listFont = getResources().getStringArray(
				R.array.font);
		StoreData data = new StoreData(cont);
		String strNameFont = "";
		if (data.getStringValue("font").equalsIgnoreCase("")) {
			strNameFont = listFont[0];
		} else {
			strNameFont = data.getStringValue("font");
		}
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
		loadJavascript(this, "javascript:" + strAddStyle);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				updateCountPage();
			}
		}, 200);
		if (textSize > MAX_TEXT_SIZE)
			return false;
		return true;
	}

	public boolean setTextSizeReduction() {
		textSize -= 0.2;
		String textSize1 = "document.body.style.fontSize = \"" + textSize
				+ "em\"";
		loadJavascript(this, "javascript:" + textSize1);
		ProgressHUD.show(cont, "", false);
		final String[] listFont = getResources().getStringArray(
				R.array.font);
		StoreData data = new StoreData(cont);
		String strNameFont = "";
		if (data.getStringValue("font").equalsIgnoreCase("")) {
			strNameFont = listFont[0];
		} else {
			strNameFont = data.getStringValue("font");
		}
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
		loadJavascript(this, "javascript:" + strAddStyle);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				updateCountPage();
			}
		}, 200);
		if (textSize < MIN_TEXT_SIZE)
			return false;
		return true;
	}

	public void setClickWeb() {
		try {
			WebView.HitTestResult result = EpubWebView.this.getHitTestResult();
			if (result.getType() == HitTestResult.IMAGE_TYPE) {
				Intent intent = new Intent(ebookFragment.getActivity(),
						ShowImageActivity.class);
				intent.putExtra("image", result.getExtra().toString());
				ebookFragment.getActivity().startActivity(intent);
			}
		} catch (Exception e) {
		}
	}

	public void changeTextColorToGray() {
		loadJavascript(this, "javascript:document.body.style.color='gray';");
		loadJavascript(this, "javascript:document.getElementById(\"table-1\").style.borderColor = \"gray\";");
	}

	public void changeTextColorToBlack() {
		loadJavascript(this, "javascript:document.body.style.color='black';");
		loadJavascript(this, "javascript:document.getElementById(\"table-1\").style.borderColor = \"black\";");
	}

	public void changeFont(String strNameFont, int id) {
		// String strNameFont = "file:///android_asset/VNI-Lithos.ttf";
		// String strNameFont = "/system/fonts/RobotoCondensed-Bold.ttf";
		// String str = "var newStyle = document.createElement( 'style' );"
		// + "newStyle.appendChild(document.createTextNode("
		// + "\"@font-face {"
		// + "font-family: MyFont;"
		// + "src: url(\'"+strNameFont+"\')"
		// + "}body {" + "font-family: MyFont;" + "font-size: medium;"
		// + "text-align: justify;}"
		// + "\"));"
		// + "document.head.appendChild( newStyle ); ";
		ProgressHUD.show(cont, "", false);
		String strRunFont = "allp = document.getElementsByTagName('p');"
				+ "for (i = 0; i < allp.length; i++) { tagp = allp[i]; tagp.style.fontFamily = 'FontName"
				+ id + "';} ";
		final String strAddStyle = "var newStyle = document.createElement( 'style' );"
				+ "newStyle.appendChild( document.createTextNode( "
				+ "\"@font-face { "
				+ "font-family: 'FontName"
				+ id
				+ "'; "
				+ "src: url('"
				+ strNameFont
				+ "') format('opentype'); "
				+ "} \") );"
				+ " document.head.appendChild( newStyle ); "
				+ strRunFont;
		this.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadJavascript(EpubWebView.this, "javascript:" + strAddStyle);
			}
		});
		// this.loadJavascript(this, "javascript:" + strAddStyle);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				updateCountPage();
			}
		}, 200);
	}

	public void updateCountPage() {
		if (EbookFragment.type_light == 3) {
			loadJavascript(this, "javascript:document.getElementById(\"table-1\").style.borderColor = \"gray\";");
			// this.loadJavascript(this, "javascript:document.getElementById(\"table-1\").style.color = \"gray\";");
			loadJavascript(this, "javascript:document.body.style.color='gray';");
		} else {
			loadJavascript(this, "javascript:document.getElementById(\"table-1\").style.borderColor = \"black\";");
			// this.loadJavascript(this, "javascript:document.getElementById(\"table-1\").style.color = \"black\";");
			loadJavascript(this, "javascript:document.body.style.color='black';");
		}
		String t0 = "for(var k = 0;k<document.getElementsByClassName(\"CellOverride-1\").length;k++)  document.getElementsByClassName(\"CellOverride-1\")[k].setAttribute(\"style\",\"padding-bottom: 15px;padding-top: 12px;\");";
		String t1 = "document.getElementsByClassName(\"見出し　-h3-\")[3].setAttribute(\"style\",\"font-weight: bold;\");";
		String t2 = "document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[3].setAttribute(\"style\",\"font-weight: bold;\")";
		String t3 = "document.getElementsByClassName(\"見出しグループ内サブタイトル（h2-subtitle）\")[0].setAttribute(\"style\",\"font-weight: bold;\")";
		String t4 = "document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[0].setAttribute(\"style\",\"font-family: Impact;\");";
		String t5 = "document.getElementsByClassName(\"見出し　-h3-\")[0].setAttribute(\"style\",\"font-weight: bold;\");";
		String t6 = "document.getElementsByClassName(\"見出し　-h3-\")[1].setAttribute(\"style\",\"font-weight: bold;\");";
		String t7 = "document.getElementsByClassName(\"見出し　-h4-\")[0].setAttribute(\"style\",\"font-weight: bold;\");";
		String t8 = "document.getElementsByClassName(\"見出し　-h3-\")[2].setAttribute(\"style\",\"font-weight: bold;\");";
		String t9 = "document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[1].setAttribute(\"style\",\"font-weight: bold;\");";
		String t10 = "document.getElementsByClassName(\"見出し　-h3-\")[4].setAttribute(\"style\",\"font-weight: bold;\");";
		String t11 = "document.getElementsByClassName(\"見出し　-h3-\")[5].setAttribute(\"style\",\"font-weight: bold;\");";
		String t12 = "document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[2].setAttribute(\"style\",\"font-weight: bold;\");";
		String t13 = "document.getElementsByClassName(\"見出し　-h3-\")[6].setAttribute(\"style\",\"font-weight: bold;\");";
		String t14 = "document.getElementsByClassName(\"見出し　-h3-\")[7].setAttribute(\"style\",\"font-weight: bold;\");";
		String t15 = "document.getElementsByClassName(\"見出しグループ内サブタイトル（h2-subtitle）\")[2].setAttribute(\"style\",\"font-weight: bold;\");";
		String t16 = "document.getElementsByClassName(\"dmManh\")[11].setAttribute(\"style\",\"font-weight: bold;";
		String t17 = "document.getElementsByClassName(\"見出し　-h3-\")[8].setAttribute(\"style\",\"font-weight: bold;\");";
		String t18 = "document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[4].setAttribute(\"style\",\"font-weight: bold;\");";
		String t19 = "document.getElementsByClassName(\"dmManh\")[14].setAttribute(\"style\",\"font-weight: bold;\");";
		String t20 = "document.getElementsByClassName(\"dmManh\")[16].setAttribute(\"style\",\"font-weight: bold;\");";
		String t21 = "document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[5].setAttribute(\"style\",\"font-weight: bold;\");";
		String t22 = "document.getElementsByClassName(\"見出し　-h3-\")[9].setAttribute(\"style\",\"font-weight: bold;\");";
		String t23 = "document.getElementsByClassName(\"見出し　-h4-\")[1].setAttribute(\"style\",\"font-weight: bold;\");";
		String t24 = "document.getElementsByClassName(\"見出し　-h4-\")[2].setAttribute(\"style\",\"font-weight: bold;\");";
		String t25 = "document.getElementsByClassName(\"見出し　-h4-\")[3].setAttribute(\"style\",\"font-weight: bold;\");";
		String t26 = "document.getElementsByClassName(\"見出し　-h3-\")[6].setAttribute(\"style\",\"font-weight: bold;\");";
		String t27 = "document.getElementsByClassName(\"見出し　-h3-\")[8].setAttribute(\"style\",\"font-weight: bold;\");";
		String t28 = "document.getElementsByClassName(\"見出しグループ内サブタイトル（h2-subtitle）\")[3].setAttribute(\"style\",\"font-weight: bold;\");";
		String t29 = "document.getElementsByClassName(\"見出しグループ内サブタイトル（h2-subtitle）\")[1].setAttribute(\"style\",\"font-weight: bold;\");";
		String t30 = "document.getElementsByClassName(\"見出し　-h3-\")[9].setAttribute(\"style\",\"font-weight: bold;\");";
		loadJavascript(this, "javascript:" + t0);
		// loadJavascript(this, "javascript:" + t1);
		// loadJavascript(this, "javascript:" + t2);
		// loadJavascript(this, "javascript:" + t3);
		// loadJavascript(this, "javascript:" + t4);
		// loadJavascript(this, "javascript:" + t5);
		// loadJavascript(this, "javascript:" + t6);
		// loadJavascript(this, "javascript:" + t7);
		// loadJavascript(this, "javascript:" + t8);
		// loadJavascript(this, "javascript:" + t9);
		// loadJavascript(this, "javascript:" + t10);
		// loadJavascript(this, "javascript:" + t11);
		// loadJavascript(this, "javascript:" + t12);
		// loadJavascript(this, "javascript:" + t13);
		// loadJavascript(this, "javascript:" + t14);
		// loadJavascript(this, "javascript:" + t15);
		// loadJavascript(this, "javascript:" + t16);
		// loadJavascript(this, "javascript:" + t17);
		// loadJavascript(this, "javascript:" + t18);
		// loadJavascript(this, "javascript:" + t19);
		// loadJavascript(this, "javascript:" + t20);
		// loadJavascript(this, "javascript:" + t21);
		// loadJavascript(this, "javascript:" + t22);
		// loadJavascript(this, "javascript:" + t23);
		// loadJavascript(this, "javascript:" + t24);
		// loadJavascript(this, "javascript:" + t25);
		// loadJavascript(this, "javascript:" + t26);
		// loadJavascript(this, "javascript:" + t27);
		// loadJavascript(this, "javascript:" + t28);
		// loadJavascript(this, "javascript:" + t29);
		// loadJavascript(this, "javascript:" + t30);

		final String strGetTop = "var heighArray = [];"
				+ "var top1 = document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[0].offsetTop; "
				+ "var top2 = document.getElementsByClassName(\"見出し　-h3-\")[0].offsetTop;"
				+ "var top3 = document.getElementsByClassName(\"見出し　-h3-\")[1].offsetTop;"
				+ "var top4 = document.getElementsByClassName(\"見出し　-h3-\")[2].offsetTop;"
				+ "var top5 = document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[1].offsetTop;"
				+ "var top6 = document.getElementsByClassName(\"見出し　-h3-\")[4].offsetTop;"
				+ "var top7 = document.getElementsByClassName(\"見出し　-h3-\")[5].offsetTop;"
				+ "var top8 = document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[2].offsetTop;"
				+ "var top9 = document.getElementsByClassName(\"見出し　-h3-\")[6].offsetTop;"
				+ "var top10 = document.getElementsByClassName(\"見出し　-h3-\")[7].offsetTop;"
				+ "var top11 = document.getElementsByClassName(\"見出しグループ内サブタイトル（h2-subtitle）\")[2].offsetTop;"
				+ "var top13 = document.getElementsByClassName(\"addmenu2\")[0].offsetTop;"
				+ "var top12 = document.getElementsByClassName(\"見出し　-h3-\")[8].offsetTop;"
				+ "var top14 = document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[4].offsetTop;"
				+ "var top15 = document.getElementsByClassName(\"addmenu1\")[0].offsetTop;"
				+ "var top16 = document.getElementsByClassName(\"addmenu\")[0].offsetTop;"
				+ "var top17 = document.getElementsByClassName(\"見出しグループ内タイトル（h1-titlle）\")[5].offsetTop;"
				+ "var top18 = document.getElementsByClassName(\"見出し　-h3-\")[9].offsetTop;"
				+ " heighArray.push(top1 + 5);" + " heighArray.push(top2 + 5);"
				+ " heighArray.push(top3 + 5);" + " heighArray.push(top4 + 5);"
				+ " heighArray.push(top5 + 5);" + " heighArray.push(top6 + 5);"
				+ " heighArray.push(top7 + 5);" + " heighArray.push(top8+5);"
				+ " heighArray.push(top9 + 5);" + " heighArray.push(top10+5);"
				+ " heighArray.push(top11 +5);" + " heighArray.push(top12+5);"
				+ " heighArray.push(top13 +5);" + " heighArray.push(top14+5);"
				+ " heighArray.push(top15 +5);" + " heighArray.push(top16+5);"
				+ " heighArray.push(top17 +5);" + " heighArray.push(top18+5);";

		loadJavascript(this, "javascript:( function () { var resultSrc = document.documentElement.scrollWidth; window.HTMLOUT.scrollWidth(resultSrc); } ) ()");
		loadJavascript(this, "javascript:( function () { var resultSrc = document.documentElement.scrollHeight; window.HTMLOUT.scrollHeight(resultSrc); } ) ()");

		loadJavascript(this, "javascript:( function () { var resultSrc2 = document.getElementsByTagName('tbody')[0].parentElement.offsetTop; window.HTMLOUT.topTable(resultSrc2); } ) ()");
		loadJavascript(this, "javascript:( function () { var resultSrc3 = document.getElementsByTagName('tbody')[0].parentElement.offsetHeight; window.HTMLOUT.heightTable(resultSrc3); } ) ()");

		final String strArrTagP = "var arrTagB = [];"
				+ " var arr = document.getElementsByTagName(\"P\");"
				+ "for(i=0;i<arr.length;i++){  arrTagB.push(arr[i].offsetTop)}";

		final String strArrTagP1 = "var arrTagB1 = [];"
				+ " var arr1 = document.getElementsByTagName(\"P\");"
				+ "for(i=0;i<arr1.length;i++){  arrTagB1.push(arr1[i].offsetHeight) }";

		new CountDownTimer(1000, 2000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}
			@Override
			public void onFinish() {
				loadJavascript(EpubWebView.this, "javascript:( function () { " + strGetTop
						+ " window.HTMLOUT.getTop(heighArray); } ) ()");
				loadJavascript(EpubWebView.this, "javascript:( function () { " + strArrTagP
						+ " window.HTMLOUT.getTopTagP(arrTagB); } ) ()");
				loadJavascript(EpubWebView.this, "javascript:( function () { " + strArrTagP1
						+ " window.HTMLOUT.getHeightP(arrTagB1); } ) ()");
				topP = currentPage
						* (int) (getHeight() / cont.getResources().getDisplayMetrics().density);
			}
		}.start();
	}

	public void scrollHorizontal() {
		isScroll = false;
		new HorizontalPagination().canScrollHorizontalPager(this);
	}

	public void scrollVertical() {
		isScroll = true;
		new VerticalPagination().canScrollVerticalPager(this);
	}

	public void setColorTextSearch(int intP, int intPos, String str) {
		String text = "function allIndexOf(str, toSearch) {"
				+ "var indices = [];"
				+ " for(var pos = str.indexOf(toSearch); pos !== -1; pos = str.indexOf(toSearch, pos + 1)) {"
				+ "indices.push(pos);"
				+ "}"
				+ "return indices;"
				+ "}"
				+ "function setColorText(pNumber, text, index){"
				+ "var obj = document.getElementsByTagName('p')[pNumber];"
				+ "var content = obj.innerText;"
				+ "var keyWordLowcase = text.toLowerCase();"
				+ "var contentLowcase = obj.innerText.toLowerCase();"
				+ "var lengthContent = content.length;"
				+ "var lengthKeyWord = text.length;"
				+ "var countNumberIndex = allIndexOf(contentLowcase, keyWordLowcase);"
				+ "if(countNumberIndex < 0){"
				+ "console.log('Not found');"
				+ "return false;}"
				+ "if(index >= countNumberIndex.length){"
				+ "console.log('Invalid index');"
				+ "return false;}"
				+ "var indexText = countNumberIndex[index];"
				+ "if(indexText >= 0){"
				+ "var iCount = 0;"
				+ "var textArray = [];"
				+ "textArray.push(content.substr(iCount, indexText));"
				+ "iCount = iCount + indexText;"
				+ "textArray.push(\"<span class='highlighted'><b>\" +  content.substr(iCount, lengthKeyWord) + \"</b></span>\");"
				+ "iCount = iCount + lengthKeyWord;"
				+ "textArray.push(content.substr(iCount,lengthContent));"
				+ "textTemp = '';"
				+ "if(textArray){"
				+ "for(i = 0; i < textArray.length; i++ ){"
				+ "textTemp += textArray[i];"
				+ "}}"
				+ "obj.innerHTML = textTemp;"
				+ "obj.getElementsByClassName('highlighted')[0].setAttribute(\"style\",\"background-color: yellow;font-family: Impact; font-size:100%;\");}}"
				+ "setColorText(" + intP + ",'"
				+ str.replace("(", "（").replace(")", "）") + "', " + intPos
				+ ");";
		loadJavascript(this, "javascript:" + text);
	}

	public void removeColorTextSearch(int intP, int intPos, String str) {
		String text = "function removeColorText(pNumber, text, index){"
				+ " var obj = document.getElementsByTagName('p')[pNumber];"
				+ " var str = obj.innerText;" + " obj.innerHTML = str;}"
				+ "removeColorText(" + intP + ",'" + str + "', " + intPos
				+ ");";
		loadJavascript(this, "javascript:" + text);
		this.updateCountPage();
	}

	@Override
	protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
		if (isScroll) {
			topP = (int) (top / cont.getResources().getDisplayMetrics().density);
			this.setCurrentPage((int) (top / getHeight()));
			this.updateSeekBar();
			this.updateCurrentState();
		}

		super.onScrollChanged(left, top, oldLeft, oldTop);
	}

	public void updateView(Callback _call) {
		call = _call;
	}

	public void updateView() {
		if (call != null)
			call.update();
	}

	public void dismisDialog() {
		try {
			ProgressHUD.mDismiss();
		} catch (Exception e) {
		}
	}

	public int getTopP() {
		return topP;
	}

	public void searchHtml(String str) {
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
				+ "	+ \"<span class='dattien' >\""
				+ "	+ s.substr(countNumberIndex[k], lengthKeyWord)"
				+ "	+ \"</span>\" "
				+ "	+ s.substr(countNumberIndex[k]+lengthKeyWord,s.length);"
				+ "}" + "obj.innerHTML = s;" + " }" + "}  setTextSearch(\""
				+ str + "\");";
		loadJavascript(this, "javascript:" + text);
		String str1 = "var arrSearch = []; for(var j = 0 ;j<document.getElementsByClassName('dattien').length;j++){ arrSearch.push(document.getElementsByClassName('dattien')[j].offsetTop);}";
		loadJavascript(this, "javascript:( function () { " + str1
				+ " window.HTMLOUT.getSearch( arrSearch ) })()");
	}

	public void removeSearch() {
		String str = "for(var i=0;i<document.getElementsByTagName('p').length;i++){  var obj = document.getElementsByTagName('p')[i];"
				+ "var str = obj.innerText;" + "obj.innerHTML = str; }";
		loadJavascript(this, "javascript:" + str);
		updateCountPage();
	}

	public float getTextSize() {
		return textSize;
	}

	public static void loadJavascript(WebView webView, String javascript) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			/**
			 In KitKat+ you should use the evaluateJavascript method
			 **/
			webView.evaluateJavascript(javascript, new ValueCallback<String>() {
				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onReceiveValue(String s) {
					JsonReader reader = new JsonReader(new StringReader(s));

					// Must set lenient to parse single values
					reader.setLenient(true);

					try {
						if(reader.peek() != JsonToken.NULL) {
							if(reader.peek() == JsonToken.STRING) {
								String msg = reader.nextString();
								if(msg != null) {
									Log.e(EpubWebView.class.getSimpleName(), msg);
								}
							}
						}
					} catch (IOException e) {
						Log.e(EpubWebView.class.getSimpleName(), "IOException", e);
					} finally {
						try {
							reader.close();
						} catch (IOException e) {
							// NOOP
						}
					}
				}
			});
		} else {
			/**
			 * For pre-KitKat+ you should use loadUrl("javascript:<JS Code Here>");
			 * To then call back to Java you would need to use addJavascriptInterface()
			 * and have your JS call the interface
			 **/
			webView.loadUrl(javascript);
		}
	}
}
