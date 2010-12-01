package de.winterberg.android.money;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * Activity for editing details on a category.
 *
 * @author Benjamin Winterberg
 */
public class Category extends Activity {
    private static final String TAG = "Category";

    private static final String ACTION_PLUS = "+";
    private static final String ACTION_MINUS = "-";
    private static final String ACTION_CLEAR = "C";

    private String category;

    private BigDecimal amount;

    private StringBuilder input = new StringBuilder();

    private TextView amountView;
    private TextView inputView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        category = getIntent().getStringExtra(Money.KEY_CATEGORY);
        amount = new BigDecimal(0.0);

        inputView = (TextView) findViewById(R.id.current_input_value);
        amountView = (TextView) findViewById(R.id.amount_value);
        amountView.setText(amount.toString());
    }

    public void onNumButtonClick(View view) {
        Button button = (Button) view;
        CharSequence num = button.getText();
        Log.d(TAG, "onNumButtonClick: " + num);
        changeInputValue(num);
    }

    private void changeInputValue(CharSequence num) {
        input.append(num);
        inputView.setText(input.toString());
    }

    public void onActionButtonClick(View view) {
        Button button = (Button) view;
        CharSequence action = button.getText();
        Log.d(TAG, "onActionButtonClick: " + action);

        if (input.length() == 0)
            return;

        if (!isInputValid()) {
            Toast.makeText(getApplicationContext(), "Input is not valid: " + input.toString(), Toast.LENGTH_LONG).show();
            clearInput();
            return;
        }

        if (ACTION_PLUS.equals(action)) {
            plus();
        } else if (ACTION_MINUS.equals(action)) {
            minus();
        } else if (ACTION_CLEAR.equals(action)) {
            clearDigit();
        }
    }

    private void clearDigit() {
        int length = input.length();
        if (length > 0) {
            input.setLength(length - 1);
            inputView.setText(input.toString());
        }
    }

    private void plus() {
        BigDecimal inputNumber = new BigDecimal(input.toString());
        amount = amount.add(inputNumber);
        clearInput();
        refreshView();
    }

    private void minus() {
        BigDecimal inputNumber = new BigDecimal(input.toString());
        amount = amount.subtract(inputNumber);
        clearInput();
        refreshView();
    }

    private void refreshView() {
        TextView amountValue = (TextView) findViewById(R.id.amount_value);
        amountValue.setText(amount.toString());
    }

    private void clearInput() {
        input.delete(0, input.length());
        inputView.setText("");
    }

    private boolean isInputValid() {
        try {
            new BigDecimal(input.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}