package com.example.wqter.androidclient_mypart.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.wqter.androidclient_mypart.ListTable.UDLRSlideAdapter;
import com.example.wqter.androidclient_mypart.R;

import java.util.List;


public class PerfectTitleAdapter extends UDLRSlideAdapter<String> {

	public PerfectTitleAdapter(Context context) {
		super(context);
	}

	public PerfectTitleAdapter(Context context, List<List<String>> data) {
		super(context, data);
	}

	@Override
	public int getItemViewTitleHeight() {
		return (int) mContext.getResources().getDimension(
				R.dimen.title_height);
	}

	@Override
	public int getItemViewContentHeight() {
		return (int) mContext.getResources().getDimension(R.dimen.item_height);
	}

	@Override
	public LinearLayout.LayoutParams getColumnViewParams(int position, int columnIndex, int columnCount) {
		if (columnIndex < mSlideStartColumn) {
			return new LinearLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.item_fixed_column_width),
												 ViewGroup.LayoutParams.MATCH_PARENT);

		} else {
			return new LinearLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.item_slide_column_width),
												 ViewGroup.LayoutParams.MATCH_PARENT);

		}
	}

	@Override
	public View getColumnView(int position, int columnIndex, int columnCount, LinearLayout columnLayout) {
		if (columnIndex < mSlideStartColumn) {
			return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_fixed_column_cell, columnLayout, false);

		} else {
			return LayoutInflater.from(mContext).inflate(R.layout.item_draggable_slide_column_cell, columnLayout, false);

		}
	}

	@Override
	public void convertColumnViewData(int position,
									  int columnIndex,
									  View columnView,
									  View rowView,
									  String columnData,
									  List<String> columnDataList) {

		String columnText = columnDataList.get(columnIndex);
		if (columnIndex < mSlideStartColumn) {
			//这里的column才是列，position才是行
			TextView textView = (TextView) columnView.findViewById(R.id.text_fixed_cell_item);
			textView.setText(columnText);
			textView.setTextSize(16);//设置第一列的字体大小
		} else {

			TextView textView = (TextView) columnView.findViewById(R.id.text_slide_cell_item);
			textView.setText(columnText);
			if(position==0){
				textView.setTextSize(18);//设置第一行的字体大小

			}else {
				textView.setTextSize(12);
			}
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Log.d("wyl", "onClick");
				}
			});
		}


//		if (columnIndex < mSlideStartColumn) {
//			columnView.setBackgroundColor(mContext.getResources().getColor(R.color.table_first));
//		} else {
//
//			columnView.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
//
//		}


		if (position == 0) {//第一列
			rowView.setBackgroundColor(mContext.getResources().getColor(R.color.table_first));


		} else {
			rowView.setBackgroundColor(mContext.getResources().getColor(R.color.background));
		}

//
//		if (columnIndex < mSlideStartColumn) {
//			columnView.setBackgroundColor(mContext.getResources().getColor(R.color.table_first));
//		} else {
//
//				//columnView.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
//
//				columnView.setBackgroundColor(mContext.getResources().getColor(R.color.color_even));
//
//		}
	}

	@Override
	public void convertRowViewData(int position, View rowView, List<String> columnDataList) {

	}

}
