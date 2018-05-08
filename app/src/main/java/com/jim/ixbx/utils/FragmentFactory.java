package com.jim.ixbx.utils;

import com.jim.ixbx.view.base.BaseFragment;
import com.jim.ixbx.view.fragment.ConFragment;
import com.jim.ixbx.view.fragment.ConversationFragment;
import com.jim.ixbx.view.fragment.MineFragment;
import com.jim.ixbx.view.fragment.PluginFragment;

/**
 * Created by Jim.
 */

public class FragmentFactory {
    private static ConversationFragment sConversationFragment;
    private static ConFragment sConFragment;
    private static PluginFragment sPluginFragment;
    private static MineFragment sMineFragment;

    public static BaseFragment getFragment(int position){
        BaseFragment baseFragment=null;
        switch (position) {
            case 0:
                if (sConversationFragment==null){
                    sConversationFragment=new ConversationFragment();
                }
                baseFragment=sConversationFragment;
                break;
            case 1:
                if (sConFragment==null){
                    sConFragment=new ConFragment();
                }
                baseFragment=sConFragment;
                break;
            case 2:
                if (sPluginFragment==null){
                    sPluginFragment=new PluginFragment();
                }
                baseFragment=sPluginFragment;
                break;
            case 3:
                if (sMineFragment==null){
                    sMineFragment=new MineFragment();
                }
                baseFragment=sMineFragment;
                break;
            default:
                break;
        }
        return baseFragment;
    }
}
