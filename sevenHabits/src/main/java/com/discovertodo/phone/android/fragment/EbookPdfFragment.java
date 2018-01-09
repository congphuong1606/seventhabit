package com.discovertodo.phone.android.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.artifex.mupdf.viewer.MuPDFCore;
import com.artifex.mupdf.viewer.OutlineActivity;
import com.artifex.mupdf.viewer.PageAdapter;
import com.artifex.mupdf.viewer.ReaderView;
import com.artifex.mupdf.viewer.SearchTask;
import com.artifex.mupdf.viewer.SearchTaskResult;
import com.discovertodo.phone.android.activity.MainActivity;
import com.discovertodo.phone.android.data.MuPdfData;
import com.discovertodo.phone.android.ebook.EpubWebView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.discovertodo.phone.android.R;

import com.discovertodo.phone.android.util.StoreData;
import com.shockwave.pdfium.PdfDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.discovertodo.phone.android.fragment.EbookFragment.isNight;
import static com.discovertodo.phone.android.global.DialogLoading.setTitle;

@SuppressLint({"ResourceAsColor", "UseValueOf"})
public class EbookPdfFragment extends BaseFragment implements SensorEventListener {

    private EbookPdfFragment.TopBarMode mTopBarMode = EbookPdfFragment.TopBarMode.Main;
    private AlertDialog.Builder mAlertBuilder;
    private boolean mLinkHighlight;
    private ArrayList<OutlineActivity.Item> mFlatOutline;
    private final int OUTLINE_REQUEST = 0;

    enum TopBarMode {Main, Search, More}

    private static final String TAG = EbookPdfFragment.class.getSimpleName();
    public static final String SAMPLE_FILE = "android_tutorial.pdf";
    Integer pageNumber = 0;
    String pdfFileName;
    private StoreData data;
    private Sensor mPressure;
    private int selectLight = 1;
    private SensorManager mSensorManager;
    public static int type_light = 1;

    private Dialog dialogMenu;
    private View layoutBarTop, btnSetting, mOutlineButton,
            mSearchButton, btnTextSmall, btnTextHuge, btnBgWhite, btnBgSepia, btnBgNight;
    private LinearLayout layoutPopup, llChangeFont;
    private SeekBar seekBarPage, seekBarBrightness;
    private TextView txtPopupWhite;
    private TextView txtPopupSepia;
    private TextView txtPopupNight;
    private ImageView mtpopupImage;
    private Switch mSwitchNight;
    private TextView tvTitleFont;
    private TextView tvTitleScroll;
    private TextView tvTitleOutNight;
    View v;
    private TextView btnBack;
    private TextView txtHome;
    private TextView tvNumberPage;
    private boolean isCheckBookmark = false;
    private WindowManager.LayoutParams layoutParams;
    private int curBrightnessValue;
    private TextView tvNameFont;
    private int mProgress;

    ImageView mLinkButton;
    private MuPDFCore core;
    private ReaderView mDocView;
    private ViewAnimator mTopBarSwitcher;
    private EditText mSearchText;
    private ImageButton mSearchBack;
    private ImageButton mSearchFwd;
    private ImageButton mSearchClose;
    private boolean mButtonsVisible;


    public EbookPdfFragment() {

    }

