package my.self.bsmg;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class BSMGApplication {

    public static void main(String[] args) {
        SpringApplication.run(BSMGApplication.class, args);
        log.info("---------------------The Server Start Successful------------------------");
    }
}
