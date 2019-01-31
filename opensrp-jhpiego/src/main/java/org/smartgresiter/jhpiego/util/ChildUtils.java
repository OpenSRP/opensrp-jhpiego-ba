package org.smartgresiter.jhpiego.util;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartgresiter.jhpiego.application.JhpiegoApplication;
import org.smartgresiter.jhpiego.rule.HomeAlertRule;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.sync.helper.ECSyncHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChildUtils {
    private static final long MILLI_SEC = 24 * 60 * 60 * 1000;//24 hrs
    private static final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] firstSecondNumber = {"Zero", "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th"};
    public static final String[] ONE_YR = {"bcg",
            "hepb", "opv1", "penta1", "pcv1", "rota1", "opv2", "penta2", "pcv2", "rota2", "opv3", "penta3", "pcv3", "rota3", "ipv", "mcv1",
            "yf"
    };
    public static final String[] TWO_YR = {"bcg",
            "hepb", "opv1", "penta1", "pcv1", "rota1", "opv2", "penta2", "pcv2", "rota2", "opv3", "penta3", "pcv3", "rota3", "ipv", "mcv1",
            "yf", "mcv2"
    };
    public static final String[] test = {"pcv 1", "bcg"};

    //Fully immunized at age 2
    public static String isFullyImmunized(int age, List<String> vaccineGiven) {
        String str = "";
        if (age < 1) {
            List<String> oneYrVac = Arrays.asList(ONE_YR);
            if (vaccineGiven.containsAll(oneYrVac)) {
                str = "1";
            }
        } else {
            List<String> twoYrVac = Arrays.asList(TWO_YR);
            if (vaccineGiven.containsAll(twoYrVac)) {
                str = "2";
            }
        }

        return str;

    }

    public static Object[] getStringWithNumber(String fullString) {
        Object[] objects = new Object[2];
        if (fullString.length() > 0) {
            fullString = StringUtils.capitalize(fullString);
            String str = "";
            String digit = "";
            for (int i = 0; i < fullString.length(); i++) {
                char c = fullString.charAt(i);
                if (Character.isDigit(c)) {
                    digit = digit + c;
                } else {
                    str = str + c;
                }

            }
            objects[0] = str;
            objects[1] = digit;
        }
        return objects;
    }

    public static String getFirstSecondAsNumber(String number) {
        try {
            int index = Integer.parseInt(number);
            return firstSecondNumber[index];
        } catch (Exception e) {

        }
        return "";

    }

    //need to add primary caregiver filter at query
    //ec_family_member.is_primary_caregiver" is true
    public static String mainSelectRegisterWithoutGroupby(String tableName, String familyTableName, String familyMemberTableName, String mainCondition) {
        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.SelectInitiateMainTable(tableName, mainColumns(tableName, familyTableName, familyMemberTableName));
        queryBUilder.customJoin("LEFT JOIN " + familyTableName + " ON  " + tableName + ".relational_id =  " + familyTableName + ".id");
        queryBUilder.customJoin("LEFT JOIN " + familyMemberTableName + " ON  " + familyMemberTableName + ".base_entity_id =  " + familyTableName + ".primary_caregiver");

        String query = queryBUilder.mainCondition(mainCondition);
//      String query=" Select ec_child.id as _id , ec_child.relational_id as relationalid , \n" +
//              "ec_child.last_interacted_with , ec_child.base_entity_id , ec_child.first_name , ec_family_member.first_name as family_first_name , ec_family_member.last_name as family_last_name , \n" +
//              "ec_family.village_town as family_home_address , \n" +
//              "ec_child.last_name , ec_child.unique_id , ec_child.gender , ec_child.dob FROM ec_child,ec_family_member,ec_family \n" +
//              "where  ec_child.relational_id =  ec_family.id group by ec_child.base_entity_id ORDER BY ec_child.last_interacted_with DESC   LIMIT 0,20";

        return query;
    }

    public static String mainSelect(String tableName, String familyTableName, String familyMemberTableName, String mainCondition) {

        String query = mainSelectRegisterWithoutGroupby(tableName, familyTableName, familyMemberTableName, tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + mainCondition + "'");


        return query;
    }

    public static String getChildListByFamilyId(String tableName, String familyId, String childId) {
        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.SelectInitiateMainTable(tableName, new String[]{DBConstants.KEY.BASE_ENTITY_ID});
        String query = queryBUilder.mainCondition(tableName + "." + DBConstants.KEY.RELATIONAL_ID + " = '" + familyId + "'");
        return query;
    }

    public static ChildHomeVisit getLastHomeVisit(String tableName, String childId) {
        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.SelectInitiateMainTable(tableName, new String[]{ChildDBConstants.KEY.LAST_HOME_VISIT, ChildDBConstants.KEY.VISIT_NOT_DONE});
        String query = queryBUilder.mainCondition(tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + childId + "'");

        ChildHomeVisit childHomeVisit = new ChildHomeVisit();
        Cursor cursor = Utils.context().commonrepository(org.smartgresiter.jhpiego.util.Constants.TABLE_NAME.CHILD).queryTable(query);
        if (cursor != null && cursor.moveToFirst()) {
            String lastVisitStr = cursor.getString(1);
            if (!TextUtils.isEmpty(lastVisitStr)) {
                try {
                    childHomeVisit.setLastHomeVisitDate(Long.parseLong(lastVisitStr));
                } catch (Exception e) {

                }
            }
            String visitNotDoneStr = cursor.getString(2);
            if (!TextUtils.isEmpty(visitNotDoneStr)) {
                try {
                    childHomeVisit.setVisitNotDoneDate(Long.parseLong(visitNotDoneStr));
                } catch (Exception e) {

                }
            }
            cursor.close();
        }

        return childHomeVisit;
    }

    private static String[] mainColumns(String tableName, String familyTable, String familyMemberTable) {

        String[] columns = new String[]{
                tableName + "." + DBConstants.KEY.RELATIONAL_ID + " as " + ChildDBConstants.KEY.RELATIONAL_ID,
                tableName + "." + DBConstants.KEY.LAST_INTERACTED_WITH,
                tableName + "." + DBConstants.KEY.BASE_ENTITY_ID,
                tableName + "." + DBConstants.KEY.FIRST_NAME,
                familyMemberTable + "." + DBConstants.KEY.FIRST_NAME + " as " + ChildDBConstants.KEY.FAMILY_FIRST_NAME,
                familyMemberTable + "." + DBConstants.KEY.LAST_NAME + " as " + ChildDBConstants.KEY.FAMILY_LAST_NAME,
                familyTable + "." + DBConstants.KEY.VILLAGE_TOWN + " as " + ChildDBConstants.KEY.FAMILY_HOME_ADDRESS,
                tableName + "." + DBConstants.KEY.LAST_NAME,
                tableName + "." + DBConstants.KEY.UNIQUE_ID,
                tableName + "." + DBConstants.KEY.GENDER,
                tableName + "." + DBConstants.KEY.DOB,
                tableName + "." + ChildDBConstants.KEY.LAST_HOME_VISIT,
                tableName + "." + ChildDBConstants.KEY.VISIT_NOT_DONE};
        return columns;
    }

    public static ChildVisit getChildVisitStatus(String dateOfBirth, long lastVisitDate, long visitNotDate) {
        ChildVisit childVisit = new ChildVisit();
//        childVisit.setLastVisitTime(lastVisitDate);
//        long diff=System.currentTimeMillis()-childVisit.getLastVisitTime();
//        if(diff<MILLI_SEC){
//            childVisit.setLastVisitDays("less than 24 hrs");
//            childVisit.setLastVisitMonthName(theMonth(Calendar.getInstance().get(Calendar.MONTH)));
//        }else{
//            childVisit.setLastVisitDays(diff/MILLI_SEC+" days");
//        }
        HomeAlertRule homeAlertRule = new HomeAlertRule(dateOfBirth, lastVisitDate, visitNotDate);
        String visitStatus = JhpiegoApplication.getInstance().getRulesEngineHelper().getButtonAlertStatus(homeAlertRule, Constants.RULE_FILE.HOME_VISIT);
        childVisit.setVisitStatus(visitStatus);
        childVisit.setNoOfMonthDue(homeAlertRule.noOfMonthDue);
        childVisit.setLastVisitDays(homeAlertRule.noOfDayDue);
        childVisit.setLastVisitMonthName(homeAlertRule.visitMonthName);
        childVisit.setLastVisitTime(lastVisitDate);
        return childVisit;

//        if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1){
//            childVisit.setVisitStatus(ChildProfileInteractor.VisitType.DUE.name());
//            if(childVisit.getLastVisitTime()!=0){
//                if(diff<MILLI_SEC){
//                    childVisit.setLastVisitDays("less than 24 hrs");
//                }else{
//                    childVisit.setLastVisitDays(diff/MILLI_SEC+" days");
//                }
//            }
//            return childVisit;
//        }
//        if(!isSameMonth(childVisit.getLastVisitTime()) && !childVisit.isVisitNotDone()){
//            if(childVisit.getLastVisitTime()==0){
//                childVisit.setVisitStatus(ChildProfileInteractor.VisitType.DUE.name());
//            }else{
//                childVisit.setVisitStatus(ChildProfileInteractor.VisitType.OVERDUE.name());
//            }
//
//            return childVisit;
//        }
//
//        if(diff<MILLI_SEC){
//            childVisit.setLastVisitDays("less than 24 hrs");
//            childVisit.setVisitStatus(ChildProfileInteractor.VisitType.LESS_TWENTY_FOUR.name());
//            childVisit.setLastVisitMonth(theMonth(Calendar.getInstance().get(Calendar.MONTH)));
//            return childVisit;
//        }
//        if(isVisitNotDone){
//            childVisit.setVisitStatus(ChildProfileInteractor.VisitType.NOT_VISIT_THIS_MONTH.name());
//            return childVisit;
//        }
//        else {
//            childVisit.setLastVisitDays(diff/MILLI_SEC+" days");
//            childVisit.setVisitStatus(ChildProfileInteractor.VisitType.VISIT_THIS_MONTH.name());
//            return childVisit;
//        }
    }

    public static boolean isSameMonth(long time) {
        if (time == 0) return false;
        int runningMonth = Calendar.getInstance().get(Calendar.MONTH);
        int runningYear = Calendar.getInstance().get(Calendar.YEAR);
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int visitMonth = cal.get(Calendar.MONTH);
        int visitYear = cal.get(Calendar.YEAR);
        if (runningMonth == visitMonth && runningYear == visitYear) {
            return true;
        }
        return false;
    }

    public static String theMonth(int month) {
        return monthNames[month];
    }

    @SuppressLint("SimpleDateFormat")
    public static String covertLongDateToDisplayDate(long callingTime) {
        Date date = new Date(callingTime);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateText2 = df2.format(date);
        return dateText2;

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    //event type="Child Home Visit"/Visit not done
    public static void updateClientStatusAsEvent(String entityId, String eventType, String attributeName, Object attributeValue, String entityType) {
        try {

            ECSyncHelper syncHelper = FamilyLibrary.getInstance().getEcSyncHelper();

            Event event = (Event) new Event()
                    .withBaseEntityId(entityId)
                    .withEventDate(new Date())
                    .withEventType(eventType)
                    .withLocationId(JhpiegoApplication.getInstance().getContext().allSharedPreferences().fetchCurrentLocality())
                    .withProviderId(JhpiegoApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM())
                    .withEntityType(entityType)
                    .withFormSubmissionId(JsonFormUtils.generateRandomUUIDString())
                    .withDateCreated(new Date());
            event.addObs((new Obs()).withFormSubmissionField(attributeName).withValue(attributeValue).withFieldCode(attributeName).withFieldType("formsubmissionField").withFieldDataType("text").withParentCode("").withHumanReadableValues(new ArrayList<Object>()));

            JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(event));
            syncHelper.addEvent(entityId, eventJson);
            long lastSyncTimeStamp = JhpiegoApplication.getInstance().getContext().allSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            FamilyLibrary.getInstance().getClientProcessorForJava().processClient(syncHelper.getEvents(lastSyncDate, BaseRepository.TYPE_Unsynced));
            JhpiegoApplication.getInstance().getContext().allSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());

            //update details


        } catch (Exception e) {
            Log.e("Error in adding event", e.getMessage());
        }
    }
}
