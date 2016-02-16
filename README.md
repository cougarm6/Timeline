[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Timeline-green.svg?style=true)](https://android-arsenal.com/details/1/3074)

# Android Timeline

Android Timeline is a custom view created to showcase the images against the time photo was taken. 

This custom view enables you to simply create a beautiful looking timeline by just including this custom view in your XML file. 

## Download

gradle-

```
compile 'com.tusharchoudhary:timeline:1.2.2'
```

maven-

```
<dependency>
  <groupId>com.tusharchoudhary</groupId>
  <artifactId>timeline</artifactId>
  <version>1.2.2</version>
</dependency>
```

## Usage

Simply include Timeline in your XML file as :
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".TimelineSampleActivity">

    <com.tusharchoudhary.timeline.Timeline
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/timeline_main_view"
        />

</LinearLayout>
```

And simply bind view with data by calling bindView method from your activity / fragment :
```
       Timeline timelineView = (Timeline) findViewById(R.id.timeline_main_view);
        List<TimelineView.TimelineDataItem> list = new ArrayList<>();
        addDummy(list);
        timelineView.bindView(list,timelineView);
```

The TimelineDataItem takes in Long epochTime and String urlToDownload. 

Run your app to find the timeline.  




![Image](images/timeline.gif?raw=true)

## Developed By

Tushar Choudhary - cougar.m6@gmail.com

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## License

Copyright 2016 Tushar Choudhary

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

