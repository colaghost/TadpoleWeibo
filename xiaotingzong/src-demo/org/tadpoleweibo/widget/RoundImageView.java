package org.tadpoleweibo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import org.tadpole.R.styleable;

public class RoundImageView extends AsyncImageView
{
  private Paint paint;
  private Paint paint2;
  private int roundHeight = 5;
  private int roundWidth = 5;

  public RoundImageView(Context paramContext)
  {
    super(paramContext);
    init(paramContext, null);
  }

  public RoundImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }

  public RoundImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }

  private void drawLiftDown(Canvas paramCanvas)
  {
    Path localPath = new Path();
    localPath.moveTo(0.0F, getHeight() - this.roundHeight);
    localPath.lineTo(0.0F, getHeight());
    localPath.lineTo(this.roundWidth, getHeight());
    localPath.arcTo(new RectF(0.0F, getHeight() - 2 * this.roundHeight, 0 + 2 * this.roundWidth, getWidth()), 90.0F, 90.0F);
    localPath.close();
    paramCanvas.drawPath(localPath, this.paint);
  }

  private void drawLiftUp(Canvas paramCanvas)
  {
    Path localPath = new Path();
    localPath.moveTo(0.0F, this.roundHeight);
    localPath.lineTo(0.0F, 0.0F);
    localPath.lineTo(this.roundWidth, 0.0F);
    localPath.arcTo(new RectF(0.0F, 0.0F, 2 * this.roundWidth, 2 * this.roundHeight), -90.0F, -90.0F);
    localPath.close();
    paramCanvas.drawPath(localPath, this.paint);
  }

  private void drawRightDown(Canvas paramCanvas)
  {
    Path localPath = new Path();
    localPath.moveTo(getWidth() - this.roundWidth, getHeight());
    localPath.lineTo(getWidth(), getHeight());
    localPath.lineTo(getWidth(), getHeight() - this.roundHeight);
    localPath.arcTo(new RectF(getWidth() - 2 * this.roundWidth, getHeight() - 2 * this.roundHeight, getWidth(), getHeight()), 0.0F, 90.0F);
    localPath.close();
    paramCanvas.drawPath(localPath, this.paint);
  }

  private void drawRightUp(Canvas paramCanvas)
  {
    Path localPath = new Path();
    localPath.moveTo(getWidth(), this.roundHeight);
    localPath.lineTo(getWidth(), 0.0F);
    localPath.lineTo(getWidth() - this.roundWidth, 0.0F);
    localPath.arcTo(new RectF(getWidth() - 2 * this.roundWidth, 0.0F, getWidth(), 0 + 2 * this.roundHeight), -90.0F, 90.0F);
    localPath.close();
    paramCanvas.drawPath(localPath, this.paint);
  }

  private void init(Context paramContext, AttributeSet paramAttributeSet)
  {
    TypedArray localTypedArray;
    if (paramAttributeSet != null)
    {
      localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RoundImageView);
      this.roundWidth = localTypedArray.getDimensionPixelSize(0, this.roundWidth);
    }
    float f;
    for (this.roundHeight = localTypedArray.getDimensionPixelSize(1, this.roundHeight); ; this.roundHeight = (int)(f * this.roundHeight))
    {
      this.paint = new Paint();
      this.paint.setColor(-1);
      this.paint.setAntiAlias(true);
      this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
      this.paint2 = new Paint();
      this.paint2.setXfermode(null);
      return;
      f = paramContext.getResources().getDisplayMetrics().density;
      this.roundWidth = (int)(f * this.roundWidth);
    }
  }

  public void draw(Canvas paramCanvas)
  {
    Bitmap localBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    super.draw(localCanvas);
    drawLiftUp(localCanvas);
    drawRightUp(localCanvas);
    drawLiftDown(localCanvas);
    drawRightDown(localCanvas);
    paramCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, this.paint2);
    localBitmap.recycle();
  }
}