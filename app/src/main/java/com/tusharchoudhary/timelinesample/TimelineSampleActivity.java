package com.tusharchoudhary.timelinesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tusharchoudhary.timeline.Timeline;
import com.tusharchoudhary.timeline.TimelineView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimelineSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_sample);
        Timeline timelineView = (Timeline) findViewById(R.id.timeline_main_view);
        List<TimelineView.TimelineDataItem> list = new ArrayList<>();
        addDummy(list);
        timelineView.bindView(list,timelineView);
    }

    private void addDummy(List<TimelineView.TimelineDataItem> list) {
        list.add(new TimelineView.TimelineDataItem((long) 1423743132*1000,"http://i.imgur.com/DvpvklR.png" ));
        list.add(new TimelineView.TimelineDataItem((long) 1423743132*1000,"http://www.jpl.nasa.gov/assets/images/content/tmp/images/nasa_images(3x1).jpg" ));
        list.add(new TimelineView.TimelineDataItem((long) 1453662903*1000,"http://www.joomlack.fr/images/demos/demo2/on-top-of-earth.jpg"));
        list.add(new TimelineView.TimelineDataItem((long) 1463055132*1000,"http://i164.photobucket.com/albums/u8/hemi1hemi/COLOR/COL9-6.jpg"));
        list.add(new TimelineView.TimelineDataItem((long) 1463055132*1000,"https://pbs.twimg.com/profile_images/579415786994839552/aE3uhPPr.jpg"));
        list.add(new TimelineView.TimelineDataItem((long) 1463055132*1000,"http://www.menucool.com/slider/prod/image-slider-2.jpg"));
        list.add(new TimelineView.TimelineDataItem((long) 1434934861*1000,"http://www.thinkstockphotos.com/CMS/StaticContent/Hero/TS_AnonHP_149000668_03.jpg"));
    }
}
