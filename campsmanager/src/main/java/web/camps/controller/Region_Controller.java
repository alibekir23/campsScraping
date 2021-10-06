package web.camps.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import web.camps.model.Accommodation;
import web.camps.model.Region;
import web.camps.repo.Camping_Repo;
import web.camps.repo.Region_Repo;

import java.util.List;

@RestController
public class Region_Controller {

    @Autowired
    Region_Repo region_repo;

    public Region addRegion(Region region)  {


        try{

                region_repo.save(region);
                System.out.println("saved region");


        }catch (Exception e){
            System.out.println("region city already exists");

        }

    return region;
    }



}
