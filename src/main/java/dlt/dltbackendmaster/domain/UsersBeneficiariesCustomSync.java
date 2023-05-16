package dlt.dltbackendmaster.domain;
// Generated Jun 13, 2022, 9:37:47 AM by Hibernate Tools 5.2.12.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dlt.dltbackendmaster.serializers.BeneficiarySerializer;
import dlt.dltbackendmaster.serializers.UsSerializer;

/**
 * UsersBeneficiariesCustomSync generated by hbm2java
 */
@Entity
@Table(name = "users_beneficiaries_sync", catalog = "dreams_db", uniqueConstraints = @UniqueConstraint(columnNames = {
		"user_id", "beneficiary_id" }))
@NamedQueries({
		@NamedQuery(name = "UsersBeneficiariesCustomSync.findByUserId", query = "SELECT ub FROM UsersBeneficiariesCustomSync ub "
				+ " INNER JOIN fetch ub.user u " + " INNER JOIN fetch ub.beneficiary b " + " where u.id = :userId"),
		@NamedQuery(name = "UsersBeneficiariesCustomSync.findByUserIdAndSyncDate", query = "SELECT ub FROM UsersBeneficiariesCustomSync ub "
				+ " INNER JOIN fetch ub.user u " 
				+ " INNER JOIN fetch ub.beneficiary b " 
				+ " where u.id = :userId "
				+ " and ub.syncDate > :lastpulledat "),
		@NamedQuery(name = "UsersBeneficiariesCustomSync.findByUserIdAndBeneficiaryId", query = "SELECT ub FROM UsersBeneficiariesCustomSync ub "
				+ " INNER JOIN fetch ub.user u " + " INNER JOIN fetch ub.beneficiary b " + " where u.id = :userId "
				+ " and b.id =:beneficiaryId"), })
public class UsersBeneficiariesCustomSync implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Beneficiaries beneficiary;
	private Users user;
	private Date syncDate = new Date();

	public UsersBeneficiariesCustomSync() {
		this.beneficiary = new Beneficiaries();
		this.user = new Users();
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
	@JoinColumn(name = "beneficiary_id")
	@JsonProperty("beneficiary")
	@JsonSerialize(using = BeneficiarySerializer.class)
	public Beneficiaries getBeneficiary() {
		return this.beneficiary;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonProperty("user")
	@JsonSerialize(using = UsSerializer.class)
	public Users getUser() {
		return this.user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sync_date", nullable = false, length = 19)
	public Date getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	public void setBeneficiary(Beneficiaries beneficiary) {
		this.beneficiary = beneficiary;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
