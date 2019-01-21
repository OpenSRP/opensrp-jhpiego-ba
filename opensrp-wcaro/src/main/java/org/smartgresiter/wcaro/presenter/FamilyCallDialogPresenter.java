package org.smartgresiter.wcaro.presenter;

import org.smartgresiter.wcaro.contract.FamilyCallDialogContract;
import org.smartgresiter.wcaro.interactor.FamilyCallDialogInteractor;

import java.lang.ref.WeakReference;

public class FamilyCallDialogPresenter implements FamilyCallDialogContract.Presenter {

    static String TAG = FamilyCallDialogPresenter.class.getCanonicalName();

    WeakReference<FamilyCallDialogContract.View> mView;
    FamilyCallDialogContract.Interactor mInteractor;

    public FamilyCallDialogPresenter(FamilyCallDialogContract.View view, String familyBaseEntityId) {
        mView = new WeakReference<>(view);
        mInteractor = new FamilyCallDialogInteractor(familyBaseEntityId);
        initalize();
    }

    @Override
    public void updateHeadOfFamily(FamilyCallDialogContract.Model model) {
        if (mView.get() != null) {
            mView.get().refreshHeadOfFamilyView(model);
        }
    }

    @Override
    public void updateCareGiver(FamilyCallDialogContract.Model model) {
        if (mView.get() != null) {
            mView.get().refreshCareGiverView(model);
        }
    }

    @Override
    public void initalize() {
        mView.get().refreshHeadOfFamilyView(null);
        mView.get().refreshCareGiverView(null);
        mInteractor.getHeadOfFamily(this);
    }
}
