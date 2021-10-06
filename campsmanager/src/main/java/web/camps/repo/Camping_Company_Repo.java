package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.camps.model.Camping;
import web.camps.model.Camping_Company;
import web.camps.model.Region;

public interface Camping_Company_Repo extends JpaRepository<Camping_Company, Integer> {
    @Query(value="SELECT * FROM Camping_Company a WHERE a.company_id = ?1 ORDER BY a.company_name desc LIMIT 1",nativeQuery = true )
    Camping_Company find(Integer id);
}
