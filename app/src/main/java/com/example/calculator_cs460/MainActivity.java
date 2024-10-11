package com.example.calculator_cs460;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.google.android.material.button.MaterialButton;

/**
 * MainActivity class for the calculator app.
 * Handles button clicks and performs basic calculations.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TextViews for showing the result and the entered calculation
    TextView resultTv, solutionTv;

    // Buttons for calculator operations
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonAdd, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    /**
     * Called when the app starts.
     * Sets up the UI and links buttons to their functions.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_main);

        // Connect the TextViews to the layout
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        // Connect the buttons to the layout
        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.open_bracket);
        assignId(buttonBrackClose, R.id.close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonAdd, R.id.button_add);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equals);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(button0, R.id.button_0);
        assignId(buttonAC, R.id.button_allclear);
        assignId(buttonDot, R.id.button_decimal);

        // Adjust padding for system bars (like the status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Connect a button to its ID and set a click listener.
     *
     * @param btn The button to link.
     * @param id The ID of the button.
     */
    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this); // Set the button to respond to clicks
    }

    /**
     * Handles button clicks.
     * Updates the display and calculates the result.
     *
     * @param v The button that was clicked.
     */
    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        // Clear everything when "AC" is clicked
        if (buttonText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        // Display the result when "=" is clicked
        if (buttonText.equals("=")) {
            solutionTv.setText(resultTv.getText());
            return;
        }

        // Remove the last character when "C" is clicked
        if (buttonText.equals("C")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else {
            // Add the button's text to the input
            dataToCalculate += buttonText;
        }

        // Update the input display
        solutionTv.setText(dataToCalculate);

        // Calculate and show the result
        String finalResult = getResult(dataToCalculate);
        if (!finalResult.equals("Error")) {
            resultTv.setText(finalResult);
        }
    }

    /**
     * Calculates the result of the input using JavaScript.
     *
     * @param data The input to calculate.
     * @return The result, or "Error" if something goes wrong.
     */
    String getResult(String data) {
        try {
            // Set up the JavaScript engine
            Context context = Context.enter();
            context.setOptimizationLevel(-1); // Disable optimizations for compatibility
            Scriptable scriptable = context.initStandardObjects();

            // Evaluate the input as a JavaScript expression
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();

            // Remove ".0" if it's a whole number
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }

            return finalResult; // Return the result
        } catch (Exception e) {
            return "Error"; // Return "Error" if something goes wrong
        }
    }
}
