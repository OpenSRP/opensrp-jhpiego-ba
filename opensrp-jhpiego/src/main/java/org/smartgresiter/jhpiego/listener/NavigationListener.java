package org.smartgresiter.jhpiego.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.smartgresiter.jhpiego.R;
import org.smartgresiter.jhpiego.activity.AncActivity;
import org.smartgresiter.jhpiego.activity.ChildRegisterActivity;
import org.smartgresiter.jhpiego.activity.FamilyPlanningActivity;
import org.smartgresiter.jhpiego.activity.FamilyRegisterActivity;
import org.smartgresiter.jhpiego.activity.LandActivity;
import org.smartgresiter.jhpiego.activity.MalariaActivity;
import org.smartgresiter.jhpiego.activity.PncActivity;
import org.smartgresiter.jhpiego.adapter.NavigationAdapter;
import org.smartgresiter.jhpiego.util.Constants;

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
                        Intent intent_land = new Intent(activity, LandActivity.class);
                        intent_land.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_land);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;

                    case Constants.DrawerMenu.PNC:
                        Intent intent_pnc = new Intent(activity, PncActivity.class);
                        intent_pnc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_pnc);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;

                    case Constants.DrawerMenu.CHILDREN:
                        Intent intent_children = new Intent(activity, ChildRegisterActivity.class);
                        intent_children.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_children);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;

                    case Constants.DrawerMenu.FAMILY_PLANNING:
                        Intent intent_family_planning = new Intent(activity, FamilyPlanningActivity.class);
                        intent_family_planning.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_family_planning);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;

                    case Constants.DrawerMenu.MALARIA:
                        Intent intent_malaria = new Intent(activity, MalariaActivity.class);
                        intent_malaria.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent_malaria);
                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;

                    default:
                        break;
                }


                navigationAdapter.setSelectedView(tag);
            }
        }
    }
}
