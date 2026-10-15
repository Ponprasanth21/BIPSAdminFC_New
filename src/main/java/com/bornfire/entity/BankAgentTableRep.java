package com.bornfire.entity;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface BankAgentTableRep extends JpaRepository<BankAgentTable, String> {
	Optional<BankAgentTable> findById(String directorId);

	@Query(value = "select * from BIPS_OTHER_BANK_AGENT_TABLE where  NVL(DEL_FLG,1) <> 'Y' and bank_code!='02'", nativeQuery = true)
	List<BankAgentTable> findAllCustom();

	@Query(value = "select * from BIPS_OTHER_BANK_AGENT_TABLE where bank_code= ?1", nativeQuery = true)
	BankAgentTable findByIdCustom(String Id);
	

	@Query(value = "select * from BIPS_OTHER_BANK_AGENT_TABLE where bank_agent= ?1", nativeQuery = true)
	BankAgentTable findByIdCustomAgent(String Id);
	
	@Query(value = "select * from BIPS_OTHER_BANK_AGENT_TABLE where bank_agent= ?1", nativeQuery = true)
	List<BankAgentTable> findByIdCustomAgent1(String Id);
}
