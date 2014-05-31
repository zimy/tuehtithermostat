package ru.hse.pi273.emy.paul.app.view.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.inject.Inject;

import roboguice.fragment.RoboDialogFragment;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;

public class DayDialog extends RoboDialogFragment implements DialogInterface.OnClickListener {
    @Inject
    TaskStringKeeper taskStrings;
    private int Day;

    private OnFragmentInteractionListener mListener;

    public DayDialog() {
    }

    public static DayDialog newInstance(int day) {
        DayDialog fragment = new DayDialog();
        Bundle args = new Bundle();
        args.putInt("Day", day);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Day = getArguments().getInt("Day");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.day_dialog_title);
        adb.setSingleChoiceItems(taskStrings.getDays(), Day, this);
        adb.setPositiveButton(R.string.button_chose, this);
        return adb.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == Dialog.BUTTON_POSITIVE)
            if (mListener != null) {
                mListener.onFragmentInteraction(DialogCallback.DAY, ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition());
            }
    }
}
