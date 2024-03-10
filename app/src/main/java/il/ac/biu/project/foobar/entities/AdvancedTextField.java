package il.ac.biu.project.foobar.entities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * An advanced text field that provides input validation and callback functionality.
 */
public class AdvancedTextField {

    private final EditText editText;
    private ValidationFunction validationFunc;
    private final InputCallback inputCallback;
    private String errorMessage;

    /**
     * Constructs an AdvancedTextField with the specified EditText and input callback.
     *
     * @param editText      The EditText widget to be associated with this AdvancedTextField.
     * @param inputCallback The callback to be invoked when the input changes.
     */
    public AdvancedTextField(EditText editText, InputCallback inputCallback) {
        this.editText = editText;
        this.inputCallback = inputCallback;
        setValidationListener();
    }

    /**
     * Constructs an AdvancedTextField with the specified EditText, input callback, and validation function.
     *
     * @param editText      The EditText widget to be associated with this AdvancedTextField.
     * @param inputCallback The callback to be invoked when the input changes.
     * @param validationFunc The validation function to check the input validity.
     */
    public AdvancedTextField(EditText editText, InputCallback inputCallback, ValidationFunction validationFunc) {
        this.editText = editText;
        this.inputCallback = inputCallback;
        this.validationFunc = validationFunc;
        setValidationListener();
    }

    private void setValidationListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                // if there's a validationFunc, it will use the colors
                if (validationFunc != null) {
                    if (validationFunc.isValid(input)) {
                        setEditTextColor(Color.GREEN);
                        editText.setError(null);

                    } else {
                        setEditTextColor(Color.RED);
                        editText.setError(errorMessage);
                    }
                }
                // updates the calling function the input that the user typed
                inputCallback.onInputChanged(input);
            }
        });
    }

    /**
     * The error message that will display if the input is invalid
     * @param errMessage The error message.
     */
    public void setErrorMessage(String errMessage) {
        this.errorMessage = errMessage;
    }

    private void setEditTextColor(int color) {
        Drawable background = editText.getBackground();

        if (background instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setStroke(2, color); // Adjust the stroke width as needed
        } else {
            GradientDrawable newDrawable = new GradientDrawable();
            newDrawable.setShape(GradientDrawable.RECTANGLE);
            newDrawable.setColor(Color.WHITE);
            newDrawable.setStroke(2, color);
            editText.setBackground(newDrawable);
        }
    }

    /**
     * Interface for providing a validation function for the AdvancedTextField.
     */
    public interface ValidationFunction {
        /**
         * Checks the validity of the input.
         *
         * @param input The input string to validate.
         * @return True if the input is valid, false otherwise.
         */
        boolean isValid(String input);
    }

    /**
     * Interface for handling input changes in the AdvancedTextField.
     */
    public interface InputCallback {
        /**
         * Called when the input changes.
         *
         * @param input The updated input string.
         */
        void onInputChanged(String input);
    }
}
