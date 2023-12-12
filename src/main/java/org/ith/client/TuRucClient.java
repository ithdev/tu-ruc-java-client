package org.ith.client;

import org.ith.model.BaseResponse;
import org.ith.model.Contribuyente;
import org.ith.model.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TuRucClient {

    @GET("api/contribuyente")
    Call<BaseResponse<Contribuyente>> getContribuyente(
            @Query(value="ruc", encoded=false) String ruc
    );

    @GET("api/contribuyente/search")
    Call<BaseResponse<SearchResponse>> searchContribuyentes(
            @Query(value="search", encoded=false) String search,
            @Query(value="page", encoded=false) Integer page
    );

}
