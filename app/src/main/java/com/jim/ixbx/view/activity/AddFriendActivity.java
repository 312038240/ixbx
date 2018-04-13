package com.jim.ixbx.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jim.ixbx.R;
import com.jim.ixbx.adapter.SearchFriendAdapter;
import com.jim.ixbx.model.UserBean;
import com.jim.ixbx.presenter.Contract.AddFriendActivityCon;
import com.jim.ixbx.presenter.activity.AddFriendActivityPre;
import com.jim.ixbx.view.base.BaseActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddFriendActivity extends BaseActivity implements AddFriendActivityCon.View, SearchView.OnQueryTextListener, SearchFriendAdapter.OnAddFriendClickListener {
    private static final String TAG = "Jim_AddFriendActivity";
    AddFriendActivityCon.Presenter mPresenter;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.iv_nodata)
    ImageView mIvNodata;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        new AddFriendActivityPre(this, this);
        mToolbar.setTitle("搜好友");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);
        /**
         * 初始化菜单中的SearchView
         */
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) menuItem.getActionView();
        /**
         * 在SearchView中添加提示
         */
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        //  mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        // mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        // mSearchView.onActionViewExpanded();
        //设置最大宽度
        //mSearchView.setMaxWidth(500);
        //设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(true);
        //获取提交按钮
       ImageView btnGo = mSearchView.findViewById(R.id.search_go_btn);
       //修改提交按钮图标
   //     btnGo.setImageResource(R.mipmap.add_friend_pressed);
        //设置点击事件
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query =  mSearchView.getQuery().toString();
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(AddFriendActivity.this, "请输入用户名再搜索！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.searchFriend(query);
                //隐藏软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
            }
        });
        //设置输入框提示语
        mSearchView.setQueryHint("搜好友");
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void setPresenter(AddFriendActivityCon.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "请输入用户名再搜索！", Toast.LENGTH_SHORT).show();
            return false;
        }
        mPresenter.searchFriend(query);
        //隐藏软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
        return true;
    }

    /**
     * 在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * 查询好友返回
     * @param list
     * @param contacts
     * @param b
     * @param o
     */
    @Override
    public void onSearchResult(List<UserBean> list, List<String> contacts, boolean b, Object o) {
        if (b){
            mIvNodata.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            SearchFriendAdapter adapter = new SearchFriendAdapter(this,list, contacts);
            adapter.setOnAddFriendClickListener(this);
            mRecyclerView.setAdapter(adapter);
        }else {
            mIvNodata.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 添加好友返回
     * @param username
     * @param b
     * @param msg
     */
    @Override
    public void onAddResult(String username, boolean b, String msg) {
        //弹出式提醒
//        Snackbar.make(view, "已删除一个会话", Snackbar.LENGTH_SHORT)
//                .setAction("撤销", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(Main2Activity.this, "撤销了删除", Toast.LENGTH_SHORT).show();
//                    }
//                }).setCallback(new Snackbar.Callback() {
                //        @Override
                //        public void onDismissed(Snackbar snackbar, int event) {在Snackbar准备消失时调用
                //
                //            switch (event) {
                //
                //                case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:新的Snackbar出现导致旧的消失
                //                case Snackbar.Callback.DISMISS_EVENT_MANUAL:调用了dismiss方法导致消失
                //                case Snackbar.Callback.DISMISS_EVENT_SWIPE:滑动导致消失
                //                case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:设置的显示时间到了导致消失
                //                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                //                    break;
                //                case Snackbar.Callback.DISMISS_EVENT_ACTION:点击了右侧按钮导致消失
                //                    Toast.makeText(MainActivity.this, "撤销了删除操作", Toast.LENGTH_SHORT).show();
                //                    break;
                //            }
                //        }
                //        @Override
                //        public void onShown(Snackbar snackbar) {在Snackbar可见时调用
                //            super.onShown(snackbar);
                //            Log.i(TAG, "onShown");
                //        }
                //    }).show();
        Snackbar.make(mToolbar,"添加好友"+username+(b?"成功":"失败:"+msg),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAddClick(String username) {
        mPresenter.addFriend(username);
    }
}
