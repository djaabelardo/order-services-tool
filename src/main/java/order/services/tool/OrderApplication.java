package order.services.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "order.services" })
@SpringBootApplication
public class OrderApplication {
    public static void main(final String[] args)
    {
        SpringApplication.run(OrderApplication.class, args);
    }
}
