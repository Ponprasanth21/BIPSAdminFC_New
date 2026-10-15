package com.bornfire.controllers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BulkTransaction;
import com.bornfire.entity.BulkTransactionRepository;
import com.bornfire.entity.BusinessHours;
import com.bornfire.entity.IPSAccessRole;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.MCMerchantQRcodeRequest;
import com.bornfire.entity.MCMerchantResponse;
import com.bornfire.entity.MandateMaster;
import com.bornfire.entity.ManualTransaction;
import com.bornfire.entity.ManualTransactionRepository;
import com.bornfire.entity.MerchantQRRegistration;
import com.bornfire.entity.MerchantQrCodeRegRep;
import com.bornfire.entity.QRUrlGobalEntity;
import com.bornfire.entity.RTPOutgoingRep;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.TransMoniRepositry;
import com.bornfire.entity.TransactionAdmin;
import com.bornfire.entity.TransationMonitorPojo;
import com.bornfire.entity.UPIQrRep;
import com.bornfire.entity.cimMerchantQRcodeResponse;
import com.bornfire.exception.IPSXException;
import com.bornfire.qrcode.core.isos.Country;
import com.bornfire.qrcode.core.isos.Currency;
import com.bornfire.services.BankAndBranchMasterServices;
import com.bornfire.services.BulkServices;
import com.bornfire.services.IPSAccessRoleService;
import com.bornfire.services.IPSServices;
import com.bornfire.services.LoginSecurityServices;
import com.bornfire.services.MandateServices;
import com.bornfire.services.MonitorService;
import com.bornfire.services.QRCODEServices;
import com.bornfire.services.SettlementAccountServices;
import com.bornfire.services.TransactionService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@RestController
public class IPSRestController {

	private static final Logger logger = LoggerFactory.getLogger(IPSRestController.class);
	@Autowired
	TransactionService newOtherTransaction;

	@Autowired
	IPSServices ipsServices;

	@Autowired
	TransMoniRepositry tranMonitorRep;

	@Autowired
	BulkTransactionRepository bulkTranRep;
	
	@Autowired
	ManualTransactionRepository manualTranRep;

	@Autowired
	BulkServices bulkServices;

	@Autowired
	SettlementAccountServices settlementAccountServices;

	@Autowired
	BankAndBranchMasterServices bankAndBranchMasterServices;

	@Autowired
	IPSAccessRoleService AccessRoleService;
	
	@Autowired
	LoginSecurityServices loginSecurityServices;
	
	@Autowired
	MonitorService monitorService;
	
	@Autowired
	MandateServices mandateServices;


	@Autowired
	Environment env;
	
	@Autowired
	IPSDaoAd ipsDao;
	
	@Autowired
	RTPOutgoingRep rtpOutgoingRep;
	
	@Autowired
	UPIQrRep upiqrrep;
	
	@Autowired
	MerchantQrCodeRegRep merchantQrCodeRegRep;
	
	@Autowired 
	QRCODEServices qrCODEServices;
	
