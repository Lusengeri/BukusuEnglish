package com.example.newdictionary.fastscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newdictionary.R;

public class FastScrollRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    public FastScrollRecyclerViewItemDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);

        float scaledWidth = ((FastScrollerRecyclerView)parent).scaledWidth;
        float sx = ((FastScrollerRecyclerView) parent).sx;
        float scaledHeight= ((FastScrollerRecyclerView) parent).scaledHeight;
        float sy = ((FastScrollerRecyclerView) parent).sy;
        String[] sections = ((FastScrollerRecyclerView) parent).sections;
        String section = ((FastScrollerRecyclerView) parent).section;
        boolean showLetter = ((FastScrollerRecyclerView) parent).showLetter;

        int currentTheme = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getInt("Theme", R.style.CustomAppTheme);

        // We draw the letter in the middle
        if (showLetter & section != null && !section.equals("")) {
            //overlay everything when displaying selected index Letter in the middle
            Paint overlayDark = new Paint();
            overlayDark.setColor(Color.BLACK);
            overlayDark.setAlpha(1);
            canvas.drawRect(0, 0, parent.getWidth(), parent.getHeight(), overlayDark);
            float middleTextSize = mContext.getResources().getDimension(R.dimen.fast_scroll_overlay_text_size);
            Paint middleLetter = new Paint();

            if (currentTheme == R.style.CustomAppTheme) {
                middleLetter.setColor(parent.getResources().getColor(R.color.primaryLight));
            } else {
                middleLetter.setColor(parent.getResources().getColor(R.color.secondaryDark));
            }

            middleLetter.setTextSize(middleTextSize);
            middleLetter.setAntiAlias(true);
            middleLetter.setFakeBoldText(true);
            middleLetter.setStyle(Paint.Style.FILL);

            int xPos = (canvas.getWidth() -  (int)middleTextSize)/ 2;
            int yPos = (int) ((canvas.getHeight() / 2) - ((middleLetter.descent() + middleLetter.ascent()) / 2));

            canvas.drawText(section.toUpperCase(), xPos, yPos, middleLetter);
        }
        // draw index A-Z

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < sections.length; i++) {
            if(showLetter & section != null && !section.equals("") && section!=null
                    && sections[i].toUpperCase().equals(section.toUpperCase())) {

                if (currentTheme == R.style.CustomAppTheme) {
                    textPaint.setColor(parent.getResources().getColor(R.color.primaryLight));
                } else {
                    textPaint.setColor(parent.getResources().getColor(R.color.secondaryDark));
                }
                textPaint.setAlpha(255);
                textPaint.setFakeBoldText(true);
                textPaint.setTextSize((float)(scaledWidth / 2));
                canvas.drawText(sections[i].toUpperCase(),
                        sx + textPaint.getTextSize() / 2, sy + parent.getPaddingTop()
                                + scaledHeight * (i + 1), textPaint);
                textPaint.setTextSize((float)(scaledWidth));
                canvas.drawText("•",
                        sx - textPaint.getTextSize()/3, sy+parent.getPaddingTop()
                                + scaledHeight * (i + 1) + scaledHeight/3, textPaint);
            } else {
                textPaint.setColor(Color.LTGRAY);
                textPaint.setAlpha(200);
                textPaint.setFakeBoldText(false);
                textPaint.setTextSize(scaledWidth / 2);
                canvas.drawText(sections[i].toUpperCase(),
                        sx + textPaint.getTextSize() / 2, sy + parent.getPaddingTop()
                                + scaledHeight * (i + 1), textPaint);
            }
        }
    }
}
