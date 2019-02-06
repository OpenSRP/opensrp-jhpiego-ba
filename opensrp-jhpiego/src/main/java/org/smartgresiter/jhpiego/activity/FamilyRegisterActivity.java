package org.smartgresiter.jhpiego.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.smartgresiter.jhpiego.BuildConfig;
import org.smartgresiter.jhpiego.custom_view.NavigationMenu;
import org.smartgresiter.jhpiego.fragment.FamilyRegisterFragment;
import org.smartgresiter.jhpiego.listener.FamilyBottomNavigationListener;
import org.smartgresiter.jhpiego.model.FamilyRegisterModel;
import org.smartgresiter.jhpiego.presenter.FamilyRegisterPresenter;
import org.smartgresiter.jhpiego.util.Constants;
import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

public class FamilyRegisterActivity extends BaseFamilyRegisterActivity {

    @Override
    protected void initializePresenter() {
        presenter = new FamilyRegisterPresenter(this, new FamilyRegisterModel());
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new FamilyRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }

    @Override
    protected void registerBottomNavigation() {
        super.registerBottomNavigation();

        if (!BuildConfig.SCAN_QR_CODE) {
            bottomNavigationView.getMenu().removeItem(org.smartregister.family.R.id.action_scan_qr);
        }

        FamilyBottomNavigationListener familyBottomNavigationListener = new FamilyBottomNavigationListener(this, bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(familyBottomNavigationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationMenu.getInstance(this, null, null);
    }


    @Override
    protected void onResumption() {
        super.onResumption();
        NavigationMenu.getInstance(this, null, null).getNavigationAdapter()
                .setSelectedView(Constants.DrawerMenu.ALL_FAMILIES);
    }
}