	@RequestMapping(value = "/getTransaction/{acct_num}", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode13(@PathVariable("acct_num") String acct_num, Model md) {
		System.out.println(acct_num);
		return newOtherTransaction.getTransaction(acct_num);

	}

	@RequestMapping(value = "/getTransaction/", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode3(Model md) {

		return newOtherTransaction.getTransaction();

	}

	@RequestMapping(value = "/getTransaction/{acct_num}/{from_date}/{to_date}", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode1(@PathVariable("from_date") String from_date,
			@PathVariable("to_date") String to_date, @PathVariable("acct_num") String acct_num, Model md) {
		System.out.println(acct_num);
		System.out.println(from_date);
		System.out.println(to_date);

		return newOtherTransaction.getTransactionDetail(from_date, to_date, acct_num);

	}

	@RequestMapping(value = "/getTransaction/{from_date}/{to_date}", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode(@PathVariable("from_date") String from_date,
			@PathVariable("to_date") String to_date, Model md) {
		System.out.println(from_date);
		System.out.println(to_date);

		return newOtherTransaction.getTransactionDetail(from_date, to_date);

	}

	/************************************************
	 * FAILED WITHOUT ACCOUNT NUMBER
	 ********************************************************************/
	@RequestMapping(value = "/getTransactionFailed/", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode7(Model md) {
		System.out.println("kkkkkk");
		return newOtherTransaction.getTransactionFailed();

	}

	@RequestMapping(value = "/getTransactionFailed/{from_date}/{to_date}", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode11(@PathVariable("from_date") String from_date,
			@PathVariable("to_date") String to_date, Model md) {
		System.out.println(from_date);
		System.out.println(to_date);

		return newOtherTransaction.getTransactionFailed(from_date, to_date);
	}

	/*****************************************
	 * WITH ACCOUNT NUMBER
	 ********************************************/

	@RequestMapping(value = "/getTransactionFailed/{acct_num}", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode10(@PathVariable("acct_num") String acct_num, Model md) {
		System.out.println(acct_num);
		return newOtherTransaction.getTransactionFailed(acct_num);

	}

	@RequestMapping(value = "/getTransactionFailed/{acct_num}/{from_date}/{to_date}", method = RequestMethod.GET)
	public List<TransactionAdmin> refcode12(@PathVariable("from_date") String from_date,
			@PathVariable("to_date") String to_date, @PathVariable("acct_num") String acct_num, Model md) {
		System.out.println(from_date);
		System.out.println(to_date);

		return newOtherTransaction.getTransactionFailed(from_date, to_date, acct_num);
	}

	/*
	 * @RequestMapping(value = "/getTranList/", method = RequestMethod.GET) public
	 * List<BulkTransaction> tranList( Model md) {
	 * 
	 * return
	 * 
	 * }
	 */

	@RequestMapping(value = "/ReversalTransaction", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public String ReverseTran(@RequestBody TranCBSTable tranCBStable, Model md,
			@RequestParam(value = "userID", required = false) String userID) throws UnknownHostException {

		
		if (tranCBStable.getTran_type().equals("DEBIT_REVERSE")) {
			System.out.println("DEBIT_REVERSE");
			String response = ipsServices.ManualDebitReversalTransaction(tranCBStable, userID);
			return response;
		} else if (tranCBStable.getTran_type().equals("CREDIT_REVERSE")) {
			String response = ipsServices.ManualCreditReversalTransaction(tranCBStable, userID);
			return response;
		} else if (tranCBStable.getTran_type().equals("BULK_CREDIT_REVERSE")) {
			String response = ipsServices.ManualBulkCreditReversalTransaction(tranCBStable, userID);
			return response;
		} else if (tranCBStable.getTran_type().equals("BULK_DEBIT_REVERSE")) {
			String response = ipsServices.ManualBulkDebitReversalTransaction(tranCBStable,userID);
			return response;
		} else {
			return "Wrong Request";
		}
	}

	@RequestMapping(value = "IPSBulkTransactionAdd", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public MCCreditTransferResponse IPSBulkTransactionAdd(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "userID", required = false) String userID,
			@ModelAttribute BulkTransaction bulkTransaction, Model md, HttpServletRequest req) throws IOException {

		MCCreditTransferResponse msg = bulkServices.getDetailsCredit(bulkTransaction, formmode,userID);
		return msg;
	}

	@RequestMapping(value = "IPSBulkDebitTransactionAdd", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public MCCreditTransferResponse IPSBulkDebitTransactionAdd(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "userID", required = false) String userID,
			@ModelAttribute BulkTransaction bulkTransaction, Model md, HttpServletRequest req) throws IOException {

		System.out.println("Welcome");
		MCCreditTransferResponse msg = bulkServices.getDetailsDebit(bulkTransaction, formmode,userID);
		return msg;
	}
	

	@RequestMapping(value = "IPSManualTransactionAdd", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public MCCreditTransferResponse IPSManualTransactionAdd(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "userID", required = false) String userID,
			@ModelAttribute ManualTransaction bulkTransaction, Model md, HttpServletRequest req) throws IOException {

		System.out.println(userID);
		MCCreditTransferResponse msg = bulkServices.getDetailsManualDebit(bulkTransaction, formmode,userID);
		
		return msg;
	}

	@RequestMapping(value = "/IPSBulkCreditTransactionList/{seqUniqueID}", method = RequestMethod.POST)
	@ResponseBody
	public List<TransationMonitorPojo> IPSBulkCreditTransactionList(
			@PathVariable(value = "seqUniqueID", required = true) String seqUniqueID, Model md) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String tranDate = dateFormat.format(new Date());
		List<TransationMonitorPojo> moniService = monitorService.findAllCustomBulkCredit(seqUniqueID, tranDate);
		//List<TransactionMonitoring> tranMonitor = tranMonitorRep.findAllCustomBulkCredit(seqUniqueID, tranDate);
		return moniService;
	}

	@RequestMapping(value = "/IPSBulkDebitTransactionList/{seqUniqueID}", method = RequestMethod.POST)
	@ResponseBody
	public List<TransationMonitorPojo> IPSBulkDebitTransactionList(
			@PathVariable(value = "seqUniqueID", required = true) String seqUniqueID, Model md) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String tranDate = dateFormat.format(new Date());
		List<TransationMonitorPojo> moniService = monitorService.findAllCustomBulkDebit(seqUniqueID, tranDate);
		//List<TransactionMonitoring> tranMonitor = tranMonitorRep.findAllCustomBulkDebit(seqUniqueID, tranDate);
		return moniService;
	}
	
	@RequestMapping(value = "/IPSBulkManualTransactionList/{seqUniqueID}", method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public List<TransationMonitorPojo> IPSBulkManualTransactionList(
			@PathVariable(value = "seqUniqueID", required = true) String seqUniqueID, Model md) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String tranDate = dateFormat.format(new Date());
		List<TransationMonitorPojo> moniService = monitorService.findAllCustomBulkManual(seqUniqueID, tranDate);
		//List<TransactionMonitoring> tranMonitor = tranMonitorRep.findAllCustomBulkManual(seqUniqueID, tranDate);
		return moniService;
	}

	@RequestMapping(value = "/IPSBulkCreditTranExe/{docRefID}", method = RequestMethod.POST)
	@ResponseBody
	public String IPSBulkCreditTranExe(@PathVariable(value = "docRefID", required = true) String docRefID,
			@RequestParam(value = "userID",required = false)String userID,Model md) throws UnknownHostException {
		List<BulkTransaction> bulkTranList = bulkTranRep.findByIdDocRefID(docRefID);
		String response = ipsServices.CreateBulkCreditTran(bulkTranList,userID);
		
		bulkServices.updateIPSRefID(docRefID, response,userID);
		return response;
	}

	@RequestMapping(value = "/IPSBulkDebitTranExe/{docRefID}", method = RequestMethod.POST)
	@ResponseBody
	public String IPSBulkDebitTranExe(@PathVariable(value = "docRefID", required = true) String docRefID,
			@RequestParam(value = "userID",required = false)String userID,Model md) throws UnknownHostException {
		List<BulkTransaction> bulkTranList = bulkTranRep.findByIdDocRefID(docRefID);
		String response = ipsServices.CreateBulkDebitTran(bulkTranList,userID);
		bulkServices.updateIPSRefID(docRefID, response,userID);
		return response;
	}
	
	@RequestMapping(value = "/IPSManualTranExe/{docRefID}", method = RequestMethod.POST)
	@ResponseBody
	public String IPSManualTranExe(@PathVariable(value = "docRefID", required = true) String docRefID, Model md,
			@RequestParam(value = "userID" ,required = false)String userID) throws UnknownHostException {
		List<ManualTransaction> bulkTranList = manualTranRep.findByIdDocRefID(docRefID);
		String response = ipsServices.CreateManualBulkTran(bulkTranList,userID);
		System.out.println("responseresponse"+response);
		bulkServices.updateIPSRefIDManual(docRefID, response,userID);
		return response;
	}

	@RequestMapping(value = "createSettlementAccount", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String createSettlementAccount(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute SettlementAccount settlementAccount, Model md, HttpServletRequest req) throws IOException {

		String msg = settlementAccountServices.CreateSettlement(settlementAccount, formmode);
		return msg;

	}
	@RequestMapping(value = "createBusHours", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String createBusHours(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BusinessHours businessHours, Model md, HttpServletRequest req) throws IOException {

		String msg = settlementAccountServices.Createbusiness(businessHours, formmode);
		return msg;

	}
	
	@RequestMapping(value = "addBankandBranch", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addBankandBranch(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BankAgentTable bankAgentTable, Model md, HttpServletRequest req) throws IOException {

		System.out.println("Formmode---->"+formmode);

		String userID = (String) req.getSession().getAttribute("USERID");
		String msg = bankAndBranchMasterServices.addBank(bankAgentTable, formmode,userID);
		
		
		return msg;

	}

	@RequestMapping(value = "deleteRuleType/{ruleid}", method = { RequestMethod.GET, RequestMethod.POST })
	public String RuleType(@PathVariable("ruleid") String ruleid, Model md) {

		System.out.println("Ajax");
		return AccessRoleService.deleteRole(ruleid);
	}
	/*
	 * @RequestMapping(value = "CANCELINUSER/{ruleid}", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * CANCELINUSER(@PathVariable("ruleid") String ruleid, Model md) {
	 * 
	 * System.out.println("Ajax"); return usermodservice.cancel(ruleid); }
	 */

	@RequestMapping(value = "/getRoleDetails/{roleid}", method = { RequestMethod.GET, RequestMethod.POST })
	public List<IPSAccessRole> gettingpatvisitdetail(@PathVariable(value = "roleid", required = true) String roleid) {

		List<IPSAccessRole> roleiddetails = AccessRoleService.gettingaccessDetails(roleid);

		return roleiddetails;
	}

	@RequestMapping(value = "editBusinessHours", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String editBusinessHours(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String srlno, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BusinessHours businessHours, Model md, HttpServletRequest req) throws IOException {
		System.out.println(businessHours);
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));
		System.out.println(srlno);
		String msg = loginSecurityServices.addHours(businessHours, formmode ,srlno);
		md.addAttribute("BusinessHours", msg);
		return msg;

	}
	
	@RequestMapping(value = "getBlobImage/{Mandate_ref_no}", method = RequestMethod.GET)
	@ResponseBody
	public String BlobImage(@PathVariable("Mandate_ref_no") String Mandate_ref_no, Model md) {
		System.out.println("Mandate_ref_no"+Mandate_ref_no);
		
		MandateMaster mandateMaster = mandateServices.BlobImage(Mandate_ref_no);
		System.out.println(mandateMaster.getMand_doc_image());
		return Base64.getEncoder().encodeToString(mandateMaster.getMand_doc_image());
	}

	@RequestMapping(value = "getQRImage/{QrMsg}", method = RequestMethod.GET)
	@ResponseBody
	public String BlobQRImage(@PathVariable("QrMsg") String QrMsg, Model md) throws WriterException, IOException {
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(QrMsg, BarcodeFormat.QR_CODE, 125, 125, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		ImageIO.write(image, "png", output);
		String imageAsBase64 = Base64.getEncoder().encodeToString(output.toByteArray());

		return imageAsBase64;

	}
	@RequestMapping(value = "addMerchantQRREG", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addMerchantQRREG(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute MerchantQRRegistration bankAgentTable, Model md, HttpServletRequest req) throws IOException {

		System.out.println("Formmode---->"+formmode);

		String userID = (String) req.getSession().getAttribute("USERID");
		String paycode = env.getProperty("ipsx.qr.payeecode");
		String globalUnique = env.getProperty("ipsx.qr.globalUnique");
		String payload = env.getProperty("ipsx.qr.payload");
		String poiMethod_static = env.getProperty("ipsx.qr.poiMethod_static");
		bankAgentTable.setPoi_method(poiMethod_static);
		bankAgentTable.setPayee_participant_code(paycode);
		bankAgentTable.setGlobal_unique_id(globalUnique);
		bankAgentTable.setPayload_format_indicator(payload);
		
		String msg = bankAndBranchMasterServices.addMerchantQRcode(bankAgentTable, formmode,userID);
		
		
		return msg;

	}
	@RequestMapping(value = "getdynamicqrcode", method = RequestMethod.GET)
	public String getdynamicqrcode(	@RequestParam(required = false) String acct_num, @RequestParam(required = false) String tran_amt,
			@RequestParam(required = false) String mob_num, @RequestParam(required = false) String loy_num,@RequestParam(required = false) String merchantid,
			@RequestParam(required = false) String sto_label, @RequestParam(required = false) String cust_label,
			@RequestParam(required = false) String ref_label, @RequestParam(required = false) String ter_label,
			@RequestParam(required = false) String pur_tran, @RequestParam(required = false) String add_det,
			@RequestParam(required = false) String bill_num,Model md) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, DatatypeConfigurationException, JAXBException, IOException {
		System.out.println();
		System.out.println(tran_amt);
		MerchantQRRegistration merchantQRgenerator = merchantQrCodeRegRep.findByIdCustom(acct_num,merchantid);
		merchantQRgenerator.setTransaction_amt(tran_amt);
		merchantQRgenerator.setBill_number(bill_num);
		merchantQRgenerator.setMobile(mob_num);
		merchantQRgenerator.setLoyalty_number(loy_num);
		merchantQRgenerator.setStore_label(sto_label);
		merchantQRgenerator.setCustomer_label(cust_label);
		merchantQRgenerator.setReference_label(ref_label);
		merchantQRgenerator.setTerminal_label(ter_label);
		merchantQRgenerator.setPurpose_of_tran(pur_tran);
		merchantQRgenerator.setAdditional_details(add_det);
		String paycode = env.getProperty("ipsx.qr.payeecode");
		String globalUnique = env.getProperty("ipsx.qr.globalUnique");
		String payload = env.getProperty("ipsx.qr.payload");
		String poiMethod_dynamic = env.getProperty("ipsx.qr.poiMethod_dynamic");
		merchantQRgenerator.setPoi_method(poiMethod_dynamic);
		merchantQRgenerator.setPayee_participant_code(paycode);
		merchantQRgenerator.setGlobal_unique_id(globalUnique);
		merchantQRgenerator.setPayload_format_indicator(payload);
		 cimMerchantQRcodeResponse merchantQRResponse=ipsServices.getMerchantQrCode(merchantQRgenerator);
		String QrImg;
		String imageAsBase64;
		QrImg=merchantQRResponse.getBase64QR();
		 imageAsBase64 = "data:image/png;base64,"+QrImg;
//		if(merchantQRResponse.getStatusCode()==HttpStatus.OK) {
//			QrImg=merchantQRResponse.getBody().getBase64QR();
//			 imageAsBase64 = "data:image/png;base64,"+QrImg;
//			
//		}else {
//			if(merchantQRResponse.getStatusCode()==HttpStatus.BAD_REQUEST) {
//				imageAsBase64=merchantQRResponse.getBody().getError().toString()+":"+
//			merchantQRResponse.getBody().getError_Desc().get(0);
//			}else {
//				imageAsBase64="Something went wrong at server end";
//			}
//		}
		return imageAsBase64;
	}
	@RequestMapping(value = "/getstaticqrcode/{acct_num}/{merchantid}", method = RequestMethod.GET)
	public String getstaticqrcode(	@PathVariable("acct_num") String acct_num,@PathVariable("merchantid") String merchantid, Model md) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, DatatypeConfigurationException, JAXBException, IOException {
		
		MerchantQRRegistration merchantQRgenerator = merchantQrCodeRegRep.findByIdCustom(acct_num,merchantid);
		String paycode = env.getProperty("ipsx.qr.payeecode");
		String globalUnique = env.getProperty("ipsx.qr.globalUnique");
		String payload = env.getProperty("ipsx.qr.payload");
		String poiMethod_static = env.getProperty("ipsx.qr.poiMethod_static");
		merchantQRgenerator.setPoi_method(poiMethod_static);
		merchantQRgenerator.setPayee_participant_code(paycode);
		merchantQRgenerator.setGlobal_unique_id(globalUnique);
		merchantQRgenerator.setPayload_format_indicator(payload);
		
		 cimMerchantQRcodeResponse  merchantQRResponse=ipsServices.getMerchantQrCode(merchantQRgenerator);
		String QrImg;
		String imageAsBase64;
//		if(merchantQRResponse.getStatusCode()==HttpStatus.OK) {
			QrImg=merchantQRResponse.getBase64QR();
			 imageAsBase64 = "data:image/png;base64,"+QrImg;
			
//		}else {
//			if(merchantQRResponse.getStatusCode()==HttpStatus.BAD_REQUEST) {
//				imageAsBase64=merchantQRResponse.getBody().getError().toString()+":"+
//			merchantQRResponse.getBody().getError_Desc().get(0);
//			}else {
//				imageAsBase64="Something went wrong at server end";
//			}
//		}
		return imageAsBase64;
	}
	
	@RequestMapping(value = "addMerchantUPIQRREG", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addMerchantUPIQRREG(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute QRUrlGobalEntity bankAgentTable, Model md, HttpServletRequest req) throws IOException {

		System.out.println("Formmode---->"+formmode);

		String userID = (String) req.getSession().getAttribute("USERID");
		
		
		String msg = bankAndBranchMasterServices.addMerchantUPIQRcode(bankAgentTable, formmode,userID);
		
		
		return msg;

	}

	@RequestMapping(value = "/getstaticupiqrcode/{acct_num}", method = RequestMethod.GET)
	public String getstaticupiqrcode(@PathVariable("acct_num") String acct_num, Model md)
			throws IOException, WriterException {

		QRUrlGobalEntity merchantQRgenerator = upiqrrep.findByIdCustom(acct_num);
		String qrcode = qrCODEServices.createQrcode(merchantQRgenerator, "");
		// String msg =
		// bankAndBranchMasterServices.generateUPIQR(merchantQRgenerator,userID);

		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrcode, BarcodeFormat.QR_CODE, 2800, 2800, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		int matrixHeight = byteMatrix.getHeight();
		BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixHeight);
		graphics.setColor(Color.BLACK);

		//BufferedImage logoimage = ImageIO.read(this.getClass().getResourceAsStream("/static/Image/NPCI_UPI_QR.png"));
		BufferedImage logoimage = ImageIO.read(this.getClass().getResourceAsStream("/static/Image/QR_CODE_SCAN_new.png"));

		logoimage.createGraphics();
		Graphics2D graphi = (Graphics2D) logoimage.getGraphics();
		graphi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixHeight; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		graphi.drawImage(image, 600, 1200, matrixWidth + 500, matrixHeight + 500, null);
		graphi.setFont(new Font("Lucida Calligraphy", Font.BOLD, 150));
		graphi.setColor(Color.RED);
		FontMetrics fm = graphi.getFontMetrics();
		String[] displayTextQR= {merchantQRgenerator.getPn(),""};
		Integer width = 350;
		Integer height = 4700;
		int startingYposition = height + 5;
		for (String displayText : displayTextQR) {
		//	graphi.drawString(displayText, (image.getWidth()) - (fm.stringWidth(displayText)/2),startingYposition);
			graphi.drawString(displayText,2200- (fm.stringWidth(displayText)/2),startingYposition);

			//graphi.drawString(displayText, 500,6500);
			startingYposition += 20;
		}

		//	BufferedImage BOB_LOGO = ImageIO.read(this.getClass().getResourceAsStream("/static/Image/CIM_logo.png"));
		BufferedImage BOB_LOGO = ImageIO.read(this.getClass().getResourceAsStream("/static/Image/BOB_Eng_logo.jpg"));
		graphi.drawImage(BOB_LOGO, 1900, 2500,  500, 500, null);
		
		ImageIO.write(logoimage, "png", output);
		String QrImg = Base64.getEncoder().encodeToString(output.toByteArray());

		String imageAsBase64;
		imageAsBase64 = "data:image/png;base64," + QrImg;

		return imageAsBase64;
	}

	/*
@RequestMapping(value = "/getstaticupiqrcode/{acct_num}", method = RequestMethod.GET)
public String getstaticupiqrcode(	@PathVariable("acct_num") String acct_num, Model md) throws IOException, WriterException {
		
		/*QRUrlGobalEntity merchantQRgenerator = upiqrrep.findByIdCustom(acct_num);
		String qrcode = qrCODEServices.createQrcode(merchantQRgenerator,"");
		//String msg = bankAndBranchMasterServices.generateUPIQR(merchantQRgenerator,userID);
		
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrcode, BarcodeFormat.QR_CODE, 350, 350, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		ImageIO.write(image, "png", output);
		String QrImg = Base64.getEncoder().encodeToString(output.toByteArray());

		

		
		String imageAsBase64;
			 imageAsBase64 = "data:image/png;base64,"+QrImg;
			
		*/	 
	/*String imageAsBase64 = null;
	QRUrlGobalEntity merchantQRgenerator = upiqrrep.findByIdCustom(acct_num);
	String[] displayTextQR= {merchantQRgenerator.getPn(),""};
	Integer width = 350;
	Integer height = 350;
	String qrcode = qrCODEServices.createQrcode(merchantQRgenerator,"");
			 String encodedQRImage="0";
				try {
					QRCodeWriter qrCodeWriter = new QRCodeWriter();
					BitMatrix bitMatrix = qrCodeWriter.encode(qrcode, BarcodeFormat.QR_CODE, width, height);

					ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
					MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
					byte[] pngData = pngOutputStream.toByteArray();
					int totalTextLineToadd = displayTextQR.length;
					InputStream in = new ByteArrayInputStream(pngData);
					BufferedImage image = ImageIO.read(in);

					BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight() + 15 * totalTextLineToadd,
							BufferedImage.TYPE_INT_ARGB);
					// If text is needed to display
					if (displayTextQR.length > 0) {
						Graphics g = outputImage.getGraphics();
						g.setColor(Color.WHITE);
						g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
						g.drawImage(image, 0, 0, null);
						g.setFont(new Font("Arial Black", Font.BOLD, 20));
						Color textColor = Color.BLACK;
						g.setColor(textColor);
						FontMetrics fm = g.getFontMetrics();
						int startingYposition = height + 5;
						for (String displayText : displayTextQR) {
							g.drawString(displayText, (outputImage.getWidth() / 2) - (fm.stringWidth(displayText) / 2),
									startingYposition);
							startingYposition += 20;
						}
						String[] titleText= {"Scan & Pay with BHIM UPI app"};
						ByteArrayOutputStream outputbytestream1 = new ByteArrayOutputStream();
						ImageIO.write(outputImage, "PNG", outputbytestream1);

						int totalTextLineToadd2 = titleText.length;

						BufferedImage outputImage2 = new BufferedImage(outputImage.getWidth()+50,
								outputImage.getHeight() + 50 * totalTextLineToadd2, BufferedImage.TYPE_INT_ARGB);
						Graphics gq = outputImage2.getGraphics();
						gq.setColor(Color.WHITE);
						gq.drawImage(outputImage, 0, 0, null);
						gq.setFont(new Font("HF HySans Bold", Font.BOLD, 25));
						Color textColor2 = Color.BLACK;
						gq.setColor(textColor2);
						FontMetrics fm2 = gq.getFontMetrics();
						int startingYposition2 = height + 50;
						for (String displayText : titleText) {
							gq.drawString(displayText, (outputImage2.getWidth() / 2) - (fm2.stringWidth(displayText) / 2),startingYposition2);
							startingYposition2 += 20;
							
							//gq.drawString(displayText, 160, 500);
						}

						ByteArrayOutputStream outputbytestream2 = new ByteArrayOutputStream();

						//File outputnew = new File("E:\\log3.png");
						ImageIO.write(outputImage2, "PNG", outputbytestream2);

						BufferedImage outputImage3 = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
						Graphics g2 = outputImage3.getGraphics();
						g2.setColor(Color.BLUE);
						g2.drawLine(160, 700, 660, 700);
						g2.drawRoundRect(160, 1, 500, 850,100,100);

						g2.dispose();
						
						
						
						Image picture = ImageIO.read(this.getClass().getResourceAsStream(
								"/static/Image/MauCAS_logo.png"));
						//BufferedImage logo = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
						Graphics gn = outputImage3.getGraphics();
						gn.setColor(Color.BLACK);

						//gn.drawImage(picture, 300, 5, 180, 180, null);
						gn.drawImage(picture, 200, 3, 400, 180, null);
						
						gn.dispose();
						
						Image picture1 = ImageIO.read(this.getClass().getResourceAsStream(
								"/static/Image/BHIM.png"));
						//BufferedImage logo = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
						Graphics gn3 = outputImage3.getGraphics();
						gn3.setColor(Color.BLACK);

						//gn.drawImage(picture, 300, 5, 180, 180, null);
						gn3.drawImage(picture1, 200, 701, 400, 180, null);
						
						gn3.dispose();

						Graphics g3 = outputImage3.getGraphics();

						g3.setColor(Color.black);

						//g3.drawRoundRect(130, 170, 550, 550, 20, 20);


						g3.drawImage(outputImage2, 230, 250, outputImage2.getWidth(), outputImage2.getHeight(), null);
						g3.dispose();

						ByteArrayOutputStream outputByteStreamDataImage = new ByteArrayOutputStream();

						//File output = new File("E:\\logofinal.png");
						ImageIO.write(outputImage3, "PNG", outputByteStreamDataImage);
						encodedQRImage = Base64.getEncoder().encodeToString(outputByteStreamDataImage.toByteArray());
						
						
						 imageAsBase64 = "data:image/png;base64,"+encodedQRImage;
						
						return imageAsBase64;

					}
				} catch (Exception ex) {
					logger.info(ex.getMessage());
					return imageAsBase64;
				}	 
		
		return imageAsBase64;
	}
	*/
	@Autowired
	ErrorResponseCode errorCode;
	
	@PostMapping(path = "/api/ws/generateMerchantQRcodes", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCMerchantResponse> genMerchantQRcode(
			@RequestHeader(value = "X-Request-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody MCMerchantQRcodeRequest mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts generate QR Code");
		
		System.out.println(mcCreditTransferRequest.toString());

		MCMerchantResponse response = null;

		if (ipsDao.checkConvenienceFeeValidationQR(mcCreditTransferRequest)) {
			if (!ipsDao.invaliQRdBankCode(
					mcCreditTransferRequest.getMerchantAcctInformation().getPayeeParticipantCode())) {
				if (Currency.exists(mcCreditTransferRequest.getCurrency())) {
					if (Country.exists(mcCreditTransferRequest.getCountryCode())) {
						response = ipsDao.createMerchantQRConnection1(psuDeviceID, psuIpAddress, psuID,
								mcCreditTransferRequest, p_id, channelID, resvfield1, resvfield2);
					} else {
						String responseStatus = errorCode.validationError("BIPS20");
						throw new IPSXException(responseStatus);
					}

				} else {
					String responseStatus = errorCode.validationError("BIPS19");
					throw new IPSXException(responseStatus);
				}

			} else {
				String responseStatus = errorCode.validationError("BIPS10");
				throw new IPSXException(responseStatus);
			}

		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
