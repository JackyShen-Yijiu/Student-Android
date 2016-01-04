package cn.sft.infinitescrollviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class CropRoundView extends View {

	private Bitmap bitmap;
	private ShapeDrawable maskDrawable;
	private Paint paint;
	private int maskId;
	private int canvasWidth = 0;
	private int canvasHeight = 0;
	private int lineWidth = 2;
	private int offset = 0;

	public void setLineWidth(int lineWidth){
		this.lineWidth = lineWidth;
		invalidate();
	}
	
	public void setBkground(Bitmap bitmap, int maskId) {
		this.bitmap = bitmap;
		this.maskId = maskId;
		invalidate();
	}

	public void setBkground(Bitmap bitmap, ShapeDrawable maskDrawable) {
		this.bitmap = bitmap;
		this.maskDrawable = maskDrawable;
		invalidate();
	}

	public void setBkground(Bitmap bitmap) {
		setBkground(bitmap, 0);
	}

	public CropRoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		if (bitmap != null) {
			int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
					| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
					| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
					| Canvas.CLIP_TO_LAYER_SAVE_FLAG;
			canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, saveFlags);
			int distance = 2;
			Rect dst = new Rect(distance * offset, distance * offset,
					canvasWidth - distance * offset, canvasHeight - distance
							* offset);
			Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, dst, null);

			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			if (maskId > 0) {
				Bitmap bkground = BitmapFactory.decodeResource(getResources(),
						maskId);
				canvas.drawBitmap(bkground, new Rect(0, 0, bkground.getWidth(),
						bkground.getHeight()), dst, paint);
			} else {
				if (maskDrawable != null) {
					Bitmap maskBitmap = drawableToBitmap(maskDrawable,
							canvasWidth - distance * 2 * offset, canvasHeight
									- distance * 2 * offset);
					canvas.drawBitmap(
							maskBitmap,
							new Rect(0, 0, maskBitmap.getWidth(), maskBitmap
									.getHeight()), dst, paint);
				} else {
					ShapeDrawable circleDrawable = new ShapeDrawable(
							new OvalShape());
					circleDrawable.setBounds(distance * offset, distance
							* offset, canvasWidth - distance * offset,
							canvasHeight - distance * offset);
					Bitmap maskBitmap = drawableToBitmap(circleDrawable,
							canvasWidth - distance * 2 * offset, canvasHeight
									- distance * 2 * offset);
					canvas.drawBitmap(
							maskBitmap,
							new Rect(0, 0, maskBitmap.getWidth(), maskBitmap
									.getHeight()), dst, paint);
				}
			}

			Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setStyle(Paint.Style.STROKE);
			p.setColor(Color.WHITE);
			p.setStrokeWidth(lineWidth);
			canvas.drawCircle(canvasWidth / 2, canvasHeight / 2, canvasWidth
					/ 2 - lineWidth / 2, p);

			canvas.restore();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int w;
		int h;

		if (widthMode == MeasureSpec.EXACTLY) {
			w = widthSize;
		} else {
			int desired = getPaddingLeft() + getPaddingRight();
			desired += getWidth();
			w = Math.max(150, desired);
			if (widthMode == MeasureSpec.AT_MOST) {
				w = Math.min(desired, widthSize);
			}
			w = 0;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			h = heightSize;
		} else {
			int rawWidth = w - getPaddingLeft() - getPaddingRight();
			int desired = (int) (getPaddingTop() + getPaddingBottom() + rawWidth);
			h = Math.max(150, desired);
			if (heightMode == MeasureSpec.AT_MOST) {
				h = Math.min(desired, heightSize);
			}
			h = 0;
		}
		if (w > 0) {
			canvasWidth = w;
		} else {
			canvasWidth = getResources().getDisplayMetrics().widthPixels;
		}
		if (h > 0) {
			canvasHeight = h;
		} else {
			canvasHeight = getResources().getDisplayMetrics().heightPixels;
		}
		setMeasuredDimension(w, h);
	}

	public Bitmap drawableToBitmap(Drawable drawable, int w, int h) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						w,
						h,
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
}
