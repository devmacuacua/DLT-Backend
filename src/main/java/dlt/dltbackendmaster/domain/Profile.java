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
 * Profile generated by hbm2java
 */
@Entity
@Table(name = "profile", catalog = "dreams_db")
public class Profile implements java.io.Serializable {

	private int id;
	private User userByUpdatedBy;
	private User userByCreatedBy;
	private String name;
	private String descriptiom;
	private int status;
	private Date dateCreated;
	private Date dateUpdated;
	private Set users = new HashSet(0);

	public Profile() {
	}

	public Profile(int id, User userByCreatedBy, String name, int status, Date dateCreated) {
		this.id = id;
		this.userByCreatedBy = userByCreatedBy;
		this.name = name;
		this.status = status;
		this.dateCreated = dateCreated;
	}

	public Profile(int id, User userByUpdatedBy, User userByCreatedBy, String name, String descriptiom, int status,
			Date dateCreated, Date dateUpdated, Set users) {
		this.id = id;
		this.userByUpdatedBy = userByUpdatedBy;
		this.userByCreatedBy = userByCreatedBy;
		this.name = name;
		this.descriptiom = descriptiom;
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
	@JoinColumn(name = "updated_by")
	public User getUserByUpdatedBy() {
		return this.userByUpdatedBy;
	}

	public void setUserByUpdatedBy(User userByUpdatedBy) {
		this.userByUpdatedBy = userByUpdatedBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	public User getUserByCreatedBy() {
		return this.userByCreatedBy;
	}

	public void setUserByCreatedBy(User userByCreatedBy) {
		this.userByCreatedBy = userByCreatedBy;
	}

	@Column(name = "name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "descriptiom", length = 250)
	public String getDescriptiom() {
		return this.descriptiom;
	}

	public void setDescriptiom(String descriptiom) {
		this.descriptiom = descriptiom;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}
