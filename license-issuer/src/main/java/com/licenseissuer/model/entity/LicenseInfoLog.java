package com.licenseissuer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.Calendar;
import java.util.Date;

/**
 * 라이센스 정보 접근 이력
 */
@Getter
@Entity
public class LicenseInfoLog {
	@Id
	@Comment("트랜잭션 아이디")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Comment("처리내용")
	@Column(nullable = false)
	protected String prcsContent;


	public LicenseInfoLog(LicenseInfo license, String processor,
						  String processorIp, String prcsContent) {
		this.license = license;
		this.processor = processor;
		this.processorIp = processorIp;
		this.prcsContent = prcsContent;
	}

	public LicenseInfoLog() {
	}

	@PrePersist
	public void prePersist() {
		if (prcsDate == null) {
			prcsDate = truncateMillis(new Date());
		}
	}

	private Date truncateMillis(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}