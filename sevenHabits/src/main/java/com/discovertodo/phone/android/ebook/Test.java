package com.discovertodo.phone.android.ebook;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by edysaputra on 8/5/14.
 */
public class Test {

	Coordinate coordinate = new Coordinate();
	GetCurrentOffset currentOffset = new GetCurrentOffset();
	EventDuration duration = new EventDuration();

	public void canScrollVerticalPager(final EpubWebView webView) {
		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				final int displayHeight = webView.getHeight();
				final int contentRange = webView.getContentHeight();
				final int cSize = contentRange / displayHeight;
				final int y = v.getScrollY();
				final int offset = (y / displayHeight);
				// The page start from
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Long ls = android.os.SystemClock.elapsedRealtime()
							- event.getDownTime(); // Get duration start
					currentOffset.setScrollYPosS(v.getScrollY());
					// Get scroll Y pos when touch down
					currentOffset.setOffset(offset);
					// Get the current offset of the page
					coordinate.setStartY(event.getY());
					// Get Y coordinate
					duration.getStartTime(ls);

					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					Long le = android.os.SystemClock.elapsedRealtime()
							- event.getDownTime();
					duration.getEndTime(le);

					final int yS = currentOffset.getScrollYPosS();
					final int yE = currentOffset.getScrollYPosE();
					final int s = (int) duration.setStartDuration();
					final int e = (int) duration.setEndDuration();
					final int timeDuration = e - s;
					// Get duration touch down and up
					int minScrollMove = displayHeight / 3;
					if (contentRange > v.getHeight()) {
						int startY = (int) coordinate.getStartY();
						int endY = (int) coordinate.getEndY();
						int currOffset = currentOffset.getOffset();
						// check if offset is 0 or first page
						if (currOffset == 0) {
							if (yS < yE && (yE - yS) > minScrollMove) {
								scrollToTop(webView, displayHeight);
								webView.setCurrentPage(currOffset + 1);
								webView.updateSeekBar();
								webView.updateCurrentState();
							} else {
								scrollToTop(webView, 0);
							}

//							if (startY != endY) {
//								if (startY > endY) {
//									if (timeDuration < 180) {
//										scrollToTop(webView, displayHeight);
//									}
//								}
//							}

						}

						// Check for second swipe and go on

						else if (currOffset != cSize && currOffset > 0) {

							if (yS < yE && (yE - yS) > minScrollMove) {
								// Get next page
								webView.setCurrentPage(currOffset+1);
                        		webView.updateSeekBar();
								webView.updateCurrentState();
								scrollToTop(webView,
										(currOffset * displayHeight)
												+ displayHeight);
							}

							else if (yS > yE && (yS - yE) > minScrollMove) {
								// Get previous page
								webView.setCurrentPage(currOffset-1);
                        		webView.updateSeekBar();
                        		webView.updateCurrentState();
								scrollToTop(webView,
										(currOffset * displayHeight)
												- displayHeight);
							}
							else {
								scrollToTop(webView,
										(currOffset * displayHeight));
							}

//							if (startY != endY) {
//								// This check for the gesture if not equal = 0
//								if (startY > endY) {
//									// If START greater thanEND move next
//									if (timeDuration < 180) {
//										// Set for finger swipe fast
//										scrollToTop(webView,
//												(currOffset * displayHeight)
//														+ displayHeight);
//									}
//								}
//								if (startY < endY && currOffset != cSize) {
//									if (timeDuration < 180) {
//										// Set for finger swipe fast
//										scrollToTop(webView,
//												(currOffset * displayHeight)
//														- displayHeight);
//									}
//								}
//							}
						}

						// Check if last page

						else if (currOffset == cSize) {
							if (yS > yE && (yS - yE) > minScrollMove) {
								webView.setCurrentPage(currOffset-1);
                        		webView.updateSeekBar();
                        		webView.updateCurrentState();
								scrollToTop(webView,
										(currOffset * displayHeight)
												- displayHeight);
							} else {
								scrollToTop(webView,
										(currOffset * displayHeight));
							}
//							if (startY != endY) {
//								if (startY < endY) {
//									if (timeDuration < 180) {
//										scrollToTop(webView,
//												(currOffset * displayHeight)
//														- displayHeight);
//									}
//								}
//
//								if (startY > endY) {
//									if (timeDuration < 180) {
//										v.scrollTo(0, (cSize * displayHeight));
//									}
//								}
//
//							}

						}

					}
					return true;
				case MotionEvent.ACTION_MOVE:
					coordinate.setEndY(event.getY());
					currentOffset.setScrollYPosE(v.getScrollY());
					return false;
				}
				return false;
			};
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