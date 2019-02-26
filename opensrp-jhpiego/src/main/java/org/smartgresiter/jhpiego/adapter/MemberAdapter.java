package org.smartgresiter.jhpiego.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.smartgresiter.jhpiego.R;
import org.smartgresiter.jhpiego.util.Constants;
import org.smartregister.family.util.DBConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

    private List<HashMap<String, String>> familyMembers;
    private View.OnClickListener clickListener;
    private Context context;

    private Integer selected = -1;

    Animation slideUp;
    Animation slideDown;
    List<String> occupation;
    List<String> leadership;
    List<String> careGiver;

    public MemberAdapter(Context context, List<HashMap<String, String>> myDataset, View.OnClickListener clickListener) {
        familyMembers = myDataset;
        this.clickListener = clickListener;
        this.context = context;

        slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_up);
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    @NonNull
    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.change_member_list, parent, false);
        return new MemberAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final HashMap<String, String> model = familyMembers.get(position);
        holder.tvName.setText(
                String.format("%s %s %s",
                        ((!model.containsKey(DBConstants.KEY.FIRST_NAME) || model.get(DBConstants.KEY.FIRST_NAME) == "null") ? "" : model.get(DBConstants.KEY.FIRST_NAME)),
                        ((!model.containsKey(DBConstants.KEY.MIDDLE_NAME) || model.get(DBConstants.KEY.MIDDLE_NAME) == "null") ? "" : model.get(DBConstants.KEY.MIDDLE_NAME)),
                        ((!model.containsKey(DBConstants.KEY.LAST_NAME) || model.get(DBConstants.KEY.LAST_NAME) == "null") ? "" : model.get(DBConstants.KEY.LAST_NAME))
                )
        );
        holder.llQuestions.setVisibility((selected == position) ? View.VISIBLE : View.GONE);

        holder.radioButton.setChecked((selected == position));

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setSelected(position);
                    onBindViewHolder(holder, position);

                    try {
                        notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
            }
        });

        Boolean isVisible = (holder.llQuestions.getVisibility() == View.VISIBLE);
        if ((selected == position) && !isVisible) {
            holder.llQuestions.setVisibility(View.VISIBLE);
            holder.llQuestions.startAnimation(slideDown);
        }

        if ((selected != position) && isVisible) {
            holder.llQuestions.setVisibility(View.GONE);
            holder.llQuestions.startAnimation(slideUp);
        }


        holder.view.setOnClickListener(clickListener);

        renderViews(holder, model);
    }

    private void renderViews(final MyViewHolder holder, HashMap<String, String> model) {
        String phoneNumber = model.get(DBConstants.KEY.PHONE_NUMBER);
        phoneNumber = (StringUtils.equalsIgnoreCase(phoneNumber, "null") ? "" : phoneNumber);

        String otherPhoneNumber = model.get(DBConstants.KEY.OTHER_PHONE_NUMBER);
        otherPhoneNumber = (StringUtils.equalsIgnoreCase(otherPhoneNumber, "null") ? "" : otherPhoneNumber);


        if (StringUtils.isNotBlank(phoneNumber)) {
            holder.llOldNumber.setVisibility(View.VISIBLE);
            holder.llNewPhone.setVisibility(View.GONE);
        } else {
            holder.llOldNumber.setVisibility(View.GONE);
            holder.llNewPhone.setVisibility(View.VISIBLE);
            holder.rbNo.setChecked(true);
        }


        holder.tvPhoneNumberConfirm.setText(String.format("Is %s phone number still %s?",
                (model.get(DBConstants.KEY.GENDER).equalsIgnoreCase("male") ? "his" : "her"),
                phoneNumber
        ));

        holder.rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.llNewPhone.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                holder.rbNo.setError(null);
                holder.rbYes.setError(null);
            }
        });


        holder.etPhone.setText(phoneNumber);
        holder.etAlternatePhone.setText(otherPhoneNumber);

        //TODO Add occupation to DBConstants
        //here, there is no DB constant for the occupation
        String occupation = model.get(DBConstants.KEY.HIGHEST_EDU_LEVEL);
        if (StringUtils.isNotBlank(occupation)) {
            switch (occupation) {
                case "Community Health Worker (CHW)":
                    holder.spOccupation.setSelection(0);
                    break;
                case "Community Based Distributor(CBD)":
                    holder.spOccupation.setSelection(1);
                    break;
                case "Community IMCI":
                    holder.spOccupation.setSelection(2);
                    break;
                case "Home Based Care (HBC)":
                    holder.spOccupation.setSelection(3);
                    break;

                case "Community HMIS (cHMIS)":
                    holder.spOccupation.setSelection(4);
                    break;

                case "Traditional Birth Attendant (TBAs)":
                    holder.spOccupation.setSelection(5);
                    break;

                case "Traditional Healer (THs)":
                    holder.spOccupation.setSelection(6);
                    break;

                case "Teacher":
                    holder.spOccupation.setSelection(7);
                    break;

                case "Farmer":
                    holder.spOccupation.setSelection(8);
                    break;

                case "Civil Servant":
                    holder.spOccupation.setSelection(9);
                    break;

                case "Nurse":
                    holder.spOccupation.setSelection(10);
                    break;

                case "Other":
                    holder.spOccupation.setSelection(11);
                    break;

                case "None":
                    holder.spOccupation.setSelection(12);
                    break;
                default:

                    holder.spOccupation.setSelection(0);
                    break;
            }
        }

        //TODO Add leadership to DBConstants
        //here there is no constants for leadership role
        String leadership = model.get(DBConstants.KEY.HIGHEST_EDU_LEVEL);
        if (StringUtils.isNotBlank(leadership)) {
            switch (leadership) {
                case "Religious leader":
                    holder.spLeadership.setSelection(0);
                    break;
                case "Political leader":
                    holder.spLeadership.setSelection(1);
                    break;
                case "Influential leader":
                    holder.spLeadership.setSelection(2);
                    break;
                case "Traditional leader":
                    holder.spLeadership.setSelection(3);
                    break;

                case "Other":
                    holder.spLeadership.setSelection(4);
                    //This doesn't work since there is no reference from the db
                    holder.llOtherRole.setVisibility(View.VISIBLE);
                    break;

                case "None":
                    holder.spLeadership.setSelection(5);
                    break;
                default:

                    holder.spLeadership.setSelection(0);
                    break;
            }
        }

        //here there is no constants for the care giver
        String caregiver = model.get(DBConstants.KEY.HIGHEST_EDU_LEVEL);
        if (StringUtils.isNotBlank(caregiver)) {
            switch (caregiver) {
                case "Yes":
                    holder.spCareGiver.setSelection(0);
                    break;
                case "No":
                    holder.spCareGiver.setSelection(1);
                    break;
                default:

                    holder.spCareGiver.setSelection(0);
                    break;
            }
        }
    }

    public boolean validateSave(MyViewHolder holder) {
        boolean res = false;

        if (holder != null) {

            int selectedId = holder.radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.rbNo) {
                String value = holder.etPhone.getText().toString();
                if (value.trim().equals("")) {
                    holder.etPhone.setError("Phone number is required");
                } else {
                    res = true;
                }
            } else if (selectedId == R.id.rbYes) {
                res = true;
            } else {
                res = false;
                holder.rbYes.setError("Select Item");//Set error to last Radio button
            }

            String text = holder.etPhone.getText().toString().trim();
            if (text.length() > 0 && !text.substring(0, 1).equals("0")) {
                holder.etPhone.setError("Must start with 0");
                res = false;
            }

            if (text.length() > 0 && text.length() != 10) {
                holder.etPhone.setError("Length must be equal to 10");
                res = false;
            }

            text = holder.etAlternatePhone.getText().toString().trim();
            if (text.length() > 0 && !text.substring(0, 1).equals("0")) {
                holder.etAlternatePhone.setError("Must start with 0");
                res = false;
            }

            if (text.length() > 0 && text.length() != 10) {
                holder.etAlternatePhone.setError("Length must be equal to 10");
                res = false;
            }

            if (!res) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Kindly complete the form before submitting");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
        return res;
    }

    public HashMap<String, String> getSelectedResults(MyViewHolder holder, int Position) {
        HashMap<String, String> res = new HashMap<>();

        res.put(DBConstants.KEY.BASE_ENTITY_ID, familyMembers.get(Position).get(DBConstants.KEY.BASE_ENTITY_ID));
        res.put(Constants.JsonAssets.FAMILY_MEMBER.PHONE_NUMBER, holder.etPhone.getText().toString());
        res.put(Constants.JsonAssets.FAMILY_MEMBER.OTHER_PHONE_NUMBER, holder.etAlternatePhone.getText().toString());
        res.put(Constants.JsonAssets.FAMILY_MEMBER.OCCUPATION, holder.spOccupation.getSelectedItem().toString());
        res.put(Constants.JsonAssets.FAMILY_MEMBER.LEADERSHIP, holder.spLeadership.getSelectedItem().toString());
        //also these constants are missing for the new details
        return res;
    }

    private List<String> getOptions() {
        if (occupation == null) {
            occupation = new ArrayList<>();
            occupation.add("Community Health Worker (CHW)");
            occupation.add("Community Based Distributor(CBD)");
            occupation.add("Community IMCI");
            occupation.add("Home Based Care (HBC)");
            occupation.add("Community HMIS (cHMIS)");
            occupation.add("Traditional Birth Attendant (TBAs)");
            occupation.add("Traditional Healer (THs)");
            occupation.add("Teacher");
            occupation.add("Farmer");
            occupation.add("Civil Servant");
            occupation.add("Nurse");
            occupation.add("Other");
            occupation.add("None");
        }
        return occupation;
    }

    private List<String> getLeadership() {
        if (leadership == null) {
            leadership = new ArrayList<>();
            leadership.add("Religious leader");
            leadership.add("Political leader");
            leadership.add("Influential leader");
            leadership.add("Traditional leader");
            leadership.add("Other");
            leadership.add("None");
        }
        return leadership;
    }

    private List<String> getCareGiver() {
        if (careGiver == null) {
            careGiver = new ArrayList<>();
            careGiver.add("Yes");
            careGiver.add("No");
        }
        return careGiver;
    }

    @Override
    public int getItemCount() {
        return familyMembers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvGender, tvPhoneNumberConfirm;
        public RadioButton radioButton, rbYes, rbNo;
        public LinearLayout llQuestions, llOldNumber, llNewPhone, llAltPhone, llOccupation,llLeadership,llOtherRole,llCareGiver;
        public View view;
        public EditText etPhone, etAlternatePhone, etOtherRole;
        public Spinner spOccupation,spLeadership,spCareGiver;
        public RadioGroup radioGroup;

        private MyViewHolder(View view) {
            super(view);
            this.view = view;

            tvName = view.findViewById(R.id.tvName);
            tvGender = view.findViewById(R.id.tvGender);
            tvPhoneNumberConfirm = view.findViewById(R.id.tvPhoneNumber);
            radioButton = view.findViewById(R.id.rbButton);
            rbYes = view.findViewById(R.id.rbYes);
            rbNo = view.findViewById(R.id.rbNo);
            llQuestions = view.findViewById(R.id.llQuestions);

            llOldNumber = view.findViewById(R.id.llOldNumber);
            llNewPhone = view.findViewById(R.id.llNewNumber);
            llAltPhone = view.findViewById(R.id.llOtherNumber);
            llOccupation = view.findViewById(R.id.llOccupation);
            llLeadership = view.findViewById(R.id.llLeadership);
            llOtherRole = view.findViewById(R.id.llOtherRole);
            llCareGiver = view.findViewById(R.id.llCareGiver);

            radioGroup = view.findViewById(R.id.rgOldNumber);
            etPhone = view.findViewById(R.id.etPhoneNumber);
            etAlternatePhone = view.findViewById(R.id.etOtherNumber);
            etOtherRole = view.findViewById(R.id.etOtherRole);
            spOccupation = view.findViewById(R.id.spOccupation);
            spLeadership = view.findViewById(R.id.spLeadership);
            spCareGiver = view.findViewById(R.id.spCareGiver);

            ArrayAdapter<String> adp1 = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, getOptions());
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spOccupation.setAdapter(adp1);

            ArrayAdapter<String> adp2 = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, getLeadership());
            adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLeadership.setAdapter(adp2);

            ArrayAdapter<String> adp3 = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, getCareGiver());
            adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCareGiver.setAdapter(adp3);

            setLengthErrorMessage(etPhone);
            setLengthErrorMessage(etAlternatePhone);

        }

        private void setLengthErrorMessage(final EditText et) {
            String error = "Length must be equal to 10";

            TextWatcher tw = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = et.getText().toString().trim();
                    if (text.length() > 0 && text.length() != 10) {
                        et.setError("Length must be equal to 10");
                    }
                    if (text.length() > 0 && !text.substring(0, 1).equals("0")) {
                        et.setError("Must start with 0");
                    }

                }
            };

            et.addTextChangedListener(tw);
        }

    }

}