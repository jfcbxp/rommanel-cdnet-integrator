package com.br.jfcbxp.rommanel.cdnet.repositorys;

import com.br.jfcbxp.rommanel.cdnet.domains.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

}

