package web.camps.service.scrapping_service;

import javassist.NotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import web.camps.Log.TrackExecutionTime;
import web.camps.controller.Accommodation_Controller;
import web.camps.controller.Camping_Controller;
import web.camps.controller.Camping_Price_Controller;
import web.camps.controller.Region_Controller;
import web.camps.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class Sandaya_Scrapping {

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
    @TrackExecutionTime
    public CompletableFuture<List<Camping>> All_Camps_Sandaya() throws InterruptedException, NotFoundException, ParseException {
        //////////////driver///////////
        ChromeDriver driver2 = new ChromeDriver();
        /////////////////////////////////
        ////////Region///////////////////////////
        Region region = new Region();
        ////////////////////////////////////////
        ////////site url/////////////////////////////////////
        String Sandaya_URL = "https://www.sandaya.fr/nos-campings";
        //////////////////////////////////////
        /////////////opening period/////////
        String s = "";
        String op = "";
        String end = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String d = "";
        Date opening = null;
        Date ending = null;
        Opening_Period p = new Opening_Period();
        ///////////////////////////////////
        ////urls+campings list//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        Camping camp = new Camping();
        List<Opening_Period> opening_periods = new ArrayList<>();
        List<Accommodation> accommodations = new ArrayList<>();
        //////////////////////////////
        ////////get all campings url//////////
        driver2.get(Sandaya_URL);
        Thread.sleep(1000);
        driver2.findElement(By.cssSelector("button.didomi-components-button.didomi-button.didomi-dismiss-button.didomi-components-button--color.didomi-button-highlight.highlight-button")).click();
        Thread.sleep(500);
        List<WebElement> refList = driver2.findElements(By.className("bloclink"));
        for (WebElement we : refList) {
            urls.add(we.getAttribute("href"));
        }
        ///////////////////////////////////////////

        ///////scrape by camping////////////////
        for (int i = 0; i < urls.size(); i++) {
            driver2.get(urls.get(i));
            Thread.sleep(500);

            //name//////
            camp.setCamping_name(driver2.findElement(By.className("main-title")).getText());
            /////////////////

            //////////region////////////////////////////////
            region.setRegion_name(driver2.findElement(By.className("region")).getText());
            region.setCity_name(driver2.findElement(By.className("city")).getText());
            //////////////////////////////////////////////


            //////note/////////////
            try {
                camp.setNote(Double.parseDouble(driver2.findElement(By.className("note")).getText()));
                //  System.out.println(driver2.findElement(By.className("note")).getText());
            } catch (Exception e) {
                camp.setNote(null);
            }
            /////////////////


            //////lien map////////////
            WebElement map_url = driver2.findElement(By.className("minimap-overlay-link"));
            ////////////

            ///////scroll///////////
            JavascriptExecutor js = (JavascriptExecutor) driver2;
            js.executeScript("window.scrollBy(0,1000)", "");
            Thread.sleep(500);
            ////////////////////////////////////


            //////////opening period/////////////////////////
            List<WebElement> seasons = driver2.findElement(By.className("usefull-info-block")).findElements(By.className("closed-note-season"));
            for (WebElement season : seasons) {
                d = season.getText();
                s = d.substring(d.indexOf("202"));
                d = d.replace(s, "");
                p.setSeason(s);
                d = d.replace("- du ", "");
                op = d.substring(0, d.indexOf("au"));
                op = op + s;
                end = d.substring(d.indexOf("au "));
                end = end.replace("au ", "");
                end = end + s;
                opening = formatter.parse(op);
                ending = formatter.parse(end);
                System.out.println(ending);
                p.setOpening_date(opening);
                p.setClosing_date(ending);

                opening_periods.add(p);
                p = new Opening_Period();
            }
            //System.out.println(opening_periods);
            ////////////////////////

            //////////map set lang and lat////
            driver2.get(map_url.getAttribute("href"));
            js.executeScript("window.scrollBy(0,1200)", "");
            Thread.sleep(500);
            camp.setAltitude(driver2.findElement(By.id("poi-block-map")).getAttribute("data-camping-latitude"));
            camp.setLongitude(driver2.findElement(By.id("poi-block-map")).getAttribute("data-camping-longitude"));
            /////////////////

            ///////////////////accommodations call accommodation scrapping////////////////
            accommodations = Acc(urls.get(i), driver2);
            ////////////////////////////////////////////////////////////////

            /////////////add region////////////////
            region_controller.addRegion(region);
            ///////////////////////////////////

            ///////Add camp+acc+opening period//////////////////////////
            campings.add(camping_controller.addcamps(1, camp, opening_periods, accommodations, region));
            /////////////////////////////////////////////////
            camp = new Camping();
            region = new Region();
            opening_periods = new ArrayList<>();

        }


        driver2.quit();
        return CompletableFuture.completedFuture(campings);

    }
    /////////////////////////////////////////////////////////////////////////////////////


    @Async
    @TrackExecutionTime
    public List<Accommodation> Acc(String camping_url, ChromeDriver driver) throws InterruptedException, NotFoundException {

        Accommodation accommodation = new Accommodation();
        List<Accommodation> accommodations = new ArrayList<>();
        ///////////////////////////////////////////////////

        ///////////////locations//////////////////////////////////////
        String area = "";
        String nbroom = "";
        driver.get(camping_url + "/nos-locations#menu");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,2000)", "");
        Thread.sleep(500);
        List<WebElement> refL = driver.findElement(By.cssSelector("div.bloc-list.row.position-relative")).findElements(By.className("infos"));
        for (WebElement we : refL) {
            try {
                ////////////////////accommodation/////////////////////////////////
                accommodation.setAccommodation_name(we.findElement(By.cssSelector("a.name.bloclink")).getText());
                accommodation.setNb_person(we.findElement(By.className("nb")).getText());
                area = we.findElement(By.className("size")).getText();
                nbroom = area.substring(area.indexOf(","));
                nbroom = nbroom.replace("'", "");
                area = area.substring(0, area.indexOf("m²"));
            } catch (Exception e) {
                nbroom = "0 chambres";
                area = "";
            }
            accommodation.setArea(area + "m²");
            accommodation.setNb_room(nbroom);
            accommodation.setType("location");
            accommodations.add(accommodation);
            accommodation = new Accommodation();
            /////////////////////////////////////////////////////////////////
        }
        ///////////////////////////////////////////////////////////////


        try {
            ///////////////////emplacements/////////////////////////////
            driver.get(camping_url + "/nos-emplacements#menu");
            js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,500)", "");
            Thread.sleep(500);
            refL = driver.findElement(By.cssSelector("div.bloc-list.row")).findElements(By.className("infos"));
            for (WebElement we : refL) {

                ////////////////////accommodation/////////////////////////////////
                accommodation.setAccommodation_name(we.findElement(By.cssSelector("a.name.bloclink")).getText());
                area = we.findElement(By.className("size")).getText();
                area = area.substring(0, area.indexOf("m²"));
                accommodation.setArea(area + "m²");
                accommodation.setType("emplacement");
                accommodations.add(accommodation);
                accommodation = new Accommodation();
                /////////////////////////////////////////////////////////////////
            }
        } catch (Exception e) {
            System.out.println("pas d'empalcements");
        }


        /////////////////////////////////////////////////////////
        return accommodations;
    }


    @Async
    @TrackExecutionTime
    public void Price_Sandaya(String start_date, String end_date, int nb_personnes) throws InterruptedException, NotFoundException {
        ////init////////////////////
        String camping_name = "";
        List<WebElement> ac = new ArrayList<>();
        List<Camping_Price> prices= new ArrayList<>();
        String area = "";
        String nbroom = "";
        String price = "";
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
        List<Accommodation> accommodations = new ArrayList<>();
        String URL = "https://www.sandaya.fr/nos-campings?availability_search%5Btype%5D=location&availability_search%5Bnb_people%5D=&availability_search%5Bstart%5D=&availability_search%5Bend%5D=&availability_search%5Bsubmit%5D=&availability_search%5Blang%5D=fr";
        ///////////////////////////////////////////////////
        end_date = end_date.replace("/", "%2F");
        start_date = start_date.replace("/", "%2F");
        URL = URL.replace("start%5D=", "start%5D=" + start_date);
        URL = URL.replace("end%5D=", "end%5D=" + end_date);
        URL = URL.replace("nb_people%5D=", "nb_people%5D=" + nb_personnes);
        /////////////////////////////////////////////////

        driver.get(URL);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // js.executeScript("window.scrollBy(0,2000)", "");

        Thread.sleep(1000);

        driver.findElement(By.cssSelector("button.didomi-components-button.didomi-button.didomi-dismiss-button.didomi-components-button--color.didomi-button-highlight.highlight-button")).click();

        List<WebElement> refL = driver.findElement(By.id("section_dispos")).findElements(By.className("bloclink"));
        for (WebElement we : refL) {
            urls.add(we.getAttribute("href"));
        }
        try {

            for (int i = 0; i < 1; i++) {


                /////////////////////////:::location///////////////////////////

                driver.get(urls.get(i) + "/nos-locations#menu");
                camping_name = driver.findElement(By.className("main-title")).getText();
                js.executeScript("window.scrollBy(0,200)", "");
                scroll2(driver);
                ac = driver.findElement(By.cssSelector("div.bloc-list.row.position-relative")).findElements(By.className("infos"));


                /////////////////////////////////////////////////////////////
                System.out.println(camping_name);
                for (WebElement a : ac) {
                        ////////////////////accommodation/////////////////////////////////
                        accommodation.setAccommodation_name(a.findElement(By.cssSelector("a.name.bloclink")).getText());
                        accommodation.setNb_person(a.findElement(By.className("nb")).getText());
                        area = a.findElement(By.className("size")).getText();
                        try {
                            nbroom = area.substring(area.indexOf(","));

                        } catch (Exception e) {
                            nbroom = "0 chambres";
                        }
                        area = area.substring(0, area.indexOf("m²"));
                        accommodation.setArea(area+"m²");
                        accommodation.setNb_room(nbroom);
                        accommodation.setType("location");
                      //  System.out.println(accommodation);
                        /////////////////////////////////////////////////////////////////

                        ////////camping price////////////////////////////////////////
                        try {
                            price = a.findElement(By.className("price-container")).findElement(By.cssSelector("span.value")).getText();
                            price = price.replace("€", "");
                            camping_price.setPrice(Double.parseDouble(price));
                        } catch (Exception e) {
                            camping_price.setPrice(null);
                        }
                        try {
                            camping_price.setPeriod(a.findElement(By.className("nights-count")).getText());

                        } catch (Exception e) {
                            camping_price.setPeriod(null);
                        }
                        camping_price.setNb_person(a.findElement(By.className("nb")).getText());
                      //  System.out.println(camping_price);
                    camping_prices.add(camping_price);
                    accommodations.add(accommodation);
                 camping_price_controller.addPrice(camping_price,accommodation,camping_name);
                        ////////////////////////////////////////////////////////////
                    camping_price = new Camping_Price();
                    accommodation = new Accommodation();





//////////////////////////////////////////////////////////////////

                }
               // camping_price_controller.Add(camping_prices,accommodations,camping_name);



////////////////////////////////////////emplacement/////////////////////////////////////////

                driver.get(urls.get(i) + "/nos-emplacements#menu");
                scroll(driver);
                ac = driver.findElement(By.cssSelector("div.bloc-list.row")).findElements(By.className("infos"));
                /////////////////////////////////////////////////////////////
                camping_name = camping_name.replace("https://www.sandaya.fr/nos-campings/", "");
               // System.out.println(camping_name);
                area = "";
                price = "";
                for (WebElement a : ac) {
                    ////////////////////accommodation/////////////////////////////////
                    accommodation.setAccommodation_name(a.findElement(By.cssSelector("a.name.bloclink")).getText());
                    // accommodation.setNb_person(a.findElement(By.className("nb")).getText());
                    area = a.findElement(By.className("size")).getText();
                    area = area.substring(0, area.indexOf("m²"));
                    accommodation.setArea(area);
                    // accommodation.setNb_room(nbroom);
                    accommodation.setType("emplacement");
                  //  System.out.println(accommodation);
                    /////////////////////////////////////////////////////////////////
                    ////////camping price////////////////////////////////////////
                    try {
                        price = a.findElement(By.className("price-container")).findElement(By.cssSelector("span.value")).getText();
                        price = price.replace("€", "");
                        camping_price.setPrice(Double.parseDouble(price));
                    } catch (Exception e) {
                        camping_price.setPrice(null);
                    }
                    try {
                        camping_price.setPeriod(a.findElement(By.className("nights-count")).getText());
                    } catch (Exception e) {
                        camping_price.setPeriod(null);
                    }
                    // camping_price.setNb_person(a.findElement(By.className("nb")).getText());
                    // camping_price.setDate(start + " au " + end);
                    //System.out.println(camping_price);
                    ////////////////////////////////////////////////////////////

                    //System.out.println(a.findElement(By.className("top")).getText());
                    //System.out.println(a.findElement(By.className("bottom")).getText());
                     camping_price_controller.addPrice(camping_price,accommodation,camping_name);
                    camping_price = new Camping_Price();
                    accommodation = new Accommodation();

                }

                ///////////////////////////////////////////////////////////////////////////////////////////////////


            }
        } catch (Exception e) {
            System.out.println("no campings at that date");
        }

    }


    public void scroll(ChromeDriver driver) throws InterruptedException {
        for (int i = 0; i < driver.findElement(By.cssSelector("div.bloc-list.row")).findElements(By.className("infos")).size(); i++) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.cssSelector("div.bloc-list.row")).findElements(By.className("infos")).get(i));
            Thread.sleep(500);
        }


    }

    public void scroll2(ChromeDriver driver) throws InterruptedException {
        for (int i = 0; i < driver.findElement(By.cssSelector("div.bloc-list.row.position-relative")).findElements(By.className("infos")).size(); i++) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.cssSelector("div.bloc-list.row.position-relative")).findElements(By.className("infos")).get(i));
            Thread.sleep(500);
        }


    }


*/
}
