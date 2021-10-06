package web.camps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "camping")
public class Camping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="camping_id")
    private Integer camping_id;

    @Column(name="camping_name")
    private String camping_name;




    @Column(name = "longitude",unique=true)

    private Double longitude;

    @Column(name = "latitude",unique=true)
    private Double latitude;

    @Column(name = "nb_stars")
    private Double nb_stars;

    @Column(name = "note")
    private Double note;

    @ManyToOne(fetch = FetchType.LAZY,optional = false )
    @JoinColumn(name = "company_id",nullable = false)
    @JsonIgnore
    Camping_Company company_id;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "region_id")
     Region region_id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="camping_id")
    private Set<Accommodation> accommodationSet;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="opening_id",fetch = FetchType.LAZY)
    private Set <Opening_Period> opening_period;


    public Camping() {

    }

    public Camping(Integer camping_id, String camping_name, Double longitude, Double latitude, Double nb_stars, Double note, Camping_Company company_id, Region region_id, Set<Accommodation> accommodationSet, Set<Camping_Price> camping_priceSet, Set<Opening_Period> opening_period) {
        this.camping_id = camping_id;
        this.camping_name = camping_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nb_stars = nb_stars;
        this.note = note;
        this.company_id = company_id;
        this.region_id = region_id;
        this.accommodationSet = accommodationSet;
        this.opening_period = opening_period;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCamping_id() {
        return camping_id;
    }

    public void setCamping_id(Integer camping_id) {
        this.camping_id = camping_id;
    }

    public String getCamping_name() {
        return camping_name;
    }

    public void setCamping_name(String camping_name) {
        this.camping_name = camping_name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getNb_stars() {
        return nb_stars;
    }

    public void setNb_stars(Double nb_stars) {
        this.nb_stars = nb_stars;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public Camping_Company getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Camping_Company company_id) {
        this.company_id = company_id;
    }

    public Region getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Region region_id) {
        this.region_id = region_id;
    }

    public Set<Accommodation> getAccommodationSet() {
        return accommodationSet;
    }

    public void setAccommodationSet(Set<Accommodation> accommodationSet) {
        this.accommodationSet = accommodationSet;
    }


    public Set<Opening_Period> getOpening_period() {
        return opening_period;
    }

    public void setOpening_period(Set<Opening_Period> opening_period) {
        this.opening_period = opening_period;
    }

    @Override
    public String toString() {
        return "Camping{" +
                "camping_id=" + camping_id +
                ", camping_name='" + camping_name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", nb_stars=" + nb_stars +
                ", note=" + note +
                ", company_id=" + company_id +
                ", region_id=" + region_id +
                ", accommodationSet=" + accommodationSet +
                ", opening_period=" + opening_period +
                '}';
    }
}
