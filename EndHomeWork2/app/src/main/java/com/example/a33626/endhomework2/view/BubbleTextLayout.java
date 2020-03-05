package com.example.a33626.endhomework2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.constants.Constants;

/**
 * 这个就是自定义聊天气泡布局的view
 */
public class BubbleTextLayout extends LinearLayout {

    /**
     * 圆角大小
     */
    private int btRadius;

    /**
     * 三角形的方向
     */

    private int btDirection;

    /**
     * 三角形的底边中心点
     */
    private Point btDatumPoint;

    /**
     * 三角形位置偏移量(默认居中)
     */
    private int btOffset;
    private Paint btBorderPaint; //
    private Path btPath; //画的路径
    private RectF btRect;


    public BubbleTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * 私有初始化方法
     */
    private void init(Context context, AttributeSet attrs){
        //这里面都有注释 就不多说了
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleTextLayout);
        //背景颜色
        int backGroundColor = typedArray.getColor(R.styleable.BubbleTextLayout_background_color, getResources().getColor(R.color.colorChatContent));
        //阴影颜色
        int shadowColor = typedArray.getColor(R.styleable.BubbleTextLayout_shadow_color, getResources().getColor(R.color.colorChatContentShadow));
        int defShadowSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 4, getResources().getDisplayMetrics());
        //阴影尺寸
        int shadowSize = typedArray.getDimensionPixelSize(R.styleable.BubbleTextLayout_shadow_size, defShadowSize);
        btRadius = typedArray.getDimensionPixelSize(R.styleable.BubbleTextLayout_radius, 0);
        //三角形方向
        btDirection = typedArray.getInt(R.styleable.BubbleTextLayout_direction, Constants.LEFT);
        btOffset = typedArray.getDimensionPixelOffset(R.styleable.BubbleTextLayout_offset, 0);
        typedArray.recycle();

        btBorderPaint = new Paint();
        btBorderPaint.setAntiAlias(true); //防抖动
        btBorderPaint.setColor(backGroundColor); //设置颜色
        btBorderPaint.setShadowLayer(shadowSize, 0, 0, shadowColor);  //设置阴影

        btPath = new Path();
        btRect = new RectF(); //Rect 也是画矩形的 区别就在于 一个是单精度浮点数 一个数整形
        btDatumPoint = new Point();

        setWillNotDraw(false); //设置 调用自己的布局
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (btDatumPoint.x > 0 && btDatumPoint.y > 0){
            switch (btDirection) {
                case Constants.LEFT:
                    drawLeftTriangle(canvas);
                    break;
                case Constants.RIGHT:
                    drawRightTriangle(canvas);
                    break;
            }
        }
    }

    private void drawLeftTriangle(Canvas canvas) {
        int triangularLength = getPaddingLeft() * 3 / 2;
        //如果padding不够是不给画的
        if (triangularLength == 0) {
            return;
        }
        btPath.addRoundRect(btRect, btRadius, btRadius, Path.Direction.CCW);
        btPath.moveTo(btDatumPoint.x, btDatumPoint.y - triangularLength / 2);
        btPath.lineTo(btDatumPoint.x - triangularLength / 2, btDatumPoint.y);
        btPath.lineTo(btDatumPoint.x, btDatumPoint.y + triangularLength / 2);
        btPath.close();
        canvas.drawPath(btPath, btBorderPaint);
    }

    private void drawRightTriangle(Canvas canvas) {
        int triangularLength = getPaddingRight();
        if (triangularLength == 0) {
            return;
        }
        btPath.addRoundRect(btRect, btRadius, btRadius, Path.Direction.CCW);
        btPath.moveTo(btDatumPoint.x, btDatumPoint.y - triangularLength / 2);
        btPath.lineTo(btDatumPoint.x + triangularLength / 2, btDatumPoint.y);
        btPath.lineTo(btDatumPoint.x, btDatumPoint.y + triangularLength / 2);
        btPath.close();
        canvas.drawPath(btPath, btBorderPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        btRect.left = getPaddingLeft();
        btRect.top = getPaddingTop();
        btRect.right = w - getPaddingRight();
        btRect.bottom = h - getPaddingBottom();

        switch (btDirection) {
            case Constants.LEFT:
                btDatumPoint.x = getPaddingLeft();
                btDatumPoint.y = h / 2;
                break;
            case Constants.RIGHT:
                btDatumPoint.x = w - getPaddingRight();
                btDatumPoint.y = h / 2;
                break;
        }
        if (btOffset != 0) {
            applyOffset();
        }

    }

    /**
     * 设置三角形偏移位置
     *
     * @param offset 偏移量
     */
    public void setTriangleOffset(int offset) {
        this.btOffset = offset;
        applyOffset();
        invalidate();
    }

    private void applyOffset() {
        switch (btDirection) {
            case Constants.LEFT:
            case Constants.RIGHT:
                btDatumPoint.y += btOffset;
                break;
        }
    }

}
