package com.bornfire.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.configuration.SequenceGenerator;
import com.bornfire.controllers.IPSDaoAd;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.BulkDebitFndTransferRequest;
import com.bornfire.entity.BulkDebitRemitterAccount;
import com.bornfire.entity.BulkTransaction;
import com.bornfire.entity.CIMMerchantQRAddlInfo;
import com.bornfire.entity.CIMMerchantQRcodeAcctInfo;
import com.bornfire.entity.CIMMerchantQRcodeRequest;
import com.bornfire.entity.FrAccount;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.MCMerchantQRAddlInfo;
import com.bornfire.entity.MCMerchantQRcodeAcctInfo;
import com.bornfire.entity.MCMerchantQRcodeRequest;
import com.bornfire.entity.MCMerchantResponse;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.ManualTransaction;
import com.bornfire.entity.MerchantQRRegistration;
import com.bornfire.entity.ToAccount;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.cimMerchantQRcodeResponse;
import com.bornfire.mcib.Mcib;
import com.google.gson.Gson;

@Component
public class IPSServices {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	
	@Autowired
	IPSDaoAd ipsDao;

	public String ManualDebitReversalTransaction(TranCBSTable tranCBStable, String userID) throws UnknownHostException {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());

		HttpEntity<TranCBSTable> entity = new HttpEntity<>(tranCBStable, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/" + userID + "/reverseDebit", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Reversal Transaction Successfully";
			} else {
				return "Something went wrong at server end";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				System.out.println("dfjhgkuy");
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}

	}

	public String ManualBulkCreditReversalTransaction(TranCBSTable tranCBStable, String userID) throws UnknownHostException {

		System.out.println("IPSServices");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());

		HttpEntity<TranCBSTable> entity = new HttpEntity<>(tranCBStable, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/" + userID + "/reverseBulkCredit", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Reversal Transaction Successfully";
			} else {
				return "Something went wrong at server end";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				System.out.println("dfjhgkuy");
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}

	}

	public String ManualBulkDebitReversalTransaction(TranCBSTable tranCBStable, String userID) throws UnknownHostException {
		System.out.println("IPSServices");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());

		HttpEntity<TranCBSTable> entity = new HttpEntity<>(tranCBStable, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/" + userID + "/reverseBulkDebit", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Reversal Transaction Successfully";
			} else {
				return "Something went wrong at server end";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				System.out.println("dfjhgkuy");
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}

	}

	public String ManualCreditReversalTransaction(TranCBSTable tranCBStable, String userID) throws UnknownHostException {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());

		HttpEntity<TranCBSTable> entity = new HttpEntity<>(tranCBStable, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/" + userID + "/reverseCredit", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Reversal Transaction Successfully";
			} else {
				return "Something went wrong at server end";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}

	}

	public String CreateBulkCreditTran(List<BulkTransaction> tranList,String userID) throws UnknownHostException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("PSU_IP_Address", "");
		httpHeaders.set("PSU_ID", "");
		httpHeaders.set("Participant_BIC", "012");
		httpHeaders.set("USER_ID", userID);
		httpHeaders.set("Participant_SOL", tranList.get(0).getRemit_account_no().substring(0, 4));

		BukCreditTransferRequest bulkTranRequest = new BukCreditTransferRequest();

		//FrAccount frAccount = new FrAccount();
		//frAccount.setAcctName(tranList.get(0).getRem_name());
		//frAccount.setAcctNumber(tranList.get(0).getRemit_account_no());
		//frAccount.setSchmType("");
	
		
		List<FrAccount> frAccount = new ArrayList<FrAccount>();
		List<ToAccount> toAccountList = new ArrayList<ToAccount>();
		double totAmt = 0;
		NumberFormat formatter = new DecimalFormat("#0.00");
		for (BulkTransaction bulkTran : tranList) {
			ToAccount toAccount = new ToAccount();
			toAccount.setAcctName(bulkTran.getBen_acct_name());
			toAccount.setAcctNumber(bulkTran.getBen_acct_no());
			toAccount.setTrAmt(bulkTran.getTran_amt().toString());
			toAccount.setBankCode(bulkTran.getBen_bank_code());
			toAccountList.add(toAccount);
			
			FrAccount frAccount1 = new FrAccount();
			frAccount1.setAcctName(bulkTran.getRem_name());
			frAccount1.setAcctNumber(bulkTran.getRemit_account_no());
			frAccount1.setSchmType("");
			frAccount.add(frAccount1);

			totAmt = totAmt + Double.parseDouble(bulkTran.getTran_amt().toString());

		}
		bulkTranRequest.setToAccountList(toAccountList);
		bulkTranRequest.setFrAccount(frAccount);
		bulkTranRequest.setCurrencyCode(tranList.get(0).getTran_crncy_code());
		bulkTranRequest.setTotAmt(formatter.format(totAmt));
		bulkTranRequest.setTrRmks("BT/"+tranList.get(0).getTran_particular());

		HttpEntity<BukCreditTransferRequest> entity = new HttpEntity<>(bulkTranRequest, httpHeaders);
		ResponseEntity<MCCreditTransferResponse> response = null;
		try {

			response = restTemplate.postForEntity(env.getProperty("ipsx.url")+"api/ws/bulkCreditFndTransfer",
					entity, MCCreditTransferResponse.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody().getTranID();
			} else {
				return "0";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "0";
			} else {
				return "0";
			}

		} catch (HttpServerErrorException ex) {
			return "0";
		}
	}

	public String CreateBulkDebitTran(List<BulkTransaction> tranList,String userID) throws UnknownHostException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID", InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("PSU_IP_Address", "");
		httpHeaders.set("USER_ID", userID);

		BulkDebitFndTransferRequest bulkTranRequest = new BulkDebitFndTransferRequest();

		ToAccount toAcctount = new ToAccount();
		toAcctount.setAcctName(tranList.get(0).getBen_acct_name());
		toAcctount.setAcctNumber(tranList.get(0).getBen_acct_no());
		toAcctount.setBankCode(tranList.get(0).getBen_bank_code());
		bulkTranRequest.setBenAccount(toAcctount);

		List<BulkDebitRemitterAccount> remitterList = new ArrayList<BulkDebitRemitterAccount>();
		double totAmt = 0;
		NumberFormat formatter = new DecimalFormat("#0.00");
		for (BulkTransaction bulkTran : tranList) {
			BulkDebitRemitterAccount remAcct = new BulkDebitRemitterAccount();
			remAcct.setRemitterName(bulkTran.getRem_name());
			remAcct.setRemitterAcctNumber(bulkTran.getRemit_account_no());
			remAcct.setTrAmt(bulkTran.getTran_amt().toString());
			remAcct.setCurrencyCode(bulkTran.getTran_crncy_code());
			/*
			 * remAcct.setTrRmks("BT/"+tranList.get(0).getBen_bank_code()+"/"+tranList.get(0
			 * ).getBen_acct_no()+"/"+ tranList.get(0).getBen_acct_name());
			 */
			
			remAcct.setTrRmks("BT/"+bulkTran.getTran_particular()+tranList.get(0).getBen_bank_code()
					+"/"+tranList.get(0).getBen_acct_no()+"/"+ tranList.get(0).getBen_acct_name());
			remitterList.add(remAcct);

		}
		bulkTranRequest.setRemitterAccount(remitterList);
		

		HttpEntity<BulkDebitFndTransferRequest> entity = new HttpEntity<>(bulkTranRequest, httpHeaders);
		ResponseEntity<MCCreditTransferResponse> response = null;
		try {

			response = restTemplate.postForEntity(env.getProperty("ipsx.url")+"api/ws/bulkDebitFndTransfer",
					entity, MCCreditTransferResponse.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody().getTranID();
			} else {
				return "0";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "0";
			} else {
				return "0";
			}

		} catch (HttpServerErrorException ex) {
			return "0";
		}
	}

	public String createPayableSettlTransaction(String frAcct, String toAcct, String tranDate, String trAmt,
			String tranType,String userID) throws UnknownHostException {
		
		HttpHeaders httpHeaders = new HttpHeaders();
	//	httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("USER_ID", userID);
		
		MCCreditTransferRequest mcCreditTransferRequest=new MCCreditTransferRequest();
		FrAccount frAccount = new FrAccount();
		frAccount.setAcctName("PAYABLE");
		frAccount.setAcctNumber(frAcct);
		mcCreditTransferRequest.setFrAccount(frAccount);
		
		ToAccount toAccount =new ToAccount();
		toAccount.setAcctName("SETTLEMENT");
		toAccount.setAcctNumber(toAcct);
		mcCreditTransferRequest.setToAccount(toAccount);
		
		mcCreditTransferRequest.setTrAmt(trAmt);
		
		
		System.out.println("t:"+mcCreditTransferRequest.toString());
		HttpEntity<MCCreditTransferRequest> entity = new HttpEntity<>(mcCreditTransferRequest, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/payableAccountNew", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Settlement Transaction Executed Successfully";
			} else if(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				return "Something went wrong at server end";
			}else {
				return "Transaction Failed";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}
	}

	public String createReceivableSettlTransaction(String frAcct, String toAcct, String tranDate, String trAmt,
			String tranType, String userID) throws UnknownHostException {
		System.out.println("1");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	//	httpHeaders.setContentType("application/json");
		httpHeaders.set("PSU_Device_ID",  InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("USER_ID", userID);
		System.out.println("2");
		MCCreditTransferRequest mcCreditTransferRequest=new MCCreditTransferRequest();
		FrAccount frAccount = new FrAccount();
		frAccount.setAcctName("RECEIVABLE");
		frAccount.setAcctNumber(frAcct);
		mcCreditTransferRequest.setFrAccount(frAccount);
		System.out.println("3");
		ToAccount toAccount =new ToAccount();
		toAccount.setAcctName("SETTLEMENT");
		toAccount.setAcctNumber(toAcct);
		mcCreditTransferRequest.setToAccount(toAccount);
		
		mcCreditTransferRequest.setTrAmt(trAmt);
		System.out.println("t:"+mcCreditTransferRequest.toString());
		HttpEntity<MCCreditTransferRequest> entity = new HttpEntity<>(mcCreditTransferRequest, httpHeaders);
		ResponseEntity<String> response = null;
		try {
			System.out.println("4");
			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/receivableAccountNew", entity, String.class);
			System.out.println("5");
			if (response.getStatusCode() == HttpStatus.OK) {
				return "Settlement Transaction Executed Successfully";
			} else if(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				return "Something went wrong at server end";
			}else {
				return "Transaction Failed";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}
	}

	public String createIncomeSettlTransaction(String frAcct, String toAcct, String tranDate, String trAmt,
			String tranType, String userID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("USER_ID", userID);
		
		MCCreditTransferRequest mcCreditTransferRequest=new MCCreditTransferRequest();
		FrAccount frAccount = new FrAccount();
		frAccount.setAcctName("INCOME");
		frAccount.setAcctNumber(frAcct);
		mcCreditTransferRequest.setFrAccount(frAccount);
		
		ToAccount toAccount =new ToAccount();
		toAccount.setAcctName("SETTLEMENT");
		toAccount.setAcctNumber(toAcct);
		mcCreditTransferRequest.setToAccount(toAccount);
		
		mcCreditTransferRequest.setTrAmt(trAmt);
		
		HttpEntity<MCCreditTransferRequest> entity = new HttpEntity<>(mcCreditTransferRequest, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/incomeAccount", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Settlement Transaction Successfully";
			} else if(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				return "Something went wrong at server end";
			}else {
				return "Transaction Failed";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}
	}

	public String createExpenseSettlTransaction(String frAcct, String toAcct, String tranDate, String trAmt,
			String tranType, String userID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("USER_ID", userID);
		
		MCCreditTransferRequest mcCreditTransferRequest=new MCCreditTransferRequest();
		FrAccount frAccount = new FrAccount();
		frAccount.setAcctName("EXPENSE");
		frAccount.setAcctNumber(frAcct);
		mcCreditTransferRequest.setFrAccount(frAccount);
		
		ToAccount toAccount =new ToAccount();
		toAccount.setAcctName("SETTLEMENT");
		toAccount.setAcctNumber(toAcct);
		mcCreditTransferRequest.setToAccount(toAccount);
		
		mcCreditTransferRequest.setTrAmt(trAmt);
		
		HttpEntity<MCCreditTransferRequest> entity = new HttpEntity<>(mcCreditTransferRequest, httpHeaders);
		ResponseEntity<String> response = null;
		try {

			response = restTemplate.postForEntity(
					env.getProperty("ipsx.url")+"api/ws/expenseAccount", entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return "Settlement Transaction Successfully";
			} else if(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				return "Something went wrong at server end";
			}else {
				return "Transaction Failed";
			}

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "Something went wrong at server end";

			} else {
				return "Transaction Failed";
			}

		} catch (HttpServerErrorException ex) {
			return "Something went wrong at server end";
		}
	}

	public String CreateManualBulkTran(List<ManualTransaction> bulkTranList,String userID) throws UnknownHostException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("PSU_Device_ID", InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("PSU_IP_Address", "");
		httpHeaders.set("USER_ID", userID);

		List<ManualFndTransferRequest> manualList = new ArrayList<>();
		
		for(ManualTransaction manual:bulkTranList) {
			ManualFndTransferRequest manReq=new ManualFndTransferRequest();
			manReq.setRemitterName(manual.getRem_name());
			manReq.setRemitterAcctNumber(manual.getRemit_account_no());
			manReq.setBeneficiaryName(manual.getBen_acct_name());
			manReq.setBeneficiaryAcctNumber(manual.getBen_acct_no());
			manReq.setBeneficiaryBankCode(manual.getBen_bank_code());
			manReq.setCurrencyCode(manual.getTran_crncy_code());
			manReq.setTrAmt(manual.getTran_amt().toString());
			manualList.add(manReq);
		}
		

		HttpEntity<List<ManualFndTransferRequest>> entity = new HttpEntity<>(manualList, httpHeaders);
		ResponseEntity<MCCreditTransferResponse> response = null;
		try {

			response = restTemplate.postForEntity(env.getProperty("ipsx.url")+"api/ws/manualCreditFndTransfer",
					entity, MCCreditTransferResponse.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody().getTranID();
			} else {
				return "0";
			}

		}catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return "0";
			} else {
				return "0";
			}

		} catch (HttpServerErrorException ex) {
			return "0";
		}
	}

	public void getMCIBresponse() {
		
		
		//HttpEntity<String> entity = new HttpEntity<>("");
		
		ResponseEntity<Mcib> response = null;
		try {

			response = restTemplate.postForEntity(
					"http://10.103.2.128:7778/mcibws/servlet/validate?username=BOB&pass=ABC123$$&group=BARB&reporttype=I&residentflag=R&countrycode=MU&entitytype=I&entitycode=B2407671403372",
					"", Mcib.class);
			

			
			//ResponseEntity<String> response1 = restTemplate.exchange("http://10.103.2.128:7778/mcibws/servlet/validate?username=BOB&pass=ABC123$$&group=BARB&reporttype=I&residentflag=R&countrycode=MU&entitytype=I&entitycode=B2407671403372",
				//	HttpMethod.POST, null, String.class);
			
			//String responseBody = response1.getBody();
			
			//System.out.println(responseBody);
			
			if (response.getStatusCode() == HttpStatus.OK) {
				System.out.println("MCIB Response->"+response.toString());
				System.out.println("MCIB Response->"+response.getBody().getCREDIT_PROFILE_RECORDS().getCREDIT_PROFILE().get(0).getADDRESS1());
				
				System.out.println("MCIB Response->"+response.getBody().getCREDIT_PROFILE_RECORDS().getCREDIT_PROFILE().get(0).getADDRESS2());

			} else {
				System.out.println("MCIB Response err->"+response.getStatusCode());
			}

		} catch (HttpClientErrorException ex) {
			System.out.println("MCIB Response erroe->"+ex.getStatusCode()+"ok"+ex.getLocalizedMessage());

		} catch (HttpServerErrorException ex) {
			System.out.println("MCIB Response erroer1->"+ex.getStatusCode()+"ok"+ex.getLocalizedMessage());
		}

		
	}
	@Autowired
	SequenceGenerator sequence;
	
	public cimMerchantQRcodeResponse getMerchantQrCode(MerchantQRRegistration merchantQRgenerator) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, DatatypeConfigurationException, JAXBException, IOException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("X-Request-ID", sequence.generateMerchantQRPID());
		httpHeaders.set("PSU-Device-ID", InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("PSU-IP-Address", InetAddress.getLocalHost().getHostAddress());
		httpHeaders.set("PSU-Channel", "BIPS");

		
		MCMerchantQRcodeRequest  cimMerchantQRcodeRequest=new MCMerchantQRcodeRequest();
		System.out.println(merchantQRgenerator.getPayload_format_indicator().toString());
		cimMerchantQRcodeRequest.setPayloadFormatIndiator(merchantQRgenerator.getPayload_format_indicator().toString());
		cimMerchantQRcodeRequest.setPointOfInitiationFormat(merchantQRgenerator.getPoi_method().toString());
	
		MCMerchantQRcodeAcctInfo merchantQRAcctInfo=new MCMerchantQRcodeAcctInfo();
		merchantQRAcctInfo.setGlobalID(merchantQRgenerator.getGlobal_unique_id());
		merchantQRAcctInfo.setPayeeParticipantCode(merchantQRgenerator.getPayee_participant_code());
		merchantQRAcctInfo.setMerchantAcctNumber(merchantQRgenerator.getMerchant_acct_no());
		merchantQRAcctInfo.setMerchantID(merchantQRgenerator.getMerchant_id());
		cimMerchantQRcodeRequest.setMerchantAcctInformation(merchantQRAcctInfo);
		
		cimMerchantQRcodeRequest.setMCC(merchantQRgenerator.getMerchant_category_code().toString());
		cimMerchantQRcodeRequest.setCurrency(merchantQRgenerator.getTransaction_crncy().toString());
		
		
		if(!String.valueOf(merchantQRgenerator.getTransaction_amt()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getTransaction_amt()).equals("")) {
			cimMerchantQRcodeRequest.setTrAmt(merchantQRgenerator.getTransaction_amt().toString());
		}
		
		
		/*if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("0")){
			cimMerchantQRcodeRequest.setConvenienceIndicator(false);
		}else {
			cimMerchantQRcodeRequest.setConvenienceIndicator(true);
			cimMerchantQRcodeRequest.setConvenienceIndicatorFeeType(merchantQRgenerator.getConv_fees_type().toString());
			cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());
		}*/
		
		if(!String.valueOf(merchantQRgenerator.getTip_or_conv_indicator()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getTip_or_conv_indicator()).equals("")) {
			if(!merchantQRgenerator.getTip_or_conv_indicator().toString().equals("")){

				if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("01")) {
					cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("01");
				}else if(merchantQRgenerator.getTip_or_conv_indicator().toString().equals("02")) {
					if(merchantQRgenerator.getConv_fees_type().equals("Fixed")) {
						cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("02");
						cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());
					}else if(merchantQRgenerator.getConv_fees_type().equals("Percentage")) {
						cimMerchantQRcodeRequest.setTipOrConvenienceIndicator("03");
						cimMerchantQRcodeRequest.setConvenienceIndicatorFee(merchantQRgenerator.getValue_conv_fees());

					}
				}
				
			}
		}
		
		
		cimMerchantQRcodeRequest.setCountryCode(merchantQRgenerator.getCountry().toString());
		cimMerchantQRcodeRequest.setMerchantName(merchantQRgenerator.getMerchant_name().toString());
		cimMerchantQRcodeRequest.setCity(merchantQRgenerator.getCity().toString());
		
		if(!String.valueOf(merchantQRgenerator.getZip_code()).equals("null")&&
				!String.valueOf(merchantQRgenerator.getZip_code()).equals("")) {
			cimMerchantQRcodeRequest.setPostalCode(merchantQRgenerator.getZip_code().toString());
		}
		
		
		
		MCMerchantQRAddlInfo cimMercbantQRAddlInfo=new MCMerchantQRAddlInfo();
		cimMercbantQRAddlInfo.setBillNumber(merchantQRgenerator.getBill_number());
		cimMercbantQRAddlInfo.setMobileNumber(merchantQRgenerator.getMobile());
		cimMercbantQRAddlInfo.setStoreLabel(merchantQRgenerator.getStore_label());
		cimMercbantQRAddlInfo.setLoyaltyNumber(merchantQRgenerator.getLoyalty_number());
		cimMercbantQRAddlInfo.setCustomerLabel(merchantQRgenerator.getCustomer_label());
		cimMercbantQRAddlInfo.setTerminalLabel(merchantQRgenerator.getTerminal_label());
		cimMercbantQRAddlInfo.setReferenceLabel(merchantQRgenerator.getReference_label());
		cimMercbantQRAddlInfo.setPurposeOfTransaction(merchantQRgenerator.getPurpose_of_tran());
		cimMercbantQRAddlInfo.setAddlDataRequest(merchantQRgenerator.getAdditional_details());
		
		cimMerchantQRcodeRequest.setAdditionalDataInformation(cimMercbantQRAddlInfo);
		
	//	HttpEntity<CIMMerchantQRcodeRequest> entity = new HttpEntity<>(cimMerchantQRcodeRequest, httpHeaders);
		 cimMerchantQRcodeResponse response = null;
	

//			response = restTemplate.postForEntity(env.getProperty("ipsadmin.url")+"api/ws/generateMerchantQRcodes",
//					entity, cimMerchantQRcodeResponse.class);
			response = ipsDao.createMerchantQRConnection(InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getHostAddress(), sequence.generateMerchantQRPID(),
					cimMerchantQRcodeRequest, sequence.generateMerchantQRPID(), "Maucas", "resvfield1", "resvfield2");
			
			System.out.println("data-->");
		 
				return response;
			 

		 


	}

}
