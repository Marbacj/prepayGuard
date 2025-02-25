package com.mapoh.ppg.client;

import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSDKClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import org.slf4j.event.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mabohv
 * @date 2025/2/24 16:49
 */

@Configuration
public class PaypalSDKClient {

    private String PAYPAL_CLIENT_ID = "AT_6ZsCpF_XORt9tS3a7zOnkVBK09pvqgZfxCc7zCl9uSQve4ImbFGlKm-BvHtXHStmFEuBxIcwhha2D";

    private String PAYPAL_CLIENT_SECRET = "EOkwQyNKus4dlVFRsFZeL8r-ThDqfOZoRzhUc5gZV3pwUm6hm-wpM_dLtOk2_3yAU9wrj9Cwfzr7AsDc";

    @Bean
    public PaypalServerSDKClient paypalClient(){
        return new PaypalServerSDKClient.Builder()
                .loggingConfig(builder -> builder
                        .level(Level.DEBUG)
                        .requestConfig(logConfigBuilder -> logConfigBuilder.body(true))
                        .responseConfig(logConfigBuilder -> logConfigBuilder.headers(true)))
                .httpClientConfig(configBuilder -> configBuilder
                        .timeout(0))
                .environment(Environment.SANDBOX)
                .clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(
                        PAYPAL_CLIENT_ID,
                        PAYPAL_CLIENT_SECRET)
                        .build())
                .build();
    }
}
