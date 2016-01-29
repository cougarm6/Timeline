package com.tusharchoudhary.timeline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tusharchaudhary on 1/23/16.
 */
public class TimelineView extends View{

    private Paint myPaint;
    private GestureDetector mDetector = new GestureDetector(this.getContext(), new mListener());
    private  Drawable timelineSelected;
    private final Drawable timelineFirstAndLast;
    private final Drawable timelineUnSelected;
    private List<TimelineDataItem> items;
    private int numberOfMonths;
    private List<Rect> itemBoundsList;
    private int interval = 1;
    private int currentItemPos = 0;
    private OnTimeLineChangeListener onTimeLineChangeListener;
    private List<TimelineDataItem> originalList;
    private DateTime firstDateTime;
    private Rect first;
    private Rect second;

    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        JodaTimeAndroid.init(context);
        timelineFirstAndLast = ContextCompat.getDrawable(context, R.drawable.icon_timeline_first_and_last);
        timelineSelected = ContextCompat.getDrawable(context, R.drawable.icon_timeline_selected);
        timelineUnSelected = ContextCompat.getDrawable(context, R.drawable.icon_timeline_unselected);
        setUpPaint();
    }

    private void setUpRectangles(int width) {
         first = new Rect(30, 30, 70, 70);
         second = new Rect(width - 70, 30, width - 30, 70);
    }

    private void setUpPaint() {
        myPaint = new Paint();
        myPaint.setColor(Color.LTGRAY);
        myPaint.setAntiAlias(true);
        myPaint.setStrokeWidth(3);
    }

    public void bindData(List<TimelineDataItem> items, OnTimeLineChangeListener onTimeLineChangeListener) {
        this.originalList = items;
        getOnlyMonthsList(originalList);
        this.onTimeLineChangeListener = onTimeLineChangeListener;
        numberOfMonths = calculateNumberOfMonthsBetweenFirstAndLastData(items.get(0), items.get(items.size() - 1));
        invalidate();
    }

    private void getOnlyMonthsList(List<TimelineDataItem> items) {
        LocalDate lastKnownDate = new LocalDate(items.get(0).epochTime);
        this.items = new ArrayList<>();
        this.items.add(new TimelineDataItem(lastKnownDate.toDate().getTime()));
        for(int i = 1; i< items.size(); i++){
            int monthDiff = getMonthsDifference(lastKnownDate,new LocalDate(items.get(i).epochTime));
            if(monthDiff >1){
                this.items.add(new TimelineDataItem(items.get(i).epochTime));
                lastKnownDate = new LocalDate(items.get(i).epochTime);
            }else {
                lastKnownDate = new LocalDate(items.get(i).epochTime);
            }
        }
    }

    private int calculateNumberOfMonthsBetweenFirstAndLastData(TimelineDataItem timelineDataItem, TimelineDataItem timelineDataItem1) {
        return getMonthsDifference(new LocalDate(timelineDataItem.epochTime),new LocalDate(timelineDataItem1.epochTime));
    }

    public  final int getMonthsDifference(LocalDate date1, LocalDate date2) {
        int m1 = date1.getYear() * 12 + date1.getMonthOfYear();
        int m2 = date2.getYear() * 12 + date2.getMonthOfYear();
        return m2 - m1 + 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(numberOfMonths!=0){
            drawView(canvas);
        }
    }

    private void drawView(Canvas canvas) {
        itemBoundsList = new ArrayList<>();
        int width = canvas.getWidth();
        setUpRectangles(width);
        drawLineBetweenFirstAndLast(canvas, width);
        setupIntervalAndFirstDate(width);
        for(int i = 1;i < items.size()-1; i++)
            drawCurrentItem(canvas, i);
        drawLastItem(canvas);
    }

    private void drawCurrentItem(Canvas canvas, int i) {
        int positionToDraw = Months.monthsBetween(firstDateTime, new DateTime(items.get(i).epochTime)).getMonths() * interval;
        Rect current = new Rect(positionToDraw, 30, positionToDraw + 40, 70);
        if(i == currentItemPos) {
            timelineSelected.setBounds(current);
            timelineSelected.draw(canvas);
        }else{
            timelineUnSelected.setBounds(current);
            timelineUnSelected.draw(canvas);
        }
        itemBoundsList.add(current);
    }

    private void setupIntervalAndFirstDate(int width) {
        interval = width / numberOfMonths;
        firstDateTime = new DateTime(items.get(0).epochTime);
    }

    private void drawLineBetweenFirstAndLast(Canvas canvas, int width) {
        canvas.drawLine(50, 50, width - 50, 51, myPaint);
        drawFirstItem(canvas);
    }

    private void drawLastItem(Canvas canvas) {
        if(currentItemPos == items.size()-1){
            timelineSelected.setBounds(second);
            timelineSelected.draw(canvas);
        }else{
            timelineFirstAndLast.setBounds(second);
            timelineFirstAndLast.draw(canvas);
        }
        itemBoundsList.add(second);
    }

    private void drawFirstItem(Canvas canvas) {
        if(currentItemPos == 0) {
            timelineSelected.setBounds(first);
            timelineSelected.draw(canvas);
        }else{
            timelineFirstAndLast.setBounds(first);
            timelineFirstAndLast.draw(canvas);
        }
        itemBoundsList.add(first);
    }

    public void setNewPosition(int position){
        if(position > -1 && position < originalList.size() - 1) {
            DateTime date = new DateTime(originalList.get(position).epochTime);
            currentItemPos = getPositionInList(date);
            invalidate();
        }
    }

    private int getPositionInList(DateTime date) {
        DateTime dateTimeInlist ;
        for(int i = 0; i < items.size(); i++){
            dateTimeInlist = new DateTime(items.get(i).epochTime);
            int firstDateMonth = dateTimeInlist.getYear() * 12 +dateTimeInlist.getMonthOfYear();
            int secondDateMonth = date.getYear() * 12 +date.getMonthOfYear();
            if(firstDateMonth == secondDateMonth)
                return i;
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Boolean x = isTouchInsideAnyOfTheRectangles(event);
                if (x != null) return x;
            }
        }
        return result;
    }

    @Nullable
    private Boolean isTouchInsideAnyOfTheRectangles(MotionEvent event) {
        int x = (int) event.getX(), y = (int) event.getY();
        for (int i = 0;i < itemBoundsList.size(); i ++) {
           Rect rectangle = itemBoundsList.get(i);
            if (x > rectangle.left-30 && x < rectangle.right+30 && y > rectangle.top-30 && y < rectangle.bottom+30) {
                if(currentItemPos!=i) {
                    try {
                    onTimeLineChangeListener.onChange(getPositionFromOriginalList(i));
                    currentItemPos = i;
                    invalidate();
                    return true;
                    }catch (Exception e){
                        Log.e("Timeline", ""+e);
                        return false;
                    }
                }
            }
        }
        return null;
    }

    private int getPositionFromOriginalList(int positionInMonthsList) throws IndexOutOfBoundsException{
            long epoch = items.get(positionInMonthsList).epochTime;
            for (int i = 0; i < originalList.size(); i++) {
                if (epoch == originalList.get(i).epochTime)
                    return i;
            }
        return 0;
    }

    public static class TimelineDataItem implements Comparable<TimelineDataItem>{
        public Long epochTime;
        public String url;
        private TimelineDataItem dataItem;
        public TimelineDataItem(Long epochTime){
            this.epochTime = epochTime;
        }
        public TimelineDataItem(Long epochTime, String url){
            this.epochTime = epochTime;
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TimelineDataItem that = (TimelineDataItem) o;

            return epochTime.equals(that.epochTime);

        }

        @Override
        public int hashCode() {
            return epochTime.hashCode();
        }


        @Override
        public int compareTo(TimelineDataItem rhs) {
            if(this.epochTime < rhs.epochTime)
                return -1;
            if(this.epochTime > rhs.epochTime)
                return 1;

            return 0;
        }
    }

    private class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    public interface OnTimeLineChangeListener{
        void onChange(int position);
    }
}
