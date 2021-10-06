package web.camps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.camps.model.Camping_Company;
import web.camps.repo.Camping_Company_Repo;

import java.util.List;

@Service
public class Camping_Company_Service {

    private final Camping_Company_Repo Company_Repo;
    @Autowired
    public Camping_Company_Service(Camping_Company_Repo company_repo) {
        this.Company_Repo = company_repo;
    }

    public Camping_Company addCompany(Camping_Company camping_company){
     return (Company_Repo.save(camping_company));
    }

    public List<Camping_Company> findAllCompanies(){
        return Company_Repo.findAll();
    }
}
