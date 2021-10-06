package web.camps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "camping_price")
public class Camping_Price implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "price_id")
    private Integer price_id;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accommodation_id")
    Accommodation accommodation_id;
    @Column(name = "arrival_date")
    private Date arrival_date;
    @Column(name = "departure_date")
    private Date departure_date;
    @Column(name = "scrapping_date")
    private Date scrapping_date;
    @Column(name = "price")
    private Double price;


    public Camping_Price() {
    }

    public Camping_Price(Integer price_id, Accommodation accommodation_id, Date arrival_date, Date departure_date, Date scrapping_date, Double price) {
        this.price_id = price_id;
        this.accommodation_id = accommodation_id;
        this.arrival_date = arrival_date;
        this.departure_date = departure_date;
        this.scrapping_date = scrapping_date;

        this.price = price;

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getPrice_id() {
        return price_id;
    }

    public void setPrice_id(Integer price_id) {
        this.price_id = price_id;
    }

    public Accommodation getAccommodation_id() {
        return accommodation_id;
    }

    public void setAccommodation_id(Accommodation accommodation_id) {
        this.accommodation_id = accommodation_id;
    }


    public Date getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(Date arrival_date) {
        this.arrival_date = arrival_date;
    }

    public Date getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(Date departure_date) {
        this.departure_date = departure_date;
    }

    public Date getScrapping_date() {
        return scrapping_date;
    }

    public void setScrapping_date(Date scrapping_date) {
        this.scrapping_date = scrapping_date;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Camping_Price{" +
                "price_id=" + price_id +
                ", accommodation_id=" + accommodation_id +
                ", arrival_date=" + arrival_date +
                ", departure_date=" + departure_date +
                ", scrapping_date=" + scrapping_date +
                ", price=" + price + '\'' +
                '}';
    }
}
