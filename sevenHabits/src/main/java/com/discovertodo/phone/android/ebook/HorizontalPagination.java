package com.discovertodo.phone.android.ebook;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class HorizontalPagination {

	private int TIME_DURATION = 150;

	private Coordinate coordinate = new Coordinate();
	private GetCurrentOffset currentOffset = new GetCurrentOffset();
	private EventDuration duration = new EventDuration();

	private float eventDownX, eventUpX, eventUpXOld;
	private long eventDownTime, eventUpTime, eventDoubleTapTimeFirst,
			eventDoubleTapTimeSecond;

	@SuppressLint("ClickableViewAccessibility")
	public void canScrollHorizontalPager(final EpubWebView webView) {
		if (!EpubWebView.isScroll) {
			webView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (!EpubWebView.isScroll) {
						final int displayWidth = webView.getWidth();
						final int cSize = webView.getTotalPageCount();
						final int contentRange = displayWidth * cSize;
						final int offset = (v.getScrollX() / displayWidth);

						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							eventDownTime = System.currentTimeMillis();
							eventDownX = event.getX();
							Long ls = android.os.SystemClock.elapsedRealtime()
									- event.getDownTime();
							currentOffset.setScrollXPosS(v.getScrollX());
							currentOffset.setOffset(offset);
							coordinate.setStartX(event.getX());
							duration.setStartTime(ls);

							return false;

						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_UP:
							eventUpTime = System.currentTimeMillis();
							eventUpX = event.getX();
							float tempX = Math.abs(eventDownX - eventUpX);
							float tempTime = eventUpTime - eventDownTime;
							if (tempX < 30 && tempTime < 150) {
								eventDoubleTapTimeSecond = System
										.currentTimeMillis();
								if ((eventDoubleTapTimeSecond - eventDoubleTapTimeFirst) < 250
										&& Math.abs(eventUpXOld - eventUpX) < 30) {
									webView.setClickWeb();
								} else {
									eventDoubleTapTimeFirst = eventDoubleTapTimeSecond;
									eventUpXOld = eventUpX;
								}
								return true;
							}
							Long le = android.os.SystemClock.elapsedRealtime()
									- event.getDownTime();
							duration.setEndTime(le);

							final int xS = currentOffset.getScrollXPosS();
							final int xE = currentOffset.getScrollXPosE();

							int minScrollMove = displayWidth / 16;

							if (contentRange > v.getWidth()) {
								int currOffset = currentOffset.getOffset();
								// check if offset is 0 or first page
								if (currOffset == 0) {
									if (xS < xE && (xE - xS) > minScrollMove) {
										scrollToLeft(webView, displayWidth);
										webView.setCurrentPage(currOffset + 1);
										webView.updateSeekBar();
										webView.updateCurrentState();
									} else {
										scrollToLeft(webView, 0); // Reset to 0
									}
								}
								// Check for second swipe and go on
								else if (currOffset != cSize && currOffset > 0) {
									if (xS < xE && (xE - xS) > minScrollMove) {
										webView.setCurrentPage(currOffset + 1);
										webView.updateSeekBar();
										webView.updateCurrentState();
										scrollToLeft(webView,
												(currOffset * displayWidth)
														+ displayWidth);
									} else if (xS > xE
											&& (xS - xE) > minScrollMove) {
										webView.setCurrentPage(currOffset - 1);
										webView.updateSeekBar();
										webView.updateCurrentState();
										scrollToLeft(webView,
												(currOffset * displayWidth)
														- displayWidth);
									} else {
										scrollToLeft(webView,
												(currOffset * displayWidth));
									}

								}

								// Check if last page
								else if (currOffset == cSize) {
									if (xS > xE && (xS - xE) > minScrollMove) {
										webView.setCurrentPage(currOffset - 1);
										webView.updateSeekBar();
										webView.updateCurrentState();
										scrollToLeft(webView,
												(currOffset * displayWidth)
														- displayWidth);
									} else {
										scrollToLeft(webView,
												(currOffset * displayWidth));
									}
								}
							}

							return true;
						case MotionEvent.ACTION_MOVE:
							coordinate.setEndX(event.getX());
							currentOffset.setScrollXPosE(v.getScrollX());
							return false;
						default:
							return true;
						}

					} else
						return false;
				};
			});
		} else {
			webView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					return false;
				}
			});
		}
	}

	public void scrollToLeft(EpubWebView v, int xOffset) {
		int x = xOffset;
		int y = 0;
		ObjectAnimator xTranslate = ObjectAnimator.ofInt(v, "scrollX", x);
		ObjectAnimator yTranslate = ObjectAnimator.ofInt(v, "scrollY", y);

		AnimatorSet animators = new AnimatorSet();
		animators.setDuration(TIME_DURATION); // Speed for the content movement
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
		private float startX;
		private float endX;

		public float getStartX() {
			return startX;
		}

		public void setStartX(float startX) {
			this.startX = startX;
		}

		public float getEndX() {
			return endX;
		}

		public void setEndX(float endX) {
			this.endX = endX;
		}
	}

	private class GetCurrentOffset {
		private int offset;
		private int scrollXStart;
		private int scrollXEnd;

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getScrollXPosS() {
			return scrollXStart;
		}

		public void setScrollXPosS(int getScrollXStart) {
			this.scrollXStart = getScrollXStart;
		}

		public int getScrollXPosE() {
			return scrollXEnd;
		}

		public void setScrollXPosE(int getScrollXEnd) {
			this.scrollXEnd = getScrollXEnd;
		}
	}

	private class EventDuration {
		private long timeDurationStart;
		private long timeDurationEnd;

		public long getStartDuration() {
			return timeDurationStart;
		}

		public long getEndDuration() {
			return timeDurationEnd;
		}

		public void setStartTime(long timeDurationStart) {
			this.timeDurationStart = timeDurationStart;
		}

		public void setEndTime(long timeDurationEnd) {
			this.timeDurationEnd = timeDurationEnd;
		}
	}
}
