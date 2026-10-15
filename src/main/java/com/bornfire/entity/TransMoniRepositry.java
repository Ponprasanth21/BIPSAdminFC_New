
package com.bornfire.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface TransMoniRepositry extends JpaRepository<TransactionMonitoring, String> {
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where msg_type='RTP' and  trunc(tran_date)=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomRTP(String tranDate);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where msg_type='OUTGOING' and  trunc(tran_date)=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomMConnect(String tranDate);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where msg_type='INCOMING' and  trunc(tran_date)=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomIncome(String tranDate);
	

	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where (msg_type='BULK_CREDIT' OR msg_type='BULK_DEBIT') and  trunc(tran_date)=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomBulk(String tranDate);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where tran_date=?1 and tran_status=?2 order by tran_date desc", nativeQuery = true)
	List<TransactionMonitoring> findAllCustom1(String tranDate,String tranStatus);

	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where bob_account=?1 and msg_type='RTP' and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomRTPFromMyt(String acctNumber);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where tran_date>=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustom(String tranDate);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where master_ref_id=?1 and  trunc(tran_date)=?2", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomBulkCredit(String seqUniqueID,String tranDate);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where master_ref_id=?1 and  trunc(tran_date)=?2", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomBulkDebit(String seqUniqueID,String tran_date);
	
	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where master_ref_id=?1 and  trunc(tran_date)=?2", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomBulkManual(String seqUniqueID,String tranDate);

	@Query(value = "select * from BIPS_TRANSACTION_MONITORING_TABLE where tran_status='REVERSE_FAILURE' and  trunc(tran_date)=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	List<TransactionMonitoring> findAllCustomReverse(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) as Count from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and msg_type='INCOMING'", nativeQuery = true)
	String countInwardReconcillation(String tranDate);

	@Query(value = "select nvl(sum(tran_amount),0)  from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='OUTGOING' or msg_type='RTP' or msg_type='BULK_CREDIT' or msg_type='BULK_DEBIT')", nativeQuery = true)
	String countOutwardReconcillation(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (tran_status!='FAILURE' and tran_status!='SUCCESS')", nativeQuery = true)
	String countUnmatchReconcillation(String tranDate);

	@Query(value = "select  nvl(sum(tran_amount),0) as Count from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='FAILURE' and (cbs_status='CBS_DEBIT_REVERSE_OK' or cbs_status='CBS_CREDIT_REVERSE_OK') and msg_type='INCOMING'" , nativeQuery = true)
	String countOffsetReconcillation(String tranDate);

	@Query(value = "select sum(entry_entrydtl_btch_tot_amt)as Count from bips_settlement_report where  value_date=?1 and (entry_entrydtl_btch_cd_dbt_ind='CRDT' or (entry_entrydtl_btch_cd_dbt_ind='DBIT' and entry_amount =0))", nativeQuery = true)
	String countSettlBOMInward(String tranDate);
	
	/*
	 * @Query(value =
	 * "select sum(entry_entrydtl_btch_tot_amt)as Count from bips_settlement_report where  msg_date=?1 and entry_entrydtl_btch_cd_dbt_ind='CRDT'"
	 * , nativeQuery = true) String countSettlBOMInward1(String tranDate);
	 */
	
	@Query(value = "select NVL(sum(entry_entrydtl_btch_tot_amt),0)as Count from bips_settlement_report  where  (entry_entrydtl_btch_cd_dbt_ind='CRDT' or (entry_entrydtl_btch_cd_dbt_ind='DBIT' and ENTRY_AMOUNT =0)) and trunc(value_date) between (select trunc(max(acct_bal_time+1))date1 from bips_settl_accts_amt_table where receivable_flg='Y')  and sysdate-1", nativeQuery = true)
	String countSettlBOMInward1(String tranDate);

	@Query(value = "select sum(entry_entrydtl_btch_tot_amt)as Count from bips_settlement_report where  value_date=?1 and entry_entrydtl_btch_cd_dbt_ind='DBIT'", nativeQuery = true)
	String countSettlBOMOutward(String tranDate);
	
	/*
	 * @Query(value =
	 * "select sum(entry_entrydtl_btch_tot_amt)as Count from bips_settlement_report where  msg_date=?1 and entry_entrydtl_btch_cd_dbt_ind='DBIT'"
	 * , nativeQuery = true) String countSettlBOMOutward1(String tranDate);
	 */
	@Query(value = "select NVL(sum(entry_entrydtl_btch_tot_amt),0)as Count from bips_settlement_report  where  entry_entrydtl_btch_cd_dbt_ind='DBIT' and trunc(value_date) between (select trunc(max(acct_bal_time+1))date1 from bips_settl_accts_amt_table where payable_flg='Y')  and sysdate-1", nativeQuery = true)
	String countSettlBOMOutward1(String tranDate);
	
	//@Query(value = "select NVL(sum(entry_entrydtl_btch_tot_amt),0)as Count from bips_settlement_report  where  entry_entrydtl_btch_cd_dbt_ind='DBIT' and trunc(value_date)=?1", nativeQuery = true)
	//String countSettlBOMOutward1(String tranDate);

	@Query(value = "select * from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and msg_type='INCOMING'", nativeQuery = true)
	List<TransactionMonitoring> inwardReconcillationList(String tranDate);

	@Query(value = "select * from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='OUTGOING' or msg_type='RTP' or msg_type='BULK_CREDIT' or msg_type='BULK_DEBIT')", nativeQuery = true)
	List<TransactionMonitoring> outwardReconcillationList(String tranDate);
	
	@Query(value = "SELECT a.tran_date,a.value_date,a.msg_type,b.entry_entrydtl_btch_cd_dbt_ind,a.bob_account,a.bob_account_name,a.ipsx_account,a.ipsx_account_name,b.entry_entrydtl_btch_msg_id,a.tran_audit_number,b.entry_entrydtl_btch_tot_amt,b.entry_entrydtl_btch_cncy,a.tran_status from bips_transaction_monitoring_table a,bips_settlement_report b where b.value_date=?1 and (b.entry_entrydtl_btch_cd_dbt_ind='CRDT' or (b.entry_entrydtl_btch_cd_dbt_ind='DBIT' and b.entry_amount =0)) and b.entry_entrydtl_btch_msg_id=a.sequence_unique_id", nativeQuery = true)
	List<Object[]> inwardSettlList(String tranDate);
	
	@Query(value = "SELECT a.tran_date,a.value_date,a.msg_type,b.entry_entrydtl_btch_cd_dbt_ind,a.bob_account,a.bob_account_name,a.ipsx_account,a.ipsx_account_name,b.entry_entrydtl_btch_msg_id,a.tran_audit_number,b.entry_entrydtl_btch_tot_amt,b.entry_entrydtl_btch_cncy,a.tran_status from bips_transaction_monitoring_table a,bips_settlement_report b where b.value_date=?1 and b.entry_entrydtl_btch_cd_dbt_ind='DBIT' and (b.entry_entrydtl_btch_msg_id=a.sequence_unique_id or b.entry_entrydtl_btch_msg_id=a.bob_message_id)  AND (a.msg_type = 'RTP'  OR a.msg_type ='OUTGOING')", nativeQuery = true)
	List<Object[]> outwardSettlList(String tranDate);

	@Query(value = "select * from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (cbs_status='CBS_DEBIT_REVERSE_OK' or cbs_status='CBS_CREDIT_REVERSE_OK')", nativeQuery = true)
	List<TransactionMonitoring> offsetReconcillationList(String tranDate);

	@Query(value = "select * from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (tran_status!='FAILURE' and tran_status!='SUCCESS')", nativeQuery = true)
	List<TransactionMonitoring> unmatchReconcillationList(String tranDate);

	@Query(value = "select * from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='FAILURE' and (cbs_status!='CBS_DEBIT_REVERSE_OK' and cbs_status!='CBS_CREDIT_REVERSE_OK')", nativeQuery = true)
	List<TransactionMonitoring> failedReconcillationList(String tranDate);

	@Query(value = "select * from bips_transaction_monitoring_table where trunc(tran_date)=?1", nativeQuery = true)
	List<TransactionMonitoring> totalReconcillationList(String tranDate);

	@Query(value = "select a.bank_code,a.bank_name,(select count(*) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and msg_type!='INCOMING' and (cdtr_agt=a.bank_agent))out_no_of_txs," + 
			" (select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and msg_type!='INCOMING' and (cdtr_agt=a.bank_agent  ))out_tot_amt ," + 
			" (select count(*) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and msg_type ='INCOMING' and (instg_agt=a.bank_agent) )in_no_of_txs," + 
			" (select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and msg_type='INCOMING' and (instg_agt=a.bank_agent ))in_tot_amt from bips_other_bank_agent_table a where a.del_flg<>'Y'", nativeQuery = true)
	List<Object[]> snapShotRecord(String tranDate);
	
	
	////MC
	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (msg_type='OUTGOING')", nativeQuery = true)
	String outwardMCListTotTxs(String tranDate);
	
	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='OUTGOING')", nativeQuery = true)
	String outwardMCLisSucTxs(String tranDate);

	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status!='SUCCESS' and (msg_type='OUTGOING')", nativeQuery = true)
	String outwardMCListFailTxs(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (msg_type='OUTGOING')", nativeQuery = true)
	String outwardMCListTotAmt(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='OUTGOING')", nativeQuery = true)
	String outwardMCLisSucAmt(String tranDate);

	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status!='SUCCESS' and (msg_type='OUTGOING')", nativeQuery = true)
	String outwardMCListFailAmt(String tranDate);
	
	
	
	///RTP
	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (msg_type='RTP')", nativeQuery = true)
	String outwardRTPListTotTxs(String tranDate);
	
	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='RTP')", nativeQuery = true)
	String outwardRTPLisSucTxs(String tranDate);

	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status!='SUCCESS' and (msg_type='RTP')", nativeQuery = true)
	String outwardRTPListFailTxs(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (msg_type='RTP')", nativeQuery = true)
	String outwardRTPListTotAmt(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='RTP')", nativeQuery = true)
	String outwardRTPLisSucAmt(String tranDate);

	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status!='SUCCESS' and (msg_type='RTP')", nativeQuery = true)
	String outwardRTPListFailAmt(String tranDate);
	
	///Inward
	
	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (msg_type='INCOMING')", nativeQuery = true)
	String outwardINCListTotTxs(String tranDate);
	
	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='INCOMING')", nativeQuery = true)
	String outwardINCLisSucTxs(String tranDate);

	@Query(value = "select nvl(count(sequence_unique_id),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status!='SUCCESS' and (msg_type='INCOMING')", nativeQuery = true)
	String outwardINCListFailTxs(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and (msg_type='INCOMING')", nativeQuery = true)
	String outwardINCListTotAmt(String tranDate);
	
	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status='SUCCESS' and (msg_type='INCOMING')", nativeQuery = true)
	String outwardINCLisSucAmt(String tranDate);

	@Query(value = "select nvl(sum(tran_amount),0) from bips_transaction_monitoring_table where trunc(tran_date)=?1 and tran_status!='SUCCESS' and (msg_type='INCOMING')", nativeQuery = true)
	String outwardINCListFailAmt(String tranDate);
	


	

	
}
