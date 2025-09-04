package com.licenseissuer.model.entity;

import com.licenseissuer.model.LicenseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.Date;

/**
 * 라이센스 정보
 */
@Getter
@Entity
public class LicenseInfo {
	@Id
	@Comment("라이센스 아이디")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Comment("라이센스 종류")
	@Column(length = 4, nullable = false)
	protected LicenseType type;

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

	@Comment("발급자 IP")
	@Column(length = 100, nullable = false)
	protected String issuerIp;

	@Comment("발급일시")
	@Column
	@ColumnDefault("CURRENT_TIMESTAMP")
	protected Date issDate;
}