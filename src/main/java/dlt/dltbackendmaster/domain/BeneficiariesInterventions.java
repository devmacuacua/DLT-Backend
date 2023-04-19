package dlt.dltbackendmaster.domain;
// Generated Jun 13, 2022, 9:37:47 AM by Hibernate Tools 5.2.12.Final

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import dlt.dltbackendmaster.domain.watermelondb.BeneficiaryInterventionSyncModel;
import dlt.dltbackendmaster.serializers.BeneficiarySerializer;
import dlt.dltbackendmaster.serializers.SubServiceSerializer;
import dlt.dltbackendmaster.serializers.UsSerializer;

/**
 * BeneficiariesInterventions generated by hbm2java
 */
@Entity
@Table(name = "beneficiaries_interventions", catalog = "dreams_db")
@NamedQueries({ 
    @NamedQuery(name = "BeneficiaryIntervention.findByLocalities", query = "SELECT bi FROM  BeneficiariesInterventions bi "
                                                            + "left join fetch bi.beneficiaries b " 
            												+ "left join fetch b.locality l "
            												+ "where l.id in (:localities)"),
    @NamedQuery(name = "BeneficiaryIntervention.findByDistricts", query = "SELECT bi FROM  BeneficiariesInterventions bi "
            												+ "left join fetch bi.beneficiaries b " 
            												+ "where b.district.id in (:districts)"),
    @NamedQuery(name = "BeneficiaryIntervention.findByProvinces", query = "SELECT bi FROM  BeneficiariesInterventions bi "
                                                            + "left join fetch bi.beneficiaries b " 
            												+ "where b.district.province.id in (:provinces)"),  
    
	@NamedQuery(name = "BeneficiaryIntervention.findAll", query = "SELECT b FROM BeneficiariesInterventions b"),
	@NamedQuery(name = "BeneficiaryIntervention.findByBeneficiaryAndSubService", 
		query = "SELECT b FROM BeneficiariesInterventions b where b.beneficiaries.id = :beneficiary_id and b.subServices.id = :sub_service_id"),
	@NamedQuery(name = "BeneficiaryIntervention.findByDateCreated", 
		query = "select b from BeneficiariesInterventions b where b.dateUpdated is null and b.dateCreated > :lastpulledat"),
	@NamedQuery(name = "BeneficiaryIntervention.findByDateUpdated",
		query = "select b from BeneficiariesInterventions b where (b.dateUpdated >= :lastpulledat) or (b.dateUpdated >= :lastpulledat and b.dateCreated = b.dateUpdated)"),
	@NamedQuery(name = "BeneficiaryIntervention.findByBeneficiaryId", 
		query = "SELECT b FROM BeneficiariesInterventions b where b.beneficiaries.id = :beneficiary_id"),	
	@NamedQuery(name = "BeneficiaryIntervention.findInterventionsPerBeneficiary", 
		query = " select count(inter.beneficiaries.id) as interventions, inter.beneficiaries.id as beneficiary_id"
				+ " from BeneficiariesInterventions inter "
				+ " group by inter.beneficiaries.id " ),
	@NamedQuery(name = "BeneficiaryIntervention.findByReferenceNotifyToOrBeneficiaryCreatedBy", query = "SELECT bi FROM BeneficiariesInterventions bi "
															+ " where bi.beneficiaries.createdBy = :userId"
															+ " or bi.beneficiaries.id in "
															+ " (SELECT distinct r.beneficiaries.id FROM  References r "										
												            + " where r.status in (0,1) "
												            + " and r.notifyTo.id = :userId) "
												            ),
	@NamedQuery(name = "BeneficiaryIntervention.findByReferenceNotifyToOrBeneficiaryCreatedByAndDateCreated", query = "SELECT bi FROM BeneficiariesInterventions bi "
															+ " where (bi.beneficiaries.createdBy = :userId"
															+ " or bi.beneficiaries.id in "
															+ " (SELECT distinct r.beneficiaries.id FROM  References r "										
												            + " where r.status in (0,1) "
												            + " and r.notifyTo.id = :userId))"
												            + " and bi.dateCreated >= :lastpulledat"
												            ),
	@NamedQuery(name = "BeneficiaryIntervention.findByReferenceNotifyToOrBeneficiaryCreatedByAndDateUpdated", query = "SELECT bi FROM BeneficiariesInterventions bi "
															+ " where (bi.beneficiaries.createdBy = :userId"
															+ " or bi.beneficiaries.id in "
															+ " (SELECT distinct r.beneficiaries.id FROM  References r "										
												            + " where r.status in (0,1) "
												            + " and r.notifyTo.id = :userId))"
												            + " and bi.dateCreated < :lastpulledat "
												            + " and bi.dateUpdated >= :lastpulledat"
												            ),
})
public class BeneficiariesInterventions implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private BeneficiariesInterventionsId id;
	private Beneficiaries beneficiaries;
	private SubServices subServices;
	private Us us;
	private String result;
	private Integer activistId;
	private String entryPoint;
	private String provider;
	private String remarks;
	private int status;
	private int createdBy;
	private Date dateCreated;
	private String updatedBy;
	private Date dateUpdated;
	private String offlineId;
	
	private LocalDate date;
	private String beneficiaryOfflineId;

	public BeneficiariesInterventions() {

		this.id = new BeneficiariesInterventionsId();
		this.beneficiaries = new Beneficiaries();
		this.subServices = new SubServices();
		this.us = new Us();
	}

	public BeneficiariesInterventions(BeneficiariesInterventionsId id, Beneficiaries beneficiaries,
			SubServices subServices, Us us, String entryPoint, int status, int createdBy, Date dateCreated) {
		this.id = id;
		this.beneficiaries = beneficiaries;
		this.subServices = subServices;
		this.us = us;
		this.entryPoint = entryPoint;
		this.status = status;
		this.createdBy = createdBy;
		this.dateCreated = dateCreated;
	}

	public BeneficiariesInterventions(BeneficiariesInterventionsId id, Beneficiaries beneficiaries,
			SubServices subServices, Us us, String result, Integer activistId, String entryPoint, String provider,
			String remarks, int status, int createdBy, Date dateCreated, String updatedBy, Date dateUpdated,
			String offlineId) {
		this.id = id;
		this.beneficiaries = beneficiaries;
		this.subServices = subServices;
		this.us = us;
		this.result = result;
		this.activistId = activistId;
		this.entryPoint = entryPoint;
		this.provider = provider;
		this.remarks = remarks;
		this.status = status;
		this.createdBy = createdBy;
		this.dateCreated = dateCreated;
		this.updatedBy = updatedBy;
		this.dateUpdated = dateUpdated;
		this.offlineId = offlineId;
	}
	
	public BeneficiariesInterventions(BeneficiaryInterventionSyncModel model, String timestamp) throws ParseException {
		this.id = new BeneficiariesInterventionsId();
		this.id.setBeneficiaryId(model.getBeneficiary_id());
		this.id.setSubServiceId(model.getSub_service_id());
		Long t = Long.valueOf(timestamp);
        Date regDate = new Date(t);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(model.getDate(), dtf);
        this.date = date;
        this.beneficiaryOfflineId = model.getBeneficiary_offline_id();
        this.id = new BeneficiariesInterventionsId(model.getBeneficiary_id(), model.getSub_service_id(), date);
        this.beneficiaries = new Beneficiaries(model.getBeneficiary_id());
        this.subServices = new SubServices(model.getSub_service_id());
        this.result = model.getResult();
        this.id.setDate(LocalDate.parse(model.getDate(), dtf));
        this.us = new Us(model.getUs_id());
        this.activistId = model.getActivist_id();
        this.entryPoint = model.getEntry_point();
        this.provider = model.getProvider();
        this.remarks = model.getRemarks();
        this.status = model.getStatus();
        this.offlineId = model.getId();
        this.dateCreated = regDate;
        this.dateUpdated = regDate;
    }

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "beneficiaryId", column = @Column(name = "beneficiary_id", nullable = false)),
			@AttributeOverride(name = "subServiceId", column = @Column(name = "sub_service_id", nullable = false)),
			@AttributeOverride(name = "date", column = @Column(name = "date", nullable = false, length = 10)) })
	public BeneficiariesInterventionsId getId() {
		return this.id;
	}

	public void setId(BeneficiariesInterventionsId id) {
		this.id = id;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beneficiary_id", nullable = false, insertable = false, updatable = false)
	@JsonProperty("beneficiary")
    @JsonSerialize(using = BeneficiarySerializer.class)
	public Beneficiaries getBeneficiaries() {
		return this.beneficiaries;
	}

	public void setBeneficiaries(Beneficiaries beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_service_id", nullable = false, insertable = false, updatable = false)
	@JsonSerialize(using = SubServiceSerializer.class)
	public SubServices getSubServices() {
		return this.subServices;
	}

	public void setSubServices(SubServices subServices) {
		this.subServices = subServices;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "us_id", nullable = false)
	@JsonProperty("us")
    @JsonSerialize(using = UsSerializer.class)
	public Us getUs() {
		return this.us;
	}

	public void setUs(Us us) {
		this.us = us;
	}

	@Column(name = "result", length = 150)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "activist_id")
	public Integer getActivistId() {
		return this.activistId;
	}

	public void setActivistId(Integer activistId) {
		this.activistId = activistId;
	}

	@Column(name = "entry_point", nullable = false, length = 50)
	public String getEntryPoint() {
		return this.entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	@Column(name = "provider", length = 100)
	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Column(name = "remarks", length = 250)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Transient
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Transient
	public String getBeneficiaryOfflineId() {
		return beneficiaryOfflineId;
	}

	public void setBeneficiaryOfflineId(String beneficiaryOfflineId) {
		this.beneficiaryOfflineId = beneficiaryOfflineId;
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

	@Column(name = "updated_by", length = 45)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
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

	@Column(name = "offline_id", length = 45)
	public String getOfflineId() {
		return this.offlineId;
	}

	public void setOfflineId(String offlineId) {
		this.offlineId = offlineId;
	}
	
	public ObjectNode toObjectNode(String lastPulledAt) {
       ObjectMapper mapper = new ObjectMapper()
        							.registerModule(new JavaTimeModule())
        							.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        ObjectNode beneficiaryIntervention = mapper.createObjectNode();

        if (offlineId != null) {
            beneficiaryIntervention.put("id", offlineId);
        } else {
            beneficiaryIntervention.put("id", id.toString());
        }

        if (dateUpdated == null || dateUpdated.compareTo(dateCreated) >= 0 || lastPulledAt == null || lastPulledAt.equals("null")) {
            beneficiaryIntervention.put("beneficiary_offline_id", beneficiaries.getOfflineId());
            beneficiaryIntervention.put("sub_service_id", subServices.getId());
            beneficiaryIntervention.put("result", result);
            beneficiaryIntervention.put("date", id.getDate().toString());
			beneficiaryIntervention.put("us_id", us == null ? null : us.getId());
            beneficiaryIntervention.put("result", result);
            beneficiaryIntervention.put("activist_id", activistId);
            beneficiaryIntervention.put("entry_point", entryPoint);
            beneficiaryIntervention.put("provider", provider);
            beneficiaryIntervention.put("remarks", remarks);
            beneficiaryIntervention.put("status", status);
            beneficiaryIntervention.put("online_id", id.toString()); // flag to control if entity is synchronized with the backend
        } else { // ensure online_id is updated first
            beneficiaryIntervention.put("online_id", id.toString());
        }
        beneficiaryIntervention.put("beneficiary_id", beneficiaries.getId());
        return beneficiaryIntervention;
    }
	
	public void update(BeneficiaryInterventionSyncModel model, String timestamp) throws ParseException {
        Long t = Long.valueOf(timestamp);
        
        this.offlineId = model.getId();
        this.dateUpdated = new Date(t);
        this.beneficiaries.setId(model.getBeneficiary_id());
        this.subServices.setId(model.getSub_service_id());
        this.beneficiaryOfflineId = model.getBeneficiary_offline_id();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.id.setDate(LocalDate.parse(model.getDate(), dtf));
        this.us = new Us(model.getUs_id());
        this.result = model.getResult();
        this.activistId = model.getActivist_id();
        this.entryPoint = model.getEntry_point();
        this.provider = model.getProvider();
        this.remarks = model.getRemarks();
        this.status = model.getStatus();
    }

}
