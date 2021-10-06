package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import web.camps.model.Camping;


import java.util.List;

public interface Camping_Repo extends JpaRepository<Camping,Integer> {
    @Query(value="SELECT * FROM Camping a WHERE a.camping_name = ?1 ORDER BY a.camping_name desc LIMIT 1",nativeQuery = true )
    Camping findByName(String name);

    @Query("SELECT distinct c FROM Camping c WHERE c.longitude = ?1 and c.latitude=?2")
    List<Camping> findBycoordinates(String longitude,String altitude);

}
