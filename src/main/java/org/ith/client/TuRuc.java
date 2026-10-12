package org.ith.client;

import com.alibaba.fastjson2.support.retrofit.Retrofit2ConverterFactory;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

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
        // Http Client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .callTimeout(callTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .build();
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(Retrofit2ConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(TuRucClient.class);
    }
}
