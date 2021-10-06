package web.camps.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import web.camps.model.Accommodation;
import web.camps.model.Camping;
import web.camps.model.Camping_Price;
import web.camps.repo.Accommodation_Repo;
import web.camps.repo.Camping_Price_Repo;
import web.camps.repo.Camping_Repo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class Camping_Price_Controller {
    @Autowired
    Accommodation_Repo accommodation_repo;
    @Autowired
    Camping_Repo camping_repo;
    @Autowired
    Camping_Price_Repo camping_price_repo;


    public int Add(List<Camping_Price> prices, List<Accommodation> accommodations, Date Arrival, Date Departure,int camping_id) throws ParseException {
        Accommodation a = new Accommodation();
        Camping_Price p = new Camping_Price();
        int nbr=prices.size();
     //   System.out.println(camping_name);
        for (int i = 0; i < prices.size(); i++) {
            try {
                a = accommodation_repo.findByAccommodation(accommodations.get(i).getAccommodation_name(), accommodations.get(i).getMax_person(), accommodations.get(i).getMin_person(), accommodations.get(i).getNb_room(), accommodations.get(i).getArea(),camping_id);
                //System.out.println(c.getCamping_id());
            } catch (Exception e) {
                System.out.println("nope");
            }
            if (a!= null) {
                prices.get(i).setAccommodation_id(a);
    //            System.out.println(prices.get(i).getPrice());
  //              System.out.println(prices.get(i).getAccommodation_id().getAccommodation_name());
            }
        }
       camping_price_repo.saveAllAndFlush(prices);
        return nbr;
    }

}
