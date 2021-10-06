package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.camps.model.Accommodation;
import web.camps.model.Camping;

import java.util.List;

public interface Accommodation_Repo extends JpaRepository<Accommodation,Integer> {


    @Query(value="SELECT * FROM Accommodation a WHERE a.accommodation_name = ?1  and a.max_person=?2 and a.min_person=?3 and a.nb_room=?4 and a.area=?5 ORDER BY a.accommodation_name desc LIMIT 1",nativeQuery = true )
    Accommodation findByAccommodation(String name,int max_person,int min_person,int nb_room,Double area,int camping_id);

}
