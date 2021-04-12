package com.example.AmateurShipper.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.AmateurShipper.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import static android.content.ContentValues.TAG;

public class FilterPaymentDialog extends DialogFragment {

    public interface OnInputSelected{
        void sendInput(String dialog_payment);
    }

    EditText dialog_payment;
    public String filter_payment;
    public OnInputSelected mOnInputSelected;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_payment,null);
        dialog_payment = view.findViewById(R.id.dialog_payment);
        dialog_payment.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)
        });
        builder.setView(view).setTitle("Hãy Nhập Số Tiền")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Lọc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filter_payment = dialog_payment.getText().toString();
                mOnInputSelected.sendInput(filter_payment);

               // Toast.makeText(getContext(), filter_payment+".000 vnđ", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.i(TAG, "onAttach: "+ e.getMessage());
        }
    }
}
