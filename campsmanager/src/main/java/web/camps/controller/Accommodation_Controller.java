package web.camps.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import web.camps.model.Accommodation;
import web.camps.model.Opening_Period;
import web.camps.repo.Accommodation_Repo;
import web.camps.repo.Camping_Repo;


@RestController
public class Accommodation_Controller {

    @Autowired
    Camping_Repo camping_repo;

    @Autowired
    Accommodation_Repo accommodation_repo;

    public Accommodation addAcc(Accommodation accommodation,Integer campingId) throws NotFoundException {
        return camping_repo.findById(campingId)
                .map(camping -> {
                    accommodation.setCamping_id(camping);
                    return accommodation_repo.save(accommodation);

                }).orElseThrow(() -> new NotFoundException("camping not found!"));
    }
}
