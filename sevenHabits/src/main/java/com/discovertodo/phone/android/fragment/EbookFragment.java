package com.discovertodo.phone.android.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.adapter.ChangeFontAdapter;
import com.discovertodo.phone.android.adapter.ResultSearchAdapter;
import com.discovertodo.phone.android.adapter.TabBookMarkAdapter;
import com.discovertodo.phone.android.adapter.TabMenuAdapter;
import com.discovertodo.phone.android.ebook.BookMark;
import com.discovertodo.phone.android.ebook.BookMarkTable;
import com.discovertodo.phone.android.ebook.EpubWebView;
import com.discovertodo.phone.android.ebook.EpubWebView.Callback;
import com.discovertodo.phone.android.ebook.EpubWebViewClient;
import com.discovertodo.phone.android.ebook.FontManager;
import com.discovertodo.phone.android.ebook.HorizontalPagination;
import com.discovertodo.phone.android.ebook.Search;
import com.discovertodo.phone.android.ebook.VerticalPagination;
import com.discovertodo.phone.android.global.DialogLoading;
import com.discovertodo.phone.android.util.ProgressHUD;
import com.discovertodo.phone.android.util.ReadEpubUtil;
import com.discovertodo.phone.android.util.StoreData;
import com.discovertodo.phone.android.util.SwitchView;
import com.discovertodo.phone.android.util.SwitchView.CallClick;
import com.discovertodo.phone.android.util.VerticalSeekBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint({ "ResourceAsColor", "UseValueOf" })
public class EbookFragment extends BaseFragment implements SensorEventListener {

	private View layoutBarTop, btnSettingWebview, btnMenu, btnBookMark,
			btnSearch;
	private SwitchView mSwitch;
	private EpubWebView webView;
	private SeekBar seekBarPage, seekBarBrightness;
	private TextView txtNumberPage, txtHome, txtPopupWhite, txtPopupSepia,
			txtPopupNight, btnBack;
	private Dialog dialogMenu;
	private View btnTextSmall, btnTextHuge, btnBgWhite, btnBgSepia, btnBgNight;
	private LinearLayout layoutPopup, llChangeFont;
	private WindowManager.LayoutParams layoutParams;
	private int curBrightnessValue;
	private long flagTimeTextSize;
	private ImageView mtpopupImage;
	private LinearLayout llTab;
	private RelativeLayout rlTab;
	private ListView lvTab;
	private SensorManager mSensorManager;
	private Sensor mPressure;
	public static int type_light = 1;
	private BookMarkTable dataBookMark;
	public static boolean isNight = false;
	private int selectLight = 1;
	private ArrayList<BookMark> listBookMark = new ArrayList<BookMark>();
	private ArrayList<BookMark> listCheckBookmark = new ArrayList<BookMark>();
	private TabBookMarkAdapter adapter;
	private String htmlEpub;
	private ListView lvSearch;
	private ResultSearchAdapter adt;
	private boolean isCheckBookmark = false;
	private StoreData data;
	private TextView tvNameFont, tvTitleFont, tvTitleScroll, tvTitleOutNight;
	public static Boolean isShowSearch = false;
	private Search mSearch;
	private String textSearch = "";
	private LinearLayout llScrollVertical;
	private VerticalSeekBar seekbarVertical;
	private int mProgress;
	public static ArrayList<Integer> list = new ArrayList<Integer>();
	private Switch mSwitchNight;
	public static EditText edtSearch;
	public static int saveTabP = 0;
	public static int saveIndex = 0;
	public static boolean ischeckScroll = false;
	public static int numberMax = 0;


	public  static EbookFragment getInstance(HomeFragment fragment){
		EbookFragment ebookFragment = new EbookFragment();
		ebookFragment.layoutBarTop = fragment.layoutEBookBarTop;
		return ebookFragment;
	}