    public static EbookPdfFragment getInstance(HomeFragment fragment) {
        EbookPdfFragment ebookPdfFragment = new EbookPdfFragment();
        ebookPdfFragment.layoutBarTop = fragment.layoutEBookBarTop;
        return ebookPdfFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        layoutBarTop.setVisibility(View.VISIBLE);
        if (data.getBooleanValue("isNight")) {
            try {
                mSensorManager.registerListener(EbookPdfFragment.this, mPressure,
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_ebook_pdf, container, false);
            data = new StoreData(activity);
            if (data.getIntValue("typelight") != 0)
                type_light = data.getIntValue("typelight");

            setupLayout(v);
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
        getCore(savedInstanceState);


        mSensorManager = (SensorManager) getActivity().getSystemService(
                Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        createUi(savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getCore(Bundle savedInstanceState) {
        mAlertBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());

        if (core == null) {
            if (savedInstanceState != null && savedInstanceState.containsKey("FileName")) {
                mFileName = savedInstanceState.getString("FileName");
            }
        }
        if (core == null) {

            byte buffer[] = null;

            Uri uri = MuPdfData.getUriFromFileCopy(getActivity());
            System.out.println("URI to open is: " + uri);
            if (uri.getScheme().equals("file")) {
                String path = uri.getPath();
                core = MuPdfData.openFile(core, path);
            } else {
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(uri);
                    int len;
                    ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
                    byte[] data = new byte[16384];
                    while ((len = is.read(data, 0, data.length)) != -1) {
                        bufferStream.write(data, 0, len);
                    }
                    bufferStream.flush();
                    buffer = bufferStream.toByteArray();
                    is.close();
                } catch (IOException e) {
                    String reason = e.toString();
                    Resources res = getResources();
                    AlertDialog alert = mAlertBuilder.create();
                    setTitle(String.format(Locale.ROOT, res.getString(com.artifex.mupdf.viewer.R.string.cannot_open_document_Reason), reason));
                    alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(com.artifex.mupdf.viewer.R.string.dismiss),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            });
                    alert.show();

                }
                core = MuPdfData.openBuffer(core, buffer, getActivity().getIntent().getType());
            }
            SearchTaskResult.set(null);


            if (core != null && core.countPages() == 0) {
                core = null;
            }
        }
        if (core == null) {
            AlertDialog alert = mAlertBuilder.create();
            alert.setTitle(com.artifex.mupdf.viewer.R.string.cannot_open_document);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(com.artifex.mupdf.viewer.R.string.dismiss),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    getActivity().finish();
                }
            });
            alert.show();
        }
    }

    private int mPageSliderRes;
    private SearchTask mSearchTask;
    private String mFileName = "android_tutorial.pdf";


    private void createUi(Bundle savedInstanceState) {
        if (core == null)
            return;

        // Now create the UI.
        // First create the document view
        mDocView = new ReaderView(getActivity().getApplicationContext()) {
            @Override
            protected void onMoveToChild(int i) {
                if (core == null)
                    return;

                tvNumberPage.setText(String.format(Locale.ROOT, "%d / %d", i + 1, core.countPages()));
                seekBarPage.setMax((core.countPages() - 1) * mPageSliderRes);
                seekBarPage.setProgress(i * mPageSliderRes);
                super.onMoveToChild(i);
            }

            @Override
            protected void onTapMainDocArea() {
                if (!mButtonsVisible) {
                    showButtons();
                } else {
                    if (mTopBarMode == EbookPdfFragment.TopBarMode.Main)
                        hideButtons();
                }
            }

            @Override
            protected void onDocMotion() {
                hideButtons();
            }
        };
        mDocView.setAdapter(new PageAdapter(getActivity().getApplicationContext(), core));

        mSearchTask = new SearchTask(getContext(), core) {
            @Override
            protected void onTextFound(SearchTaskResult result) {
                SearchTaskResult.set(result);
                // Ask the ReaderView to move to the resulting page
                mDocView.setDisplayedViewIndex(result.pageNumber);
                // Make the ReaderView act on the change to SearchTaskResult
                // via overridden onChildSetup method.
                mDocView.resetupChildren();
            }
        };
        int smax = Math.max(core.countPages() - 1, 1);
        mPageSliderRes = ((10 + smax - 1) / smax) * 2;

        seekBarPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDocView.pushHistory();
                mDocView.setDisplayedViewIndex((seekBar.getProgress() + mPageSliderRes / 2) / mPageSliderRes);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                updatePageNumView((progress + mPageSliderRes / 2) / mPageSliderRes);
            }
        });

        // Activate the search-preparing button
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchModeOn();
            }
        });

        mSearchClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchModeOff();
            }
        });

        // Search invoking buttons are disabled while there is no text specified
        mSearchBack.setEnabled(false);
        mSearchFwd.setEnabled(false);
        mSearchBack.setColorFilter(Color.argb(255, 128, 128, 128));
        mSearchFwd.setColorFilter(Color.argb(255, 128, 128, 128));

        // React to interaction with the text widget
        mSearchText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean haveText = s.toString().length() > 0;
                setButtonEnabled(mSearchBack, haveText);
                setButtonEnabled(mSearchFwd, haveText);

                // Remove any previous search results
                if (SearchTaskResult.get() != null && !mSearchText.getText().toString().equals(SearchTaskResult.get().txt)) {
                    SearchTaskResult.set(null);
                    mDocView.resetupChildren();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        //React to Done button on keyboard
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    search(1);
                return false;
            }
        });

        mSearchText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                    search(1);
                return false;
            }
        });

        // Activate search invoking buttons
        mSearchBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search(-1);
            }
        });
        mSearchFwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search(1);
            }
        });

        mLinkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLinkHighlight(!mLinkHighlight);
            }
        });

        if (core.hasOutline()) {
            mOutlineButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mFlatOutline == null)
                        mFlatOutline = core.getOutline();
                    if (mFlatOutline != null) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), OutlineActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("POSITION", mDocView.getDisplayedViewIndex());
                        bundle.putSerializable("OUTLINE", mFlatOutline);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, OUTLINE_REQUEST);
                    }
                }
            });
        } else {
            mOutlineButton.setVisibility(View.GONE);
        }

        // Reenstate last state if it was recorded
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));

        if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
            showButtons();

        if (savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false))
            searchModeOn();

        // Stick the document view and the buttons overlay into a parent view
        RelativeLayout layout = new RelativeLayout(getActivity().getApplicationContext());
        layout.setBackgroundColor(Color.DKGRAY);
        layout.addView(mDocView);
        layout.addView(v);
        getActivity().setContentView(layout);


    }

    private void searchModeOn() {
        if (mTopBarMode != EbookPdfFragment.TopBarMode.Search) {
            mTopBarMode = EbookPdfFragment.TopBarMode.Search;
            //Focus on EditTextWidget
            mSearchText.requestFocus();
            showKeyboard();
            mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        }
    }

    private void searchModeOff() {
        if (mTopBarMode == EbookPdfFragment.TopBarMode.Search) {
            mTopBarMode = EbookPdfFragment.TopBarMode.Main;
            hideKeyboard();
            mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
            SearchTaskResult.set(null);
            // Make the ReaderView act on the change to mSearchTaskResult
            // via overridden onChildSetup method.
            mDocView.resetupChildren();
        }
    }

    private void updatePageNumView(int index) {
        if (core == null)
            return;
        tvNumberPage.setText(String.format(Locale.ROOT, "%d / %d", index + 1, core.countPages()));
    }

    private void showButtons() {
        if (core == null)
            return;
        if (!mButtonsVisible) {
            mButtonsVisible = true;
            // Update page number text and slider
            int index = mDocView.getDisplayedViewIndex();
            updatePageNumView(index);
            seekBarPage.setMax((core.countPages() - 1) * mPageSliderRes);
            seekBarPage.setProgress(index * mPageSliderRes);
            if (mTopBarMode == EbookPdfFragment.TopBarMode.Search) {
                mSearchText.requestFocus();
                showKeyboard();
            }

            Animation anim = new TranslateAnimation(0, 0, -mTopBarSwitcher.getHeight(), 0);
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    mTopBarSwitcher.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                }
            });
            mTopBarSwitcher.startAnimation(anim);

            anim = new TranslateAnimation(0, 0, seekBarPage.getHeight(), 0);
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    seekBarPage.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    tvNumberPage.setVisibility(View.VISIBLE);
                }
            });
            seekBarPage.startAnimation(anim);
        }
    }

    private void hideButtons() {
        if (mButtonsVisible) {
            mButtonsVisible = false;
            hideKeyboard();

            Animation anim = new TranslateAnimation(0, 0, 0, -mTopBarSwitcher.getHeight());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    mTopBarSwitcher.setVisibility(View.INVISIBLE);
                }
            });
            mTopBarSwitcher.startAnimation(anim);

            anim = new TranslateAnimation(0, 0, 0, seekBarPage.getHeight());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    tvNumberPage.setVisibility(View.INVISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    seekBarPage.setVisibility(View.INVISIBLE);
                }
            });
            seekBarPage.startAnimation(anim);
        }
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(mSearchText, 0);
    }

    private void setButtonEnabled(ImageButton button, boolean enabled) {
        button.setEnabled(enabled);
        button.setColorFilter(enabled ? Color.argb(255, 255, 255, 255) : Color.argb(255, 128, 128, 128));
    }

    private void setLinkHighlight(boolean highlight) {
        mLinkHighlight = highlight;
        // LINK_COLOR tint
        mLinkButton.setColorFilter(highlight ? Color.argb(0xFF, 0x00, 0x66, 0xCC) : Color.argb(0xFF, 255, 255, 255));
        // Inform pages of the change.
        mDocView.setLinksEnabled(highlight);
    }

    private void setupLayout(View v) {
        mSearchBack = (ImageButton) v.findViewById(R.id.btn_searchBack);
        mSearchFwd = (ImageButton) v.findViewById(R.id.btn_searchForward);
        mSearchClose = (ImageButton) v.findViewById(R.id.btn_search_close);
        mSearchText = (EditText) view.findViewById(R.id.edt_search_text);
        btnBack = (TextView) v.findViewById(R.id.btn_back);
        txtHome = (TextView) v.findViewById(R.id.btn_menu_ebook);
        seekBarPage = (SeekBar) v.findViewById(R.id.sb_hori);
        tvNumberPage = (TextView) v.findViewById(R.id.tv_numberpage);
        mTopBarSwitcher.setVisibility(View.INVISIBLE);
        seekBarPage.setVisibility(View.INVISIBLE);
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }

    private void search(int direction) {
        hideKeyboard();
        int displayPage = mDocView.getDisplayedViewIndex();
        SearchTaskResult r = SearchTaskResult.get();
        int searchPage = r != null ? r.pageNumber : -1;
        mSearchTask.go(mSearchText.getText().toString(), direction, displayPage, searchPage);
    }


    private void setupPopupMenu() {
        dialogMenu = new Dialog(activity);
        dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMenu.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogMenu.setContentView(R.layout.popup_ebook_menu);
        dialogMenu.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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

        mSwitchNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                isNight = isChecked;
                data.setBooleanValue("isNight", isChecked);
                if (isNight) {
                    try {
                        mSensorManager.registerListener(EbookPdfFragment.this,
                                mPressure, SensorManager.SENSOR_DELAY_NORMAL);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        mSensorManager.unregisterListener(EbookPdfFragment.this);
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
        mSwitchScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

//                EpubWebView.isScroll = isChecked;
//                data.setBooleanValue("Scroll",isChecked);
//                ischeckScroll = true;
//                ProgressHUD.show(activity, "", false);
//                if (isChecked) {
//                    saveScrollHorizon();
//                    seekBarPage.setVisibility(View.GONE);
//                    llScrollVertical.setVisibility(View.VISIBLE);
//                    webView.scrollVertical();
//                    webView.updateView(new EpubWebView.Callback() {
//                        @Override
//                        public void update() {
//                            webView.goToPage(mProgress);
//                        }
//                    });
//                    setupListener();
//                    webView.loadDataWithBaseURL(
//                            "file://"
//                                    + Environment.getExternalStorageDirectory()
//                                    + "/SevenHabitsBooks/", htmlEpub,
//                            "text/html", "utf-8", null);
//                } else {
//                    saveScrollVertical();
//                    llScrollVertical.setVisibility(View.GONE);
//                    seekBarPage.setVisibility(View.VISIBLE);
//                    webView.scrollHorizontal();
//                    setupListener();
//                    webView.loadDataWithBaseURL(
//                            "file://"
//                                    + Environment.getExternalStorageDirectory()
//                                    + "/SevenHabitsBooks/", htmlEpub,
//                            "text/html", "utf-8", null);
//                }
            }
        });
