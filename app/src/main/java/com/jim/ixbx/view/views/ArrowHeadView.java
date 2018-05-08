package com.jim.ixbx.view.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.ixbx.R;


/**
 *  页面的公共头部
 */

public class ArrowHeadView extends LinearLayout{
    private TextView titleText, rightText, leftText;
    private LinearLayout leftLinear, rightLinear;
    private ImageView arrow;
    public ArrowHeadView(Context context) {
        super(context);
        init(context);
    }

    public ArrowHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArrowHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View headerView = layoutInflater.inflate(R.layout.view_head_layout_arrow, null);
        leftText = headerView.findViewById(R.id.text_title_left);
        arrow = headerView.findViewById(R.id.arrow);
        titleText = headerView.findViewById(R.id.title);
        rightText = headerView.findViewById(R.id.title_right_text);
        leftLinear = headerView.findViewById(R.id.bt_title_left);
        rightLinear = headerView.findViewById(R.id.bt_title_right);
        setOrientation(HORIZONTAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.head_height)));
        removeAllViews();
        addView(headerView);
    }
    public void setLeftText(int leftId){
        leftText.setText(leftId);
    }
    public void setLeftText(String leftStr){
        leftText.setText(leftStr);
    }
    public void setTitle(int titleId){
        titleText.setText(titleId);
    }
    public void setTitle(String titleStr){
        titleText.setText(titleStr);
    }
    public void setRightText(int rightId){
        if(rightId < 0){
            rightText.setText("");
        } else {
            rightText.setText(rightId);
        }
    }
    public void setRightText(String rightStr){
        rightText.setText(rightStr);
    }
    public void setRightButtonText(int rightId, OnClickListener listener){
        setRightText(rightId);
        rightLinear.setOnClickListener(listener);
    }
    public void setLeftButton(OnClickListener listener){
        leftLinear.setOnClickListener(listener);
    }


    public void enableLeftButton(boolean flag){
        if(flag) {
            leftLinear.setVisibility(View.VISIBLE);
        } else {
            leftLinear.setVisibility(View.GONE);
        }
    }
    public void enableRightButton(boolean flag){
        if(flag) {
            rightLinear.setVisibility(View.VISIBLE);
        } else {
            rightLinear.setVisibility(View.GONE);
        }
    }
    public void enableLeftArrow(boolean flag){
        if(flag) {
            arrow.setVisibility(View.VISIBLE);
        } else {
            arrow.setVisibility(View.GONE);
        }
    }
    public void setLeftButtonText(int buttonText, OnClickListener onClickListener) {
        if (buttonText == -1) {
            leftText.setText("");
        } else {
            leftText.setText(buttonText);
        }

        leftLinear.setOnClickListener(onClickListener);
    }

}
