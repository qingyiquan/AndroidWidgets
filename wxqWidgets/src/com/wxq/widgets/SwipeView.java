package com.wxq.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
/**
 * 滑动打开的View
 * @author wuxiaoquan
 *
 */
public class SwipeView extends FrameLayout {
	
	float slop;
	
	//当前允许的滑动方式
	public static final int SWIPE_MODE_NONE = 0;
	public static final int SWIPE_MODE_LEFT = 1;
	public static final int SWIPE_MODE_RIGHT = 2;
	public static final int SWIPE_MODE_BOTH = 3;
	int currentSwipeMode = SWIPE_MODE_BOTH;
	
	//目标view的打开状态
	public static final int STATE_OPEN_NONE = 0;
	public static final int STATE_OPEN_LEFT = 1;
	public static final int STATE_OPEN_RIGHT = 2;
	int currentOpenState = STATE_OPEN_NONE;
	
	public float defaultLeftOffset = 200;
	public float defaultRightOffset = 200;
	
	//frontView 的初始位置
	float frontOriginX = 0;
	//按下时刻 frontView的位置
	float frontStartX = 0 ;

	View frontView;
	View backView;
	
	boolean isFrontViewInited = false;
	
	@SuppressLint("NewApi")
	GestureDetector.SimpleOnGestureListener frontViewOnGestureListener =
			new GestureDetector.SimpleOnGestureListener()
	{
		@Override
		public boolean onDown(MotionEvent e) {
			//按下时刻做frontView记录
			frontStartX = frontView.getX();
			return super.onDown(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 发现了拖动,设置拖动的门限
			float delaX = e2.getRawX() - e1.getRawX();
			
			if ( currentSwipeMode == SWIPE_MODE_RIGHT ){
				//允许往右侧滑动
				if (currentOpenState == STATE_OPEN_NONE) {
					//未打开状态
					if (delaX <= defaultRightOffset && delaX >= 0) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if(delaX > defaultRightOffset){
						generateViewAnim(frontView, frontStartX + defaultRightOffset);
					} else {
						generateViewAnim(frontView, frontStartX);
					}
				} else if (currentOpenState == STATE_OPEN_RIGHT) {
					//允许右滑动并且往右已经打开
					if (delaX <=0 && delaX >= -defaultRightOffset) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if (delaX > 0) {
						generateViewAnim(frontView, frontStartX);
					} else {
						generateViewAnim(frontView, frontStartX - defaultRightOffset);
					}
				}
				
			} else if (currentSwipeMode == SWIPE_MODE_LEFT) {
				//允许往左侧滑动
				if (currentOpenState == STATE_OPEN_NONE) {
					//当前关闭状态
					if (delaX >= -defaultLeftOffset && delaX <= 0) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if(delaX < -defaultLeftOffset){
						generateViewAnim(frontView, frontStartX - defaultLeftOffset);
					} else {
						generateViewAnim(frontView, frontStartX);
					}
				}
				else if( currentOpenState == STATE_OPEN_LEFT ){
					//当前打开状态
					if (delaX >=0 && delaX < defaultLeftOffset) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if (delaX < 0) {
						generateViewAnim(frontView, frontStartX);
					} else {
						generateViewAnim(frontView, frontStartX + defaultLeftOffset);
					}
				}
			} else if (currentSwipeMode == SWIPE_MODE_BOTH) {
				//允许往两侧滑动
				if (currentOpenState == STATE_OPEN_NONE) {
					//当前关闭状态
					if (delaX >= -defaultLeftOffset && delaX <= defaultRightOffset) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if (delaX  < -defaultLeftOffset) {
						generateViewAnim(frontView, frontStartX - defaultLeftOffset);
					} else {
						generateViewAnim(frontView, frontStartX + defaultRightOffset);
					}
				} else if (currentOpenState == STATE_OPEN_LEFT) {
					//往左打开
					if (delaX >=0 && delaX <= (defaultLeftOffset + defaultRightOffset)) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if (delaX < 0) {
						generateViewAnim(frontView, frontStartX);
					} else {
						generateViewAnim(frontView, frontStartX + defaultLeftOffset + defaultRightOffset);
					}
				} else if (currentOpenState == STATE_OPEN_RIGHT) {
					if (delaX <= 0 && delaX >= (-defaultLeftOffset-defaultRightOffset)) {
						generateViewAnim(frontView, frontStartX + delaX);
					} else if (delaX > 0) {
						generateViewAnim(frontView, frontStartX);
					} else {
						generateViewAnim(frontView, frontStartX - defaultLeftOffset - defaultRightOffset);
					}
				}
				
			}
			
			return true;
		}
	};
	
	GestureDetector mGestureDetector = new GestureDetector(getContext(), frontViewOnGestureListener);
	
	View.OnTouchListener frontViewOnTouchListener = new OnTouchListener() {
		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 前面板view的触摸序列
			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
				//当listview滑动的时候会收到cancel
				//处理结束时期的动画
				float frontEndX = frontView.getX();
				float deltaX = frontEndX - frontOriginX;
				if (currentSwipeMode == SWIPE_MODE_RIGHT) {
					if ( deltaX >= defaultRightOffset/2 ) {
						//往右打开
						openFrontRight();
					} else {
						//关闭状态
						closeFront();
					}
				} else if (currentSwipeMode == SWIPE_MODE_LEFT) {
					if ( deltaX <= -defaultLeftOffset/2 ) {
						openFrontLeft();
					} else {
						closeFront();
					}
				} else if (currentSwipeMode == SWIPE_MODE_BOTH) {
					if ( deltaX <= -defaultLeftOffset/2 ) {
						openFrontLeft();
					} else if( deltaX >= defaultRightOffset/2 ){
						//往右打开
						openFrontRight();
					} else {
						closeFront();
					}
				}
			}
			mGestureDetector.onTouchEvent(event);
			return true;
		}
	};
	
	public SwipeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//取得滚动时刻手指悬停的位移
		slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	public void addView(View child, int index, LayoutParams params) {
		// 添加view的时刻
		super.addView(child, index, params);
		if (child.getId() == R.id.frontView ) {
			initFrontAnim();
		}
	}

	public View getFrontView() {
		if (frontView == null) {
			frontView = findViewById(R.id.frontView);
		}
		return frontView;
	}
	
	public View getBackView() {
		if (backView == null) {
			backView = findViewById(R.id.backView);
		}
		return backView;
	}
	
	public void setSwipeMode(int mode) {
		currentSwipeMode = mode;
	}
	
	public int getSwipeMode() {
		return currentSwipeMode;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void initFrontAnim() {
		if (isFrontViewInited) {
			return;
		}
		getFrontView();
		//为frontView添加触摸事件监听器
		if (frontView == null) {
			Log.d("wxq", "frontView = " + frontView);
			return;
		}
		frontOriginX = frontView.getX();
		frontView.setOnTouchListener(frontViewOnTouchListener);
		isFrontViewInited = true;
	}
	
	public void openFrontLeft() {
		generateViewAnim(frontView, frontOriginX - defaultLeftOffset);
		currentOpenState = STATE_OPEN_LEFT;
	}
	public void openFrontRight() {
		//往右打开
		generateViewAnim(frontView, frontOriginX + defaultRightOffset);
		currentOpenState = STATE_OPEN_RIGHT;
	}
	public void closeFront() {
		if (frontView == null) {
			initFrontAnim();
		}
		generateViewAnim(frontView, frontOriginX);
		currentOpenState = STATE_OPEN_NONE;
	}
	
	/**
	 * 产生动画。返回后立即执行动画，禁用父亲视图阻止触摸事件，可以使得左右互动不被打断
	 * @param view 动画目标
	 * @param newX 目标新的X位置
	 */
	@SuppressLint("NewApi")
	public static void generateViewAnim(View view,float newX) {
		
		disallowPrarentInterceptEvent(view);
		ViewPropertyAnimator vpa =  view.animate();
		vpa.x(newX);
		vpa.setDuration(0);
	}
	
	public static void disallowPrarentInterceptEvent(View view) {
		if(view.getParent()!=null)
		{
			view.getParent().requestDisallowInterceptTouchEvent(true); //禁用父亲阻挡孩子的事件。
		}
	}
}
