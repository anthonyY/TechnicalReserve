package com.aiitec.technicalreserve.achartengine;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;


/**
 * 饼图 *
 * 
 * 
 */
public class PieCharActivity extends BaseActivity {
	private ArrayList<Information> list;
	private int[] colorsArr;
	private String title;
	private int[] colorLib = { Color.CYAN, Color.BLUE, Color.GREEN, Color.RED,
			Color.YELLOW, Color.WHITE };

	@Resource(R.id.ll_content)
	private LinearLayout ll_content;
	@ContentView(R.layout.activity_common)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();

		DefaultRenderer renderer = buildCategoryRenderer(colorsArr); // 把分布的颜色传给渲染器
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(20);
		renderer.setInScroll(true);
		// View view = ChartFactory.getPieChartView(this,
		// buildCategoryDataset("Project budget", values), renderer);
		// 饼状图文字信息和对应的百分比
		View view = ChartFactory.getPieChartView(this,
				buildCategoryDataset("Project budget", list), renderer);
		view.setBackgroundColor(Color.BLACK);

		ll_content.addView(view);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 获取main传来的集合
		list = (ArrayList<Information>) getIntent().getSerializableExtra(
				"informations");
		title = getIntent().getStringExtra("title"); // 设置标题
		// 把数据所需颜色复制到数组
		colorsArr = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			colorsArr[i] = colorLib[i % 6];
		}
	}

	/**
	 * 把分布的颜色传给渲染器
	 * 
	 * @param colors
	 * @return
	 */
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		renderer.setChartTitle(title);
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * 饼状图文字信息
	 * 
	 * @param title
	 * @param list
	 * @return
	 */
	protected CategorySeries buildCategoryDataset(String title,
			ArrayList<Information> list) {
		CategorySeries series = new CategorySeries(title);
		// 根据list值分配视图 颜色
		for (Information information : list) {
			double num = Double.parseDouble(information.dulian1);
			series.add(information.weidu + " (" + information.dulian1
					+ information.dw + ")", num);
		}
		return series;
	}
}