package il.ac.biu.project.foobar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class CreatePostActivity extends AppCompatActivity {
    private PostDetails postDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post); // Correctly sets the layout
        postDetails = getIntent().getParcelableExtra("postDetails");
        UserDetails user = UserDetails.getInstance();

        // Directly set the template using the current layout
        setTemplate(user);

        TextView postPublish = findViewById(R.id.post_publish);
        postPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishPost(user);
            }
        });
    }

    private void publishPost(UserDetails user) {
        EditText postContent = findViewById(R.id.user_input_create_post);
        postDetails.setUserInput(postContent.getText().toString());
        postDetails.setAuthorDisplayName(user.getDisplayName());
        postDetails.setAuthorProfilePicture(user.getImg());

        Intent returnIntent = new Intent();
        returnIntent.putExtra("modifiedPostDetails", postDetails);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void setTemplate(UserDetails user) {
        // Using findViewById to get the current views from the layout
        ImageView profileImage = findViewById(R.id.profile_picture_create_post);
        profileImage.setImageBitmap(user.getImg()); // Assuming user.getImg() returns the correct bitmap

        TextView nameView = findViewById(R.id.user_name_create_post);
        nameView.setText(user.getDisplayName());
    }
}

