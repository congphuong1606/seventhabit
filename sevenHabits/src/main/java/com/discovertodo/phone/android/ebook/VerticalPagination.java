package com.discovertodo.phone.android.ebook;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;

public class VerticalPagination {

	long lasTimeClick = 0;
	long firstTimeClick = 0;

	Coordinate coordinate = new Coordinate();
	GetCurrentOffset currentOffset = new GetCurrentOffset();
	EventDuration duration = new EventDuration();

	public void canScrollVerticalPager(final EpubWebView webView) {
		webView.setVerticalScrollBarEnabled(true);
		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()){
					case MotionEvent.ACTION_DOWN: firstTimeClick = System.currentTimeMillis();break;
					case MotionEvent.ACTION_UP:
						if (firstTimeClick-lasTimeClick< 300){
							webView.setClickWeb();
						}
						lasTimeClick = firstTimeClick;
						break;
				}
				return false;
			}
		});
	}

	public void scrollToTop(EpubWebView v, int yOffset) {
		int x = 0;
		int y = yOffset;
		ObjectAnimator xTranslate = ObjectAnimator.ofInt(v, "scrollX", x);
		ObjectAnimator yTranslate = ObjectAnimator.ofInt(v, "scrollY", y);

		AnimatorSet animators = new AnimatorSet();
		animators.setDuration(200L); // Speed for the content movement
		animators.playTogether(xTranslate, yTranslate);
		animators.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		animators.start();
	}

	private class Coordinate {

		private float startY;
		private float endY;

		public float getStartY() {
			return startY;
		}

		public void setStartY(float startY) {
			this.startY = startY;
		}

		public float getEndY() {
			return endY;
		}

		public void setEndY(float endY) {
			this.endY = endY;
		}
	}

	private class GetCurrentOffset {

		private int offset;
		private int getScrollYStart;
		private int getScrollYEnd;

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getScrollYPosS() {
			return getScrollYStart;
		}

		public int getScrollYPosE() {
			return getScrollYEnd;
		}

		public void setScrollYPosS(int getScrollYStart) {
			this.getScrollYStart = getScrollYStart;
		}

		public void setScrollYPosE(int getScrollYEnd) {
			this.getScrollYEnd = getScrollYEnd;
		}
	}

	private class EventDuration {

		private long timeDurationStart;
		private long timeDurationEnd;

		public long setStartDuration() {
			return timeDurationStart;
		}

		public long setEndDuration() {
			return timeDurationEnd;
		}

		public void getStartTime(long timeDurationStart) {
			this.timeDurationStart = timeDurationStart;
		}

		public void getEndTime(long timeDurationEnd) {
			this.timeDurationEnd = timeDurationEnd;
		}

	}
}