//        btnTextSmall.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                long temp = System.currentTimeMillis();
//                if ((temp - flagTimeTextSize) > 1000) {
//                    flagTimeTextSize = temp;
//                    if (!webView.setTextSizeReduction()) {
//                        btnTextSmall.setEnabled(false);
//                        btnTextHuge.setEnabled(true);
//                    } else {
//                        btnTextSmall.setEnabled(true);
//                        btnTextHuge.setEnabled(true);
//                    }
//                }
//            }
//        });
//        btnTextHuge.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                long temp = System.currentTimeMillis();
//                if ((temp - flagTimeTextSize) > 1000) {
//                    flagTimeTextSize = temp;
//                    if (!webView.setTextSizeIncrease()) {
//                        btnTextSmall.setEnabled(true);
//                        btnTextHuge.setEnabled(false);
//                    } else {
//                        btnTextSmall.setEnabled(true);
//                        btnTextHuge.setEnabled(true);
//                    }
//                }
//            }
//        });
        btnBgWhite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                typeLightWhite();
                selectLight = 1;
            }
        });
        btnBgSepia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                typeLightSepia();
                selectLight = 2;
            }
        });
        btnBgNight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                typeLightNight();
                selectLight = 3;
            }
        });
        seekBarBrightness
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onProgressChanged(SeekBar seekBar, int position, boolean arg2) {
                        if (position < 1)
                            position = 1;
                        seekBarBrightness.setProgress(position);
                        setBrightness(position);
                    }
                });
        seekBarBrightness.setProgress(curBrightnessValue);
        setThemSelect(0);
        llChangeFont.setOnClickListener(new View.OnClickListener() {

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

    private void toat(String aa) {
        Toast.makeText(getActivity(), aa, Toast.LENGTH_LONG).show();
    }

    private void setupPopupChangeFont() {
        toat("setupPopupChangeFont");
    }

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

    private void setBrightness(float position) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            android.provider.Settings.System.putInt(activity.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    (int) position * 255 / 20);
            layoutParams.screenBrightness = position / 20f;
            getActivity().getWindow().setAttributes(layoutParams);
        }
    }

    private boolean isSupported() {
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
//        if (isShowSearch) {
//            if (mSearch != null)
//                webView.removeColorTextSearch(mSearch.getIntP(),
//                        mSearch.getIntPos(), mSearch.getText());
//            isShowSearch = false;
//        }
    }

    private void onMenu() {

        btnBack.setVisibility(View.VISIBLE);
        mOutlineButton.setVisibility(View.GONE);
        mSearchButton.setVisibility(View.GONE);
        mLinkButton.setVisibility(View.GONE);
        btnSetting.setVisibility(View.GONE);
    }

    private void offMenu() {
        btnBack.setVisibility(View.GONE);
        mOutlineButton.setVisibility(View.VISIBLE);
        mSearchButton.setVisibility(View.VISIBLE);
        mLinkButton.setVisibility(View.VISIBLE);
        btnSetting.setVisibility(View.VISIBLE);
    }

    private void onClick() {
        mOutlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenu();
//                if (mSwitch.getIsOn()) {
//                    loadTabMenu();
//                } else {
//                    loadTabBookMark();
//                }
                toat("click btnmenu");
            }
        });

