package com.discovertodo.phone.android.fragment;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.util.StoreData;

/**
 * Created by Ngoc on 8/10/2015.
 */
public class FinderPurposeResult extends BaseFragment {
    private HomeFragment parentFragment;
    private View layoutTopHomeFinder, btnHomeFinder, btnShareFinder;
    private TextView txtA0, finderPart1, finderPart2;
    private Button btnEditFinder, btnBack;
    private String txtContent = "";
    private String content = "";
    private String strA0, strA1, strA2, strA3, strA4, strA5, strA6, strA7,
            strA8, strA9, strA10, strA11, strA12, strA13, strA14, strA15, strA16;
    private FinderPurposeEditResult finderPurposeEditResult;
    private StoreData storeData;
//    public FinderPurposeResult(HomeFragment fragment) {
//        this.parentFragment = fragment;
//        layoutTopHomeFinder = parentFragment.layoutTopHomeFinder;
//    }

    public static FinderPurposeResult getInstance(HomeFragment homeFragment){
        FinderPurposeResult finderPurposeResult = new FinderPurposeResult();
        finderPurposeResult.parentFragment = homeFragment;
        finderPurposeResult.layoutTopHomeFinder = finderPurposeResult.parentFragment.layoutTopHomeFinder;
        return finderPurposeResult;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.finder_purpose_result, container, false);
            setupLayout();
            setupValue();
            txtContent = txtA0.getText().toString() + "\n\n\n" +
                    getString(R.string.finder_text1) + "\n\n" +
                    finderPart1.getText().toString() + "\n" +
                    getString(R.string.finder_text2) + "\n\n" +
                    finderPart2.getText().toString() + "\n";
            btnShareFinder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFinder(txtContent);
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
                    dialog.setContentView(R.layout.dialog_finder_purpose);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button back = (Button) dialog.findViewById(R.id.back);
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().beginTransaction().replace(R.id.home_content_fragment,FinderPurposeInputFragment.getInstance(parentFragment)).commit();
                            if (storeData.isExists("edt_part1") && storeData.isExists("edt_part2")){
                                storeData.clearValue("edt_part1");
                                storeData.clearValue("edt_part2");
                            }
                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }
            });

            btnEditFinder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
                    String str = txtA0.getText().toString();
                    String part1 = finderPart1.getText().toString() + "\n";
                    String part2 = finderPart2.getText().toString() + "\n";
//                    bundle.putString("STR", str);
//                    bundle.putString("PART1", part1);
//                    bundle.putString("PART2", part2);
                    storeData.setStringValue("STR", str);
                	storeData.setStringValue("edt_part1",part1);
			        storeData.setStringValue("edt_part2",part2);
//                    finderPurposeEditResult = new FinderPurposeEditResult(parentFragment);
                    finderPurposeEditResult = FinderPurposeEditResult.getInstance(parentFragment);
//                    finderPurposeEditResult.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.home_content_fragment, finderPurposeEditResult).addToBackStack(null).commit();
                }
            });

        } else {
            if (finderPurposeEditResult != null) {
                finderPart1.setText(storeData.getStringValue("edt_part1"));
                finderPart2.setText(storeData.getStringValue("edt_part2"));
                content = txtA0.getText().toString() + "\n\n\n" +
                        getString(R.string.finder_text1) + "\n\n" +
                        storeData.getStringValue("edt_part1") + "\n" +
                        getString(R.string.finder_text2) + "\n\n" +
                        storeData.getStringValue("edt_part2") + "\n";
                btnShareFinder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareFinder(content);
                    }
                });

            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        layoutTopHomeFinder.setVisibility(View.VISIBLE);
        btnShareFinder.setVisibility(View.VISIBLE);
        parentFragment.setTitle(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        layoutTopHomeFinder.setVisibility(View.GONE);
        btnShareFinder.setVisibility(View.GONE);
    }

    public void setupLayout() {

        storeData = new StoreData(activity);
        btnHomeFinder = parentFragment.btnHomeFinder;
        btnShareFinder = parentFragment.btnShareFinder;
        btnEditFinder = (Button) view.findViewById(R.id.btnEditFinder);
        btnBack = (Button) view.findViewById(R.id.btnBack);

        txtA0 = (TextView) view.findViewById(R.id.txtFinderResult0);
        finderPart1 = (TextView) view.findViewById(R.id.part1);
        finderPart2 = (TextView) view.findViewById(R.id.part2);
    }

    private void setupValue() {
        boolean check = storeData.getBooleanValue("check");
        if (check) {
            strA0 = storeData.getStringValue("Q0");
            strA1 = storeData.getStringValue("Q1");
            strA2 = storeData.getStringValue("Q2");
            strA3 = storeData.getStringValue("Q3");
            strA4 = storeData.getStringValue("Q4");
            strA5 = storeData.getStringValue("Q5");
            strA6 = storeData.getStringValue("Q6");
            strA7 = storeData.getStringValue("Q7");
            strA8 = storeData.getStringValue("Q8");
            strA9 = storeData.getStringValue("Q9");
            strA10 = storeData.getStringValue("Q10");
            strA11 = storeData.getStringValue("Q11");
            strA12 = storeData.getStringValue("Q12");
            strA13= storeData.getStringValue("Q13");
            strA14 = storeData.getStringValue("Q14");
            strA15= storeData.getStringValue("Q15");
            strA16= storeData.getStringValue("Q16");
        }
        txtA0.setText(getString(R.string.finder_result0, strA0));
        if (storeData.isExists("edt_part1")){
            finderPart1.setText(storeData.getStringValue("edt_part1"));
            finderPart2.setText(storeData.getStringValue("edt_part2"));
        }else {
            finderPart1.setText(getString(R.string.finder_part1, strA1, strA2, strA3, strA4, strA5, strA6, strA7, strA8, strA9));
            finderPart2.setText(getString(R.string.finder_part2, strA10, strA11, strA12, strA13, strA14, strA15, strA16));
        }

    }

    private void shareFinder(String content) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
            intent.setType("message/rfc822");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.menu_purpose_mean));
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Discover-to-Doから共有：\n\n" +
                    content);
            Intent mailer = Intent.createChooser(intent, "Choose an Email client :");
            startActivity(mailer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
