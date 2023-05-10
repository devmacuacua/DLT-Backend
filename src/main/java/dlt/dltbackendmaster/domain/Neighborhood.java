package dlt.dltbackendmaster.domain;
// Generated Jun 13, 2022, 9:37:47 AM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dlt.dltbackendmaster.serializers.LocalitySerializer;
import dlt.dltbackendmaster.serializers.UsSerializer;

/**
 * Neighborhood generated by hbm2java
 */
@Entity
@Table(name = "neighborhood", catalog = "dreams_db")
@NamedQueries({

    @NamedQuery(name = "Neighborhood.findBySyncLocalities", query = "SELECT n FROM  Neighborhood n "
                                                + "left join fetch n.locality l "
                                                + "where l.id in (:localities)"),
    @NamedQuery(name = "Neighborhood.findBySyncDistricts", query = "SELECT n FROM  Neighborhood n "
                                                + "left join fetch n.locality l "
                                                + "where l.district.id in (:districts)"),
    @NamedQuery(name = "Neighborhood.findBySyncProvinces", query = "SELECT n FROM  Neighborhood n "
                                                + "left join fetch n.locality l "
                                                + "where l.district.province.id in (:provinces)"),
    @NamedQuery(name = "Neighborhood.findAll", query = "SELECT c FROM Neighborhood c"),
    @NamedQuery(name = "Neighborhood.findByLocalities", query = "SELECT c FROM Neighborhood c WHERE c.locality.id in (:localities) and c.status=1"),
    @NamedQuery(name = "Neighborhood.findByDateCreated", query = "SELECT c FROM Neighborhood c WHERE c.dateCreated >= :lastpulledat"),
    @NamedQuery(name = "Neighborhood.findByDateUpdated", query = "SELECT c FROM Neighborhood c WHERE c.dateCreated < :lastpulledat and c.dateUpdated >= :lastpulledat"),
    @NamedQuery(name = "Neighborhood.findById", query = "SELECT n FROM  Neighborhood n where n.id = :id"),   
    @NamedQuery(name = "Neighborhood.findByIds", query = "SELECT n FROM  Neighborhood n where n.id in :ids"),  
})
public class Neighborhood implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Locality locality;

    private Us us;

    private String name;

    private String description;

    private int status;

    private int createdBy;

    private Date dateCreated;

    private Integer updatedBy;

    private Date dateUpdated;

    private Set<Beneficiaries> beneficiarieses = new HashSet<Beneficiaries>(0);

    public Neighborhood() {}

    public Neighborhood(Locality locality, Us us, String name, int status, int createdBy, Date dateCreated) {
        this.locality = locality;
        this.us = us;
        this.name = name;
        this.status = status;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
    }

    public Neighborhood(Locality locality, Us us, String name, String description, int status, int createdBy, Date dateCreated,
                        Integer updatedBy, Date dateUpdated, Set<Beneficiaries> beneficiarieses) {
        this.locality = locality;
        this.us = us;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.updatedBy = updatedBy;
        this.dateUpdated = dateUpdated;
        this.beneficiarieses = beneficiarieses;
    }

    public Neighborhood(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_id", nullable = false)
    @JsonProperty("locality")
    @JsonSerialize(using = LocalitySerializer.class)
    public Locality getLocality() {
        return this.locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "us_id", nullable = false)
    @JsonProperty("us")
    @JsonSerialize(using = UsSerializer.class)
    public Us getUs() {
        return us;
    }

    public void setUs(Us us) {
        this.us = us;
    }

    @Column(name = "name", nullable = false, length = 150)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 250)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "created_by", nullable = false)
    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, length = 19)
    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Column(name = "updated_by")
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated", length = 19)
    public Date getDateUpdated() {
        return this.dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "neighborhood")
    public Set<Beneficiaries> getBeneficiarieses() {
        return this.beneficiarieses;
    }

    public void setBeneficiarieses(Set<Beneficiaries> beneficiarieses) {
        this.beneficiarieses = beneficiarieses;
    }

    public ObjectNode toObjectNode() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode neighborhood = mapper.createObjectNode();
        neighborhood.put("id", id);
        neighborhood.put("locality_id", locality == null? null : locality.getId());
        neighborhood.put("us_id", us == null? null : us.getId());
        neighborhood.put("name", name);
        neighborhood.put("description", description);
        neighborhood.put("status", status);
        neighborhood.put("online_id", id); // flag to control if entity is synchronized with the backend
        return neighborhood;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Neighborhood other = (Neighborhood) obj;
		if (!id.equals(other.id))
			return false;
		return true;
	}

}