//        mSwitch.setIsOn(true, new CallClick() {
//            @Override
//            public void clickRight() {
//                loadTabBookMark();
//            }
//
//            @Override
//            public void clickLeft() {
//                loadTabMenu();
//            }
//        });
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                offMenu();
//                setIconBookmark(webView.getCurrentPage() + 1, true);
//            }
//        });
//        mLinkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int height = 0;
//                if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
//                    height = (int) EpubWebViewClient.heightWeb;
//                } else
//                    height = EpubWebViewClient.totalHeight;
//                if (isCheckBookmark) {
//                    removeTabP(webView.getCurrentPage(), height);
//                } else {
//                    addTabP(webView.getCurrentPage(), height);
//
//                }
//            }
//        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogSearch();
            }
        });
    }

    private void showDialogSearch() {
        toat("showDialogSearch");
    }

    private void setupListener() {
        btnSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup();
            }
        });

    }


    private void showPopup() {
        dialogMenu.show();
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

    }


    private void typeLightWhite() {
        type_light = 1;
        setBackgroundFull(R.color.white, R.drawable.bg_popup_while);
//        setSeekBarColor(R.color.bluex);
//        webView.changeTextColorToBlack();

        ((ImageView) btnSetting)
                .setImageResource(R.drawable.ic_change_web_blue);
        mSearchButton.setBackgroundResource(R.drawable.seach);
        mOutlineButton.setBackgroundResource(R.drawable.menu0);
        if (isCheckBookmark)
            mLinkButton.setBackgroundResource(R.drawable.ic_bookmark_del);
        else
            mLinkButton.setBackgroundResource(R.drawable.ic_bookmark);

//        llTab.setBackgroundResource(R.color.white);
//        llScrollVertical.setBackgroundColor(Color.TRANSPARENT);
//
//        mSwitch.setColor(type_light);
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
//        setSeekBarColor(R.color.brown);
//        webView.changeTextColorToBlack();
        try {
            mtpopupImage.setImageResource(R.drawable.ic_mtpopup_sepia);
            tvNameFont.setTextColor(Color.BLACK);
            tvTitleFont.setTextColor(Color.BLACK);
            tvTitleScroll.setTextColor(Color.BLACK);
            tvTitleOutNight.setTextColor(Color.BLACK);
            setThemSelect(1);
        } catch (Exception e) {
        }

        ((ImageView) btnSetting)
                .setImageResource(R.drawable.ic_change_web_brown);
        mSearchButton.setBackgroundResource(R.drawable.seach1);
        mOutlineButton.setBackgroundResource(R.drawable.menu1);
        if (isCheckBookmark)
            mLinkButton.setBackgroundResource(R.drawable.ic_bookmark_del);
        else
            mLinkButton.setBackgroundResource(R.drawable.ic_bookmark1);
//        llTab.setBackgroundResource(R.color.sepia);
//        llScrollVertical.setBackgroundColor(Color.TRANSPARENT);
//        mSwitch.setColor(type_light);
    }

    private void setBackgroundFull(int color, int bgLayout) {
        try {
            layoutPopup.setBackgroundResource(bgLayout);
        } catch (Exception e) {
        }
//        pdfView.setBackgroundColor(getResources().getColor(color));
//        layoutBarTop.setBackgroundColor(getResources().getColor(color));
//        ((View) seekBarPage.getParent().getParent())
//                .setBackgroundColor(getResources().getColor(color));

//        ((View) seekbarVertical.getParent().getParent())
//                .setBackgroundColor(getResources().getColor(color));
    }

    private void typeLightNight() {
        type_light = 3;
        setBackgroundFull(R.color.night, R.drawable.bg_popup_night);
//        setSeekBarColor(R.color.gray_3);
//        webView.changeTextColorToGray();
//        ((ImageView) btnSettingWebview)
//                .setImageResource(R.drawable.ic_change_web_gray);
        mSearchButton.setBackgroundResource(R.drawable.seach2);
        mOutlineButton.setBackgroundResource(R.drawable.menu2);
        if (isCheckBookmark)
            mLinkButton.setBackgroundResource(R.drawable.ic_bookmark_del);
        else
            mLinkButton.setBackgroundResource(R.drawable.ic_bookmark2);
//        llTab.setBackgroundResource(R.color.night);
//        llScrollVertical.setBackgroundColor(Color.TRANSPARENT);
//
//        mSwitch.setColor(type_light);
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


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mSearchTask != null)
            mSearchTask.stop();
        if (mFileName != null && mDocView != null) {
            SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
            edit.apply();
        }
    }
}
