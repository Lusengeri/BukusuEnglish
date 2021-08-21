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
    public float sx;
    public float sy;
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
        sx = this.getWidth() - this.getPaddingRight() + (this.getPaddingRight()/2) - (scaledWidth/2);

        //The following vertically centers the 'Index-bar'
        sy = (float) ((this.getHeight() - (scaledHeight * sections.length)) / 2.0);
        boolean setupThings = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (indexBarVisibility) {
            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (((x < sx) || x > (sx +scaledWidth) ) || y < sy || y > (sy + scaledHeight * sections.length)) {
                        return super.onTouchEvent(e);
                    } else {
                        //We touched the index bar
                        float yy = y - this.getPaddingTop() - getPaddingBottom() - sy;
                        int currentPosition = (int) Math.floor(yy / scaledHeight);

                        if (currentPosition < 0) {
                            currentPosition = 0;
                        }
                        if (currentPosition >=sections.length) {
                            currentPosition = sections.length - 1;
                        }
                        section = sections[currentPosition];
                        showLetter = true;
                        int positionInData = 0;
                        int itemCount = 0;

                        if (((AlphabetScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(section.toUpperCase())) {
                            positionInData = ((AlphabetScrollRecyclerViewInterface) getAdapter()).getMapIndex().get(section.toUpperCase());
                            //itemCount = getAdapter().getItemCount();
                        }
                        this.scrollToPosition(positionInData+10);
                        AlphabetScrollRecyclerView.this.invalidate();
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    if (!showLetter && (x < sx || x > (sx+scaledWidth) || y < sy || y > (sy + scaledHeight*sections.length)))
                        return super.onTouchEvent(e);
                    else {
                        float yy = y - sy;
                        int currentPosition = (int) Math.floor(yy / scaledHeight);
                        if(currentPosition<0)currentPosition=0;
                        if(currentPosition>=sections.length)currentPosition=sections.length-1;
                        section = sections[currentPosition];
                        showLetter = true;
                        int positionInData = 0;
                        if(((AlphabetScrollRecyclerViewInterface)getAdapter()).getMapIndex().containsKey(section.toUpperCase()) )
                            positionInData = ((AlphabetScrollRecyclerViewInterface)getAdapter()).getMapIndex().get(section.toUpperCase());
                        this.scrollToPosition(positionInData);
                        AlphabetScrollRecyclerView.this.invalidate();
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    Handler listHandler = new ListHandler();
                    listHandler.sendEmptyMessageDelayed(0, 100);
                    if (x < sx || x > (sx+scaledWidth) || y < sy || y > (sy + scaledHeight*sections.length))
                        return super.onTouchEvent(e);
                    else
                        return true;
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
