package com.jim.ixbx.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jim.ixbx.R;
import com.jim.ixbx.presenter.Contract.MainActivityCon;
import com.jim.ixbx.presenter.activity.MainActivityPre;
import com.jim.ixbx.utils.FragmentFactory;
import com.jim.ixbx.view.base.BaseActivity;
import com.jim.ixbx.view.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements MainActivityCon.View, BottomNavigationBar.OnTabSelectedListener {
    private static final String TAG = "MainActivity";
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.fl_content)
    FrameLayout mFlContent;
    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @InjectView(R.id.activity_main)
    LinearLayout mActivityMain;
    private int[] titleIds = {R.string.conversation, R.string.contact, R.string.plugin};
    private BadgeItem mBadgeItem;
    MainActivityCon.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainActivityPre(this,this);
        ButterKnife.inject(this);
        initToolbar();
        initBottomNavigation();
        initFirstFragment();
    }

    /**
     * 初始化标题栏/用toolbar代替Actionbar
     */
    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mTvTitle.setText(titleIds[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomNavigation() {
        BottomNavigationItem conversationItem = new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息");
        mBadgeItem = new BadgeItem();
        mBadgeItem.setGravity(Gravity.RIGHT);
        mBadgeItem.setTextColor("#ffffff");
        mBadgeItem.setBackgroundColor("#ff0000");
        mBadgeItem.setText("5");
        mBadgeItem.show();//设置红点

        conversationItem.setBadgeItem(mBadgeItem);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, R.string.conversation).setBadgeItem(mBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, R.string.contact))
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, R.string.plugin))//依次添加item,分别icon和名称
                .setActiveColor(R.color.btn_normal)
                .setInActiveColor(R.color.inActive)//设置选择颜色
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        mBottomNavigationBar.setTabSelectedListener(this);//设置点击切换监听
        //设置切换效果及背景色
//        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
//        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
    }

    /**
     * 初始化fragment,默认消息页面
     */
    private void initFirstFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < titleIds.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(i + "");
            if (fragment != null) {
                transaction.remove(fragment);
            }
        }
        transaction.commit();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_content,FragmentFactory.getFragment(0), "0")
                .commit();
    }

    /**
     * 选中
     *
     * @param position
     */
    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(position);
        if (!fragment.isAdded()){
            transaction.add(R.id.fl_content,fragment,""+position);
        }
        transaction.show(fragment).commit();
        mTvTitle.setText(titleIds[position]);
    }

    /**
     * 取消选中
     *
     * @param position
     */
    @Override
    public void onTabUnselected(int position) {
        getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
    }

    /**
     * 再次选中
     *
     * @param position
     */
    @Override
    public void onTabReselected(int position) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_friend:
                Toast.makeText(this, "添加好友", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_scan:
                Toast.makeText(this, "分享好友", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                Toast.makeText(this, "关于我们", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
                break;
            default:

                break;
        }
        return true;
    }

    @Override
    public void setPresenter(MainActivityCon.Presenter presenter) {
        this.mPresenter=presenter;
    }
}
