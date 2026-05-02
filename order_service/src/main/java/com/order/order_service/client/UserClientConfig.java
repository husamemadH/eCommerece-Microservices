package com.order.order_service.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.order.order_service.exception.ProductNotFoundException;

@Configuration
public class UserClientConfig {

  @Bean
  public UserClient restUserClientInterface(@Qualifier("restClientBuilder") RestClient.Builder restClientBuilder) {

    RestClient restClient = restClientBuilder
        .baseUrl("http://user-service")
        .defaultStatusHandler(
            status -> status == HttpStatus.NOT_FOUND,
            (request, response) -> {
              throw new ProductNotFoundException("User not found with id: "
                  + request.getURI().getPath().substring(request.getURI().getPath().lastIndexOf('/') + 1));
            })
        .build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);

    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    UserClient userClient = factory.createClient(UserClient.class);

    return userClient;

  }

}
