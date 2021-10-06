package web.camps.service.scrapping_service;

import javassist.NotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import web.camps.Log.TrackExecutionTime;
import web.camps.model.Accommodation;
import web.camps.model.Camping;
import web.camps.model.Camping_Price;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service

public class Tohapi_Scrapping {




    @Async
    @TrackExecutionTime
    public List<Accommodation> All_Accommodations_YellohVillage(String camping_url, ChromeDriver driver) throws InterruptedException, NotFoundException {
        Accommodation accommodation = new Accommodation();
        List<Accommodation> accommodations = new ArrayList<>();
        ///////////////////////////////////////////////////


        ///////////////locations//////////////////////////////////////
        String area = "";
        String nbroom = "";
        String price = "";
        String max_person = "";
        String min_person = "";
        driver.get(camping_url + "/nos_locations#content");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)", "");
        Thread.sleep(500);
        List<WebElement> refL = driver.findElements(By.cssSelector("div.block-left"));
        for (WebElement we : refL) {
            ////////////////////accommodation/////////////////////////////////
            accommodation.setAccommodation_name(we.findElement(By.tagName("a")).getText());

            //System.out.println(we.findElement(By.className("icon-person")).getText());
            if (we.findElement(By.className("icon-person")).getText().contains("/")) {

                max_person = we.findElement(By.className("icon-person")).getText();
                min_person = max_person.substring(0, max_person.indexOf("/"));
                max_person = max_person.substring(max_person.indexOf("/") + 1);

                accommodation.setMin_person(Integer.parseInt(min_person));
                accommodation.setMax_person(Integer.parseInt(max_person));

            } else {
                accommodation.setMax_person(Integer.parseInt(we.findElement(By.className("icon-person")).getText()));
                accommodation.setMin_person(1);
            }

            area = we.findElements(By.tagName("span")).get(0).getText();
            try {
                nbroom = we.findElements(By.tagName("span")).get(1).getText();
                nbroom = nbroom.replace(" chambres", "");
                area = area.replace(" m²", "");
                accommodation.setArea(Double.parseDouble(area));
                accommodation.setNb_room(Integer.parseInt(nbroom));

            } catch (Exception e) {
                nbroom = null;
                // accommodation.setArea(null);
                //accommodation.setNb_room(null);
            }
            accommodations.add(accommodation);
            accommodation = new Accommodation();
            /////////////////////////////////////////////////////////////////
        }

