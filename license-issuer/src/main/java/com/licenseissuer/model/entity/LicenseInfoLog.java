package com.licenseissuer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.Date;

/**
 * 라이센스 정보 접근 이력
 */
@Getter
@Entity
public class LicenseInfoLog {
	@Id
	@Comment("트랜잭션 아이디")
	protected Long tid;

	@Comment("라이센스 아이디")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "license_id", nullable = false)
	protected LicenseInfo license;

	@Comment("처리자명")
	@Column(length = 100, nullable = false)
	@ColumnDefault("'anonymous'")
	protected String processor;

	@Comment("처리자 IP")
	@Column(length = 100, nullable = false)
	protected String processorIp;

	@Comment("처리일시")
	@Column
	@ColumnDefault("CURRENT_TIMESTAMP")
	protected Date prcsDate;

	@Comment("실행쿼리")
	@Lob
	@Column(nullable = false)
	protected String queryText;
}