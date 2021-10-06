package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import web.camps.model.Camping;
import web.camps.model.Camping_Price;

public interface Camping_Price_Repo extends JpaRepository<Camping_Price,Integer> {
}
