package com.example.insense.ui.tabs.categories.Category.Occupation.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.insense.application.App;
import com.example.insense.databinding.FragmentEditActivityBinding;
import com.example.insense.repository.room.activityDB.Activity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment {
    FragmentEditActivityBinding fragmentEditActivityBinding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Activity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityFragment() {
        // Required empty public constructor
    }

    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("uid");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentEditActivityBinding = FragmentEditActivityBinding.inflate(inflater, container, false);
        fragmentEditActivityBinding.buttonBackWhatewher.setOnClickListener(view -> {
            NavHostFragment.findNavController(getParentFragment()).popBackStack();
        });
        final Calendar cStart = Calendar.getInstance();
        final Calendar cEnd = Calendar.getInstance();


        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        materialDateBuilder.setTitleText("Выберите дату");
        materialDateBuilder.setSelection(today);
        final MaterialDatePicker<Long> materialDatePickerStart = materialDateBuilder.build();
        materialDatePickerStart.addOnPositiveButtonClickListener(
                selection -> {
                    int hour = cStart.get(Calendar.HOUR_OF_DAY);
                    int minute = cStart.get(Calendar.MINUTE);
                    MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(hour)
                            .setMinute(minute)
                            .build();

                    materialTimePicker.show(getParentFragmentManager(), "fragment_tag");
                    cStart.setTimeInMillis(selection + hour * 60 * 60 * 1000 + minute * 60 * 1000);
                });
        final MaterialDatePicker<Long> materialDatePickerEnd = materialDateBuilder.build();
        materialDatePickerEnd.addOnPositiveButtonClickListener(
                selection -> {
                    // now update selected date preview text
                    int hour = cEnd.get(Calendar.HOUR_OF_DAY);
                    int minute = cEnd.get(Calendar.MINUTE);
                    MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(hour)
                            .setMinute(minute)
                            .build();

                    materialTimePicker.show(getParentFragmentManager(), "fragment_tag");
                    cEnd.setTimeInMillis(selection + hour * 60 * 60 * 1000 + minute * 60 * 1000);
                });

        fragmentEditActivityBinding.StartTimeButton.setOnClickListener(
                v -> {
                    materialDatePickerStart.show(getParentFragmentManager(), "MATERIALDATE_PICKER");
                });

        fragmentEditActivityBinding.EndTimeButton.setOnClickListener(
                v -> {
                    materialDatePickerEnd.show(getParentFragmentManager(), "MATERIALDATE_PICKER");
                });

        if (mParam1 != null) {
            activity = App.getInstance().getActivityRepository().getDatabase().userDao().loadById(Integer.parseInt(mParam1, 10));
            fragmentEditActivityBinding.nameOfOccupationText.setText(activity.occupation);
            fragmentEditActivityBinding.activityNameText.setText(activity.name);
            fragmentEditActivityBinding.activityDescriptionText.setText(activity.occupation);
            fragmentEditActivityBinding.StartTimeButton.setText(activity.startDate.format(DateTimeFormatter.ofPattern("HH:mm:ss dd / MM / yyyy")));
            fragmentEditActivityBinding.EndTimeButton.setText(activity.endDate.format(DateTimeFormatter.ofPattern("HH:mm:ss dd / MM / yyyy")));
            fragmentEditActivityBinding.saveActivity.setOnClickListener(view -> {
                activity.name = fragmentEditActivityBinding.activityNameText.getText().toString();
                activity.occupation = fragmentEditActivityBinding.nameOfOccupationText.getText().toString();
                activity.description = fragmentEditActivityBinding.activityDescriptionText.getText().toString();
                TimeZone tz = cStart.getTimeZone();
                ZoneId zoneId = tz.toZoneId();
                activity.startDate = LocalDateTime.ofInstant(cStart.toInstant(), zoneId);
                activity.endDate = LocalDateTime.ofInstant(cEnd.toInstant(), zoneId);
                App.getInstance().getActivityRepository().getDatabase().userDao().updateActivity(activity);
                NavHostFragment.findNavController(getParentFragment()).popBackStack();

            });

        }


        // Inflate the layout for this fragment
        return fragmentEditActivityBinding.getRoot();
    }
}
