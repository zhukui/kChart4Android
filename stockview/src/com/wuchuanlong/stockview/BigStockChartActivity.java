package com.wuchuanlong.stockview;

import java.util.ArrayList;
import java.util.List;

import com.example.stockview.R;
import com.wuchuanlong.stockview.chart.ChartTouchEvent;
import com.wuchuanlong.stockview.chart.KChartUtil;
import com.wuchuanlong.stockview.chart.PriceInfo;
import com.wuchuanlong.stockview.chart.SingleStockInfo;
import com.wuchuanlong.stockview.chart.StockCache;
import com.wuchuanlong.stockview.chart.Type;
import com.wuchuanlong.stockview.chart.WuDangInfo;
import com.wuchuanlong.stockview.fragment.DayChartFragment;
import com.wuchuanlong.stockview.fragment.MonthChartFragment;
import com.wuchuanlong.stockview.fragment.TimeChartFragment;
import com.wuchuanlong.stockview.fragment.WeekChartFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class BigStockChartActivity extends FragmentActivity implements ChartTouchEvent {
	ViewPager viewPager;
	private TextView mStockNameTv;// ����
	private TextView mStockCodeTv;// ����
	private TextView mNowPriceTv;// ��ǰ��
	private TextView mStockZdTv;// �ǵ�
	private TextView mStockZfTv;// �ǵ���
	private TextView mOpenPriceTv;// ��
	private TextView mHighPriceTv;// ��߼�
	private TextView mDealCountTv;// �ɽ���
	private TextView mLowPriceTv;// ��ͼ�
	private TextView mHsTv;// ������
	private TextView mZfTv;// ���
	private String name;
	private String code;
	private String market;
	final int kSize = 4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chart_activity_main);
		name = StockCache.get(StockCache.NAME, String.class);
		code = StockCache.get(StockCache.CODE, String.class);
		market = StockCache.get(StockCache.MARKET,String.class);
