package com.jnu.student.Count;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jnu.student.PlayTaskMainActivity;
import com.jnu.student.R;

import java.util.List;


public class YearCountFragment extends Fragment implements OnChartValueSelectedListener {
    private BarChart yearIncomeChar;
    private BarChart yearExpendChar;

    public YearCountFragment() {
        // Required empty public constructor
    }

    public static YearCountFragment newInstance(String param1, String param2) {
        YearCountFragment fragment = new YearCountFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_year_count, container, false);
        yearIncomeChar = rootView.findViewById(R.id.year_income_char);
        yearExpendChar = rootView.findViewById(R.id.year_expend_char);
        initChart(yearIncomeChar);
        initChart(yearExpendChar);
        updateDate();



        return rootView;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void initChart(BarChart chart){
        chart.setOnChartValueSelectedListener(this);

        chart.setDrawBorders(true);
        chart.setBorderColor(Color.BLACK); // 设置边框颜色
        chart.setBorderWidth(1f);          // 设置边框宽度
        chart.setDrawValueAboveBar(true);
        chart.setTouchEnabled(false);


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
                return String.valueOf((int) value + 1).concat("月");
            }
        });
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(11f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘

        //透明化图例
        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setForm(Legend.LegendForm.NONE);
//        legend.setTextColor(Color.WHITE);

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);
    }

    public void updateDate(){
        // 1.设置x轴和y轴的点
        List<BarEntry> entriesIncome = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryYearIncome();
        List<BarEntry> entriesExpend = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryYearExpend();

        // 2.把数据赋值到线条
        BarDataSet income = new BarDataSet(entriesIncome, "月收入"); // add entries to dataset
        BarDataSet expend = new BarDataSet(entriesExpend, "月花费");

        // 设置线条样式
        income.setColor(Color.parseColor("#96E9FF"));  //线条颜色
        expend.setColor(Color.parseColor("#96E9FF"));

        // 3.chart设置数据
        BarData barDataIncome = new BarData(income);
        BarData barDataExpend = new BarData(expend);

        yearIncomeChar.setData(barDataIncome);
        yearExpendChar.setData(barDataExpend);
        yearIncomeChar.invalidate(); // refresh
        yearExpendChar.invalidate();
    }
}