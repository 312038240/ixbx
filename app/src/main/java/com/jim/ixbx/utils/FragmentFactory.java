package com.jim.ixbx.utils;

import com.jim.ixbx.view.base.BaseFragment;
import com.jim.ixbx.view.fragment.ContactFragment;
import com.jim.ixbx.view.fragment.ConversationFragment;
import com.jim.ixbx.view.fragment.PluginFragment;

/**
 * Created by Jim.
 */

public class FragmentFactory {
    private static ConversationFragment sConversationFragment;
    private static ContactFragment sContactFragment;
    private static PluginFragment sPluginFragment;

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
                if (sContactFragment==null){
                    sContactFragment=new ContactFragment();
                }
                baseFragment=sContactFragment;
                break;
            case 2:
                if (sPluginFragment==null){
                    sPluginFragment=new PluginFragment();
                }
                baseFragment=sPluginFragment;
                break;
            default:
                break;
        }
        return baseFragment;
    }
}
