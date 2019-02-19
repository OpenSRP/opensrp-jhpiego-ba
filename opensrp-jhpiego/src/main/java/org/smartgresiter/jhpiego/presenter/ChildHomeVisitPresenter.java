package org.smartgresiter.jhpiego.presenter;

import android.util.Log;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.smartgresiter.jhpiego.contract.ChildHomeVisitContract;
import org.smartgresiter.jhpiego.interactor.ChildHomeVisitInteractor;
import org.smartgresiter.jhpiego.model.ChildRegisterModel;
import org.smartgresiter.jhpiego.util.Constants;
import org.smartgresiter.jhpiego.util.JsonFormUtils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.util.DBConstants;
import org.smartregister.util.FormUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChildHomeVisitPresenter implements ChildHomeVisitContract.Presenter, ChildHomeVisitContract.InteractorCallback {
    private WeakReference<ChildHomeVisitContract.View> view;
    private ChildHomeVisitContract.Interactor interactor;
    private CommonPersonObjectClient childClient;
    private FormUtils formUtils = null;

    public ChildHomeVisitPresenter(ChildHomeVisitContract.View view) {
        this.view = new WeakReference<>(view);
        interactor = new ChildHomeVisitInteractor();
    }

    public void setChildClient(CommonPersonObjectClient childClient) {
        this.childClient = childClient;

    }

    public int getSaveSize() {
        return ((ChildHomeVisitInteractor) interactor).getSaveSize();
    }

    @Override
    public void startBirthCertForm() {
        try {
            JSONObject form = getFormUtils().getFormJson(Constants.JSON_FORM.BIRTH_CERTIFICATION);
            String dobString = org.smartregister.family.util.Utils.getDuration(org.smartregister.family.util.Utils.getValue
                    (childClient.getColumnmaps(), DBConstants.KEY.DOB, false));

            JSONObject revForm = JsonFormUtils.getBirthCertFormAsJson(form, childClient.getCaseId(), "", dobString);
            getView().startFormActivity(revForm);
        } catch (Exception e) {
            Log.e("BIRTH CERT FORM", e.getMessage());
        }

    }

    @Override
    public void startConsellingForm() {
        try {

            //TODO check if the child is either less than a month's old or btn 1month and 5years and act accordingly
            String dobString = org.smartregister.family.util.Utils.getDob(childClient.age());
            Period diff = new Period(DateTimeFormat.forPattern("dd-MM-yy").parseLocalDate(dobString), LocalDate.now());
//            Log.e("DOBSTRING", "DOBSTRING" + dobString + "Date NOW" + LocalDate.now() + "diff" + diff.getDays());
            if(diff.getDays() < 31) {
                JSONObject form = getFormUtils().getFormJson(Constants.JSON_FORM.HOME_VISIT_LESS_THAN_1_MONTH);
                JSONObject revForm = JsonFormUtils.getHomeVisitLessThanOneMonthFormAsJson(form, childClient.getCaseId(),"");
                getView().startFormActivity(revForm);
            } else if(diff.getDays() >= 31 && diff.getDays() <= 1825) {
                JSONObject form = getFormUtils().getFormJson(Constants.JSON_FORM.HOME_VISIT_1MONTH_5YEARS);
                JSONObject revForm = JsonFormUtils.getHomeVisit1Month5YearsFormAsJson(form, childClient.getCaseId(), "");
                getView().startFormActivity(revForm);
            }
        } catch (Exception e) {
            Log.e("COUNSELLING FORM", e.getMessage());
        }
    }

    @Override
    public void startObsIllnessCertForm() {
        try {

            JSONObject form = getFormUtils().getFormJson(Constants.JSON_FORM.OBS_ILLNESS);
            String dobString = org.smartregister.family.util.Utils.getDuration(org.smartregister.family.util.Utils.getValue
                    (childClient.getColumnmaps(), DBConstants.KEY.DOB, false));

            JSONObject revForm = JsonFormUtils.getOnsIllnessFormAsJson(form, childClient.getCaseId(), "", dobString);
            getView().startFormActivity(revForm);
        } catch (Exception e) {
            Log.e("OBS ILLNESS", e.getMessage());
        }

    }

    @Override
    public void updateBirthStatusTick() {
        getView().updateBirthStatusTick();

    }

    @Override
    public void updateObsIllnessStatusTick() {
        getView().updateObsIllnessStatusTick();

    }

    @Override
    public void generateBirthIllnessForm(String jsonString) {
        interactor.generateBirthIllnessForm(jsonString, this);

    }

    @Override
    public void saveForm() {
        interactor.saveForm();
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        view = null;//set to null on destroy

        // Inform interactor
        interactor.onDestroy(isChangingConfiguration);

        // Activity destroyed set interactor to null
        if (!isChangingConfiguration) {
            interactor = null;
        }
    }

    @Override
    public ChildHomeVisitContract.View getView() {
        if (view != null) {
            return view.get();
        } else {
            return null;
        }
    }

    private FormUtils getFormUtils() {
        if (formUtils == null) {
            try {
                formUtils = FormUtils.getInstance(org.smartregister.family.util.Utils.context().applicationContext());
            } catch (Exception e) {
                Log.e(ChildRegisterModel.class.getCanonicalName(), e.getMessage(), e);
            }
        }
        return formUtils;
    }
}
