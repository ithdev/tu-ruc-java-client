package org.ith.client;

import org.ith.model.BaseResponse;
import org.ith.model.Contribuyente;
import org.ith.model.DataTableResponse;
import org.ith.model.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TuRucClient {

    @GET("api/contribuyente")
    Call<BaseResponse<Contribuyente>> getContribuyenteWithParam(
            @Query(value="ruc", encoded=false) String ruc
    );

    @GET("api/contribuyente/{ruc}")
    Call<BaseResponse<Contribuyente>> getContribuyenteWithPathVariable(
            @Path(value="ruc", encoded=false) String ruc
    );

    @GET("api/contribuyente/search")
    Call<BaseResponse<SearchResponse>> searchContribuyentesSimple(
            @Query(value="search", encoded=false) String search,
            @Query(value="page", encoded=false) Integer page
    );

    @GET("api/contribuyente/table")
    Call<DataTableResponse<Contribuyente>> searchContribuyentesTable(
            @Query(value="draw", encoded=false) int draw,
            @Query(value="start", encoded=false) int start,
            @Query(value="length", encoded=false) int length,
            @Query(value="search", encoded=false) String search
    );

}
