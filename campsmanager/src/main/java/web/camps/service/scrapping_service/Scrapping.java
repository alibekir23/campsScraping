package web.camps.service.scrapping_service;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import web.camps.model.Camping;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@EnableAsync
public class Scrapping {

    static LocalDate futureDate = LocalDate.now().plusMonths(1);
    static LocalDate d = futureDate;
    @Autowired
    Sandaya_Scrapping sandayaScrapping;
    @Autowired
    YellohVillage_Scrapping yellohVillage_scrapping;
    @Autowired
    Tohapi_Scrapping tohapi_scrapping;
    @Autowired
    Capfun_Scrapping capfun_scrapping;
    @Autowired
    Siblu_Scrapping siblu_scrapping;

    @Async
    @PostConstruct
  //  @Scheduled(cron = " 0 0 12 * * *")
    void postConstruct() throws InterruptedException, NotFoundException, Exception {
        ChromeDriver driver=new ChromeDriver();
        siblu_scrapping.All_Accommodations_Tohapi("https://www1.tohapi.fr/aquitaine/camping-atlantic-club-montalivet.php",driver);
      //  yellohVillage_scrapping.Price("30/09/2021", 1, 364);
    }
}
