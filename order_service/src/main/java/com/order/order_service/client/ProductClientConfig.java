package com.order.order_service.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.order.order_service.exception.ProductNotFoundException;

@Configuration
public class ProductClientConfig {

  @Bean
  @Primary
  public RestClient.Builder standardRestClientBuilder() {

    return RestClient.builder();

  }

  @Bean
  @LoadBalanced
  public RestClient.Builder restClientBuilder() {
    return RestClient.builder();
  }

  @Bean
  public ProductClient restClientInterface(@Qualifier("restClientBuilder") RestClient.Builder restClientBuilder) {

    RestClient restClient = restClientBuilder
        .baseUrl("http://product-service")
        .defaultStatusHandler(
            status -> status == HttpStatus.NOT_FOUND,
            (request, response) -> {
              throw new ProductNotFoundException("Product not found with id: " + request.getURI().getPath().substring(request.getURI().getPath().lastIndexOf('/') + 1));
            })
        .build();

    RestClientAdapter adapter = RestClientAdapter.create(restClient);

    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    ProductClient productClient = factory.createClient(ProductClient.class);
    return productClient;

  }

}
