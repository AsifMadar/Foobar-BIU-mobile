package il.ac.biu.project.foobar.viewmodels;

import static il.ac.biu.project.foobar.MyApplication.context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
public class FriendRequestsViewModel extends ViewModel {
    private LiveData<ArrayList<String>> friendRequests;


    public FriendRequestsViewModel() {
    }
}
