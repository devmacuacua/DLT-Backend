package dlt.dltbackendmaster.reports.domain;

import java.util.HashMap;
import java.util.Map;

public class AgywPrevData {

	private String reportingPeriod;
	private String province;
	private String district;
	private Map<String, Integer> disaggregations;

	public AgywPrevData() {
		super();
	}

	public AgywPrevData(String reportingPeriod, String province, String district) {
		super();
		this.reportingPeriod = reportingPeriod;
		this.province = province;
		this.district = district;
		this.disaggregations = new HashMap<>();
	}

	public String getReportingPeriod() {
		return reportingPeriod;
	}

	public void setReportingPeriod(String reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Map<String, Integer> getDisaggregations() {
		return disaggregations;
	}

	public void setDisaggregations(Map<String, Integer> disaggregations) {
		this.disaggregations = disaggregations;
	}

}
