package il.ac.biu.project.foobar.api.users;

import il.ac.biu.project.foobar.entities.requests.UserDetailsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersWebServiceAPI {
    @GET("{id}") // Use @Path to specify that "id" is a variable part of the URL
    Call<UserDetailsResponse> getUserDetails(@Path("id") String userID, @Header("Authorization") String jwtToken);
    // Add @Header to dynamically add the JWT token to the request
}
