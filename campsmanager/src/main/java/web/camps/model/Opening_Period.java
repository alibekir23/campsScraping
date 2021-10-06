package web.camps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "opening_period")
public class Opening_Period implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="opening_id")
    private Integer opening_id;

    @Column(name="season")
    private String season;
    @Column(name="opening_date")
    private Date opening_date;
    @Column(name="closing_date")
    private  Date closing_date;
    @ManyToOne(fetch = FetchType.LAZY,optional = false )
    @JoinColumn(name = "camping_id",nullable = false)
    @JsonIgnore
    Camping camping_id;

    public Opening_Period() {
    }

    public Opening_Period(Integer opening_id, String season, Date opening_date, Date closing_date, Camping camping_id) {
        this.opening_id = opening_id;
        this.season = season;
        this.opening_date = opening_date;
        this.closing_date = closing_date;
        this.camping_id = camping_id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getOpening_id() {
        return opening_id;
    }

    public void setOpening_id(Integer opening_id) {
        this.opening_id = opening_id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Date getOpening_date() {
        return opening_date;
    }

    public void setOpening_date(Date opening_date) {
        this.opening_date = opening_date;
    }

    public Date getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(Date closing_date) {
        this.closing_date = closing_date;
    }

    public Camping getCamping_id() {
        return camping_id;
    }

    public void setCamping_id(Camping camping_id) {
        this.camping_id = camping_id;
    }

    @Override
    public String toString() {
        return "Opening_Period{" +
                "opening_id=" + opening_id +
                ", season='" + season + '\'' +
                ", opening_date='" + opening_date + '\'' +
                ", closing_date='" + closing_date + '\'' +
                ", camping=" + camping_id +
                '}';
    }
}
