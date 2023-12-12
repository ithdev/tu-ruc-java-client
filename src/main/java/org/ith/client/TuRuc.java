package org.ith.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class TuRuc {

    private static final int DEFAULT_TIMEOUT = 60;

    private final String apiUrl;
    private final int callTimeout;
    private final int writeTimeout;
    private final int readTimeout;
    private final int connectTimeout;


    public TuRuc(String apiUrl) {
        this.apiUrl = apiUrl;
        this.callTimeout = DEFAULT_TIMEOUT;
        this.writeTimeout = DEFAULT_TIMEOUT;
        this.readTimeout = DEFAULT_TIMEOUT;
        this.connectTimeout = DEFAULT_TIMEOUT;
    }

    public TuRuc(String apiUrl, int callTimeout, int writeTimeout, int readTimeout, int connectTimeout) {
        this.apiUrl = apiUrl;
        this.callTimeout = callTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    public TuRucClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Http Client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .callTimeout(callTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .build();
        //Serializator
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(TuRucClient.class);
    }

}
