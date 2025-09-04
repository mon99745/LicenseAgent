package com.licenseissuer.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LicenseReadResponse {
	protected String type;
	protected String projectName;
	protected String ipAddress;
	protected String expDate;
	protected String issuer;
	protected String issuerIp;
	protected String issueDate;
}