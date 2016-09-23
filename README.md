# ArcProgressBar
Arc ProgressBar Configurable   圆弧环形进度条
###DEMO
![](https://github.com/ZeeeeeeNo/ArcProgressBar/blob/master/demo.gif)

###Attributes
|name|format|description|
|:---:|:---:|:---:|
| borderWidth | integer | 圆弧边框的宽度
| progressStyle | tick/arc | 进度条类型，tick为带刻度的
| radius | integer | 半径
| arcbgColor | color | 圆弧的边框背景
| degree | integer | 弧度，设置为0即为圆形进度条，180为半圆
| tickWidth | integer | 刻度的宽度
| tickDensity | integer | 刻度的密度  2~8 越小越密
| bgShow | boolean | 是否显示圆弧边框背景
| arcCapRound | boolean | 圆弧的笔触是否为圆形，tick无效

###interface
提供了绘制圆弧中间区域的一个接口
```java
/**
   * 
   * @param canvas  
   * @param rectF  圆弧的Rect
   * @param x      圆弧的中心x
   * @param y      圆弧的中心y
   * @param storkeWidth   圆弧的边框宽度
   * @param progress      当前进度
   */
public interface  OnCenterDraw {
  public  void draw(Canvas canvas, RectF rectF, float x, float y,float storkeWidth,int progress);
}
```
默认提供了两个实现
**onImageCenter**

**OnTextCenter**


### Use
```xml
    <com.czp.library.ArcProgress
        android:id="@+id/myProgress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:degree="0"
        app:progressStyle="arc" />
```

```xml

    <com.czp.library.ArcProgress
        android:id="@+id/myProgress01"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/myProgress"
        app:radius="80dp"
        app:progressColor="@color/progressColor"
        app:tickDensity="3" />
```
```xml
    <com.czp.library.ArcProgress
        android:id="@+id/myProgress02"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_toRightOf="@id/myProgress"
        app:radius="90dp"
        app:arcCapRound="true"
        app:degree="180"
        app:progressStyle="arc"
        app:progressColor="@color/progressColorBlue"
        app:tickDensity="3" />
```

```java
 mProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth,int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                textPaint.setStrokeWidth(35);
                textPaint.setColor(getResources().getColor(R.color.textColor));
                String progressStr = String.valueOf(progress+"%");
                float textX = x-(textPaint.measureText(progressStr)/2);
                float textY = y-((textPaint.descent()+textPaint.ascent())/2);
                canvas.drawText(progressStr,textX,textY,textPaint);
            }
        });
```
###Including in your project

```xml
dependencies {
  compile 'com.czp.arcProgressBar:ArcProgressBar:1.0.1'
}
```


###License
<pre>
Copyright 2016 Cai ZePeng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
