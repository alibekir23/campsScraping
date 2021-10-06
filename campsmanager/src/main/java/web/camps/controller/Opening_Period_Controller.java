package web.camps.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import web.camps.model.Camping;
import web.camps.model.Opening_Period;
import web.camps.model.Region;
import web.camps.repo.Camping_Repo;
import web.camps.repo.Opening_Period_Repo;

import java.util.List;

@RestController
public class Opening_Period_Controller {

    @Autowired
    Camping_Repo camping_repo;

    @Autowired
    Opening_Period_Repo opening_period_repo;

    public Opening_Period addopening(Integer campingId, Opening_Period op) throws NotFoundException {

        return camping_repo.findById(campingId)
                .map(camping -> {
                    op.setCamping_id(camping);
                    return opening_period_repo.save(op);
                }).orElseThrow(() -> new NotFoundException("camping not found!"));
    }




}
