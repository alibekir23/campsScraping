package web.camps.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="camping_company")
public class Camping_Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="company_id")
    private Integer company_id;
    @Column(name="company_name")
    private String company_name;
    @Column(name="company_url")
    private String company_url;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="company_id",fetch = FetchType.LAZY)
    private Set<Camping> campingSet;





    public Camping_Company() {

    }

    public Camping_Company(Integer company_id, String company_name, String company_url, Set<Camping> campingSet) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_url = company_url;
        this.campingSet = campingSet;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_url() {
        return company_url;
    }

    public void setCompany_url(String company_url) {
        this.company_url = company_url;
    }

    public Set<Camping> getCampingSet() {
        return campingSet;
    }

    public void setCampingSet(Set<Camping> campingSet) {
        this.campingSet = campingSet;
    }


    @Override
    public String toString() {
        return "Camping_Company{" +
                "company_id=" + company_id +
                ", company_name='" + company_name + '\'' +
                ", company_url='" + company_url + '\'' +
                ", campingSet=" + campingSet +
                '}';
    }
}
