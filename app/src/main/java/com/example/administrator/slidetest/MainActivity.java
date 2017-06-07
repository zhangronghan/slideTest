package com.example.administrator.slidetest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private float startX, startY;
    private PointF pointMid;
    private float oldDis, newDis;
    private float scale;

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
                    pointMid=getMidPointByEvent(event);
                    break;

                case MotionEvent.ACTION_MOVE:
                    newDis=getDisByXY(event);
                    scale=newDis /oldDis;
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

    private PointF getMidPointByEvent(MotionEvent event) {
        PointF pointF=new PointF();
        pointF.x=(event.getX() + event.getX(1))/2;
        pointF.y=(event.getY() + event.getY(1))/2;
        return pointF;
    }

    private float getDisByXY(MotionEvent event) {
        float x1=event.getX();
        float y1=event.getY();

        float x2=event.getX();
        float y2=event.getY();
        return (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }


}
