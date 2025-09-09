package com.licenseissuer.model.entity;

import com.licenseissuer.model.LicenseStatusType;
import com.licenseissuer.model.LicenseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.Calendar;
import java.util.Date;

/**
 * 라이센스 정보
 */
@ToString
@Getter
@Entity
public class LicenseInfo {
	@Id
	@Comment("라이센스 아이디")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Comment("라이센스 종류")
	@Enumerated(EnumType.STRING)
	@Column(length = 4, nullable = false)
	protected LicenseType type;

	@Comment("라이센스 상태")
	@Enumerated(EnumType.STRING)
	@Column(length = 4, nullable = false)
	protected LicenseStatusType status;

	@Comment("프로젝트명")
	@Column(length = 100, nullable = false)
	protected String projectName;

	@Comment("할당 IP")
	@Column(length = 100)
	protected String ipAddress;

	@Comment("만료일시")
	@Column
	protected Date expDate;

	@Comment("발급자명")
	@Column(length = 100, nullable = false)
	protected String issuer;

	@ToString.Exclude
	@Comment("발급자 IP")
	@Column(length = 100, nullable = false)
	protected String issuerIp;

	@ToString.Exclude
	@Comment("발급일시")
	@Column(nullable = false, updatable = false)
	@ColumnDefault("CURRENT_TIMESTAMP")
	protected Date issDate;

	@Comment("수정일시")
	@Column(nullable = false)
	@ColumnDefault("CURRENT_TIMESTAMP")
	protected Date modDate;

	public LicenseInfo() {
	}

	public LicenseInfo(LicenseType type, LicenseStatusType status, String projectName, String ipAddress,
					   Date expDate, String issuer, String issuerIp) {
		this.type = type;
		this.status = status;
		this.projectName = projectName;
		this.ipAddress = ipAddress;
		this.expDate = expDate;
		this.issuer = issuer;
		this.issuerIp = issuerIp;
	}

	@PrePersist
	public void prePersist() {
		if (issDate == null) {
			issDate = truncateMillis(new Date());
		}
		if (modDate == null) {
			modDate = truncateMillis(new Date());
		}
	}

	@PreUpdate
	public void preUpdate() {
		modDate = truncateMillis(new Date());
	}

	private Date truncateMillis(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/** 기존 라이센스를 무효화 처리 */
	public void deactivate() {
		this.status = LicenseStatusType.DEACTIVE;
	}

	/** 라이센스 활성화 처리 (필요 시) */
	public void activate() {
		this.status = LicenseStatusType.ACTIVE;
	}
}