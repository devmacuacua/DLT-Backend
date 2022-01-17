package dlt.dltbackendmaster.domain;
// Generated Jan 17, 2022, 10:49:25 AM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Neighborhood generated by hbm2java
 */
@Entity
@Table(name = "neighborhood", catalog = "dreams_db")
public class Neighborhood implements java.io.Serializable {

	private int id;
	private Locality locality;
	private User userByCreatedBy;
	private User userByUpdatedBy;
	private String name;
	private String description;
	private int status;
	private Date dateCreated;
	private Date dateUpdated;

	public Neighborhood() {
	}

	public Neighborhood(int id, Locality locality, User userByCreatedBy, String name, int status, Date dateCreated) {
		this.id = id;
		this.locality = locality;
		this.userByCreatedBy = userByCreatedBy;
		this.name = name;
		this.status = status;
		this.dateCreated = dateCreated;
	}

	public Neighborhood(int id, Locality locality, User userByCreatedBy, User userByUpdatedBy, String name,
			String description, int status, Date dateCreated, Date dateUpdated) {
		this.id = id;
		this.locality = locality;
		this.userByCreatedBy = userByCreatedBy;
		this.userByUpdatedBy = userByUpdatedBy;
		this.name = name;
		this.description = description;
		this.status = status;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
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
	@JoinColumn(name = "locality_id", nullable = false)
	public Locality getLocality() {
		return this.locality;
	}

	public void setLocality(Locality locality) {
		this.locality = locality;
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

}