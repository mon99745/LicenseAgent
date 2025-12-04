package com.licenseweb.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Token - DTO & Entity
 */
@Builder
@Data
@AllArgsConstructor
public class Token {
	/**
	 * 토큰 타입
	 */
	private String grantType;

	/**
	 * 인증 토큰
	 */
	private String accessToken;

	/**
	 * 재발급 토큰
	 */
	private String refreshToken;


	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Entity
	@Builder
	@Table(name = "ValidToken")
	public static class ValidToken {
		/**
		 * 토큰 식별 번호
		 */
		@Id
		@Column(nullable = false)
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		/**
		 * 인증 토큰
		 */
		private String accessToken;

		/**
		 * 재발급 토큰
		 */
		private String refreshToken;

		/**
		 * 토큰 상태 정보
		 */
		@Enumerated(EnumType.STRING)
		private Status status;
	}
}