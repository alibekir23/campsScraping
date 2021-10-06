package web.camps.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "scraping_log")
public class Scraping_Log implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer log_id;
    @Column(name = "date")
    private Date date;
    @Column(name = "scraping_time")
    private Date scraping_time;
    @Column(name = "nbr_price")
    private int nbr_price;
    @Column(name = "company_id")
    private int company_id;

    public Scraping_Log() {
    }

    public Scraping_Log(Integer log_id, Date date, Date scraping_time, int nbr_price, int company_id) {
        this.log_id = log_id;
        this.date = date;
        this.scraping_time = scraping_time;
        this.nbr_price = nbr_price;
        this.company_id = company_id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLog_id() {
        return log_id;
    }

    public void setLog_id(Integer log_id) {
        this.log_id = log_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getScraping_time() {
        return scraping_time;
    }

    public void setScraping_time(Date scraping_time) {
        this.scraping_time = scraping_time;
    }

    public int getNbr_price() {
        return nbr_price;
    }

    public void setNbr_price(int nbr_price) {
        this.nbr_price = nbr_price;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    @Override
    public String toString() {
        return "Scraping_Log{" +
                "log_id=" + log_id +
                ", date=" + date +
                ", scraping_time=" + scraping_time +
                ", nbr_price=" + nbr_price +
                ", company_id=" + company_id +
                '}';
    }
}
