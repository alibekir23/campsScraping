package web.camps.service.scrapping_service;

import javassist.NotFoundException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import web.camps.Log.TrackExecutionTime;
import web.camps.controller.Accommodation_Controller;
import web.camps.controller.Camping_Controller;
import web.camps.controller.Camping_Price_Controller;
import web.camps.controller.Region_Controller;
import web.camps.model.*;
import web.camps.repo.Camping_Price_Repo;
import web.camps.repo.Camping_Repo;
import web.camps.repo.Scraping_Log_Repo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class YellohVillage_Scrapping {
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


    @Async
    @TrackExecutionTime
    public CompletableFuture<List<Camping>> All_Camps_YellohVillage() throws InterruptedException, NotFoundException, ParseException, Exception {
        ////////Region///////////////////////////
        Region region = new Region();
        String latit = "";
        String longit = "";
        ////////////////////////////////////////
        ///////opening period///////////////////////
        String d = "";
        String op = "";
        String end = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date opening = null;
        Date ending = null;
        Opening_Period p = new Opening_Period();
        List<Opening_Period> opening_periods = new ArrayList<>();
        ///////////////////////////////////////////////////////////////////////
        //////accommodation//////////////////////////////////////////////////
        List<Accommodation> accommodations = new ArrayList<>();
        //////////////////////////////////////////////////////////////
        ////urls+campings//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        Camping camp = new Camping();
        String s = new String();
        List<String> gps = new ArrayList<>();
        ////////////////////////////////////
        //////////////////driver///////////////////
        ChromeDriver driver = new ChromeDriver();
        String Yelloh_URL = "https://www.yellohvillage.fr/choisissez_votre_camping/par_pays/france";
        ////////////////////////////////////////////////////////////////////


        ////////campings url//////////
        driver.get(Yelloh_URL);
        Thread.sleep(500);
        driver.findElement(By.cssSelector("button.didomi-components-button.didomi-button.didomi-dismiss-button.didomi-components-button--color.didomi-button-highlight.highlight-button")).click();
        //////////////////////////////

        //////////get all campings url///////////////
        List<WebElement> refList = driver.findElements(By.className("link-village"));
        for (WebElement we : refList) {
            urls.add(we.getAttribute("href"));
        }
        ///////////////////////////////////////////

        ///////scrape campings/////////////////
        for (int i = 1; i < urls.size(); i++) {
            driver.get(urls.get(i));
            Thread.sleep(500);
            ////////////stars////////////////////////////
            String stars = new String();
            stars = driver.findElement(By.cssSelector("div.fiche-village.at-element-marker")).findElement(By.tagName("a")).findElement(By.tagName("h1")).getAttribute("class").toString();
            //  System.out.println(stars.substring(stars.lastIndexOf("-") + 1, stars.length()));
            camp.setNb_stars(Double.parseDouble(stars.substring(stars.lastIndexOf("-") + 1, stars.length())));
            ///////////////////

            //////region////////////////////////////////////
            region.setCity_name(driver.findElement(By.cssSelector("a.link-geographique")).getText());
            region.setRegion_name(driver.findElements(By.cssSelector("a.link-geographique")).get(1).getText());
            /////////////////////////////////////////////////////

            //////note/////////////
            try {
                camp.setNote(Double.parseDouble(driver.findElement(By.className("average")).getText()));
                //  System.out.println(driver.findElement(By.className("average")).getText());
            } catch (Exception e) {
                camp.setNote(null);
            }
            /////////////////

            //name//////
            ///////scroll///////////
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,700)", "");
            Thread.sleep(500);
            camp.setCamping_name(driver.findElement(By.className("introduction-sp-village")).findElement(By.tagName("div")).getText());
            //   System.out.println(driver.findElement(By.className("introduction-sp-village")).findElement(By.tagName("div")).getText());
            /////////////////

            //////////opening period/////////////////////////
            List<WebElement> seasons = driver.findElements(By.className("dates-ouverture"));
            //seasons.forEach(s -> System.out.println(s.getText()));
            for (WebElement season : seasons) {
                d = season.getText();
                d = d.replace("Ouvert du ", "");
                op = d.substring(0, d.indexOf(" "));
                end = d.substring(d.indexOf("au "), d.length());
                end = end.replace("au ", "");
                opening = formatter.parse(op);
                // System.out.println(opening);
                ending = formatter.parse(end);
                //System.out.println(ending);
                p.setOpening_date(opening);
                p.setClosing_date(ending);
                p.setSeason(d.substring(d.lastIndexOf("202")));
                opening_periods.add(p);
                p = new Opening_Period();
            }
            // System.out.println(opening_periods);
            ////////////////////////


            //////////map////
            WebElement map_url = driver.findElement(By.cssSelector("div.map.float-right")).findElement(By.tagName("a"));
            driver.get(map_url.getAttribute("href"));
            Thread.sleep(500);
            s = driver.findElement(By.id("block-localisation")).findElement(By.tagName("p")).getText();

            latit = s.substring(s.indexOf("L"), s.indexOf("''"));
            longit = s.substring(s.lastIndexOf(":"), s.lastIndexOf("''"));
            latit = latit.substring(latit.indexOf(":") + 4);
            longit = longit.substring(longit.indexOf(":") + 4);
            latit = latit.replace("°", ".");
            latit = latit.replace("'", "");
            latit = latit.replace(" ", "");
            longit = longit.replace("°", ".");
            longit = longit.replace("'", "");
            longit = longit.replace(" ", "");
            System.out.println(latit);
            System.out.println(longit);
            camp.setLatitude(Double.parseDouble(latit));
            camp.setLongitude(Double.parseDouble(longit));
            /////////////////////////////////////////////////////////////

            //////////////////////////////accommodations call accommodation scrapping////////////////
            accommodations = All_Accommodations_YellohVillage(urls.get(i), driver);
            //////////////////////////////////////////////////////////////
            //  System.out.println(accommodations);

            //////////region////////////////////////////////
            //System.out.println(region);
            //////////////////////////////////////////////

            /////////////add region////////////////
            region_controller.addRegion(region);
            ///////////////////////////////////

            ///////Add camp+acc+opening period//////////////////////////
            campings.add(camping_controller.addcamps(2, camp, opening_periods, accommodations, region));
            /////////////////////////////////////////////////

            camp = new Camping();
            region = new Region();
            opening_periods = new ArrayList<>();

        }
        // System.out.println(campings);
        return CompletableFuture.completedFuture(campings);
    }
    ////////////////////////////////////////////////////////////////////////


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


    public static String addOneDay(String date)
            throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        c.add(Calendar.DATE, 1);
        return sdf.format(c.getTime());
    }

    @Async
    @TrackExecutionTime
    public void Price(String start_date, int nb_personnes, int days) throws InterruptedException, NotFoundException, ParseException {
        String new_start_date = "";
        Date newDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Scraping_Log log = new Scraping_Log();
        int nbr_price = 0;

        for (int i = 0; i < days; i++) {
            new_start_date = start_date;
            start_date = addOneDay(start_date);
            nbr_price = Price_YellohVillage(new_start_date, start_date, nb_personnes);
            log.setDate(formatter.parse(new_start_date));
            log.setScraping_time(newDate);
            log.setNbr_price(nbr_price);
            log.setCompany_id(2);
            scraping_log_repo.save(log);
            log = new Scraping_Log();
        }

        System.out.println(log);
    }




    @Async
    @TrackExecutionTime
    public int Price_YellohVillage(String start_date, String end_date, int nb_personnes) throws InterruptedException, NotFoundException, ParseException {
        ////init////////////////////

        Date newDate = new Date();
        int nbr_price = 0;
        String camping_name = "";
        List<WebElement> ac = new ArrayList<>();
        String emp_url = "";
        Date arrival = null;
        Date departure = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String start = start_date;
        String end = end_date;
        String max_person = "";
        String min_person = "";
        ChromeDriver driver = new ChromeDriver();
        Camping_Price camping_price = new Camping_Price();
        Accommodation accommodation = new Accommodation();
        ////urls+campings list//////////
        List<Camping> campings = new ArrayList<Camping>();
        List<String> urls = new ArrayList<String>();
        List<String> urls_acc = new ArrayList<String>();
        List<Camping_Price> camping_prices = new ArrayList<>();
        List<Camping_Price> prices = new ArrayList<>();
        List<Accommodation> accommodations = new ArrayList<>();
        List<WebElement> details = new ArrayList<>();
        String area = "";
        String nbroom = "";
        String price = "";
        Camping camping=new Camping();
        String URL = "https://www.yellohvillage.fr/camping/search?search_text=France&campings_content_ids=&poi_id=&total_count_village=91&date_start=&date_end=&hebergement=rental_unit&nb_personnes=";
        ///////////////////////////////////////////////////

        end_date = end_date.replace("/", "%2F");
        start_date = start_date.replace("/", "%2F");
        URL = URL.replace("date_start=", "date_start=" + start_date);
        URL = URL.replace("date_end=", "date_end=" + end_date);
        URL = URL.replace("nb_personnes=", "nb_personnes=" + nb_personnes);
        /////////////////////////////////////////////////
        try {

            driver.get(URL);
            Thread.sleep(300);
            driver.findElement(By.cssSelector("button.didomi-components-button.didomi-button.didomi-dismiss-button.didomi-components-button--color.didomi-button-highlight.highlight-button")).click();
            driver.get("https://www.yellohvillage.fr/");
            driver.findElement(By.className("Search-btn")).submit();
            JavascriptExecutor js = (JavascriptExecutor) driver;


            List<WebElement> refL = driver.findElement(By.cssSelector("section.CampingList-subsection.CampingList-subsection--strict")).findElement(By.className("CampingList-masonry")).findElements(By.className("Camping"));


            for (WebElement we : refL) {
                urls.add(we.findElement(By.className("Camping-buttons")).findElement(By.tagName("a")).getAttribute("href"));
            }
        } catch (Exception e) {
            driver.quit();
        }

        if (urls != null) {
            for (int i = 0; i < urls.size(); i++) {
                try {
                    /////////////////////////:::location///////////////////////////
                    driver.get(urls.get(i));
                    camping_name = driver.findElement(By.className("SectionHeadVillage-name")).getText();
                   // System.out.println(camping_name);
                    // camping_name = camping_name.replace("Camping ", "");
                    scroll(driver);
                    ac = driver.findElements(By.className("AccomodationBlock"));
                    // ac.forEach(a-> System.out.println(a.getText()));
                    /////////////////////////////////////////////////////////////


                    for (WebElement a : ac) {
                        if (a.findElement(By.className("AccommodationAvailabilityBlock-line")).findElement(By.tagName("span")).getText().equals("Disponible")) {

                            ////////////////////accommodation/////////////////////////////////
                            accommodation.setAccommodation_name(a.findElement(By.className("AccomodationBlock-title")).getText());


                            max_person = a.findElement(By.cssSelector("div.AccommodationDetails-characs.AccommodationDetails-characs--persons")).findElement(By.tagName("span")).getText();
                            if (max_person.contains("/")) {
                                min_person = max_person.substring(0, max_person.indexOf("/"));
                                max_person = max_person.substring(max_person.indexOf("/") + 1);
                            } else {
                                min_person = "1";
                            }


                            //System.out.println(max_person);
                            //System.out.println(min_person);
                            //System.out.println(a.findElement(By.className("PriceTag-priceWrapper")).findElement(By.cssSelector("p")).getText());
                            accommodation.setMin_person(Integer.parseInt(min_person));
                            accommodation.setMax_person(Integer.parseInt(max_person));


                            try {

                                details = a.findElements(By.className("AccommodationDetails-characs"));

                                area=null;
                                nbroom="0";
                                for (WebElement d : details) {

                                    if (d.getText().contains("m²")) {
                                        area = d.getText();
                                        area = area.substring(0, area.indexOf(" "));
                                    } else if (d.getText().contains("ch")) {
                                        nbroom = d.getText();
                                        nbroom = nbroom.substring(0, nbroom.indexOf(" "));
                                    }
                                }

                                accommodation.setArea(Double.parseDouble(area));
                                accommodation.setNb_room(Integer.parseInt(nbroom));
                            } catch (Exception e) {

                            }
                            /////////////////////////////////////////////////////////////////

                            ////////camping price////////////////////////////////////////
                            try {
                                price = a.findElement(By.cssSelector("p.PriceTag-price.PriceTag-finalPrice")).getText();
                               // System.out.println(price);
                                 price = price.substring(0, price.indexOf(" "));
                                 camping_price.setPrice(Double.parseDouble(price));
                            } catch (Exception e) {
                                camping_price.setPrice(Double.parseDouble("-1"));
                                System.out.println("no price");
                            }

                        try {
                            arrival = formatter.parse(start);
                            departure = formatter.parse(end);
                            camping_price.setArrival_date(arrival);
                            camping_price.setDeparture_date(departure);

                        } catch (Exception e) {
                            camping_price.setArrival_date(arrival);
                            camping_price.setDeparture_date(departure);
                        }

                        camping_price.setScrapping_date(newDate);


                        if (camping_price.getPrice() != Double.parseDouble("-1")) {
                            accommodations.add(accommodation);
                            camping_prices.add(camping_price);
                        }


                        //  System.out.println(camping_price);
                          //  System.out.println(accommodation);
                        ////////////////////////////////////////////////////////////


                        camping_price = new Camping_Price();
                        accommodation = new Accommodation();

                    }


                    }

                    camping=camping_repo.findByName(camping_name);
                    try {

                        nbr_price = nbr_price + (camping_price_controller.Add(camping_prices, accommodations, arrival, departure,camping.getCamping_id()));

                    } catch (Exception e) {
                        System.out.println("error aading price");

                    }

                    camping_prices = new ArrayList<>();
                    accommodations = new ArrayList<>();
//////////////////////////////////////////////////////////////////


                } catch (Exception e) {
                    System.out.println("error");
                }



            /*
            /////////////////////////:::emplacement///////////////////////////
            Thread.sleep(500);
            emp_url = urls.get(i);
            emp_url = emp_url.replace("nos_locations", "nos_emplacements");
            driver.get(emp_url);
            ///////////////////////////////////////////////////////////////////
            scroll(driver);
            ac = driver.findElements(By.className("element-list-hebergement"));
            /////////////////////////////////////////////////////////////
            area = "";
            price = "";
            for (WebElement a : ac) {
                ////////////////////accommodation/////////////////////////////////
                try {
                    if (a.findElement(By.className("block-price")).getText() != null) {
                        accommodation.setAccommodation_name(a.findElement(By.className("block-left")).findElement(By.tagName("a")).getText());
                        area = a.findElement(By.className("block-left")).findElements(By.tagName("span")).get(0).getText();
                        try {
                            accommodation.setArea(area);
                            accommodation.setType("emplacement");
                        } catch (Exception e) {
                            accommodation.setArea("");
                        }
                        /////////////////////////////////////////////////////////////////

                        ////////camping price////////////////////////////////////////
                        try {
                            price = a.findElement(By.className("block-price")).getText();
                            price = price.substring(0, price.indexOf(" "));
                            camping_price.setPrice(Double.parseDouble(price));
                            camping_price.setPeriod(a.findElement(By.className("nb-nuit")).getText());
                        } catch (Exception e) {
                        }
                        arrival = formatter.parse(start);
                        departure = formatter.parse(end);
                        camping_price.setPeriod(a.findElement(By.className("nb-nuit")).getText());
                        camping_price.setArrival_date(arrival);
                        camping_price.setDeparture_date(departure);
                  //      System.out.println(camping_price);
                        ////////////////////////////////////////////////////////////
                    }
                } catch (Exception e) {
                    System.out.println("not available");
                }
               // camping_price_controller.addPrice(camping_price, accommodation, camping_name);
                camping_price = new Camping_Price();
                accommodation = new Accommodation();
            }

             */
//////////////////////////////////////////////////////////////////
            }
        }
        driver.quit();
        return nbr_price;
    }


///////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////



    public void scroll(ChromeDriver driver) throws InterruptedException {
        for (int i = 0; i < driver.findElements(By.className("AccomodationBlock")).size(); i++) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElements(By.className("AccomodationBlock")).get(i));
            Thread.sleep(200);
        }


    }


}
