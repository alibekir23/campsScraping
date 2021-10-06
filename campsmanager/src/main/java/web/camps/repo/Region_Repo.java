package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.camps.model.Accommodation;
import web.camps.model.Region;

import java.util.List;

public interface Region_Repo extends JpaRepository<Region,Integer> {
    @Query(value="SELECT * FROM Region a WHERE a.city_name = ?1 ORDER BY a.city_name desc LIMIT 1",nativeQuery = true )
    Region findByName(String name);
}
