package nirays.com.airspy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.codeandmagic.android.gauge.GaugeView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import nirays.com.airspy.R;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;


/**
 * Created by kirant400 on 18/3/16.
 */
public class DashboardFragment extends Fragment {
    private BarChart mChart_pm2;
    private TextView txtpm2;
    private BarChart mChart_pm10;
    private TextView txtpm10;
    private BarChart mChart_o3;
    private TextView txto3;
    private BarChart mChart_no2;
    private TextView txtno2;
    private BarChart mChart_co;
    private TextView txtco;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public DashboardFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DashboardFragment newInstance(int sectionNumber) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        GaugeView mGaugeView1 = (GaugeView) rootView.findViewById(R.id.gauge_view1);
        TextView textView = (TextView) rootView.findViewById(R.id.text_value);
        txtpm2 = (TextView) rootView.findViewById(R.id.pm_two_text);
        int value = 530;
        mGaugeView1.setTargetValue(value);
        //mGaugeView2.setTargetValue(42);

        textView.setText(value + "");
        textView.setTextColor(getColor(value));

        //graph
        mChart_pm2 = (BarChart) rootView.findViewById(R.id.pm_two_chart);

        initSensorChart(mChart_pm2);

        setData(mChart_pm2,txtpm2,"PM2");

        // add a nice and smooth animation
        mChart_pm2.animateY(2500);

        mChart_pm10 = (BarChart) rootView.findViewById(R.id.pm_ten_chart);
        txtpm10 = (TextView) rootView.findViewById(R.id.pm_ten_text);

        initSensorChart(mChart_pm10);

        setData(mChart_pm10,txtpm10,"PM10");

        // add a nice and smooth animation
        mChart_pm2.animateY(2500);


        mChart_o3 = (BarChart) rootView.findViewById(R.id.o_three_chart);
        txto3 = (TextView) rootView.findViewById(R.id.o_three_text);

        initSensorChart(mChart_o3);

        setData(mChart_o3,txto3,"O3");

        // add a nice and smooth animation
        mChart_o3.animateY(2500);

        mChart_no2 = (BarChart) rootView.findViewById(R.id.no_two_chart);
        txtno2 = (TextView) rootView.findViewById(R.id.no_two_text);

        initSensorChart(mChart_no2);

        setData(mChart_no2,txtno2,"NO2");

        // add a nice and smooth animation
        mChart_no2.animateY(2500);

        mChart_co = (BarChart) rootView.findViewById(R.id.co_chart);
        txtco = (TextView) rootView.findViewById(R.id.co_text);

        initSensorChart(mChart_co);

        setData(mChart_co,txtco,"CO");

        // add a nice and smooth animation
        mChart_co.animateY(2500);
        String response = null;
        /*try {
            response = run("http://freshfreedom-appzware.rhcloud.com/api/sensor");
            Log.i("AirSpy",response);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return rootView;
    }

    private void initSensorChart(BarChart chart) {
        chart.setDescription("");
        YAxis leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        leftAxis.setDrawLabels(true); // no axis labels
        leftAxis.setDrawAxisLine(true); // no axis line
        leftAxis.setDrawZeroLine(true); // draw a zero line
        leftAxis.setTextColor(Color.WHITE);
        chart.getLegend().setEnabled(false);
        //XAxisValueFormatter custom = new DateDayXAxisValueFormatter();
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);


        chart.getAxisLeft().setDrawGridLines(false);

    }

    private int getColor(int val) {
        String[] valRanges = getResources().getStringArray(R.array.ranges);
        String[] valRangeColor = getResources().getStringArray(R.array.rangeColors);
        int retColor = Color.parseColor("#db0000");
        for (int i = 0;i<valRanges.length;i++
             ) {
            int valRange = Integer.parseInt(valRanges[i]);
            if (val<= valRange){
                retColor = Color.parseColor(valRangeColor[i]);
                break;
            }
        }
        return retColor;
    }

    public void setData(BarChart chart,TextView text,String sens) {

        int xTotal = 10;
        float maxVal = 0;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < xTotal + 1; i++) {
            float mult = (500 + 1);
            float val1 = (float) (Math.random() * mult) + mult / 3;
            maxVal = maxVal>val1?maxVal:val1;
            yVals1.add(new BarEntry((int) val1, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = xTotal; i >= 0; i--) {
            Calendar c= Calendar.getInstance();
            c.add(Calendar.DATE,-1*i);
            String str = formatDate(c);
            xVals.add(str);
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(getDataColors(yVals1));
        set1.setDrawValues(true);
        set1.setValueTextColor(Color.WHITE);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        chart.setData(data);
        chart.invalidate();
        text.setText(sens+":"+(int)maxVal);
        text.setTextColor(getColor((int)maxVal));
    }

    private String formatDate(Calendar cal) {
        String str = "%1$ta%n%1$tb%n%ty";
        return String.format(str, cal);
        //return "kiran\\nsda";
    }

    private int[] getDataColors(ArrayList<BarEntry> yVals1) {
        int[] colors = new int[yVals1.size()];
        int i =0;
        for (BarEntry entry : yVals1)
        {
           colors[i] = getColor((int)entry.getVal());
           i++;
        }
        return  colors;
    }
    /*
    OkHttpClient client = new OkHttpClient();

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }*/
}