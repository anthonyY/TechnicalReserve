package com.aiitec.technicalreserve.achartengine;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;


/**
 * 折线图
 * 
 * @author admin
 * 
 */
public class ZhexianActivity extends BaseActivity {
	private ArrayList<Information> list;
	private String title;
	private double maxValue = 0; // 数据的最大值

	@Resource(R.id.ll_content)
	private LinearLayout ll_content;
	@ContentView(R.layout.activity_common)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initCompont(); // 初始化组件
		String[] titles = new String[] { title }; // 表示目前就一条折线信息
		// List有几条数据
		List<double[]> x = new ArrayList<double[]>();
		// X轴所显示的数目 1,2,3,4,5.... 1条数据有几个点信息
		double[] xInfo = new double[list.size()];
		int count = 1;
		for (int i = 0; i < list.size(); i++) {
			xInfo[i] = count;
			count++;
		}

		// 折线数量 1
		for (int i = 0; i < titles.length; i++) {
			x.add(xInfo);
		}
		// 折线各个点的值
		double[] xValue = new double[list.size()];
		for (int i = 0; i < xValue.length; i++) {
			xValue[i] = Double.parseDouble(list.get(i).dulian1);
			// 把数据最大值赋给maxValue
			if (xValue[i] > maxValue) {
				maxValue = xValue[i];
			}
		}
		maxValue = maxValue + (maxValue / 8);

		List<double[]> values = new ArrayList<double[]>();
		values.add(xValue);
		int[] colors = new int[] { Color.CYAN };// 折线的颜色
		PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND }; // 折线的样式
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		setChartSettings(renderer, title, "", "", 0.5, 12.5, -10, 40,
				Color.LTGRAY, Color.LTGRAY);
		renderer.setZoomButtonsVisible(true);
		View view = ChartFactory.getLineChartView(this,
				buildDataset(titles, x, values), renderer);
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
		title = title + " (单位：" + list.get(0).dw + ")"; // 处理null字符串
	}

	private XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(12); // 标注字
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setYTitle("数量");// 设置Y轴名称
		renderer.setXAxisMin(0.5);// 设置X轴的最小值为0.5
		renderer.setXAxisMax(5.5);// 设置X轴的最大值为5
		renderer.setYAxisMin(0);// 设置Y轴的最小值为0
		renderer.setYAxisMax(maxValue);// 设置Y轴最大值为500
		renderer.setDisplayChartValues(true); // 设置是否在柱体上方显示值
		renderer.setShowGrid(true);// 设置是否在图表中显示网格
		renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
		renderer.setBarSpacing(1);
		renderer.setXLabels(0);// 设置X轴显示的刻度标签的个数
		int tempNum = 1;
		for (Information information : list) {
			renderer.addXTextLabel(tempNum, information.weidu);
			tempNum++;
		}
	}

	private XYMultipleSeriesDataset buildDataset(String[] titles,
			List<double[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset, titles, xValues, yValues, 0);
		return dataset;
	}

	private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
			List<double[]> xValues, List<double[]> yValues, int scale) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
	}
}