package com.lmq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.czscg.R;
  
public class LoadingGif extends View {  
  
    private Movie mMovie;  
    private long mMovieStart = 0;
    @SuppressWarnings("ResourceType")
    public LoadingGif(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mMovie = Movie.decodeStream(getResources().openRawResource(R.drawable.loading));
    }  
  
    @Override  
    public void onDraw(Canvas canvas) {  
        long now = android.os.SystemClock.uptimeMillis();  
  
        if (mMovieStart == 0) {  
            mMovieStart = now;  
        }  
  
        if (mMovie != null) {  
            int dur = mMovie.duration();  
              
            if (dur == 0) {  
                dur = 1000;  
            }  
              
            int relTime = (int) ((now - mMovieStart) % dur);  
            mMovie.setTime(relTime);  
            mMovie.draw(canvas, 0, 0);  
              
            invalidate();  
        }  
    }  
}  