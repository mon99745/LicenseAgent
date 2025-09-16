package com.licenseissuer.repository;

import com.licenseissuer.model.entity.LicenseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseInfoRepository extends JpaRepository<LicenseInfo, Long> {
	List<LicenseInfo> findByIssuer(String issuer);
}