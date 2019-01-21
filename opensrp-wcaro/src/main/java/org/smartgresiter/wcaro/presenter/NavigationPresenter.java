package org.smartgresiter.wcaro.presenter;

import android.app.Activity;
import android.util.Log;

import org.smartgresiter.wcaro.contract.NavigationContract;
import org.smartgresiter.wcaro.interactor.NavigationInteractor;
import org.smartgresiter.wcaro.model.NavigationModel;
import org.smartgresiter.wcaro.model.NavigationOption;
import org.smartgresiter.wcaro.util.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

public class NavigationPresenter implements NavigationContract.Presenter {

    private NavigationContract.Model mModel;
    private NavigationContract.Interactor mInteractor;
    private WeakReference<NavigationContract.View> mView;

    public NavigationPresenter(NavigationContract.View view) {
        mView = new WeakReference<>(view);
        mInteractor = NavigationInteractor.getInstance();
        mModel = NavigationModel.getInstance();
    }

    @Override
    public NavigationContract.View getNavigationView() {
        return mView.get();
    }

    @Override
    public void refreshNavigationCount(final Activity activity) {

        int x = 0;
        while (x < mModel.getNavigationItems().size()) {

            final int finalX = x;
            switch (mModel.getNavigationItems().get(x).getMenuTitle()) {
                case Constants.DrawerMenu.ALL_FAMILIES:
                    mInteractor.getFamilyCount(new NavigationContract.InteractorCallback<Integer>() {
                        @Override
                        public void onResult(Integer result) {
                            mModel.getNavigationItems().get(finalX).setRegisterCount(result);
                            Log.d("NavigationPresenter", String.valueOf(result));
                            getNavigationView().refreshCount();
                        }

                        @Override
                        public void onError(Exception e) {
                            getNavigationView().displayToast(activity, "Error retrieving count for " + Constants.DrawerMenu.ALL_FAMILIES);
                        }
                    });
                    break;
                case Constants.DrawerMenu.CHILD_CLIENTS:
                    mInteractor.getChildrenCount(new NavigationContract.InteractorCallback<Integer>() {
                        @Override
                        public void onResult(Integer result) {
                            mModel.getNavigationItems().get(finalX).setRegisterCount(result);
                            getNavigationView().refreshCount();
                        }

                        @Override
                        public void onError(Exception e) {
                            getNavigationView().displayToast(activity, "Error retrieving count for " + Constants.DrawerMenu.CHILD_CLIENTS);
                        }
                    });
                    break;
                default:
                    break;
            }

            x++;
        }

    }


    @Override
    public void refreshLastSync() {
        // get last sync date
        getNavigationView().refreshLastSync(mInteractor.Sync());
    }

    @Override
    public void displayCurrentUser() {
        getNavigationView().refreshCurrentUser(mModel.getCurrentUser());
    }

    @Override
    public void Sync(Activity activity) {
        mInteractor.Sync();
    }

    @Override
    public List<NavigationOption> getOptions() {
        return mModel.getNavigationItems();
    }
}
