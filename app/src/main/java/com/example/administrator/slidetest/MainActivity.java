package com.example.administrator.slidetest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private float startX, startY;
    private PointF pointMid;
    private float oldDis, newDis;
    private float scale;
    private float startEdge;
    private float endEdge;
    private float rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView= (ImageView) findViewById(R.id.iv_image);
        mMatrix=new Matrix();
        mBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.scenery);
        mImageView.setImageBitmap(mBitmap);
        mImageView.setImageMatrix(mMatrix);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointCount=event.getPointerCount();
        if(pointCount==1){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startX=event.getX();
                    startY=event.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float x=event.getX();
                    float y=event.getY();
                    mMatrix.postTranslate(x-startX,y-startY);
                    startX=x;
                    startY=y;

                    mImageView.setImageMatrix(mMatrix);
                    break;

                case MotionEvent.ACTION_UP:
                    break;

                default:
                    break;
            }
        } else if(pointCount==2){
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDis=getDisByXY(event);
                    startEdge=getTanAngle(event);
                    Log.e("AAA","Start   "+startEdge);
                    pointMid=getMidPointByEvent(event);
                    break;

                case MotionEvent.ACTION_MOVE:
                    newDis=getDisByXY(event);
                    scale=newDis /oldDis;
                    endEdge=getTanAngle(event);
                    rotate=endEdge-startEdge;
                    Log.e("AAA","Rotate  "+ rotate);
                    mMatrix.postRotate(rotate,pointMid.x,pointMid.y);
                    mMatrix.postScale(scale,scale,pointMid.x,pointMid.y);
                    mImageView.setImageMatrix(mMatrix);
                    oldDis=newDis;
                    break;

                default:
                    break;
            }
        }
        return true;
    }

    private float getTanAngle(MotionEvent event) {
        float x1=event.getX();
        float y1=event.getY();
        float x2=event.getX(1);
        float y2=event.getY(1);

        float x=x1-x2;
        float y=y1-y2;

        return (float) (Math.atan(Math.abs(x/y)) / (Math.PI) * 180);
    }


    //获得中心点的坐标
    private PointF getMidPointByEvent(MotionEvent event) {
        PointF pointF=new PointF();
        pointF.x=(event.getX() + event.getX(1))/2;
        pointF.y=(event.getY() + event.getY(1))/2;
        return pointF;
    }


    //计算两点之间的距离
    private float getDisByXY(MotionEvent event) {
        float x1=event.getX();
        float y1=event.getY();
        float x2=event.getX(1);
        float y2=event.getY(1);

        return (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    private Bitmap shot(Activity activity) {
        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度 /
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
//        Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }


}
