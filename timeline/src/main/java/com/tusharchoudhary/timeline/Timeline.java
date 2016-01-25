package com.tusharchoudhary.timeline;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;



/**
 * Created by tusharchaudhary on 1/25/16.
 */
public class Timeline extends LinearLayout implements TimelineView.OnTimeLineChangeListener{
    private final Context context;
    private RecyclerView recyclerView;
    private TextView startDateTv, endDateTv;
    private TimelineView timelineView;
    private LinearLayoutManager mLayoutManager;
    private TimelineleAdapter mAdapter;
    private int firstVisibleItem, visibleItemCount, totalItemCount ,currentItemVisible;
    private TextView currentDateTv;
    private List<TimelineView.TimelineDataItem> list;

    public Timeline(Context context) {
        this(context, null);
    }

    public Timeline(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Timeline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    public void bindView(List<TimelineView.TimelineDataItem> list,Timeline container) {
        if(list != null && list.size()>0) {
            this.list = list;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View timeline = inflater.inflate(R.layout.timeline, null);
            container.addView(timeline);
            initTimelineViews(timeline);
            setDates(list);
            timelineView.bindData(list, this);
            initRecycler(list);
        }
    }

    private void setDates(List<TimelineView.TimelineDataItem> list) {
        currentDateTv.setText(DateTimeFormat.forPattern("d MMMM, YYYY").print(new DateTime(list.get(0).epochTime)));
        startDateTv.setText(DateTimeFormat.forPattern("MMMM, YYYY").print(new DateTime(list.get(0).epochTime)));
        endDateTv.setText(DateTimeFormat.forPattern("MMMM, YYYY").print(list.get(list.size() - 1).epochTime).toString());
    }

    private void initTimelineViews(View timeline) {
        recyclerView = (RecyclerView) timeline.findViewById(R.id.rv_timeline);
        startDateTv = (TextView) timeline.findViewById(R.id.tv_timeline_start_date);
        endDateTv = (TextView) timeline.findViewById(R.id.tv_timeline_end_date);
        currentDateTv = (TextView) timeline.findViewById(R.id.tv_timeline_current_date);
        timelineView = (TimelineView) timeline.findViewById(R.id.timeline);
    }

    private void initRecycler(List<TimelineView.TimelineDataItem> list) {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerScrollListener());
        mAdapter = new TimelineleAdapter(context,list);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onChange(int position) {
        mLayoutManager.scrollToPositionWithOffset(position, 20);
        currentDateTv.setText(DateTimeFormat.forPattern("d MMMM, YYYY").print(new DateTime(list.get(position).epochTime)));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private class TimelineleAdapter extends RecyclerView.Adapter<TimelineleAdapter.ViewHolder> {
        private final Context context;
        private List<TimelineView.TimelineDataItem> list;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView timelineIv;

            public ViewHolder(View v) {
                super(v);
                timelineIv = (ImageView) v.findViewById(R.id.iv_timeline);
            }
        }

        public TimelineleAdapter(Context context, List<TimelineView.TimelineDataItem> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public TimelineleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_timeline, parent, false);

            makeViewHolderFitScreenWidth(v);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        private void makeViewHolderFitScreenWidth(View v) {
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            int width = point.x - dpToPx(40);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(dpToPx(20), 0, dpToPx(20), 0);
            v.setLayoutParams(layoutParams);
        }

        private int pixelToDp(Context context, int px) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int dp = (int) (px / (metrics.densityDpi / 160f));
            return dp;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TimelineView.TimelineDataItem item = list.get(position);
            Picasso.with(context).load(item.url).placeholder(R.drawable.placeholder_timeline).into(holder.timelineIv);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    public class RecyclerScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy)
        {
            if(dx!=0){
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    Log.e("scrolled"," positions: visibleItemCount "+visibleItemCount+", totalItemCount"+totalItemCount+ ", firstVisibleItemCount "+firstVisibleItem);
                if(currentItemVisible !=firstVisibleItem){
                    currentItemVisible = firstVisibleItem;
                    timelineView.setNewPosition(firstVisibleItem);
                    if(list!=null)
                    currentDateTv.setText(DateTimeFormat.forPattern("d MMMM, YYYY").print(new DateTime(list.get(firstVisibleItem).epochTime)));
                }
                }
        }
    }
}
