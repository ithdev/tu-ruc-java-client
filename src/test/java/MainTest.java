import com.google.gson.Gson;
import org.ith.client.TuRuc;
import org.ith.client.TuRucClient;
import org.ith.model.BaseResponse;
import org.ith.model.Contribuyente;
import org.ith.model.SearchResponse;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

public class MainTest {

    private static final String API_URL = "http://localhost:8080/";

    private TuRucClient getTuRucClient(){
        TuRuc tuRuc = new TuRuc(API_URL);
        return tuRuc.getClient();
    }

    @Test
    public void test() {
        System.out.println("Hello World!");
    }

    @Test
    public void getContribuyente() throws IOException {
        TuRucClient tuRucClient = getTuRucClient();
        Response<BaseResponse<Contribuyente>> response = tuRucClient.getContribuyente("31244-2").execute();
        if (response.isSuccessful()) {
            System.out.println(response.body());
        }else {
            assert response.errorBody() != null;
            System.out.println(new Gson().fromJson(response.errorBody().string(), BaseResponse.class));
        }
    }

    @Test
    public void searchContribuyentes() throws IOException {
        TuRucClient tuRucClient = getTuRucClient();
        Response<BaseResponse<SearchResponse>> response = tuRucClient.searchContribuyentes("ESTEBAN", 0).execute();
        if (response.isSuccessful()) {
            System.out.println(response.body());
        }else {
            assert response.errorBody() != null;
            System.out.println(new Gson().fromJson(response.errorBody().string(), BaseResponse.class));
        }
    }

}
