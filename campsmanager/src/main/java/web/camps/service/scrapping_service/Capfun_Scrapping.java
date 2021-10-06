package web.camps.service.scrapping_service;

import javassist.NotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import web.camps.controller.Accommodation_Controller;
import web.camps.controller.Camping_Controller;
import web.camps.controller.Camping_Price_Controller;
import web.camps.controller.Region_Controller;
import web.camps.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class Capfun_Scrapping {
    @Autowired
    private Camping_Controller camping_controller;
    @Autowired
    private Accommodation_Controller accommodation_controller;
    @Autowired
    private Camping_Price_Controller camping_price_controller;
    @Autowired
    private Region_Controller region_controller;





/*
    @Async
    public CompletableFuture<List<Camping>> Camping_Scrape_capfun() throws InterruptedException, NotFoundException {
        //////////////driver///////////
        ChromeDriver driver = new ChromeDriver();
        /////////////////////////////////

        ////////Region///////////////////////////
        Region region = new Region();
        ////////////////////////////////////////

        ////////site url/////////////////////////////////////
        String Sandaya_URL = "https://www.capfun.com/camping-france-FR-pays.html";
        //////////////////////////////////////


        ////urls+campings list//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        Camping camp = new Camping();
        List<Opening_Period> opening_periods = new ArrayList<>();
        List<Accommodation> accommodations = new ArrayList<>();
        //////////////////////////////


        ////////get all campings url//////////
        driver.get(Sandaya_URL);
        Thread.sleep(1000);
        List<WebElement> refList = driver.findElements(By.cssSelector("div.col-sm-4.mt-3"));
        for (WebElement we : refList) {
            System.out.println(we.findElement(By.className("cc-titre")).getText());
            System.out.println(we.findElement(By.cssSelector("span.cc-localisation.mt-2")).findElement(By.tagName("a")).getText());
            System.out.println(we.findElement(By.className("mr-1")).getAttribute("alt"));



        }
        ///////////////////////////////////////////

return null;
    }

    @Async
    public void price(String start_date, String end_date) throws InterruptedException, NotFoundException {
        ////init////////////////////
        String start = start_date;
        String end = end_date;
        ChromeDriver driver = new ChromeDriver();
        Camping_Price camping_price = new Camping_Price();
        Accommodation accommodation = new Accommodation();
        ////urls+campings list//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        List<String> urls_acc = new ArrayList<String>();
        List<Camping_Price> camping_prices = new ArrayList<>();
        String URL = "https://www.capfun.com/resultats.html?lang=FR&pays_region=pays_1&hebergement=TOUS_LOCATIF&btnFormRecherche.x=150&btnFormRecherche.y=35&type_sejour=libre&cs_date_arrivee=&cs_date_depart=";
        ///////////////////////////////////////////////////
        end_date = end_date.replace("/", "%2F");
        start_date = start_date.replace("/", "%2F");
        URL = URL.replace("date_arrivee=", "date_arrivee=" + start_date);
        URL = URL.replace("date_depart=", "date_depart=" + end_date);

        /////////////////////////////////////////////////

        driver.get(URL);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // js.executeScript("window.scrollBy(0,2000)", "");

        Thread.sleep(1000);

        List<WebElement> refL = driver.findElements(By.cssSelector("div.row.entourage.mb-4.p-2"));
        for (WebElement we : refL) {


        }

    }
*/
}