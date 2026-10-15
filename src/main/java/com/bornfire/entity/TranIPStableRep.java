package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface TranIPStableRep extends JpaRepository<TranIPSTable, String>{
	@Query(value = "select * from bips_tran_ips_table where trunc(msg_date)=?1 and msg_code='camt.053.001.08'", nativeQuery = true)
	List<TranIPSTable> findByIdCustomCamt53(String tranDate);
}


