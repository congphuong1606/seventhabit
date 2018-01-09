package com.discovertodo.phone.android.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.ShowImageActivity;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class MindCardFragment extends BaseFragment {

    private static final int DURATION = 400;
    public HomeFragment parentFragment;
    public View btnFlip, btnBack, layoutTopMindCard;
    private ViewFlipper vf;
    private View mFrontView, mBackView;
    private RelativeLayout card1, card13, card12, card9;
    private LinearLayout card11, card10, card8, card7, card6, card5, card4, card3, card2;
    private ImageView img_card_1_1, img_card_13_1, img_card_13_2, img_card_12_1, img_card_9_1, img_card_2_1, img_card_3_1, img_card_4_1,
            img_card_5_1, img_card_6_1, img_card_7_1, img_card_8_1, img_card_10_1, img_card_11_1, img_card_11_2;
    private ScrollView scroll_1,scroll_1_2,scroll_2,scroll_3,scroll_4,scroll_5,scroll_6,scroll_7,scroll_8,scroll_9,scroll_9_2,scroll_10,scroll_11,scroll_12,scroll_12_2,scroll_13,scroll_13_2;
    boolean[] check = {true, true, true, true};

//    public MindCardFragment(HomeFragment fragment) {
//        parentFragment = fragment;
//        layoutTopMindCard = fragment.layoutTopMindCard;
//    }

    public static MindCardFragment getInstance(HomeFragment fragment){
        MindCardFragment mindCardFragment = new MindCardFragment();
        mindCardFragment.parentFragment = fragment;
        mindCardFragment.layoutTopMindCard = fragment.layoutTopMindCard;
        return mindCardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mind_card_info, container, false);
            setupLayout();
            if (vf.getDisplayedChild() == 0) {

                mFrontView = view.findViewById(R.id.card1_1);
                mBackView = view.findViewById(R.id.card1_2);
                btnFlip.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (check[0]) {
                            flip(mFrontView, mBackView, DURATION);
                            check[0] = false;
                        } else {
                            flip(mBackView, mFrontView, DURATION);
                            check[0] = true;
                        }
                    }
                });
                setImageListener(img_card_1_1, R.drawable.mind_card1_1_1);
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        layoutTopMindCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        layoutTopMindCard.setVisibility(View.GONE);
    }


    public void flip(final View front, final View back, final int duration) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            AnimatorSet set = new AnimatorSet();
            set.playSequentially(
                    ObjectAnimator.ofFloat(front, "rotationY", 90).setDuration(duration / 2),
                    ObjectAnimator.ofInt(front, "visibility", View.GONE).setDuration(0),
                    ObjectAnimator.ofFloat(back, "rotationY", -90).setDuration(0),
                    ObjectAnimator.ofInt(back, "visibility", View.VISIBLE).setDuration(0),
                    ObjectAnimator.ofFloat(back, "rotationY", 0).setDuration(duration / 2)
            );
            set.start();
        } else {
            front.animate().rotationY(90).setDuration(duration / 2).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    front.setVisibility(View.GONE);
                    back.setRotationY(-90);
                    back.setVisibility(View.VISIBLE);
                    back.animate().rotationY(0).setDuration(duration / 2).setListener(null);
                }
            });
        }
    }

    public void setCard() {
        switch (vf.getDisplayedChild()) {
            case 0:
                btnFlip.setVisibility(View.VISIBLE);
                mFrontView = view.findViewById(R.id.card1_1);
                mBackView = view.findViewById(R.id.card1_2);
                btnFlip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check[0]) {
                            flip(mFrontView, mBackView, DURATION);
                            check[0] = false;
                        } else {
                            flip(mBackView, mFrontView, DURATION);
                            check[0] = true;
                        }
                    }
                });
                setImageListener(img_card_1_1, R.drawable.mind_card1_1_1);
                break;
            case 1:
                btnFlip.setVisibility(View.VISIBLE);
                mFrontView = view.findViewById(R.id.card13_1);
                mBackView = view.findViewById(R.id.card13_2);
                btnFlip.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (check[1]) {
                            flip(mFrontView, mBackView, DURATION);
                            check[1] = false;
                        } else {
                            flip(mBackView, mFrontView, DURATION);
                            check[1] = true;
                        }
                    }
                });
                setImageListener(img_card_13_1, R.drawable.mindcard_13_1_a);
                setImageListener(img_card_13_2, R.drawable.mind_card_13_2_1);
                break;
            case 2:
                btnFlip.setVisibility(View.VISIBLE);
                mFrontView =view.findViewById(R.id.card12_1);
                mBackView = view.findViewById(R.id.card12_2);
                btnFlip.setVisibility(View.VISIBLE);
                btnFlip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check[2]) {
                            flip(mFrontView, mBackView, DURATION);
                            check[2] = false;
                        } else {
                            flip(mBackView, mFrontView, DURATION);
                            check[2] = true;
                        }
                    }
                });
                setImageListener(img_card_12_1, R.drawable.mindcard_12_1_a);
                break;
            case 5:
                btnFlip.setVisibility(View.VISIBLE);
                mFrontView = view.findViewById(R.id.card9_1);
                mBackView = view.findViewById(R.id.card9_2);
                btnFlip.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (check[3]) {
                            flip(mFrontView, mBackView, DURATION);
                            check[3] = false;
                        } else {
                            flip(mBackView, mFrontView, DURATION);
                            check[3] = true;
                        }
                    }
                });
                setImageListener(img_card_9_1, R.drawable.mind_card_9_1_1);
                break;
            default:
                btnFlip.setVisibility(View.GONE);
                break;

        }
        Log.i("getDisplayedChild()", vf.getDisplayedChild() + "");
    }

    public void setupLayout() {
        btnFlip = parentFragment.btnFlipCard;
        btnBack = parentFragment.btnBackCard;
        vf = (ViewFlipper) view.findViewById(R.id.view_flipper);
        card1 = (RelativeLayout) view.findViewById(R.id.card1);
        card13 = (RelativeLayout) view.findViewById(R.id.card13);
        card12 = (RelativeLayout) view.findViewById(R.id.card12);
        card11 = (LinearLayout) view.findViewById(R.id.card11);
        card10 = (LinearLayout) view.findViewById(R.id.card10);
        card9 = (RelativeLayout) view.findViewById(R.id.card9);
        card8 = (LinearLayout) view.findViewById(R.id.card8);
        card7 = (LinearLayout) view.findViewById(R.id.card7);
        card6 = (LinearLayout) view.findViewById(R.id.card6);
        card5 = (LinearLayout) view.findViewById(R.id.card5);
        card4 = (LinearLayout) view.findViewById(R.id.card4);
        card3 = (LinearLayout) view.findViewById(R.id.card3);
        card2 = (LinearLayout) view.findViewById(R.id.card2);
        vf.removeAllViews();
        vf.addView(card1);
        vf.addView(card13);
        vf.addView(card12);
        vf.addView(card11);
        vf.addView(card10);
        vf.addView(card9);
        vf.addView(card8);
        vf.addView(card7);
        vf.addView(card6);
        vf.addView(card5);
        vf.addView(card4);
        vf.addView(card3);
        vf.addView(card2);

        img_card_1_1 = (ImageView) view.findViewById(R.id.img_card_1_1);
        img_card_13_1 = (ImageView) view.findViewById(R.id.img_card_13_1);
        img_card_13_2 = (ImageView) view.findViewById(R.id.img_card_13_2);
        img_card_12_1 = (ImageView) view.findViewById(R.id.image12_1_1);
        img_card_9_1 = (ImageView) view.findViewById(R.id.img_card_9_1);
        img_card_2_1 = (ImageView) view.findViewById(R.id.img_card_2_1);
        img_card_3_1 = (ImageView) view.findViewById(R.id.img_card_3_1);
        img_card_4_1 = (ImageView) view.findViewById(R.id.img_card_4_1);
        img_card_5_1 = (ImageView) view.findViewById(R.id.img_card_5_1);
        img_card_6_1 = (ImageView) view.findViewById(R.id.img_card_6_1);
        img_card_7_1 = (ImageView) view.findViewById(R.id.img_card_7_1);
        img_card_8_1 = (ImageView) view.findViewById(R.id.img_card_8_1);
        img_card_10_1 = (ImageView) view.findViewById(R.id.img_card_10_1);
        img_card_11_1 = (ImageView) view.findViewById(R.id.img_card_11_1);
        img_card_11_2 = (ImageView) view.findViewById(R.id.img_card_11_2);

        scroll_1 = (ScrollView)view.findViewById(R.id.scroll_1);
        scroll_2 = (ScrollView)view.findViewById(R.id.scroll_2);
        scroll_3 = (ScrollView)view.findViewById(R.id.scroll_3);
        scroll_4 = (ScrollView)view.findViewById(R.id.scroll_4);
        scroll_5 = (ScrollView)view.findViewById(R.id.scroll_5);
        scroll_6 = (ScrollView)view.findViewById(R.id.scroll_6);
        scroll_7 = (ScrollView)view.findViewById(R.id.scroll_7);
        scroll_8 = (ScrollView)view.findViewById(R.id.scroll_8);
        scroll_9 = (ScrollView)view.findViewById(R.id.scroll_9);
        scroll_10 = (ScrollView)view.findViewById(R.id.scroll_10);
        scroll_11 = (ScrollView)view.findViewById(R.id.scroll_11);
        scroll_12 = (ScrollView)view.findViewById(R.id.scroll_12);
        scroll_13 = (ScrollView)view.findViewById(R.id.scroll_13);
        scroll_13_2 = (ScrollView)view.findViewById(R.id.scroll_13_2);
        scroll_12_2 = (ScrollView)view.findViewById(R.id.scroll_12_2);
        scroll_9_2 = (ScrollView)view.findViewById(R.id.scroll_9_2);
        scroll_1_2 = (ScrollView)view.findViewById(R.id.scroll_1_2);
        setScrollListener(scroll_1);
        setScrollListener(scroll_1_2);
        setScrollListener(scroll_2);
        setScrollListener(scroll_3);
        setScrollListener(scroll_4);
        setScrollListener(scroll_5);
        setScrollListener(scroll_6);
        setScrollListener(scroll_7);
        setScrollListener(scroll_8);
        setScrollListener(scroll_9);
        setScrollListener(scroll_9_2);
        setScrollListener(scroll_10);
        setScrollListener(scroll_11);
        setScrollListener(scroll_12);
        setScrollListener(scroll_12_2);
        setScrollListener(scroll_13);
        setScrollListener(scroll_13_2);

        setImageListener(img_card_2_1, R.drawable.mind_card_2_1_1);
        setImageListener(img_card_3_1, R.drawable.mind_card_3_1_1);
        setImageListener(img_card_4_1, R.drawable.mind_card_4_1_1);
        setImageListener(img_card_5_1, R.drawable.mind_card_5_1_1);
        setImageListener(img_card_6_1, R.drawable.mind_card_6_1_1);
        setImageListener(img_card_7_1, R.drawable.mind_card_7_1_1);
        setImageListener(img_card_8_1, R.drawable.mind_card_8_1_1);
        setImageListener(img_card_10_1, R.drawable.mind_card_10_1_1);
        setImageListener(img_card_11_1, R.drawable.mind_card_11_1_3);
        setImageListener(img_card_11_2, R.drawable.mind_card_11_1_3);
    }

    public void setClickImage(int path) {
        Intent intent = new Intent(getActivity(), ShowImageActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context c) {
            gestureDetector = new GestureDetector(c, new GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 200;
            private static final int SWIPE_VELOCITY_THRESHOLD = 200;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {
                onDoubleClick();
                return super.onDoubleTap(e);
            }


            // Determines the fling velocity and then fires the appropriate swipe event accordingly
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
            if (vf.getDisplayedChild() == 0) {
                return;
            } else {
                vf.setInAnimation(activity, R.anim.in_from_left);
                vf.setOutAnimation(activity, R.anim.out_to_right);
                vf.showNext();
            }
            if (vf.getDisplayedChild() == 1 || vf.getDisplayedChild() == 2 || vf.getDisplayedChild() == 5 || vf.getDisplayedChild() == 0) {
                btnFlip.setVisibility(View.VISIBLE);
                setCard();
            } else {
                btnFlip.setVisibility(View.GONE);
            }
        }

        public void onSwipeLeft() {
            if (vf.getDisplayedChild() == 1) {
                return;
            } else {
                vf.setInAnimation(activity, R.anim.in_from_right);
                vf.setOutAnimation(activity, R.anim.out_to_left);
                vf.showPrevious();
            }
            if (vf.getDisplayedChild() == 0 || vf.getDisplayedChild() == 2 || vf.getDisplayedChild() == 5 || vf.getDisplayedChild() == 1) {
                btnFlip.setVisibility(View.VISIBLE);
                setCard();
            } else {
                btnFlip.setVisibility(View.GONE);
            }
        }

        public void onDoubleClick() {

        }

    }

    public void setImageListener(ImageView img, final int path) {
        img.setSoundEffectsEnabled(false);
        img.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
                setClickImage(path);
            }
        });
    }

    public void setScrollListener(ScrollView scrollview){
        scrollview.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }
        });
    }

}
