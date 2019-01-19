package app.myandroidlocations.com.myandroidlocationsapp.Utils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import app.myandroidlocations.com.myandroidlocationsapp.R;
import app.myandroidlocations.com.myandroidlocationsapp.databinding.AlertDoubleAnswerBinding;
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

    public static void showTwoAnswerDialog(Context thisContext, String textToShow, String leftButtonText, String rightButtonText, boolean isCancelable, DoubleButtonClickInterface doubleButtonClickInterface) {
        AlertDoubleAnswerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(thisContext), R.layout.alert_double_answer, null, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        builder.setView(binding.getRoot());
        final AlertDialog alertDialog = builder.show();
        alertDialog.setCancelable(isCancelable);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        binding.warningTextView.setText(textToShow);
        binding.leftButton.setText(leftButtonText);
        binding.rightButton.setText(rightButtonText);
        binding.leftButton.setOnClickListener((View view) -> doubleButtonClickInterface.onLeftButtonClicked(alertDialog));
        binding.rightButton.setOnClickListener((View view) -> doubleButtonClickInterface.onRightButtonClicked(alertDialog));
    }

    public interface DoubleButtonClickInterface {
        void onLeftButtonClicked(AlertDialog alertDialog);

        void onRightButtonClicked(AlertDialog alertDialog);
    }
}
