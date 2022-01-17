package dlt.dltbackendmaster.domain;
// Generated Jan 17, 2022, 10:49:25 AM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Partner generated by hbm2java
 */
@Entity
@Table(name = "partner", catalog = "dreams_db")
public class Partner implements java.io.Serializable {

	private int id;
	private District district;
	private PartnerType partnerType;
	private User userByCreatedBy;
	private User userByUpdatedBy;
	private String name;
	private String abbreviation;
	private String description;
	private String logo;
	private int status;
	private Date dateCreated;
	private Date dateUpdated;
	private Set users = new HashSet(0);

	public Partner() {
	}

	public Partner(int id, PartnerType partnerType, User userByCreatedBy, String name, int status, Date dateCreated) {
		this.id = id;
		this.partnerType = partnerType;
		this.userByCreatedBy = userByCreatedBy;
		this.name = name;
		this.status = status;
		this.dateCreated = dateCreated;
	}

	public Partner(int id, District district, PartnerType partnerType, User userByCreatedBy, User userByUpdatedBy,
			String name, String abbreviation, String description, String logo, int status, Date dateCreated,
			Date dateUpdated, Set users) {
		this.id = id;
		this.district = district;
		this.partnerType = partnerType;
		this.userByCreatedBy = userByCreatedBy;
		this.userByUpdatedBy = userByUpdatedBy;
		this.name = name;
		this.abbreviation = abbreviation;
		this.description = description;
		this.logo = logo;
		this.status = status;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
		this.users = users;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id")
	public District getDistrict() {
		return this.district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "partner_type_id", nullable = false)
	public PartnerType getPartnerType() {
		return this.partnerType;
	}

	public void setPartnerType(PartnerType partnerType) {
		this.partnerType = partnerType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	public User getUserByCreatedBy() {
		return this.userByCreatedBy;
	}

	public void setUserByCreatedBy(User userByCreatedBy) {
		this.userByCreatedBy = userByCreatedBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	public User getUserByUpdatedBy() {
		return this.userByUpdatedBy;
	}

	public void setUserByUpdatedBy(User userByUpdatedBy) {
		this.userByUpdatedBy = userByUpdatedBy;
	}

	@Column(name = "name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "abbreviation", length = 150)
	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Column(name = "description", length = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "logo", length = 250)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Column(name = "status", nullable = false)
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 19)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_updated", length = 19)
	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partner")
	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}