	@Override
	public void onStart() {
		super.onStart();
		layoutBarTop.setVisibility(View.VISIBLE);
		if (data.getBooleanValue("isNight")) {
			try {
				mSensorManager.registerListener(EbookFragment.this, mPressure,
						SensorManager.SENSOR_DELAY_NORMAL);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		layoutBarTop.setVisibility(View.GONE);
		data.setIntValue("typelight", type_light);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.ebook_layout, container, false);
			data = new StoreData(activity);
			if (data.getIntValue("typelight") != 0)
				type_light = data.getIntValue("typelight");
			
			EpubWebView.isScroll = data.getBooleanValue("Scroll");
			
			setupLayout();
			setupValue();
			setupListener();
			onClick();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					setupPopupMenu();
				}
			}, 200);
		}
		try {
			if (EpubWebView.isScroll) {
				seekBarPage.setVisibility(View.GONE);
				llScrollVertical.setVisibility(View.VISIBLE);
			} else {
				llScrollVertical.setVisibility(View.GONE);
				seekBarPage.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
		}

		mSensorManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		dataBookMark = new BookMarkTable(getActivity());
		adapter = new TabBookMarkAdapter(EbookFragment.this, listBookMark);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void setupLayout() {
		btnSettingWebview = layoutBarTop
				.findViewById(R.id.home_btn_ebook_webview_setting);
		btnMenu = layoutBarTop.findViewById(R.id.home_btn_ebook_webview_menu);
		btnSearch = layoutBarTop
				.findViewById(R.id.home_btn_ebook_webview_search);
		btnBookMark = layoutBarTop
				.findViewById(R.id.home_btn_ebook_webview_book);
		btnBack = (TextView) layoutBarTop.findViewById(R.id.btn_back);
		txtHome = (TextView) layoutBarTop
				.findViewById(R.id.home_btn_menu_ebook);
		webView = (EpubWebView) view.findViewById(R.id.ebook_webview);
		seekBarPage = (SeekBar) view.findViewById(R.id.ebook_seekbar);
		txtNumberPage = (TextView) view
				.findViewById(R.id.ebook_numberpage_text);
		rlTab = (RelativeLayout) view.findViewById(R.id.rl_tab);
		llTab = (LinearLayout) view.findViewById(R.id.ll_tab);
		mSwitch = (SwitchView) view.findViewById(R.id.switch_menu);
		mSwitch.setColor(type_light);
		lvTab = (ListView) view.findViewById(R.id.lv_tab);
		llScrollVertical = (LinearLayout) view.findViewById(R.id.ll_vertical);
		llScrollVertical.setVisibility(View.GONE);
		seekbarVertical = (VerticalSeekBar) view
				.findViewById(R.id.seekBarVertical);
		switch (type_light) {
		case 1:
			typeLightWhite();
			break;
		case 2:
			typeLightSepia();
			break;
		case 3:
			typeLightNight();
			break;
		default:
			break;
		}
	}

	private void setupValue() {
		layoutParams = getActivity().getWindow().getAttributes();
		try {
			curBrightnessValue = android.provider.Settings.System.getInt(
					activity.getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS) * 20 / 255;
			if (curBrightnessValue < 1)
				curBrightnessValue = 1;
		} catch (Exception e) {
			e.printStackTrace();
			curBrightnessValue = 10;
		}
		webView.setEbookFragment(this);
		if (!EpubWebView.isScroll)
			new HorizontalPagination().canScrollHorizontalPager(webView);
		else
			new VerticalPagination().canScrollVerticalPager(webView);

		webView.setWebViewClient(new EpubWebViewClient(activity, webView));
		DialogLoading.showLoading(activity, getString(R.string.loading));
		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
				htmlEpub = ReadEpubUtil.getEpub(activity);
				Document document = Jsoup.parse(htmlEpub);
				for (Element span : document.select("p")) {
					list.add(span.text().toString().length());
				}
				activity.runOnUiThread(new Runnable() {
					public void run() {
						webView.loadDataWithBaseURL(
								"file://"
										+ Environment
												.getExternalStorageDirectory()
										+ "/SevenHabitsBooks/", htmlEpub,
								"text/html", "utf-8", null);
					}
				});
			};
		}.start();

	}

	private void setupListener() {
		btnSettingWebview.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showPopup();
			}
		});

		seekBarPage.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				webView.goToPage(mProgress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (progress > seekBar.getMax()) {
					progress = seekBar.getMax();
				}
				mProgress = progress;
				txtNumberPage.setText((progress + 1) + "/"
						+ (seekBar.getMax() + 1));
				numberMax = seekBar.getMax() + 1;
				try {
					setIconBookmark(progress + 1, false);
				} catch (Exception e) {
				}
			}
		});
		seekbarVertical
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						webView.goToPage(mProgress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (progress > seekBar.getMax()) {
							progress = seekBar.getMax();
						}
						mProgress = progress;
						txtNumberPage.setText((progress + 1) + "/"
								+ (seekBar.getMax() + 1));
						numberMax = seekBar.getMax() + 1;
						try {
							setIconBookmark(progress + 1, false);
						} catch (Exception e) {
						}
					}
				});

	}

	public SeekBar getSeekBar() {
		return seekBarPage;
	}

	public VerticalSeekBar getSeekBarVertical() {
		return seekbarVertical;

	}

	private void showPopup() {
		dialogMenu.show();
	}

	private void setupPopupMenu() {
		dialogMenu = new Dialog(activity);
		dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMenu.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogMenu.setContentView(R.layout.popup_ebook_menu);
		dialogMenu.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialogMenu.setCanceledOnTouchOutside(true);

		WindowManager.LayoutParams wmlp = dialogMenu.getWindow()
				.getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
		wmlp.y = layoutBarTop.getHeight() - 15;
		llChangeFont = (LinearLayout) dialogMenu
				.findViewById(R.id.ll_change_font);
		layoutPopup = (LinearLayout) dialogMenu
				.findViewById(R.id.ebook_popup_menu_layout);
		seekBarBrightness = (SeekBar) dialogMenu
				.findViewById(R.id.ebook_popup_menu_brightness_seekbar);
		btnTextSmall = dialogMenu
				.findViewById(R.id.ebook_popup_menu_text_btn_small);
		btnTextHuge = dialogMenu
				.findViewById(R.id.ebook_popup_menu_text_btn_huge);
		btnBgWhite = dialogMenu
				.findViewById(R.id.ebook_popup_menu_bg_btn_white);
		btnBgSepia = dialogMenu
				.findViewById(R.id.ebook_popup_menu_bg_btn_sepia);
		btnBgNight = dialogMenu
				.findViewById(R.id.ebook_popup_menu_bg_btn_night);
		txtPopupWhite = (TextView) dialogMenu
				.findViewById(R.id.ebook_popup_menu_text_white);
		txtPopupSepia = (TextView) dialogMenu
				.findViewById(R.id.ebook_popup_menu_text_sepia);
		txtPopupNight = (TextView) dialogMenu
				.findViewById(R.id.ebook_popup_menu_text_night);
		mtpopupImage = (ImageView) dialogMenu
				.findViewById(R.id.popup_setting_choose_session_mtpopup_right);
		mSwitchNight = (Switch) dialogMenu.findViewById(R.id.sw_night);
		Switch mSwitchScroll = (Switch) dialogMenu.findViewById(R.id.sw_scroll);
		mSwitchScroll.setChecked(EpubWebView.isScroll);
		tvTitleFont = (TextView) dialogMenu.findViewById(R.id.tv_font);
		tvTitleScroll = (TextView) dialogMenu
				.findViewById(R.id.tv_title_scroll);
		tvTitleOutNight = (TextView) dialogMenu
				.findViewById(R.id.tv_title_out_night);
		if (data.getBooleanValue("isNight"))
			mSwitchNight.setChecked(true);
		else
			mSwitchNight.setChecked(isNight);
		RelativeLayout rlLight = (RelativeLayout) dialogMenu
				.findViewById(R.id.rl_light);
		if (isSupported()) {
			rlLight.setVisibility(View.VISIBLE);
		} else
			rlLight.setVisibility(View.GONE);

		mSwitchNight.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isNight = isChecked;
				data.setBooleanValue("isNight", isChecked);
				if (isNight) {
					try {
						mSensorManager.registerListener(EbookFragment.this,
								mPressure, SensorManager.SENSOR_DELAY_NORMAL);
					} catch (Exception e) {
					}
				} else {
					try {
						mSensorManager.unregisterListener(EbookFragment.this);
					} catch (Exception e) {
					}
					switch (selectLight) {
					case 1:
						typeLightWhite();
						break;
					case 2:
						typeLightSepia();
						break;
					case 3:
						typeLightNight();
						break;
					default:
						break;
					}
				}
			}
		});
		mSwitchScroll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				EpubWebView.isScroll = isChecked;
				data.setBooleanValue("Scroll",isChecked);
				ischeckScroll = true;
				ProgressHUD.show(activity, "", false);
				if (isChecked) {
					saveScrollHorizon();
					seekBarPage.setVisibility(View.GONE);
					llScrollVertical.setVisibility(View.VISIBLE);
					webView.scrollVertical();
					webView.updateView(new Callback() {
						@Override
						public void update() {
							webView.goToPage(mProgress);
						}
					});
					setupListener();
					webView.loadDataWithBaseURL(
							"file://"
									+ Environment.getExternalStorageDirectory()
									+ "/SevenHabitsBooks/", htmlEpub,
							"text/html", "utf-8", null);
				} else {
					saveScrollVertical();
					llScrollVertical.setVisibility(View.GONE);
					seekBarPage.setVisibility(View.VISIBLE);
					webView.scrollHorizontal();
					setupListener();
					webView.loadDataWithBaseURL(
							"file://"
									+ Environment.getExternalStorageDirectory()
									+ "/SevenHabitsBooks/", htmlEpub,
							"text/html", "utf-8", null);
				}
			}
		});
		btnTextSmall.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				long temp = System.currentTimeMillis();
				if ((temp - flagTimeTextSize) > 1000) {
					flagTimeTextSize = temp;
					if (!webView.setTextSizeReduction()) {
						btnTextSmall.setEnabled(false);
						btnTextHuge.setEnabled(true);
					} else {
						btnTextSmall.setEnabled(true);
						btnTextHuge.setEnabled(true);
					}
				}
			}
		});
		btnTextHuge.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				long temp = System.currentTimeMillis();
				if ((temp - flagTimeTextSize) > 1000) {
					flagTimeTextSize = temp;
					if (!webView.setTextSizeIncrease()) {
						btnTextSmall.setEnabled(true);
						btnTextHuge.setEnabled(false);
					} else {
						btnTextSmall.setEnabled(true);
						btnTextHuge.setEnabled(true);
					}
				}
			}
		});
		btnBgWhite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				typeLightWhite();
				selectLight = 1;
			}
		});
		btnBgSepia.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				typeLightSepia();
				selectLight = 2;
			}
		});
		btnBgNight.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				typeLightNight();
				selectLight = 3;
			}
		});
		seekBarBrightness
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onProgressChanged(SeekBar seekBar,
							int position, boolean arg2) {
						if (position < 1)
							position = 1;
						seekBarBrightness.setProgress(position);
						setBrightness(position);
					}
				});
		seekBarBrightness.setProgress(curBrightnessValue);
		setThemSelect(0);
		llChangeFont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setupPopupChangeFont();
			}
		});
		tvNameFont = (TextView) dialogMenu.findViewById(R.id.tv_name_font);
		String[] listNameFont = getActivity().getResources().getStringArray(
				R.array.fontname);

		// String[] listNameFont = getFont();

		if (data.getStringValue("font").equalsIgnoreCase("")) {
			tvNameFont.setText(listNameFont[0].replace("/system/fonts/", "")
					.replace(".ttf", ""));
		} else {
			tvNameFont.setText(listNameFont[data.getIntValue("fontId")]
					.replace("/system/fonts/", "").replace(".ttf", ""));
		}
		switch (type_light) {
		case 1:
			typeLightWhite();
			break;
		case 2:
			typeLightSepia();
			break;
		case 3:
			typeLightNight();
			break;
		default:
			break;
		}
	}

	private void typeLightWhite() {
		type_light = 1;
		setBackgroundFull(R.color.white, R.drawable.bg_popup_while);
		setSeekBarColor(R.color.bluex);
		webView.changeTextColorToBlack();

		((ImageView) btnSettingWebview)
				.setImageResource(R.drawable.ic_change_web_blue);
		btnSearch.setBackgroundResource(R.drawable.seach);
		btnMenu.setBackgroundResource(R.drawable.menu0);
		if (isCheckBookmark)
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark_del);
		else
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark);

		llTab.setBackgroundResource(R.color.white);
		llScrollVertical.setBackgroundColor(Color.TRANSPARENT);

		mSwitch.setColor(type_light);
		try {
			mtpopupImage.setImageResource(R.drawable.ic_mtpopup_white);
			tvNameFont.setTextColor(Color.BLACK);
			tvTitleFont.setTextColor(Color.BLACK);
			tvTitleScroll.setTextColor(Color.BLACK);
			tvTitleOutNight.setTextColor(Color.BLACK);
			setThemSelect(0);
		} catch (Exception e) {
		}
	}

	private void typeLightSepia() {
		type_light = 2;
		setBackgroundFull(R.color.sepia, R.drawable.bg_popup_sepia);
		setSeekBarColor(R.color.brown);
		webView.changeTextColorToBlack();
		try {
			mtpopupImage.setImageResource(R.drawable.ic_mtpopup_sepia);
			tvNameFont.setTextColor(Color.BLACK);
			tvTitleFont.setTextColor(Color.BLACK);
			tvTitleScroll.setTextColor(Color.BLACK);
			tvTitleOutNight.setTextColor(Color.BLACK);
			setThemSelect(1);
		} catch (Exception e) {
		}

		((ImageView) btnSettingWebview)
				.setImageResource(R.drawable.ic_change_web_brown);
		btnSearch.setBackgroundResource(R.drawable.seach1);
		btnMenu.setBackgroundResource(R.drawable.menu1);
		if (isCheckBookmark)
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark_del);
		else
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark1);
		llTab.setBackgroundResource(R.color.sepia);
		llScrollVertical.setBackgroundColor(Color.TRANSPARENT);
		mSwitch.setColor(type_light);
	}

	private void typeLightNight() {
		type_light = 3;
		setBackgroundFull(R.color.night, R.drawable.bg_popup_night);
		setSeekBarColor(R.color.gray_3);
		webView.changeTextColorToGray();
		((ImageView) btnSettingWebview)
				.setImageResource(R.drawable.ic_change_web_gray);
		btnSearch.setBackgroundResource(R.drawable.seach2);
		btnMenu.setBackgroundResource(R.drawable.menu2);
		if (isCheckBookmark)
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark_del);
		else
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark2);
		llTab.setBackgroundResource(R.color.night);
		llScrollVertical.setBackgroundColor(Color.TRANSPARENT);

		mSwitch.setColor(type_light);
		try {
			mtpopupImage.setImageResource(R.drawable.ic_mtpopup_night);
			tvNameFont.setTextColor(Color.GRAY);
			tvTitleFont.setTextColor(Color.GRAY);
			tvTitleScroll.setTextColor(Color.GRAY);
			tvTitleOutNight.setTextColor(Color.GRAY);
			setThemSelect(2);
		} catch (Exception e) {
		}
	}

	private void setupPopupChangeFont() {
		final Dialog dialogMenu = new Dialog(activity);
		dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMenu.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogMenu.setContentView(R.layout.popup_font);
		dialogMenu.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialogMenu.setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams wmlp = dialogMenu.getWindow()
				.getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
		wmlp.y = layoutBarTop.getHeight() - 15;
		LinearLayout llBack = (LinearLayout) dialogMenu
				.findViewById(R.id.ll_back);
		LinearLayout llBg = (LinearLayout) dialogMenu
				.findViewById(R.id.ebook_popup_menu_layout);
		TextView tvFont = (TextView) dialogMenu.findViewById(R.id.tv_font);
		TextView tvBack = (TextView) dialogMenu.findViewById(R.id.tv_back);
		ImageView imgBack = (ImageView) dialogMenu.findViewById(R.id.img_back);
		ImageView imgPopup = (ImageView) dialogMenu
				.findViewById(R.id.popup_setting_choose_session_mtpopup_right);
		switch (type_light) {
		case 1:
			llBg.setBackgroundResource(R.drawable.bg_popup_while);
			tvFont.setTextColor(Color.BLACK);
			tvBack.setTextColor(getResources().getColor(R.color.bluex));
			imgBack.setBackgroundResource(R.drawable.ic_back_);
			imgPopup.setImageResource(R.drawable.ic_mtpopup_white);
			break;
		case 2:
			llBg.setBackgroundResource(R.drawable.bg_popup_sepia);
			tvFont.setTextColor(Color.BLACK);
			tvBack.setTextColor(getResources().getColor(R.color.brown));
			imgBack.setBackgroundResource(R.drawable.ic_back_1);
			imgPopup.setImageResource(R.drawable.ic_mtpopup_sepia);
			break;
		case 3:
			llBg.setBackgroundResource(R.drawable.bg_popup_night);
			tvFont.setTextColor(Color.GRAY);
			tvBack.setTextColor(Color.GRAY);
			imgBack.setBackgroundResource(R.drawable.ic_back_2);
			imgPopup.setImageResource(R.drawable.ic_mtpopup_night);
			break;
		default:
			break;
		}
		final String[] listfontName = getResources().getStringArray(
				R.array.fontname);
		// final String[] listfontName = getFont();
		llBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogMenu.dismiss();

			}
		});
		dialogMenu.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				try {
					if (data.getStringValue("font").equalsIgnoreCase("")) {
						tvNameFont.setText(listfontName[0].replace(
								"/system/fonts/", "").replace(".ttf", ""));
					} else
						tvNameFont.setText(listfontName[data
								.getIntValue("fontId")].replace(
								"/system/fonts/", "").replace(".ttf", ""));
				} catch (Exception e) {
				}
			}
		});

		ListView lvFont = (ListView) dialogMenu.findViewById(R.id.lv_font);

		final String[] listFont = getResources().getStringArray(R.array.font);
		// final String[] listFont = getFont();

		final ChangeFontAdapter adapter = new ChangeFontAdapter(activity,
				listFont);
		lvFont.setAdapter(adapter);
		lvFont.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (data.getStringValue("font") == null
						|| data.getStringValue("font").equalsIgnoreCase("")) {
					data.setStringValue("font", listFont[0]);
					data.setIntValue("fontId", 0);
				}

				data.setStringValue("font", listFont[position]);
				data.setIntValue("fontId", position);
				idFont = idFont + 1;
				webView.changeFont(listFont[position], idFont);
				adapter.notifyDataSetChanged();
			}
		});
		// getFont();
		dialogMenu.show();
	}

	public static String[] getFont1() {
		HashMap<String, String> map = FontManager.enumerateFonts();
		String[] list = new String[map.size()];
		int i = 0;
		for (String key : map.keySet()) {
			// Log.e("DAT", key.toString().replace(
			// "/system/fonts/", "").replace(".ttf", ""));
			list[i] = key;
			i = i + 1;
		}
		return list;
	}

	int idFont = 0;

	private void setThemSelect(int select) {
		switch (select) {
		case 0:
			btnBgWhite.setSelected(true);
			btnBgSepia.setSelected(false);
			btnBgNight.setSelected(false);
			break;
		case 1:
			btnBgWhite.setSelected(false);
			btnBgSepia.setSelected(true);
			btnBgNight.setSelected(false);
			break;
		case 2:
			btnBgWhite.setSelected(false);
			btnBgSepia.setSelected(false);
			btnBgNight.setSelected(true);
			break;
		}
	}

	private void setBackgroundFull(int color, int bgLayout) {
		try {
			layoutPopup.setBackgroundResource(bgLayout);
		} catch (Exception e) {
		}
		webView.setBackgroundColor(getResources().getColor(color));
		layoutBarTop.setBackgroundColor(getResources().getColor(color));
		((View) seekBarPage.getParent().getParent())
				.setBackgroundColor(getResources().getColor(color));

		((View) seekbarVertical.getParent().getParent())
				.setBackgroundColor(getResources().getColor(color));
	}

	@SuppressLint("NewApi")
	private void setSeekBarColor(int colorId) {
		txtHome.setTextColor(getResources().getColor(colorId));
		btnBack.setTextColor(getResources().getColor(colorId));
		txtNumberPage.setTextColor(getResources().getColor(colorId));
		try {
			((TextView) btnTextHuge).setTextColor(getResources().getColor(
					colorId));
			((TextView) btnTextSmall).setTextColor(getResources().getColor(
					colorId));
			txtPopupNight.setTextColor(getResources().getColor(colorId));
			txtPopupSepia.setTextColor(getResources().getColor(colorId));
			txtPopupWhite.setTextColor(getResources().getColor(colorId));
			seekBarBrightness.getProgressDrawable().setColorFilter(
					getResources().getColor(colorId), Mode.SRC_IN);
			seekBarBrightness.getThumb().setColorFilter(
					getResources().getColor(colorId), Mode.SRC_IN);
		} catch (Exception e) {
		}
		seekBarPage.getProgressDrawable().setColorFilter(
				getResources().getColor(colorId), Mode.SRC_IN);
		seekBarPage.getThumb().setColorFilter(getResources().getColor(colorId),
				Mode.SRC_IN);
		seekbarVertical.getProgressDrawable().setColorFilter(
				getResources().getColor(colorId), Mode.SRC_IN);
		seekbarVertical.getThumb().setColorFilter(
				getResources().getColor(colorId), Mode.SRC_IN);
	}

	private void setBrightness(float position) {
		if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
			android.provider.Settings.System.putInt(activity.getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS,
					(int) position * 255 / 20);
			layoutParams.screenBrightness = position / 20f;
			getActivity().getWindow().setAttributes(layoutParams);
		}
	}

	public void onClick() {

		btnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMenu();
				if (mSwitch.getIsOn()) {
					loadTabMenu();
				} else {
					loadTabBookMark();
				}
			}
		});

		mSwitch.setIsOn(true, new CallClick() {
			@Override
			public void clickRight() {
				loadTabBookMark();
			}

			@Override
			public void clickLeft() {
				loadTabMenu();
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				offMenu();
				setIconBookmark(webView.getCurrentPage() + 1, true);
			}
		});
		btnBookMark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int height = 0;
				if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
					height = (int) EpubWebViewClient.heightWeb;
				} else
					height = EpubWebViewClient.totalHeight;
				if (isCheckBookmark) {
					removeTabP(webView.getCurrentPage(), height);
				} else {
					addTabP(webView.getCurrentPage(), height);

				}
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogSearch();
			}
		});
	}

	private void onMenu() {
		rlTab.setVisibility(View.GONE);
		llTab.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);
		btnSearch.setVisibility(View.GONE);
		btnBookMark.setVisibility(View.GONE);
		btnSettingWebview.setVisibility(View.GONE);
	}

	private void offMenu() {
		rlTab.setVisibility(View.VISIBLE);
		llTab.setVisibility(View.GONE);
		btnBack.setVisibility(View.GONE);
		btnMenu.setVisibility(View.VISIBLE);
		btnSearch.setVisibility(View.VISIBLE);
		btnBookMark.setVisibility(View.VISIBLE);
		btnSettingWebview.setVisibility(View.VISIBLE);
	}

	private ArrayList<String> listMenu = new ArrayList<String>();

	private void loadTabMenu() {
		listMenu.clear();
		String[] list = getResources().getStringArray(R.array.menu);
		for (int i = 0; i < list.length; i++) {
			listMenu.add(list[i].toString());
		}
		TabMenuAdapter adapter = new TabMenuAdapter(activity, listMenu);
		lvTab.setAdapter(adapter);
		lvTab.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int height = 0;
				if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
					height = (int) EpubWebViewClient.heightWeb;
				} else
					height = EpubWebViewClient.totalHeight;
				offMenu();
				webView.goToPage(EpubWebViewClient.arrMenu[position] / height);
				webView.updateSeekBar();
			}
		});
	}

	private void loadTabBookMark() {
		listBookMark.clear();
		listBookMark.addAll(dataBookMark.getAllBookmark());
		adapter.setSwipe();
		adapter.notifyDatasetChanged();
		lvTab.setAdapter(adapter);
	}

	public void OpenItemBookmark(int position) {
		offMenu();
		webView.goToPage(position);
		webView.updateSeekBar();
	}

	public void DeleteItemBookmark(BookMark mBookmark) {
		dataBookMark.deleteBookmark(mBookmark);
		listBookMark.remove(mBookmark);
		adapter.notifyDatasetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		try {
			mSensorManager.unregisterListener(this);
		} catch (Exception e) {
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.values[0] < 30) {
			if (type_light != 3) {
				if (event.values[0] < 10) {
					type_light = 3;
					typeLightNight();
				}
			} else {
				type_light = 3;
				typeLightNight();
			}
		} else {
			if (type_light != 1) {
				if (event.values[0] > 84) {
					type_light = 1;
					typeLightWhite();
				}
			} else {
				typeLightWhite();
			}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private void setIconBookmark(int number, boolean is) {
		int height = 0;
		if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
			height = (int) EpubWebViewClient.heightWeb;
		} else
			height = EpubWebViewClient.totalHeight;

		isCheckBookmark = false;
		listCheckBookmark.clear();
		listCheckBookmark = dataBookMark.getAllBookmark();

		for (int i = 0; i < listCheckBookmark.size(); i++) {
			// Log.e("DAT",
			// number
			// + "/"
			// + height
			// + "/"
			// + "lenght:"
			// + EpubWebViewClient.arrListHeightTagP[listCheckBookmark
			// .get(i).getNumber()]
			// + "top:"
			// + EpubWebViewClient.arrListTagP[listCheckBookmark
			// .get(i).getNumber()] + ">>>trang:" + number
			// + "/tabP:" + listCheckBookmark.get(i).getNumber()
			// + "/count:" + listCheckBookmark.get(i).getPage()
			// + "//" + lenghtTabP(i) + "//textcount:"
			// + list.get(listCheckBookmark.get(i).getNumber()));

			int lengthP = lenghtTabP(i);
			if (listCheckBookmark.get(i).getNumber() == 311)
				lengthP = 20;

			if (EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
					.getNumber()] >= (number - 1) * height
					&& EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
							.getNumber()] + lengthP < number * height) {
				isCheckBookmark = true;
				i = listCheckBookmark.size() + 1;
			} else if (EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
					.getNumber()] < (number - 1) * height
					&& EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
							.getNumber()] + lengthP >= (number - 1) * height
					&& EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
							.getNumber()] + lengthP < number * height) {
				isCheckBookmark = true;
				i = listCheckBookmark.size() + 1;
				Log.e("DAT", "222222222222222222222");
			}
		}

		if (isCheckBookmark) {
			btnBookMark.setBackgroundResource(R.drawable.ic_bookmark_del);
		} else {
			switch (type_light) {
			case 1:
				btnBookMark.setBackgroundResource(R.drawable.ic_bookmark);
				break;
			case 2:
				btnBookMark.setBackgroundResource(R.drawable.ic_bookmark1);
				break;
			case 3:
				btnBookMark.setBackgroundResource(R.drawable.ic_bookmark2);
				break;
			}
		}
	}

	private void showDialogSearch() {
		clearTextSearch();
		final Dialog dialogMenu = new Dialog(activity);
		dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMenu.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogMenu.setContentView(R.layout.dialog_search_ebook);
		dialogMenu.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialogMenu.setCanceledOnTouchOutside(true);
		LinearLayout llBg = (LinearLayout) dialogMenu.findViewById(R.id.ll_bg);
		LinearLayout llBgSearch = (LinearLayout) dialogMenu
				.findViewById(R.id.ll_bg_search);
		TextView tvBack = (TextView) dialogMenu.findViewById(R.id.tv_back);
		TextView tvBg = (TextView) dialogMenu.findViewById(R.id.tv_bg);
		switch (type_light) {
		case 1:
			llBg.setBackgroundColor(Color.WHITE);
			tvBack.setTextColor(getResources().getColor(R.color.bluex));
			llBgSearch.setBackgroundResource(R.drawable.bg_edit_search);
			tvBg.setBackgroundResource(R.color.gray_1);
			break;
		case 2:
			tvBg.setBackgroundResource(R.color.brown);
			llBg.setBackgroundResource(R.color.sepia);
			tvBack.setTextColor(getResources().getColor(R.color.brown));
			llBgSearch.setBackgroundResource(R.drawable.bg_edit_search1);
			break;
		case 3:
			tvBg.setBackgroundResource(R.color.gray_1);
			llBg.setBackgroundColor(Color.BLACK);
			tvBack.setTextColor(Color.GRAY);
			llBgSearch.setBackgroundResource(R.drawable.bg_edit_search);
			break;

		default:
			break;
		}
		edtSearch = (EditText) dialogMenu.findViewById(R.id.edt_search);
		lvSearch = (ListView) dialogMenu.findViewById(R.id.lv_result_search);
		tvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogMenu.dismiss();
			}
		});
		adt = new ResultSearchAdapter(activity, listSearch);
		lvSearch.setAdapter(adt);
		edtSearch.setText(textSearch);
		edtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				listSearch.clear();
				textSearch = edtSearch.getText().toString();
				searchText(edtSearch.getText().toString());
				adt.notifyDataSetChanged();
			}
		});

		lvSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position != lvSearch.getCount() - 1) {
					isShowSearch = true;
					mSearch = listSearch.get(position);
					webView.setColorTextSearch(mSearch.getIntP(),
							mSearch.getIntPos(), mSearch.getText());
					// Log.e("DAT",
					// ">>>>>>>>>"
					// + mSearch.getIntP()
					// + "///"
					// + mSearch.getIntPos()
					// + "///"
					// + mSearch.getText()
					// + "//"
					// + EpubWebViewClient.arrListTagP[mSearch
					// .getIntP()]
					// + "//"
					// + EpubWebViewClient.arrListHeightTagP[mSearch
					// .getIntP()] + "//"
					// + mSearch.getIntP());
					webView.goToPage(Integer.parseInt(listSearch.get(position)
							.getNumber()) - 1);
					webView.updateSeekBar();
					dialogMenu.dismiss();
				}
			}
		});
		ImageView imgClose = (ImageView) dialogMenu
				.findViewById(R.id.img_close);
		imgClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textSearch = "";
				edtSearch.setText("");
				listSearch.clear();
				adt.notifyDataSetChanged();
			}
		});
		listSearch.clear();
		searchText(edtSearch.getText().toString());
		adt.notifyDataSetChanged();
		dialogMenu.show();
	}

	private void searchText(String str) {
		Document document = Jsoup
				.parse(htmlEpub);
		int i = 0;
		for (Element span : document.select("p")) {
			// if (i < 213 || i > 249) {
			getSearch(span.text().toString(), str, i);
			// }
			i++;
		}
	}

	ArrayList<Search> listSearch = new ArrayList<Search>();

	@SuppressLint("DefaultLocale")
	private void getSearch(String str, String strResult, int pos) {

		String strTam = strResult.toLowerCase();
		strTam = strTam.replace("(", "（").replace(")", "）");
		int i = str.toLowerCase().indexOf(strTam);
		if (strResult.equalsIgnoreCase(""))
			i = -1;
		int j = 0;
		if (i > -1) {
			String s = strResult;
			if (i >= 10 && str.length() >= i + strResult.length() + 20) {
				s = "..." + str.substring(i - 10, i + strResult.length() + 20)
						+ "...";
				s = addBoldText(s, strResult, 13);
			} else if (i < 10 && str.length() < i + strResult.length() + 20) {
				s = str.substring(i, str.length());
				s = addBoldText(s, strResult, s.toLowerCase()
						.indexOf(strTam, i));
			} else if (i >= 10 && str.length() < i + strResult.length() + 20) {
				s = "..." + str.substring(i - 10, str.length());
				s = addBoldText(s, strResult, 13);
			} else if (i < 10 && str.length() >= i + strResult.length() + 20) {
				s = str.substring(i, i + strResult.length() + 20) + "...";
				s = addBoldText(s, strResult, s.toLowerCase()
						.indexOf(strTam, i));
			}

			listSearch.add(new Search(menuPage(pos), s, numberPage(pos, str,
					strResult, i) + "", pos, 0, strResult));
			while (i > -1) {
				j++;
				i = str.toLowerCase().indexOf(strTam, i + strResult.length());
				if (i > -1) {
					s = "";
					if (i >= 10 && str.length() >= i + strResult.length() + 20) {
						s = "..."
								+ str.substring(i - 10, i + strResult.length()
										+ 20) + "...";
						s = addBoldText(s, strResult, 13);
					} else if (i < 10
							&& str.length() < i + strResult.length() + 20) {
						s = str.substring(i, str.length()) + "...";
						s = addBoldText(s, strResult, s.indexOf(strTam, i));
					} else if (i >= 10
							&& str.length() < i + strResult.length() + 20) {
						s = "..." + str.substring(i - 10, str.length());
						s = addBoldText(s, strResult, 13);
					} else if (i < 10
							&& str.length() >= i + strResult.length() + 20) {
						s = str.substring(i, i + strResult.length() + 20)
								+ "...";
						s = addBoldText(s, strResult, s.indexOf(strTam, i));
					}
					listSearch.add(new Search(menuPage(pos), s, numberPage(pos,
							str, strResult, i) + "", pos, j, strResult));
				}
			}
		}
	}

	private String addBoldText(String str, String s, int i) {
		if (i == -1)
			i = 0;
		try {
			str = str.substring(0, i) + "<b>"
					+ str.substring(i, i + s.length()) + "</b>"
					+ str.substring(i + s.length(), str.length());
		} catch (Exception e) {
			return str;
		}
		return str;
	}

	private int numberPage(int pos, String str, String strResult, int i) {
		int height = 0;
		if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
			height = (int) EpubWebViewClient.heightWeb;
		} else
			height = EpubWebViewClient.totalHeight;
		int page = EpubWebViewClient.arrListTagP[pos] / height + 1;

		try {
			int tong = EpubWebViewClient.arrListHeightTagP[pos];
			int page1 = (EpubWebViewClient.arrListTagP[pos] + tong) / height
					+ 1;
			if (page != page1) {
				String s = String.valueOf(StoreData.bigRound(
						(float) EpubWebViewClient.arrListTagP[pos]
								/ (float) height, 1));
				String l = ((float) EpubWebViewClient.arrListTagP[pos] / (float) height)
						+ "";
				s = "0" + l.substring(l.indexOf("."), l.length());
				int leght1 = (int) ((1 - Float.parseFloat(s)) * height);

				int leght2 = Math.round(((float) i / (float) str.length())
						* (float) tong);
				int leghtNow = leght2 - leght1;
				if (leght2 > leght1) {
					Log.e("DAT", page + ">>>>>>>>>>>>??" + (leghtNow / height));
					switch (leghtNow / height) {
					case 0:
						page = page + 1;
						break;
					case 1:
						page = page + 2;
						break;
					case 2:
						page = page + 3;
						break;
					case 3:
						page = page + 4;
						break;
					case 4:
						page = page + 5;
						break;
					case 5:
						page = page + 6;
						break;
					default:
						page = page + 1;
						break;
					}

				}
			}

		} catch (Exception e) {
		}
		Log.e("DAT", page + ">>>>>>>>>>>>??");
		return page;
	}

	private String menuPage(int pos) {
		try {
			String[] list = getResources().getStringArray(R.array.menu);
			for (int i = EpubWebViewClient.arrMenu.length - 1; i >= 0; i--) {
				if (EpubWebViewClient.arrListTagP[pos] >= EpubWebViewClient.arrMenu[i] - 5) {
					return list[i].toString();
				}
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isSupported() {
		boolean supported;
		if (activity != null) {
			mSensorManager = (SensorManager) activity
					.getSystemService(Context.SENSOR_SERVICE);
			List<Sensor> sensors = mSensorManager
					.getSensorList(Sensor.TYPE_LIGHT);
			//supported = new Boolean(sensors.size() > 0);
			supported = Boolean.FALSE;
		} else {
			supported = Boolean.FALSE;
		}
		return supported;
	}

	public void clearTextSearch() {
		if (isShowSearch) {
			if (mSearch != null)
				webView.removeColorTextSearch(mSearch.getIntP(),
						mSearch.getIntPos(), mSearch.getText());
			isShowSearch = false;
		}
	}

	private void addTabP(int page, int height) {
		for (int i = 0; i < EpubWebViewClient.arrListTagP.length; i++) {
			if (EpubWebViewClient.arrListTagP[i] >= page * height
					&& EpubWebViewClient.arrListTagP[i] < page * height
							+ height) {
				dataBookMark.addBookmark(new BookMark(new Date().getTime(), i,
						0));
				setIconBookmark(webView.getCurrentPage() + 1, true);
				i = EpubWebViewClient.arrListTagP.length;
			} else if (EpubWebViewClient.arrListTagP[i] < page * height
					&& EpubWebViewClient.arrListTagP[i]
							+ EpubWebViewClient.arrListHeightTagP[i] > page
							* height) {
				String s = String.valueOf(StoreData.bigRound(
						(float) EpubWebViewClient.arrListTagP[i]
								/ (float) height, 1));
				String l = ((float) EpubWebViewClient.arrListTagP[i] / (float) height)
						+ "";
				s = "0" + l.substring(l.indexOf("."), l.length());
				int leght1 = (int) ((1 - Float.parseFloat(s)) * height);
				switch (((page * height - EpubWebViewClient.arrListTagP[i]) - leght1)
						/ height) {
				case 0:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1, i)));
					break;
				case 1:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1 + height, i)));
					break;
				case 2:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1 + height * 2, i)));
					break;
				case 3:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1 + height * 3, i)));
					break;
				case 4:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1 + height * 4, i)));
					break;
				case 5:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1 + height * 5, i)));
					break;
				default:
					dataBookMark.addBookmark(new BookMark(new Date().getTime(),
							i, countText(leght1, i)));
					break;
				}

				setIconBookmark(webView.getCurrentPage() + 1, true);
				i = EpubWebViewClient.arrListTagP.length;
			}
		}
	}

	private void removeTabP(int page, int height) {
		listCheckBookmark.clear();
		listCheckBookmark = dataBookMark.getAllBookmark();
		page = page + 1;
		for (int i = 0; i < listCheckBookmark.size(); i++) {

			int lengthP = lenghtTabP(i);
			if (listCheckBookmark.get(i).getNumber() == 311)
				lengthP = 20;
			if (EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
					.getNumber()] >= (page - 1) * height
					&& EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
							.getNumber()] + lengthP < page * height) {
				dataBookMark.deleteBookmark(listCheckBookmark.get(i)
						.getNumber(), listCheckBookmark.get(i).getPage());
				// i = listCheckBookmark.size() + 1;
			} else if (EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
					.getNumber()] < (page - 1) * height
					&& EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
							.getNumber()] + lengthP >= (page - 1) * height
					&& EpubWebViewClient.arrListTagP[listCheckBookmark.get(i)
							.getNumber()] + lengthP < page * height) {
				dataBookMark.deleteBookmark(listCheckBookmark.get(i)
						.getNumber(), listCheckBookmark.get(i).getPage());
				// i = listCheckBookmark.size() + 1;
			}
		}
		setIconBookmark(webView.getCurrentPage() + 1, true);
	}

	private int countText(int count, int id) {
		return Math
				.round(((float) count / (float) EpubWebViewClient.arrListHeightTagP[id])
						* list.get(id));
	}

	private int lenghtTabP(int id) {
		return Math
				.round(((float) (listCheckBookmark.get(id).getPage() + 3) / (float) list
						.get(listCheckBookmark.get(id).getNumber()))
						* (float) EpubWebViewClient.arrListHeightTagP[listCheckBookmark
								.get(id).getNumber()]);
	}

	public void setUpdateAdapter() {
		if (adapter != null) {
			adapter.setSwipe();
			adapter.notifyDatasetChanged();
		}
	}

	private void saveScrollHorizon() {
		int height = 0;
		if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
			height = (int) EpubWebViewClient.heightWeb;
		} else
			height = EpubWebViewClient.totalHeight;
		int page = webView.getCurrentPage();

		for (int i = 0; i < EpubWebViewClient.arrListTagP.length; i++) {

			if (EpubWebViewClient.arrListTagP[i] >= page * height
					&& EpubWebViewClient.arrListTagP[i] < page * height
							+ height) {
				saveTabP = i;
				saveIndex = 0;
				i = EpubWebViewClient.arrListTagP.length;
			} else if (EpubWebViewClient.arrListTagP[i] < page * height
					&& EpubWebViewClient.arrListTagP[i]
							+ EpubWebViewClient.arrListHeightTagP[i] > page
							* height) {
				saveTabP = i;
				String s = String.valueOf(StoreData.bigRound(
						(float) EpubWebViewClient.arrListTagP[i]
								/ (float) height, 1));
				String l = ((float) EpubWebViewClient.arrListTagP[i] / (float) height)
						+ "";
				s = "0" + l.substring(l.indexOf("."), l.length());
				int leght1 = (int) ((1 - Float.parseFloat(s)) * height);
				switch (((page * height - EpubWebViewClient.arrListTagP[i]) - leght1)
						/ height) {
				case 0:
					saveIndex = countText(leght1, i);
					break;
				case 1:
					saveIndex = countText(leght1 + height, i);
					break;
				case 2:
					saveIndex = countText(leght1 + height * 2, i);
					break;
				case 3:
					saveIndex = countText(leght1 + height * 3, i);
					break;
				case 4:
					saveIndex = countText(leght1 + height * 4, i);
					break;
				case 5:
					saveIndex = countText(leght1 + height * 5, i);
					break;
				default:
					saveIndex = countText(leght1, i);
					break;
				}
				i = EpubWebViewClient.arrListTagP.length;
			}
		}
		// Log.e("DAT", "===" + saveTabP + "///" + saveIndex + "//");
	}

	private void saveScrollVertical() {
		int height = 0;
		if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
			height = (int) EpubWebViewClient.heightWeb;
		} else
			height = EpubWebViewClient.totalHeight;
		int page = webView.getTopP();
		if (page < 0)
			page = 0;

		for (int i = 0; i < EpubWebViewClient.arrListTagP.length; i++) {

			if (EpubWebViewClient.arrListTagP[i] >= page
					&& EpubWebViewClient.arrListTagP[i] < page + height) {
				saveTabP = i;
				saveIndex = 0;
				i = EpubWebViewClient.arrListTagP.length;
			} else if (EpubWebViewClient.arrListTagP[i] < page
					&& EpubWebViewClient.arrListTagP[i]
							+ EpubWebViewClient.arrListHeightTagP[i] > page) {
				saveTabP = i;

				saveIndex = Math
						.round(((float) (page - EpubWebViewClient.arrListTagP[i]) / (float) EpubWebViewClient.arrListHeightTagP[i])
								* list.get(i));

				i = EpubWebViewClient.arrListTagP.length;
			}
		}
		// Log.e("DAT", page + ">>>" + saveTabP + "///" + saveIndex + "//"
		// + height);
	}

}
