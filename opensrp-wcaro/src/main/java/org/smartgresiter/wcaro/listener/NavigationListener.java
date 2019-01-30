package org.smartgresiter.wcaro.listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.smartgresiter.wcaro.R;
import org.smartgresiter.wcaro.activity.AncActivity;
import org.smartgresiter.wcaro.activity.ChildRegisterActivity;
import org.smartgresiter.wcaro.activity.FamilyRegisterActivity;
import org.smartgresiter.wcaro.adapter.NavigationAdapter;
import org.smartgresiter.wcaro.util.Constants;

public class NavigationListener implements View.OnClickListener {

    private Activity activity;
    private NavigationAdapter navigationAdapter;

    public NavigationListener(Activity activity, NavigationAdapter adapter) {
        this.activity = activity;
        this.navigationAdapter = adapter;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            if (v.getTag() instanceof String) {
                String tag = (String) v.getTag();

                switch (tag) {
                    case Constants.DrawerMenu.ALL_FAMILIES:

                        Intent intent_fam = new Intent(activity, FamilyRegisterActivity.class);
                        intent_fam.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_fam);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                        break;
                    case Constants.DrawerMenu.ANC:
                        Intent intent_anc = new Intent(activity, AncActivity.class);
                        intent_anc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_anc);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                        break;
                    case Constants.DrawerMenu.LD:
                        break;
                    case Constants.DrawerMenu.PNC:
                        break;
                    case Constants.DrawerMenu.CHILDREN:
                        break;
                    case Constants.DrawerMenu.FAMILY_PLANNING:
                        break;
                    case Constants.DrawerMenu.MALARIA:
                        break;
                    default:
                        break;
                }


                navigationAdapter.setSelectedView(tag);
            }
        }
    }
}
