package web.camps.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.camps.model.*;
import web.camps.repo.Camping_Company_Repo;
import web.camps.repo.Camping_Repo;
import web.camps.repo.Opening_Period_Repo;
import web.camps.repo.Region_Repo;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/camping")
public class Camping_Controller {

    @Autowired
    Camping_Repo camping_repo;

    @Autowired
    Region_Repo region_repo;

    @Autowired
    Opening_Period_Controller opening_period_controller;
    @Autowired
    Camping_Company_Repo camping_company_repo;
    @Autowired
    Accommodation_Controller accommodation_controller;


    @GetMapping("/all")
    public ResponseEntity<List<Camping_Repo>> getAllCampings() {
        List<Camping> Camps = camping_repo.findAll();
        return new ResponseEntity(Camps, HttpStatus.OK);
    }


    public Camping addcamps(Integer companyId, Camping camping, List<Opening_Period> op, List<Accommodation> accommodations, Region reg) throws NotFoundException {
        System.out.println("fgfdf");
        try {
                camping.setCompany_id(camping_company_repo.find(companyId));
            System.out.println("1");
                camping.setRegion_id(region_repo.findByName(reg.getCity_name()));

                camping_repo.save(camping);
            System.out.println("2");
                for (Opening_Period period : op) {
                    try {
                        opening_period_controller.addopening(camping.getCamping_id(), period);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }

                for (Accommodation ac : accommodations) {
                    try {
                        accommodation_controller.addAcc(ac, camping.getCamping_id());
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("saved camping");
                return camping;

        } catch (Exception e) {
            System.out.println("camping already exists");
            return null;
        }
    }
}
