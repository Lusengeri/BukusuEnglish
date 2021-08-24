package com.alsoftware.bukusuenglish.alphabetscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alsoftware.bukusuenglish.R;

public class AlphabetScrollRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final Context mContext;
    public AlphabetScrollRecyclerViewItemDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);

        if (((AlphabetScrollRecyclerView) parent).indexBarVisibility) {
            float scaledWidth = ((AlphabetScrollRecyclerView) parent).scaledWidth;
            float scaledHeight = ((AlphabetScrollRecyclerView) parent).scaledHeight;

            float indexBarPosX = ((AlphabetScrollRecyclerView) parent).indexBarPosX;
            float indexBarPosY = ((AlphabetScrollRecyclerView) parent).indexBarPosY;

            String[] sections = ((AlphabetScrollRecyclerView) parent).sections;
            String section = ((AlphabetScrollRecyclerView) parent).section;

            boolean showLetter = ((AlphabetScrollRecyclerView) parent).showLetter;

            int currentTheme = PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getInt("Theme", R.style.CustomAppTheme);

            // We draw the letter in the middle
            if (showLetter && section != null && !section.equals("")) {
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

                int xPos = (canvas.getWidth() - (int) middleTextSize) / 2;
                int yPos = (int) ((canvas.getHeight() / 2) - ((middleLetter.descent() + middleLetter.ascent()) / 2));

                canvas.drawText(section.toUpperCase(), xPos, yPos, middleLetter);
            }
            // draw index A-Z

            Paint textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Paint.Style.FILL);

            for (int i = 0; i < sections.length; i++) {
                if (showLetter && section != null && !section.equals("") && sections[i].toUpperCase().equals(section.toUpperCase())) {

                    if (currentTheme == R.style.CustomAppTheme) {
                        textPaint.setColor(parent.getResources().getColor(R.color.primaryLight));
                    } else {
                        textPaint.setColor(parent.getResources().getColor(R.color.secondaryDark));
                    }
                    textPaint.setAlpha(255);
                    textPaint.setFakeBoldText(true);
                    textPaint.setTextSize(scaledWidth / 2);
                    canvas.drawText(sections[i].toUpperCase(),
                            indexBarPosX + textPaint.getTextSize() / 2, indexBarPosY + parent.getPaddingTop()
                                    + scaledHeight * (i + 1), textPaint);
                    textPaint.setTextSize(scaledWidth);
                    canvas.drawText("•",
                            indexBarPosX - textPaint.getTextSize() / 3, indexBarPosY + parent.getPaddingTop()
                                    + scaledHeight * (i + 1) + scaledHeight / 3, textPaint);
                } else {
                    textPaint.setColor(Color.LTGRAY);
                    textPaint.setAlpha(200);
                    textPaint.setFakeBoldText(false);
                    textPaint.setTextSize(scaledWidth / 2);
                    canvas.drawText(sections[i].toUpperCase(),
                            indexBarPosX + textPaint.getTextSize() / 2, indexBarPosY + parent.getPaddingTop()
                                    + scaledHeight * (i + 1), textPaint);
                }
            }
        }
    }
}
