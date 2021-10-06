package web.camps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name ="accommodation")
public class Accommodation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="accommodation_id")
    private Integer accommodation_id;

    @Column(name="accommodation_name")
    private String accommodation_name;


    @ManyToOne(fetch = FetchType.LAZY,optional = false )
    @JoinColumn(name = "camping_id",nullable = false)
    @JsonIgnore
    Camping camping_id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="accommodation_id")
    private Set<Camping_Price> camping_priceSet;



    @Column(name="max_person")
    private int max_person;

    @Column(name="min_person")
    private int min_person;

    @Column(name="nb_room")
    private int nb_room;

    @Column(name="area")
    private Double area;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getAccommodation_id() {
        return accommodation_id;
    }

    public void setAccommodation_id(Integer accommodation_id) {
        this.accommodation_id = accommodation_id;
    }

    public String getAccommodation_name() {
        return accommodation_name;
    }

    public void setAccommodation_name(String accommodation_name) {
        this.accommodation_name = accommodation_name;
    }

    public Camping getCamping_id() {
        return camping_id;
    }

    public void setCamping_id(Camping camping_id) {
        this.camping_id = camping_id;
    }

    public Set<Camping_Price> getCamping_priceSet() {
        return camping_priceSet;
    }

    public void setCamping_priceSet(Set<Camping_Price> camping_priceSet) {
        this.camping_priceSet = camping_priceSet;
    }

    public int getMax_person() {
        return max_person;
    }

    public void setMax_person(int max_person) {
        this.max_person = max_person;
    }

    public int getMin_person() {
        return min_person;
    }

    public void setMin_person(int min_person) {
        this.min_person = min_person;
    }

    public int getNb_room() {
        return nb_room;
    }

    public void setNb_room(int nb_room) {
        this.nb_room = nb_room;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }


    public Accommodation() {
    }

    public Accommodation(Integer accommodation_id, String accommodation_name, Camping camping_id, Set<Camping_Price> camping_priceSet, int max_person, int min_person, int nb_room, Double area) {
        this.accommodation_id = accommodation_id;
        this.accommodation_name = accommodation_name;
        this.camping_id = camping_id;
        this.camping_priceSet = camping_priceSet;
        this.max_person = max_person;
        this.min_person = min_person;
        this.nb_room = nb_room;
        this.area = area;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "accommodation_id=" + accommodation_id +
                ", accommodation_name='" + accommodation_name + '\'' +
                ", camping_id=" + camping_id +
                ", camping_priceSet=" + camping_priceSet +
                ", max_person=" + max_person +
                ", min_person=" + min_person +
                ", nb_room=" + nb_room +
                ", area=" + area +
                '}';
    }
}
