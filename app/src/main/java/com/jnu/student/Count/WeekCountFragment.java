package com.jnu.student.Count;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.jnu.student.PlayTaskMainActivity;
import com.jnu.student.R;

import java.util.List;

public class WeekCountFragment extends Fragment {

    private LineChart weekIncomeChart;
    private LineChart weekExpendChart;

    public WeekCountFragment() {
        // Required empty public constructor
    }

    public static WeekCountFragment newInstance(String param1, String param2) {
        WeekCountFragment fragment = new WeekCountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_week_count, container, false);
        weekIncomeChart = rootView.findViewById(R.id.week_income_chart);
        weekExpendChart = rootView.findViewById(R.id.week_expend_chart);
        initChart(weekIncomeChart);
        initChart(weekExpendChart);
        updateData();

        return rootView;
    }

    private void initChart(LineChart chart){

        chart.setDrawBorders(true);
        chart.setBorderColor(Color.BLACK); // 设置边框颜色
        chart.setBorderWidth(1f);          // 设置边框宽度

        //设置x和y轴样式
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);    //设置图表右边的y轴禁用
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setEnabled(false); //设置图表左边的y轴禁用
        //设置x轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value + 1);
            }
        });
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(6f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        // 图例
        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setForm(Legend.LegendForm.NONE);
//        legend.setTextColor(Color.WHITE);

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);

    }

    public void updateData(){
        // 1.设置x轴和y轴的点
        List<Entry> incomeEntry = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryWeekIncome();
        List<Entry> expendEntry = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryWeekExpend();

        // 2.把数据赋值到线条
        LineDataSet income = new LineDataSet(incomeEntry, "日收入");
        LineDataSet expend = new LineDataSet(expendEntry, "日花费");

        // 设置线条样式
        income.setColor(Color.parseColor("#00BFFF"));//线条颜色
        income.setCircleColor(Color.parseColor("#7d7d7d"));//圆点颜色
        income.setLineWidth(1f);//线条宽度

        expend.setColor(Color.parseColor("#00BFFF"));//线条颜色
        expend.setCircleColor(Color.parseColor("#7d7d7d"));//圆点颜色
        expend.setLineWidth(1f);//线条宽度

        // 3.chart设置数据
        LineData incomeData = new LineData(income);
        LineData expendData = new LineData(expend);

//        incomeData.setDrawValues(false);  //是否绘制线条上的文字
        weekIncomeChart.setData(incomeData);
        weekExpendChart.setData(expendData);
        weekIncomeChart.invalidate(); // refresh
        weekExpendChart.invalidate();
    }
}