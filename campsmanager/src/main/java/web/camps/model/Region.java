package web.camps.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="region_id")
    private Integer region_id;
    @Column(name="region_name")
    private String region_name;
    @Column(name="city_name",unique = true)
    private String city_name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="region_id")
    private Set<Camping> campingS;

    public Region() {
    }

    public Region(Integer region_id, String region_name, Set<Camping> campingSet) {
        this.region_id = region_id;
        this.region_name = region_name;

        this.campingS = campingSet;
    }

    public Region(Integer region_id, String region_name, String city_name, Set<Camping> campingS) {
        this.region_id = region_id;
        this.region_name = region_name;
        this.city_name = city_name;

        this.campingS = campingS;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Set<Camping> getCampingS() {
        return campingS;
    }

    public void setCampingS(Set<Camping> campingS) {
        this.campingS = campingS;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Integer region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }



    public Set<Camping> getCampingSet() {
        return campingS;
    }

    public void setCampingSet(Set<Camping> campingS) {
        this.campingS = campingS;
    }

    @Override
    public String toString() {
        return "Region{" +
                "region_id=" + region_id +
                ", region_name='" + region_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", campingS=" + campingS +
                '}';
    }
}
