package me.kqlqk.behealthy.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class View extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(View.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(View.class);
    }
}
/*
0 logout button instead sign in/out in header-authorize
1 tests
2 if userCondition not set, kcals counter don't access
3 button edit condition
4 button to kcals counter
 */
