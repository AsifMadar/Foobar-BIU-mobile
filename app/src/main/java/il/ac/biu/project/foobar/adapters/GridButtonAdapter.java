package il.ac.biu.project.foobar.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import il.ac.biu.project.foobar.R;

public class GridButtonAdapter extends RecyclerView.Adapter<GridButtonAdapter.ButtonViewHolder> {
    private List<String> buttonNames;
    // Constructor to initialize the adapter with a list of button names
    public GridButtonAdapter(List<String> buttonNames) {
        this.buttonNames = buttonNames;
    }
    // Method to create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view by inflating the grid_item_button layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_button, parent, false);
        return new ButtonViewHolder(itemView);
    }
    // Method to replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        // Get the button text at the specified position
        String buttonText = buttonNames.get(position);
        // Set the text of the button
        holder.button.setText(buttonText);
    }
    // Method to return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return buttonNames.size();
    }
    // ViewHolder class to hold references to the views for each item in the RecyclerView
    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;
        // Constructor to initialize the ViewHolder
        ButtonViewHolder(View itemView) {
            super(itemView);
            // Get a reference to the Button view from the inflated layout
            button = itemView.findViewById(R.id.grid_button);
        }
    }
}
