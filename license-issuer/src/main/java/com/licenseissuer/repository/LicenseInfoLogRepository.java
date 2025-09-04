package com.licenseissuer.repository;

import com.licenseissuer.model.entity.LicenseInfoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseInfoLogRepository extends JpaRepository<LicenseInfoLog, Long> {
}