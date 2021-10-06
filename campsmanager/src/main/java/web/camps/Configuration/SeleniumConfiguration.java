package web.camps.Configuration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {

    /*
    @Bean
    public ChromeDriver driver(){
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.addArguments("--headless");

        return new ChromeDriver(chromeOptions);
    }*/
}
