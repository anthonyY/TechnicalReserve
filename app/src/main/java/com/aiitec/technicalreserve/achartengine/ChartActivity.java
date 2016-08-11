package com.aiitec.technicalreserve.achartengine;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;


/**
 * @author shc
 * 
 * 快速集成说明：
 * 1、将 /libs 目录下的 achartengine-1.1.0.jar 绘表引擎拷贝到自己项目中
 * 2、关于绘图引擎具体的使用 API，请找度娘，该类的3种图表，仅作参考。
 * 
 * AChartEngine 地址 : https://code.google.com/p/achartengine/ ;
 * -- 示例代码下载地址 : https://achartengine.googlecode.com/files/achartengine-1.1.0-demo-source.zip;
 * -- 参考文档下载地址 : https://achartengine.googlecode.com/files/achartengine-1.1.0-javadocs.zip;
 * -- jar包下载地址 : https://achartengine.googlecode.com/files/achartengine-1.1.0.jar;
 *  CSDN资源下载 : http://download.csdn.net/detail/han1202012/7741579;
 */

public class ChartActivity extends BaseActivity {
	ArrayList<Information> informations;

	@ContentView(R.layout.activity_achartengine)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("报表");
		informations = new ArrayList<Information>();
		// 数据假设 初始化
		informations.add(new Information("福建", "7434832", "人"));
		informations.add(new Information("广东", "9434832", "人"));
		informations.add(new Information("浙江", "5434832", "人"));
		informations.add(new Information("江苏", "7436832", "人"));
		informations.add(new Information("香港", "7464832", "人"));
		informations.add(new Information("陕西", "6464832", "人"));
		informations.add(new Information("上海", "4534832", "人"));
		informations.add(new Information("北京", "9435832", "人"));

	}

	/**
	 * 按键监听
	 */
	
	/**	饼图	*/
	public void bing(View view) {
		Intent intent = new Intent(this, PieCharActivity.class);
		intent.putExtra("informations", informations);
		intent.putExtra("title", "人口数据");
		startActivity(intent);
	}

	/**	柱状图	*/
	public void zhu(View view) {
		Intent intent = new Intent(this, StackedActivity.class);
		intent.putExtra("informations", informations);
		intent.putExtra("title", "人口数据");
		startActivity(intent);
	}

	/**	折线图	*/
	public void zhexian(View view) {
		Intent intent = new Intent(this, ZhexianActivity.class);
		intent.putExtra("informations", informations);
		intent.putExtra("title", "人口数据");
		startActivity(intent);
	}

}
