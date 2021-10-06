package web.camps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import web.camps.service.scrapping_service.Scrapping;
import web.camps.model.Camping;
import web.camps.model.Camping_Company;
import web.camps.repo.Camping_Company_Repo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/company")
@EnableAsync

public class Camping_Company_Controller {

   @Autowired
   Camping_Company_Repo company_repo;

   @PostMapping("/add")
    public ResponseEntity<Camping_Company> addCompany(@RequestBody Camping_Company company) {

           Camping_Company cm= company_repo.save(company);
           return new ResponseEntity<>(cm, HttpStatus.CREATED);

   }

    @GetMapping("/all")
    public ResponseEntity<List<Camping_Company_Repo>> getAllCompanies(){
        List<Camping_Company> Companies= company_repo.findAll();
        return new ResponseEntity(Companies,HttpStatus.OK);
    }









}
