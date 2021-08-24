package com.alsoftware.bukusuenglish.alphabetscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class AlphabetScrollRecyclerView extends RecyclerView {

    public String[] sections;
    public float scaledWidth;
    public float scaledHeight;
    public static int indWidth = 25;
    public static int indHeight = 22;
    public float indexBarPosX;
    public float indexBarPosY;
    public String section;
    public boolean showLetter = false;
    public boolean indexBarVisibility;

    public AlphabetScrollRecyclerView(@NonNull Context context) {
        super(context);
        setContextDependent(context);
    }

    public AlphabetScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setContextDependent(context);
    }

    public AlphabetScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setContextDependent(context);
    }

    private void setContextDependent(Context context) {
        // The following variables hold the size of the individual index elements on the 'Index Bar'
        scaledWidth = indWidth * context.getResources().getDisplayMetrics().density;
        scaledHeight = indHeight * context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void setAdapter(@Nullable  RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        // This gets the set of letters corresponding to the beginnings of the various list sections i.e. A, B, C etc.
        Set<String> sectionSet = ((AlphabetScrollRecyclerViewInterface)adapter).getMapIndex().keySet();
        ArrayList<String> listSection = new ArrayList<>(sectionSet);
        Collections.sort(listSection);
        sections = new String[listSection.size()];
        int i = 0;

        for (String s : listSection) {
            sections[i++] = s;
        }
    }

    @Override
    public void onDraw(Canvas c) {
        setupThings();
        super.onDraw(c);
    }

    private void setupThings() {
        int indexBarHeight = (sections.length * (int) scaledHeight);

        indexBarVisibility = indexBarHeight < this.getHeight();

        //The following horizontally positions the 'Index Bar' at the center of the space
        //between the RecyclerView and the right-hand edge of the screen
        indexBarPosX = this.getWidth() - this.getPaddingRight() + (this.getPaddingRight()/2) - (scaledWidth/2);

        //The following vertically centers the 'Index-bar'
        indexBarPosY = (float) ((this.getHeight() - (scaledHeight * sections.length)) / 2.0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (indexBarVisibility) {
            float touchPositionX = e.getX();
            float touchPositionY = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (((touchPositionX < indexBarPosX) ||
                            touchPositionX > (indexBarPosX +scaledWidth) ) ||
                            touchPositionY < indexBarPosY ||
                            touchPositionY > (indexBarPosY + scaledHeight * sections.length)) {
                        //On touching an area not on the 'Index-Bar'
                        showLetter = false;
                        AlphabetScrollRecyclerView.this.invalidate();
                        return super.onTouchEvent(e);
                    } else {
                        //Ensure the touched letter will be shown
                        showLetter = true;

                        //Obtain the fractional distance between the top of the 'Index-Bar' and the touched position
                        float distanceToTouched = (touchPositionY - indexBarPosY) / (scaledHeight*sections.length);

                        //Calculate the offset to the touched section/letter
                        int yOffset = (int) Math.floor(distanceToTouched * (sections.length));

                        if (yOffset < 0) {
                            yOffset = 0;
                        } else if (yOffset == sections.length){
                            yOffset = sections.length - 1;
                        }

                        section = sections[yOffset];

                        int positionInData = 0;

                        if (((AlphabetScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(section.toUpperCase())) {
                            positionInData = ((AlphabetScrollRecyclerViewInterface) getAdapter()).getMapIndex().get(section.toUpperCase());
                        }
                        this.scrollToPosition(positionInData);
                        AlphabetScrollRecyclerView.this.invalidate();
                        return true;
                    }
                }
                case MotionEvent.ACTION_MOVE: {
                    if ((touchPositionX < indexBarPosX ||
                            touchPositionX > (indexBarPosX +scaledWidth) ||
                            touchPositionY < indexBarPosY ||
                            touchPositionY > (indexBarPosY + scaledHeight*sections.length))) {
                        //If finger moved off 'Index-Bar' revert to normal touch response
                        showLetter = false;
                        AlphabetScrollRecyclerView.this.invalidate();
                        return super.onTouchEvent(e);
                    }
                    else {
                        showLetter = true;
                        //Obtain the fractional distance between the top of the 'Index-Bar' and the touched position
                        float distanceToTouched = (touchPositionY - indexBarPosY) / (scaledHeight*sections.length);
                        //Calculate the offset to the touched section/letter
                        int yOffset = (int) Math.floor(distanceToTouched * (sections.length));

                        if (yOffset < 0) {
                            yOffset = 0;
                        } else if (yOffset == sections.length){
                            yOffset = sections.length - 1;
                        }

                        section = sections[yOffset];

                        int positionInData = 0;

                        if (((AlphabetScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(section.toUpperCase())) {
                            positionInData = ((AlphabetScrollRecyclerViewInterface) getAdapter()).getMapIndex().get(section.toUpperCase());
                        }
                        this.scrollToPosition(positionInData);
                        AlphabetScrollRecyclerView.this.invalidate();
                        return true;
                    }
                }
                case MotionEvent.ACTION_UP: {
                    Handler listHandler = new ListHandler();
                    listHandler.sendEmptyMessageDelayed(0, 100);

                    showLetter = false;
                    AlphabetScrollRecyclerView.this.invalidate();
                    return super.onTouchEvent(e);
                }
            }
            return true;
        } else {
            return super.onTouchEvent(e);
        }
    }

    private class ListHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showLetter = false;
            AlphabetScrollRecyclerView.this.invalidate();
        }
    }
}
