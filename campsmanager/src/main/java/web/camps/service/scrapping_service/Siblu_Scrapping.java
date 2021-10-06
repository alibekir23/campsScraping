package web.camps.service.scrapping_service;

import javassist.NotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import web.camps.Log.TrackExecutionTime;
import web.camps.controller.Accommodation_Controller;
import web.camps.controller.Camping_Controller;
import web.camps.controller.Camping_Price_Controller;
import web.camps.controller.Region_Controller;
import web.camps.model.Accommodation;
import web.camps.repo.Camping_Repo;
import web.camps.repo.Scraping_Log_Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class Siblu_Scrapping {
    @Autowired
    private Camping_Controller camping_controller;
    @Autowired
    private Accommodation_Controller accommodation_controller;
    @Autowired
    private Camping_Price_Controller camping_price_controller;
    @Autowired
    private Region_Controller region_controller;
    @Autowired
    Scraping_Log_Repo scraping_log_repo;
    @Autowired
    Camping_Repo camping_repo;


    public void scroll(ChromeDriver driver) throws InterruptedException {
        for (int i = 0; i < driver.findElements(By.cssSelector("div.cppm-table__row__accotitle")).size(); i++) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElements(By.cssSelector("div.cppm-table__row__accotitle")).get(i));
            Thread.sleep(200);
        }
    }


    @Async
    @TrackExecutionTime
    public List<Accommodation> All_Accommodations_Tohapi(String camping_url, ChromeDriver driver) throws InterruptedException, NotFoundException {
        Accommodation accommodation = new Accommodation();
        List<Accommodation> accommodations = new ArrayList<>();
        ///////////////////////////////////////////////////


        ///////////////locations//////////////////////////////////////
        String text = "";
        String area = "";
        String nbroom = "";
        String price = "";
        String max_person = "";
        String min_person = "";
        driver.get(camping_url);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        scroll(driver);


        driver.findElement(By.id("ssolarge")).findElements(By.tagName("tbody")).get(1).findElement(By.tagName("td")).click();
        // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.cssSelector("button.ghostTertiary_3kvDg.button_3pCSR")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(200);
        driver.findElement(By.cssSelector("button.flatSecondary_15JdG.button_3pCSR")).click();
        Thread.sleep(200);

        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.info_qtVfT")));


        List<WebElement> refL = driver.findElements(By.cssSelector("div.info_qtVfT"));

        //   refL.forEach(e -> System.out.println(e.getText()));


        for (WebElement we : refL) {

            text = we.findElement(By.cssSelector("h4")).getText();

            min_person = "1";
            if (text.contains("/")) {
                max_person = text.substring(text.indexOf("/") - 1, text.indexOf("/") + 2);

                max_person = max_person.substring(max_person.indexOf("/") + 1, max_person.length());
                accommodation.setAccommodation_name(text.substring(0, text.indexOf("personnes") - 5));
                //System.out.println(accommodation.getAccommodation_name());
                text = text.replace(accommodation.getAccommodation_name() + " ", "");
                //System.out.println(text);
                nbroom = text.substring(text.indexOf("chambres") - 2, text.indexOf("chambres") - 1);
                // System.out.println(nbroom);
                accommodation.setNb_room(Integer.parseInt(nbroom));
                accommodation.setMin_person(Integer.parseInt(min_person));
                accommodation.setMax_person(Integer.parseInt(max_person));
                //System.out.println(accommodation);
            } else if(text.contains("personnes")) {
                accommodation.setAccommodation_name(text.substring(0, text.indexOf("personnes")-3));

                text = text.replace(accommodation.getAccommodation_name() + " ", "");
                max_person=text.substring(0,1);
                nbroom = text.substring(text.indexOf("chambres") - 2, text.indexOf("chambres") - 1);
                 //System.out.println(text);
                accommodation.setMax_person(Integer.parseInt(max_person));
                accommodation.setMin_person(Integer.parseInt(min_person));
                accommodation.setNb_room(Integer.parseInt(nbroom));


            }
            System.out.println(accommodation);


            ////////////////////accommodation/////////////////////////////////

            //nbroom=text.substring();
            //    System.out.println(nbroom);
/*

                accommodation.setAccommodation_name(text.substring(0,text.indexOf("personnes")));
               System.out.println(accommodation);

                nbroom=text.substring(text.indexOf("personne")+1);
                nbroom=nbroom.substring(nbroom.indexOf(" "));
                System.out.println(nbroom);


            accommodations.add(accommodation);
            accommodation = new Accommodation();
            /////////////////////////////////////////////////////////////////
    */
        }


        ///////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////
        // System.out.println(accommodations);
        return accommodations;
    }


}
