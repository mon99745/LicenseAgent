package com.licenseissuer.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LicenseReadRequest {
	protected String operation;
	protected String projectName;
	protected String ipAddress;
	protected String expDate;
	protected String issuer;
}