package il.ac.biu.project.foobar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private ArrayList<String> friendsList;

    public FriendsAdapter(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String friend = friendsList.get(position);
        holder.friendTextView.setText(friend);

        // Handle click event to show bottom sheet dialog
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show bottom sheet dialog with menu options
                showBottomSheetDialog(v.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView friendTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendTextView = itemView.findViewById(R.id.friendTextView);
        }
    }

    private void showBottomSheetDialog(Context context) {
        // Inflate the layout for menu options
        View view = LayoutInflater.from(context).inflate(R.layout.menu_options, null);

        // Create a BottomSheetDialog
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();
    }
}
