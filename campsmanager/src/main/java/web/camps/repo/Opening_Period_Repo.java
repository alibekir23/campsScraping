package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import web.camps.model.Camping;
import web.camps.model.Opening_Period;

public interface Opening_Period_Repo extends JpaRepository<Opening_Period,Integer> {
}
