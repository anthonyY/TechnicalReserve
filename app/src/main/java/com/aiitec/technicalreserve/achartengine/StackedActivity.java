package com.aiitec.technicalreserve.achartengine;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;


/**
 * 柱状图
 * 
 * @author admin
 * 
 */
public class StackedActivity extends BaseActivity {
	private ArrayList<Information> list;
	private String title;
	private double maxValue = 0; // 数据的最大值
	@Resource(R.id.ll_content)
	private LinearLayout ll_content;
	@ContentView(R.layout.activity_common)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initCompont();
		View view = ChartFactory.getBarChartView(this, getBarDataset(list),
				getBarRenderer(list), Type.STACKED); // Type.STACKED
		view.setBackgroundColor(Color.BLACK);
		ll_content.addView(view);

	}

	/**
	 * 初始化数据
	 */
	private void initCompont() {
		list = (ArrayList<Information>) getIntent().getSerializableExtra(
				"informations");
		title = getIntent().getStringExtra("title");
		// 算出所有数据的最大值
		for (Information information : list) {
			double value = Double.parseDouble(information.dulian1);
			if (value > maxValue) {
				maxValue = value;
			}
		}
		maxValue = maxValue + (maxValue / 8); // 让左边刻度线高出数字的最大值
	}

	// 描绘器设置
	public XYMultipleSeriesRenderer getBarRenderer(
			ArrayList<Information> informations) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 通过SimpleSeriesDenderer设置描绘器的颜色
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.rgb(1, 128, 205)); // 定义柱状图的颜色
		renderer.addSeriesRenderer(r);

		setChartSettings(renderer, informations);// 设置描绘器的其他属性
		return renderer;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer,
			ArrayList<Information> informations) {
		// renderer.setChartTitle("个人收支表");// 设置柱图名称
		// renderer.setXTitle("名单");// 设置X轴名称
		// renderer.setYTitle("数量");// 设置Y轴名称
		renderer.setXAxisMin(0.5);// 设置X轴的最小值为0.5
		renderer.setXAxisMax(5.5);// 设置X轴的最大值为5
		renderer.setYAxisMin(0);// 设置Y轴的最小值为0
		renderer.setYAxisMax(maxValue);// 设置Y轴最大值为500
		renderer.setDisplayChartValues(true); // 设置是否在柱体上方显示值
		renderer.setShowGrid(true);// 设置是否在图表中显示网格
		renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
		renderer.setBarSpacing(0.2); // 柱状间的间隔
		renderer.setZoomButtonsVisible(true);
		// 为X轴的每个柱状图设置底下的标题 比如 福建 ，广东.....
		int count = 1;
		for (Information information : informations) {
			renderer.addXTextLabel(count, information.weidu);
			count++;
		}

	}

	// 数据设置
	private XYMultipleSeriesDataset getBarDataset(
			ArrayList<Information> informations) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		CategorySeries series = new CategorySeries(title + " (单位："
				+ informations.get(0).dw + ")");
		// 声明一个柱形图
		// 为柱形图添加值
		for (Information information : informations) {
			series.add(Double.parseDouble(information.dulian1));
		}
		dataset.addSeries(series.toXYSeries());// 添加该柱形图到数据设置列表

		return dataset;
	}
}
