package web.camps.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import web.camps.model.Scraping_Log;

public interface Scraping_Log_Repo extends JpaRepository<Scraping_Log,Integer> {
}
