package app.myandroidlocations.com.myandroidlocationsapp.Utils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.AlertOneAnswerBinding;

public class CommunicationUtils {

    public static void showOneAnswerDialog(Context thisContext, String textToShow, String buttonText, boolean isCancelable, SingleButtonClickInterface singleButtonClickInterface) {
        AlertOneAnswerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(thisContext), R.layout.alert_one_answer, null, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        builder.setView(binding.getRoot());
        final AlertDialog alertDialog = builder.show();
        alertDialog.setCancelable(isCancelable);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        binding.warningTextView.setText(textToShow);
        binding.button.setText(buttonText);
        binding.button.setOnClickListener((View view) -> singleButtonClickInterface.onSingleButtonClicked(alertDialog));
    }

    public interface SingleButtonClickInterface {
        void onSingleButtonClicked(AlertDialog alertDialog);
    }

}
