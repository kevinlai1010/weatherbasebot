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
import ubibots.weatherbase.model.BeanCurrentView;
import ubibots.weatherbase.model.BeanLineView;
import ubibots.weatherbase.model.BeanTabMessage;
import ubibots.weatherbase.util.ContextUtil;
import ubibots.weatherbase.util.RequestUtil;

public class HourView {
    public BeanTabMessage hour;

    private BeanLineView hourBeanLineView;
    private List<View> hourViewList;
    private TextView[] hourDots;
    private int hourCurrentIndex;
    private ViewPager hourViewPager;
    private ProgressBar hourProgressBar;
    private Activity activity;
    private BeanCurrentView currentView;

    public BeanLineView getHourBeanLineView() {
        return hourBeanLineView;
    }

    public ViewPager getHourViewPager() {
        return hourViewPager;
    }

    public ProgressBar getHourProgressBar() {
        return hourProgressBar;
    }

    HourView(Activity activity, BeanCurrentView currentView) {
        this.activity = activity;
        this.currentView = currentView;

        hourViewPager = (ViewPager) activity.findViewById(R.id.hourView);
        hourViewList = new ArrayList<>();
        View view1 = View.inflate(ContextUtil.getInstance(), R.layout.temperaturehour, null);
        LineChartView temperatureHourView = (LineChartView) view1.findViewById(R.id.temperaturehour);
        temperatureHourView.setInteractive(false);
        temperatureHourView.setZoomType(ZoomType.HORIZONTAL);
        temperatureHourView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        temperatureHourView.setVisibility(View.VISIBLE);
        View view2 = View.inflate(ContextUtil.getInstance(), R.layout.humidityhour, null);
        LineChartView humidityHourView = (LineChartView) view2.findViewById(R.id.humidityhour);
        humidityHourView.setInteractive(false);
        humidityHourView.setZoomType(ZoomType.HORIZONTAL);
        humidityHourView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        humidityHourView.setVisibility(View.VISIBLE);
        View view3 = View.inflate(ContextUtil.getInstance(), R.layout.airhour, null);
        LineChartView airHourView = (LineChartView) view3.findViewById(R.id.airhour);
        airHourView.setInteractive(false);
        airHourView.setZoomType(ZoomType.HORIZONTAL);
        airHourView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        airHourView.setVisibility(View.VISIBLE);
        hourViewList.add(view1);
        hourViewList.add(view2);
        hourViewList.add(view3);
        hourBeanLineView = new BeanLineView(temperatureHourView, humidityHourView, airHourView);

        initHourDots();
        PagerAdapter hourPagerAdapter = new PagerAdapter() {
            //?????????????????????
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            //??????????????????????????????
            @Override
            public int getCount() {
                return hourViewList.size();
            }

            //???????????????item
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(hourViewList.get(position));
                return hourViewList.get(position);
            }

            //????????????item
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(hourViewList.get(position));
            }

        };
        hourViewPager.setAdapter(hourPagerAdapter);
        hourViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setHourDots(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        hourProgressBar = (ProgressBar) activity.findViewById(R.id.hourProgressBar);
    }

    /**
     * ?????????????????????
     */
    private void initHourDots() {
        hourDots = new TextView[3];
        hourDots[0] = (TextView) activity.findViewById(R.id.currentTemperature);
        hourDots[1] = (TextView) activity.findViewById(R.id.currentHumidity);
        hourDots[2] = (TextView) activity.findViewById(R.id.currentPM);
        hourCurrentIndex = 0;
        hourDots[0].setTextColor(Color.RED);
    }

    /**
     * ???????????????????????????????????????
     */
    private void setHourDots(int position) {
        if (position < 0 || position > hourViewList.size() - 1
                || hourCurrentIndex == position) {
            return;
        }

        hourDots[hourCurrentIndex].setTextColor(Color.WHITE);
        hourDots[position].setTextColor(Color.RED);
        hourCurrentIndex = position;
    }

    public void flushView(BeanLineView lineView, BeanTabMessage tab) {
        RequestUtil.flushView(lineView, tab, "???:???:???");
    }

    public void flushCurrentView(BeanTabMessage tab) {
        String msg;
        msg = tab.getTemperature().get(tab.getTemperature().size() - 1) + " ???";
        currentView.getCurrentTemperature().setText(msg);
        msg = tab.getHumidity().get(tab.getHumidity().size() - 1) + " %RH";
        currentView.getCurrentHumidity().setText(msg);
        msg = tab.getAir().get(tab.getAir().size() - 1) + " ??g/m3";
        currentView.getCurrentPM2_5().setText(msg);
        msg = tab.getPressure().get(tab.getPressure().size() - 1) + " hPa";
        currentView.getCurrentAirPressure().setText(msg);
        msg = tab.getWindSpeed().get(tab.getWindSpeed().size() - 1) + " mph";
        currentView.getCurrentWindSpeed().setText(msg);
        msg = "" + tab.getWindDirection().get(tab.getWindDirection().size() - 1);
        currentView.getCurrentWindDirection().setText(msg);
    }
}
