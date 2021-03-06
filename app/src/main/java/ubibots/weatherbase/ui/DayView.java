package ubibots.weatherbase.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.view.LineChartView;
import ubibots.weatherbase.R;
import ubibots.weatherbase.model.BeanLineView;
import ubibots.weatherbase.model.BeanTabMessage;
import ubibots.weatherbase.util.ContextUtil;
import ubibots.weatherbase.util.RequestUtil;

public class DayView {
    public BeanTabMessage day;

    private BeanLineView dayBeanLineView;
    private List<View> dayViewList;
    private TextView[] dayDots;
    private int dayCurrentIndex;
    private ViewPager dayViewPager;
    private ProgressBar dayProgressBar;
    private Activity activity;

    public BeanLineView getDayBeanLineView() {
        return dayBeanLineView;
    }

    public ViewPager getDayViewPager() {
        return dayViewPager;
    }

    public ProgressBar getDayProgressBar() {
        return dayProgressBar;
    }

    DayView(Activity activity) {
        this.activity = activity;

        dayViewPager = (ViewPager) activity.findViewById(R.id.dayView);
        dayViewList = new ArrayList<>();
        View view1 = View.inflate(ContextUtil.getInstance(), R.layout.temperatureday, null);
        LineChartView temperatureDayView = (LineChartView) view1.findViewById(R.id.temperatureday);
        temperatureDayView.setInteractive(false);
        temperatureDayView.setZoomType(ZoomType.HORIZONTAL);
        temperatureDayView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        temperatureDayView.setVisibility(View.VISIBLE);
        View view2 = View.inflate(ContextUtil.getInstance(), R.layout.humidityday, null);
        LineChartView humidityDayView = (LineChartView) view2.findViewById(R.id.humidityday);
        humidityDayView.setInteractive(false);
        humidityDayView.setZoomType(ZoomType.HORIZONTAL);
        humidityDayView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        humidityDayView.setVisibility(View.VISIBLE);
        View view3 = View.inflate(ContextUtil.getInstance(), R.layout.airday, null);
        LineChartView airDayView = (LineChartView) view3.findViewById(R.id.airday);
        airDayView.setInteractive(false);
        airDayView.setZoomType(ZoomType.HORIZONTAL);
        airDayView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        airDayView.setVisibility(View.VISIBLE);
        dayViewList.add(view1);
        dayViewList.add(view2);
        dayViewList.add(view3);
        dayBeanLineView = new BeanLineView(temperatureDayView, humidityDayView, airDayView);

        initDayDots();
        PagerAdapter dayPagerAdapter = new PagerAdapter() {
            //?????????????????????
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            //??????????????????????????????
            @Override
            public int getCount() {
                return dayViewList.size();
            }

            //???????????????item
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(dayViewList.get(position));
                return dayViewList.get(position);
            }

            //????????????item
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(dayViewList.get(position));
            }

        };
        dayViewPager.setAdapter(dayPagerAdapter);
        dayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setDayDots(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        dayProgressBar = (ProgressBar) activity.findViewById(R.id.dayProgressBar);
    }

    /**
     * ?????????????????????
     */
    private void initDayDots() {
        dayDots = new TextView[3];
        dayDots[0] = (TextView) activity.findViewById(R.id.currentTemperature);
        dayDots[1] = (TextView) activity.findViewById(R.id.currentHumidity);
        dayDots[2] = (TextView) activity.findViewById(R.id.currentPM);
        dayCurrentIndex = 0;
        dayDots[0].setTextColor(Color.RED);
    }


    /**
     * ???????????????????????????????????????
     */
    private void setDayDots(int position) {
        if (position < 0 || position > dayViewList.size() - 1
                || dayCurrentIndex == position) {
            return;
        }
        dayDots[dayCurrentIndex].setTextColor(Color.WHITE);
        dayDots[position].setTextColor(Color.RED);
        dayCurrentIndex = position;
    }

    public void flushView(BeanLineView lineView, BeanTabMessage tab) {
        RequestUtil.flushView(lineView, tab, "??? ???:???");
    }
}
