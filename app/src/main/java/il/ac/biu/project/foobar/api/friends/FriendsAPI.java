package il.ac.biu.project.foobar.api.friends;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendsAPI {
    private Retrofit retrofit;
    private FriendsWebServiceAPI friendsWebServiceAPI;

    public FriendsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://foo.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        friendsWebServiceAPI = retrofit.create(FriendsWebServiceAPI.class);
    }

    public FriendsWebServiceAPI getFriendsWebServiceAPI() {
        return friendsWebServiceAPI;
    }
}
