package com.bornfire.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SettlementReportRep extends JpaRepository<SettlementReport, String> {

	@Query(value = "SELECT a.sequence_unique_id,b.ENTRY_ENTRYDTL_BTCH_MSG_ID,a.msg_type,a.bob_account,a.ipsx_account,a.tran_currency,a.tran_amount,b.ENTRY_ENTRYDTL_TXDTS_AMT,a.tran_status,"
			+ "a.tran_date,b.entry_entrydtl_btch_cd_dbt_ind,a.bob_account_name,a.ipsx_account_name,b.entry_entrydtl_btch_cncy" + 
			" FROM bips_transaction_monitoring_table a,bips_settlement_report b where a.sequence_unique_id=b.entry_entrydtl_btch_msg_id and b.msg_date=?1 order by a.tran_date DESC", nativeQuery = true)
	List<Object[]> getSettleCurrentReport(String tranDate);
	
	@Query(value = "SELECT a.sequence_unique_id,b.ENTRY_ENTRYDTL_BTCH_MSG_ID,a.msg_type,a.bob_account,a.ipsx_account,a.tran_currency,a.tran_amount,b.ENTRY_ENTRYDTL_TXDTS_AMT,a.tran_status,"
			+ "a.tran_date,b.entry_entrydtl_btch_cd_dbt_ind,a.bob_account_name,a.ipsx_account_name,b.entry_entrydtl_btch_cncy" + 
			" FROM bips_transaction_hist_monitoring_table a,bips_settlement_report b where b.msg_date=?1 and (a.sequence_unique_id=b.entry_entrydtl_btch_msg_id or a.bob_message_id=b.entry_entrydtl_btch_msg_id) order by a.tran_date DESC", nativeQuery = true)
	List<Object[]> getSettleCurrentReport1(String tranDate);
	
	/*@Query(value = "SELECT sequence_unique_id,ENTRY_ENTRYDTL_BTCH_MSG_ID,msg_type,bob_account,ipsx_account,tran_currency,tran_amount,ENTRY_ENTRYDTL_TXDTS_AMT,tran_status,tran_date" + 
			" FROM transaction_hist_monitoring_table" + 
			" LEFT JOIN settlement_report ON ENTRY_ENTRYDTL_BTCH_MSG_ID = sequence_unique_id where trunc(tran_date)=?1" + 
			" ORDER BY tran_date DESC", nativeQuery = true)
	List<Object[]> getSettleCurrentReport1(String tranDate);*/

	@Query(value = "select a.value_date,a.entry_entrydtl_btch_msg_id,b.bob_account,a.enter_entrydtl_txdts_cncy,a.entry_entrydtl_txdts_amt from bips_settlement_report a,bips_transaction_hist_monitoring_table b where b.sequence_unique_id=a.entry_entrydtl_btch_msg_id and a.value_date=?1 and b.msg_type='OUTGOING'", nativeQuery = true)
	List<Object[]> findAllCustomTranOutwardSuccess(String tranDate);
	
	@Query(value = "select a.value_date,a.entry_entrydtl_btch_msg_id,b.bob_account,a.enter_entrydtl_txdts_cncy,a.entry_entrydtl_txdts_amt from bips_settlement_report a,bips_transaction_hist_monitoring_table b where b.sequence_unique_id=a.entry_entrydtl_btch_msg_id and a.value_date=?1 and b.msg_type='INCOMING'", nativeQuery = true)
	List<Object[]> findAllCustomTranInwardSuccess(String tranDate);
	
	@Query(value = "select a.value_date,a.entry_entrydtl_btch_msg_id,b.bob_account,a.enter_entrydtl_txdts_cncy,a.entry_entrydtl_txdts_amt from bips_settlement_report a,bips_transaction_hist_monitoring_table b where b.sequence_unique_id=a.entry_entrydtl_btch_msg_id and a.value_date=?1 and b.msg_type='INCOMING'", nativeQuery = true)
	List<Object[]> findAllCustomTranRTPSuccess(String tranDate);

	@Query(value = "select * from bips_settlement_report where value_date=?1", nativeQuery = true)
	List<SettlementReport> totReconcillationList(String tranDate);
	
	
	@Query(value = "SELECT nvl(count(a.sequence_unique_id),0) FROM bips_transaction_hist_monitoring_table a,bips_settlement_report b where b.msg_date=?1 and (a.sequence_unique_id=b.entry_entrydtl_btch_msg_id or a.bob_message_id=b.entry_entrydtl_btch_msg_id) order by a.tran_date DESC", nativeQuery = true)
	String getSettleCurrentReport1TotTr(String tranDate);
	
	@Query(value = "SELECT nvl(sum(a.tran_amount),0) FROM bips_transaction_hist_monitoring_table a,bips_settlement_report b where b.msg_date=?1 and (a.sequence_unique_id=b.entry_entrydtl_btch_msg_id or a.bob_message_id=b.entry_entrydtl_btch_msg_id) order by a.tran_date DESC", nativeQuery = true)
	String getSettleCurrentReport1TotAmt(String tranDate);
	
	
	

}