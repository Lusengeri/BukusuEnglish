package com.alsoftware.bukusuenglish.alphabetscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class AlphabetScrollRecyclerView extends RecyclerView {
    private Context ctx;

    private boolean setupThings = false;
    public String[] sections;
    public float scaledWidth;
    public float scaledHeight;
    public static int indWidth = 25;
    public static int indHeight = 22;
    public float sx;
    public float sy;
    public String section;
    public boolean showLetter = false;
    private Handler listHandler;

    public AlphabetScrollRecyclerView(@NonNull Context context) {
        super(context);
        ctx = context;
    }

    public AlphabetScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public AlphabetScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
    }

    @Override
    public void onDraw(Canvas c) {
        if (!setupThings) {
            setupThings();
        }

        super.onDraw(c);
    }

    private void setupThings() {
        //Create az text data
        /* This gets the set letters corresponding to the beginnings of the various list sections i.e. A, B, C etc. */
        Set<String> sectionSet = ((AlphabetScrollRecyclerViewInterface)getAdapter()).getMapIndex().keySet();

        ArrayList<String> listSection = new ArrayList<>(sectionSet);
        Collections.sort(listSection);
        sections = new String[listSection.size()];
        int i = 0;

        for (String s : listSection) {
            sections[i++] = s;
        }

        /* The following variables hold the size on the individual indices on the 'Index Bar'*/
        scaledWidth = indWidth * ctx.getResources().getDisplayMetrics().density;
        scaledHeight = indHeight * ctx.getResources().getDisplayMetrics().density;
        /* The following establishes the position of the 'Index Bar' to the left of the (rhs) scroll bar */
        sx = this.getWidth() - this.getPaddingRight() + (this.getPaddingRight()/2) - (scaledWidth/2);
        sy = (float) ((this.getHeight() - (scaledHeight * sections.length)) / 2.0);
        setupThings = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
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
                        itemCount = getAdapter().getItemCount();
                    }
                    this.scrollToPosition(positionInData+10);
                    //this.findViewHolderForAdapterPosition(positionInData);
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
                listHandler = new ListHandler();
                listHandler.sendEmptyMessageDelayed(0, 100);
                if (x < sx || x > (sx+scaledWidth) || y < sy || y > (sy + scaledHeight*sections.length))
                    return super.onTouchEvent(e);
                else
                    return true;
            }
        }
        return true;
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