//		name = StockCache.get(StockCache.STOCK_TYPE, String.class);
		initView();
		initData();
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		mStockNameTv = (TextView) findViewById(R.id.tv_stock_name);// ����
		mStockCodeTv = (TextView) findViewById(R.id.tv_stock_code);// ����
		mNowPriceTv = (TextView) findViewById(R.id.tv_now_price);// ��ǰ��
		mStockZdTv = (TextView) findViewById(R.id.tv_stock_zd);// �ǵ�
		mStockZfTv = (TextView) findViewById(R.id.tv_stock_zf);// �ǵ���
		mOpenPriceTv = (TextView) findViewById(R.id.tv_open_price);// ��
		mHighPriceTv = (TextView) findViewById(R.id.tv_high_price);// ��߼�
		mDealCountTv = (TextView) findViewById(R.id.tv_deal_count);// �ɽ���
		mLowPriceTv = (TextView) findViewById(R.id.tv_low_price);// ��ͼ�
		mHsTv = (TextView) findViewById(R.id.tv_hs);// ������
		mZfTv = (TextView) findViewById(R.id.tv_zf);// ���

		mStockNameTv.setText(name);
		mStockCodeTv.setText(code);
	}

	public void initData() {
		viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setOffscreenPageLimit(kSize);
		viewPager.setCurrentItem(0);
	}

	/**
	 * ��ָ����
	 */
	public void updateRelativeView(SingleStockInfo info, Type chartType) {
		if (info != null) {
			// ��ָ����
			// ���ǵ���
			PriceInfo stock = StockCache.get(StockCache.STOCK_INFO, PriceInfo.class);
			if (stock != null && chartType.getValue().equals(Type.HOUR.getValue())) {
				String priceZd = KChartUtil.getZdF(info.getNow(), stock.getYesterday());
				mStockZdTv.setText(String.valueOf(KChartUtil.format2(stock.getYesterday() - info.getNow())));// �ǵ�
				mStockZfTv.setText(priceZd);// �ǵ���
				mNowPriceTv.setText(info.getNow() + "");// ��ǰ��
			} else {
				mStockZdTv.setText("--");// �ǵ�
				mStockZfTv.setText("--");// �ǵ���
				mNowPriceTv.setText("--");// ��ǰ��
			}

			mOpenPriceTv.setText(info.getOpen() + "");// ��
			mHighPriceTv.setText(info.getHigh() + "");// ��߼�
			mDealCountTv.setText((Math.rint(info.getDealCount() / 10000)) + "����");// �ɽ���
			mLowPriceTv.setText(info.getLow() + "");// ��ͼ�

			mHsTv.setText("--");// ������
			mZfTv.setText("--");// ���
		} else {
			// ��ָ�뿪
			PriceInfo stockInfo = StockCache.get("stock", PriceInfo.class);
			updateRelativeView(stockInfo, chartType);
		}
	}

	public void updateRelativeView(PriceInfo info, Type chartType) {
		if (info != null) {
			mNowPriceTv.setText(info.getNow() + "");// ��ǰ��
			mStockZdTv.setText(info.getUp() + "");// �ǵ�
			mStockZfTv.setText(info.getUppercent() + "");// �ǵ���
			mHsTv.setText(info.getHsl() + "");// ������
			mZfTv.setText(info.getFlux() + "");// ���
			mOpenPriceTv.setText(info.getOpen() + "");// ��
			mHighPriceTv.setText(info.getHigh() + "");// ��߼�
			mDealCountTv.setText(info.getVolume());// �ɽ���
			mLowPriceTv.setText(info.getLow() + "");// ��ͼ�
		}
	}

	@Override
	public void ifParentIterceptorEvent(boolean interceptor) {
		viewPager.requestDisallowInterceptTouchEvent(interceptor);
	}

	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.tv_time) {
			viewPager.setCurrentItem(0);
		} else if (id == R.id.tv_day) {
			viewPager.setCurrentItem(1);
		} else if (id == R.id.tv_week) {
			viewPager.setCurrentItem(2);
		} else if (id == R.id.tv_month) {
			viewPager.setCurrentItem(3);
		} else if (id == R.id.tv_finish) {
			finish();
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// updateNavigater(arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			updateNavigater(arg0);
		}

		@Override
		public void onPageSelected(int arg0) {

		}

	}

	class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		List<Fragment> fragmentList = new ArrayList<>(kSize);

		@Override
		public Fragment getItem(int arg0) {
			return getFragmentByPosition(arg0);
		}

		@Override
		public int getCount() {
			return kSize;
		}

		Fragment timeFragment;
		Fragment dayFragment;
		Fragment weekFragment;
		Fragment monthFragment;

		private Fragment getFragmentByPosition(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				if (timeFragment == null) {
					timeFragment = new TimeChartFragment();
					fragmentList.add(0, timeFragment);
				}
				fragment = fragmentList.get(0);
				break;
			case 1:
				if (dayFragment == null) {
					dayFragment = new DayChartFragment();
					fragmentList.add(1, dayFragment);
				}
				fragment = fragmentList.get(1);
				break;
			case 2:
				if (weekFragment == null) {
					weekFragment = new WeekChartFragment();
					fragmentList.add(2, weekFragment);
				}
				fragment = fragmentList.get(2);
				break;
			case 3:
				if (monthFragment == null) {
					monthFragment = new MonthChartFragment();
					fragmentList.add(3, monthFragment);
				}
				fragment = fragmentList.get(3);
				break;
			}
			return fragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}

	public void updateNavigater(int position) {
		int normalRes = R.drawable.stock_k_time_text_normal;
		int pressRes = R.drawable.stock_k_time_text_press;
		findViewById(R.id.tv_time).setBackgroundResource(position == 0 ? pressRes : normalRes);
		findViewById(R.id.tv_day).setBackgroundResource(position == 1 ? pressRes : normalRes);
		findViewById(R.id.tv_week).setBackgroundResource(position == 2 ? pressRes : normalRes);
		findViewById(R.id.tv_month).setBackgroundResource(position == 3 ? pressRes : normalRes);
	}

	@Override
	public void clickedTwo() {
		finish();
	}

}