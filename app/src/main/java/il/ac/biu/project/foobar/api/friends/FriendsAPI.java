package il.ac.biu.project.foobar.api.friends;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendsAPI {
    private static FriendsAPI instance;
    private final Retrofit retrofit;
    private final FriendsWebServiceAPI friendsWebServiceAPI;

    private FriendsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://foo.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        friendsWebServiceAPI = retrofit.create(FriendsWebServiceAPI.class);
    }

    public static FriendsAPI getInstance() {
        if (instance == null) {
            instance = new FriendsAPI();
        }
        return instance;
    }

    public FriendsWebServiceAPI getFriendsWebServiceAPI() {
        return friendsWebServiceAPI;
    }
}
