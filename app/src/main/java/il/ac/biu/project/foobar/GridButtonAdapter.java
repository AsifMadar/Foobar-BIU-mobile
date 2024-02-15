package il.ac.biu.project.foobar;
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

    public GridButtonAdapter(List<String> buttonNames) {
        this.buttonNames = buttonNames;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_button, parent, false);
        return new ButtonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        String buttonText = buttonNames.get(position);
        holder.button.setText(buttonText);
    }

    @Override
    public int getItemCount() {
        return buttonNames.size();
    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;

        ButtonViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.grid_button);
        }
    }
}
