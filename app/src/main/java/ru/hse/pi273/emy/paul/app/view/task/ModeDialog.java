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

public class ModeDialog extends RoboDialogFragment implements DialogInterface.OnClickListener {
    @Inject
    TaskStringKeeper taskStrings;
    private int mode;
    private OnFragmentInteractionListener mListener;

    public ModeDialog() {
    }

    public static ModeDialog newInstance(int mode) {
        ModeDialog fragment = new ModeDialog();
        Bundle args = new Bundle();
        args.putInt("Mode", mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt("Mode");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.mode_dialog_title);
        adb.setSingleChoiceItems(taskStrings.getModes(), mode, this);
        adb.setPositiveButton(R.string.button_choose, this);
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
                mListener.onFragmentInteraction(DialogCallback.MODE, ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition());
            }
    }

}
