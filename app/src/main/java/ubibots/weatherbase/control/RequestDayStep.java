package ubibots.weatherbase.control;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ubibots.weatherbase.model.BeanConstant;
import ubibots.weatherbase.model.BeanTabMessage;
import ubibots.weatherbase.ui.DayView;
import ubibots.weatherbase.util.RequestUtil;

public class RequestDayStep extends AsyncTask<String, Integer, String> {

    public final static int MAX = 48;
    private BeanTabMessage day;
    private String strURL;
    private int time;

    public RequestDayStep(BeanTabMessage day, int time) {
        this.day = day;
        this.time = time;
    }

    //�÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
    @Override
    protected String doInBackground(String... params) {
        //System.out.println("Url: " + params[0]);
        URL url;
        try {
            url = new URL(params[0]);
            strURL = params[0];
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true); //����������������������
            urlConn.setDoOutput(true); //������������������ϴ�
            urlConn.setUseCaches(false); //��ʹ�û���
            urlConn.setRequestMethod("POST"); //ʹ��get����
            InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            String result = "";
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                result += readLine;
            }
            in.close();
            urlConn.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Pattern pattern = Pattern.compile("<TD>(.*?)</TD>");
            Matcher matcher = pattern.matcher(result);
            ArrayList<String> tmp = new ArrayList<>();
            while (matcher.find()) {
                tmp.add(matcher.group(1));
            }
            if (tmp.size() >= 3) {
                String dateString = tmp.get(0);
                double temp = 0;
                String tempString = tmp.get(1);
                if (!tempString.equals("---")) {
                    temp = Double.valueOf(tempString);
                }
                String humiString = tmp.get(2);
                double humi = 0;
                if(!humiString.equals("---")){
                    humi = Double.valueOf(humiString);
                }
                String airString = tmp.get(3);
                double air = 0;
                if(!airString.equals("---")){
                    air = Double.valueOf(airString);
                }

                //�����ط�
                if (dateString.length() != 24 || temp <= 0 || humi <= 0 || air < 0) {
                    reconnect(strURL, day);
                    return;
                }

                dateString = dateString.substring(0, 10) + " " + dateString.substring(11, 23);
                Calendar calendar = RequestUtil.dateToCalender(dateString,"yyyy-MM-dd HH:mm:ss.SSS");
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 8);
                SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm", Locale.getDefault());
                dateString = sdf.format(calendar.getTime());

                day.getDate().remove(0);
                day.getDate().add(dateString);
                day.getTemperature().remove(0);
                day.getTemperature().add(temp);
                day.getHumidity().remove(0);
                day.getHumidity().add(humi);
                day.getAir().remove(0);
                day.getAir().add(air);

                //ˢ�½���
                RequestUtil.reflashLineView(DayView.getDayBeanLineView(), day, "�� ʱ:��");

                System.out.println("Time: " + day.getDate().get(MAX - 1) + " " + "Temperature: " + day.getTemperature().get(MAX - 1) + " " + "Humidity: " + day.getHumidity().get(MAX - 1) + " " + "Time: " + time);
            } else {//�����ط�
                reconnect(strURL, day);
            }
        } else {
            RequestUtil.connectFailed();
        }
    }

    //�÷���������UI�̵߳���,����������UI�̵߳��� ���Զ�UI�ռ��������
    @Override
    protected void onPreExecute() {
    }

    public void reconnect(String strURL, BeanTabMessage day) {
        int time = this.time + 1;
        if (time <= BeanConstant.MAXTIME) {
            RequestDayStep another = new RequestDayStep(day, time + 1);
            System.out.println("time: " + time);
            System.out.println(strURL);
            another.execute(strURL);
        }
    }
}