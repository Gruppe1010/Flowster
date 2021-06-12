package gruppe10.flowster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlowsterApplication {

    public static void main(String[] args)
    {
        int a = 0;
        int b = 3;
        int c = 3;

        c = a < b ? 4 : 5;


        System.out.println(a);


        SpringApplication.run(FlowsterApplication.class, args);

    }

}