        ///////////////////////////////////////////////////////////////

/*
        ///////////////////emplacements/////////////////////////////
        driver.get(camping_url + "/nos_emplacements_de_camping#menu");
        js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(500);
        refL = driver.findElements(By.cssSelector("div.block-left"));
        for (WebElement we : refL) {
            ////////////////////accommodation/////////////////////////////////
            accommodation.setAccommodation_name(we.findElement(By.tagName("a")).getText());
            // accommodation.setNb_person(a.findElement(By.className("nb")).getText());
            area = we.findElements(By.tagName("span")).get(0).getText();
            accommodation.setArea(Double.parseDouble(area));
            accommodations.add(accommodation);
            accommodation = new Accommodation();
            /////////////////////////////////////////////////////////////////
        }

*/
        /////////////////////////////////////////////////////////
        System.out.println(accommodations);
        return accommodations;
    }































    /*
    @Async
    public CompletableFuture<List<Camping>> Camping_Scrape_Tohapi() throws InterruptedException {
        ChromeDriver driver = new ChromeDriver();
        String URL = "https://www.tohapi.fr/camping-france/";
        Thread.sleep(1000);
        ////urls+campings list//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        Camping camp = new Camping();
        ////////////////////////////////////

        ////////campings url//////////
        driver.get(URL);
        Thread.sleep(1000);
        //////////////////////////////

        //////////get all campings url///////////////
        List<WebElement> refList = driver.findElements(By.className("js-card-child-link"));
        for (WebElement we : refList) {
            urls.add(we.getAttribute("href"));
        }
        ///////////////////////////////////////////

        ///////scrape campings/////////////////
        for (int i = 0; i < 1; i++) {
            driver.get(urls.get(i));
            Thread.sleep(1000);


            ////////////stars////////////////////////////
            System.out.println(driver.findElements(By.cssSelector("span.icon.icon-star")).size());
            camp.setNb_stars(Double.valueOf(driver.findElements(By.cssSelector("span.icon.icon-star")).size()));
            ///////////////////


            //name//////


            camp.setCamping_name(driver.findElement(By.className("campsite-header__cards__info-title")).getText());
            System.out.println(driver.findElement(By.className("campsite-header__cards__info-title")).getText());
            /////////////////

            ///////////////////////////////
            ///////scroll///////////
            String s = new String();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,5000)", "");
            Thread.sleep(1000);
            System.out.println(driver.findElements(By.className("campsite-information__general__content")).get(1).getText());
            s = driver.findElements(By.className("campsite-information__general__content")).get(0).getText();
            System.out.println(s);
            camp.setAltitude(s.substring(0, s.indexOf(" ")));
            camp.setLongitude(s.substring(s.indexOf("/") + 2));

            /////////////////////////////////////

            campings.add(i, camp);
            camp = new Camping();

        }
        System.out.println(campings);
        return CompletableFuture.completedFuture(campings);
    }
///////////////////////////////////////////////////////


    public List<Accommodation> Accomodation_Scrapping_Sandaya(String camping_url) throws InterruptedException, NotFoundException {
        ///////////init/////////////////////
        ChromeDriver driver = new ChromeDriver();
        List<Accommodation> accommodations = new ArrayList<>();
        Accommodation accommodation = new Accommodation();
        List<String> urls = new ArrayList<String>();
        /////////////////////////////////////////////////


        /////////get accs url list////////////////////
        driver.get(camping_url + "/nos-locations#menu");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)", "");
        Thread.sleep(1000);

        List<WebElement> refList = driver.findElements(By.cssSelector("a.name.bloclink"));
        for (WebElement we : refList) {
            if (we.getAttribute(("href")).toString().contains("nos-locations")) {
                urls.add(we.getAttribute("href"));
            }
        }
        List<WebElement> attrs = new ArrayList<>();
///////////////////////////////////////////////////////

        //////////////scrape acc////////////////////////////
        for (int i = 0; i < 1; i++) {
            driver.get(urls.get(i));


            js.executeScript("window.scrollBy(0,500)", "");


            WebElement ac = driver.findElement(By.cssSelector("div.content.clearbg"));

            ////////acc name//////////////
            accommodation.setAccommodation_name(ac.findElement(By.tagName("h1")).getText());
            //System.out.println(ac.findElement(By.tagName("h1")).getText());
            /////////////////////////////

            /////////Acc attributes////////////////////
            attrs = driver.findElements(By.cssSelector("div.col-4.spec-item"));
            attrs.forEach(a -> System.out.println(a.getText()));

            accommodation.setArea(attrs.get(0).getText());
            accommodation.setNb_person(attrs.get(1).getText());
            accommodation.setNb_room(attrs.get(2).getText());
            ///////////////////////////////////////////

            js.executeScript("window.scrollBy(0,500)", "");

            //////acc description///////////////////////////
            //System.out.println(ac.findElement(By.className("ezrichtext-field")).findElement(By.tagName("p")).getText());
            accommodation.setDescription(ac.findElement(By.className("ezrichtext-field")).findElement(By.tagName("p")).getText());
            ////////////////////////////////////////////

            ////add acc/////////////////////////////
            accommodations.add(accommodation);
            ///////////////////////////////////////

            accommodation = new Accommodation();
        }
        driver.quit();
        return accommodations;
    }


    @Async
    public void price(String start_date, Integer nbr_nights, int nb_personnes) throws InterruptedException, NotFoundException {
        ////init////////////////////
        String params = "";
        String nbr = "";
        String url = "";
        String start = start_date;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        ChromeDriver driver = new ChromeDriver(options);
        Camping_Price camping_price = new Camping_Price();
        Accommodation accommodation = new Accommodation();
        ////urls+campings list//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        List<String> urls_acc = new ArrayList<String>();
        List<Camping_Price> camping_prices = new ArrayList<>();
        String URL = "https://www.tohapi.fr/recherche-camping/camping-france/P/L/ALL";
        System.out.println(URL);
        ///////////////////////////////////////////////////


        URL = URL.replace("ALL", "ALL/" + start_date + "/" + nbr_nights + "/" + nb_personnes + "/0/?AC=1");
        System.out.println(URL);
        /////////////////////////////////////////////////
        driver.manage().deleteAllCookies();
        driver.get(URL);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,2000)", "");
        Thread.sleep(500);

        driver.findElement(By.id("ssobar")).findElements(By.tagName("table")).get(1).findElement(By.tagName("td")).click();

        List<WebElement> refL = driver.findElements(By.className("result-cards__cta__price"));
        for (WebElement we : refL) {
            urls.add(we.getAttribute("href"));
        }
        System.out.println(urls);

        String camping_name = "";
        List<WebElement> ac = new ArrayList<>();
        String emp_url = "";
        for (int i = 0; i < 1; i++) {

            /////////////////////////:::location///////////////////////////
            url = urls.get(i);
            url = url.replace("www1", "www");
            driver.manage().deleteAllCookies();
            driver.get(urls.get(i));
            Thread.sleep(500);
            //   driver.get(url);

            int sc = 0;

            camping_name = driver.findElement(By.className("campsite-header__cards__info-title")).getText();
            driver.findElements(By.cssSelector("a.js-cpnav-link.campsite-nav__item__link")).get(7).click();
            js.executeScript("window.scrollBy(0,200)", "");
            Thread.sleep(1000);

            ac = driver.findElements(By.className("cppm-table__row"));
            // ac.forEach(a-> System.out.println(a.getText()));

            driver.findElement(By.cssSelector("span.cppm-table__row__case--roomtype_more")).click();
            Thread.sleep(1000);

            System.out.println(driver.findElement(By.className("mfp-content")).getText());
            driver.findElement(By.className("mfp-close")).click();
            /////////////////////////////////////////////////////////////

            //System.out.println(camping_name);
/*
            String area = "";
            String nbroom = "";
            String price = "";

            for (WebElement a : ac) {

                ////////////////////accommodation/////////////////////////////////
                try {
                    if (a.findElement(By.className("block-price")).getText() != null) {

                        accommodation.setAccommodation_name(a.findElement(By.className("block-left")).findElement(By.tagName("a")).getText());

                        accommodation.setNb_person(a.findElement(By.className("icon-person")).getText());
                        area = a.findElement(By.className("block-left")).findElements(By.tagName("span")).get(0).getText();
                        try {
                            nbroom = a.findElement(By.className("block-left")).findElements(By.tagName("span")).get(1).getText();
                            ;
                            accommodation.setArea(area);
                            accommodation.setNb_room(nbroom);
                            accommodation.setType("location");
                        } catch (Exception e) {
                            nbroom = "0 chambres";
                            accommodation.setArea("");
                            accommodation.setNb_room(nbroom);
                            accommodation.setType("empalcement");
                        }

                        System.out.println(accommodation);
                        /////////////////////////////////////////////////////////////////


                        ////////camping price////////////////////////////////////////
                        try {
                            price = a.findElement(By.className("block-price")).getText();
                            price = price.substring(0, price.indexOf(" "));
                            camping_price.setPrice(Double.parseDouble(price));
                            camping_price.setPeriod(a.findElement(By.className("nb-nuit")).getText());
                            System.out.println(price);
                        } catch (Exception e) {
                            // camping_price.setPrice(null);
                            System.out.println("no price");
                            //camping_price.setPeriod(null);
                        }

                        camping_price.setPeriod(a.findElement(By.className("nb-nuit")).getText());
                        camping_price.setNb_person(a.findElement(By.className("icon-person")).getText());
                        camping_price.setDate(start);
                        System.out.println(camping_price);
                        ////////////////////////////////////////////////////////////
                    }
                } catch (Exception e) {
                    System.out.println("not available");
                }

                // camping_price_controller.addPrice(camping_price,accommodation,camping_name);
                camping_price = new Camping_Price();
                accommodation = new Accommodation();

            }


//////////////////////////////////////////////////////////////////


        }


    }
    */
}
