package com.bornfire.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.configuration.SequenceGenerator;
import com.bornfire.entity.AccessandRolesRepository;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.BulkTransaction;
import com.bornfire.entity.BulkTransactionRepository;
import com.bornfire.entity.BusinessHours;
import com.bornfire.entity.BusinessHoursRep;
import com.bornfire.entity.IPSAccessRole;
import com.bornfire.entity.IPSAuditTableRep;
import com.bornfire.entity.IPSChargesAndFees;
import com.bornfire.entity.IPSChargesAndFeesRepository;
import com.bornfire.entity.IPSUserPreofileMod;
import com.bornfire.entity.LoginSecurity;
import com.bornfire.entity.LoginSecurityRepository;
import com.bornfire.entity.MandateMaster;
import com.bornfire.entity.ManualTransaction;
import com.bornfire.entity.ManualTransactionRepository;
import com.bornfire.entity.MerchantCategoryCodeEntity;
import com.bornfire.entity.MerchantCategoryRep;
import com.bornfire.entity.MerchantQrCodeRegRep;
import com.bornfire.entity.ParameterRepository;
import com.bornfire.entity.RTPOutgoingHistRep;
import com.bornfire.entity.RTPOutgoingRep;
import com.bornfire.entity.RegPublicKeyRep;
import com.bornfire.entity.RegPublicKeyTmp;
import com.bornfire.entity.SettlementAccountAmtRep;
import com.bornfire.entity.SettlementAccountAmtTable;
import com.bornfire.entity.SettlementAccountRepository;
import com.bornfire.entity.SettlementReportRep;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.TranCBSTableRep;
import com.bornfire.entity.TranHistMonitorHistRep;
import com.bornfire.entity.TranIPStableRep;
import com.bornfire.entity.TranNetStatRep;
import com.bornfire.entity.TransMoniRepositry;
import com.bornfire.entity.TransactionMonitoring;
import com.bornfire.entity.TransationMonitorPojo;
import com.bornfire.entity.UPIQrRep;
import com.bornfire.entity.UserProfile;
import com.bornfire.entity.UserProfileRep;
import com.bornfire.services.BIPSBankandBranchServices;
import com.bornfire.services.BulkServices;
import com.bornfire.services.IPSAccessRoleService;
import com.bornfire.services.IPSServices;
import com.bornfire.services.LoginSecurityServices;
import com.bornfire.services.LoginServices;
import com.bornfire.services.MandateServices;
import com.bornfire.services.MonitorService;
import com.bornfire.services.NotificationParmsServices;
import com.bornfire.services.SettlementAccountServices;
import com.bornfire.services.TransactionService;
import com.bornfire.services.UserProfileService;
import com.google.gson.Gson;

import net.sf.jasperreports.engine.JRException;

@Controller
@ConfigurationProperties("default")

public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	LoginSecurityRepository loginSecurityRepository;

	@Autowired
	SettlementAccountServices settlementAccountServices;

	@Autowired
	TranNetStatRep tranNetStatRep;

	@Autowired
	BulkTransactionRepository bulkTransactionRepository;

	@Autowired
	ManualTransactionRepository manualTransactionRepository;

	@Autowired
	BusinessHoursRep businessHoursRep;

	@Autowired
	ParameterRepository parameterRepository;

	@Autowired
	LoginSecurityServices loginSecurityServices;

	@Autowired
	BulkServices bulkServices;

	@Autowired
	MonitorService monitorService;

	@Autowired
	LoginServices loginServices;

	@Autowired
	MandateServices mandateServices;

	@Autowired
	private TransMoniRepositry transMoniRepositry;

	@Autowired
	private TranHistMonitorHistRep tranHistMonitorRep;

	@Autowired
	private TranCBSTableRep tranCBSTableRep;

	@Autowired
	private RegPublicKeyRep regPublicKeyRep;

	@Autowired
	private SettlementAccountRepository settlementAccountRepository;

	@Autowired
	BankAgentTableRep bankAgentTableRep;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	IPSChargesAndFeesRepository ipsChargesAndFeesRep;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	IPSAccessRoleService AccessRoleService;

	@Autowired
	SettlementAccountAmtRep settlAcctAmtRep;

	@Autowired
	IPSServices ipsServices;

	@Autowired
	IPSAuditTableRep ipsAuditTableRep;

	@Autowired
	private AccessandRolesRepository accessandrolesrepository;

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private TranIPStableRep tranIPStableRep;
	
	@Autowired
	private SettlementReportRep settlementReportRep;
	
	@Autowired
	RTPOutgoingHistRep rtpOutgoingHistRep;
	
	@Autowired
	RTPOutgoingRep rtpOutgoingRTP;
	
	@Autowired
	MerchantQrCodeRegRep merchantQrCodeRegRep;
	
	@Autowired
	MerchantCategoryRep merchantCategoryRep;
	
	@Autowired
	Environment env;
	
	@Autowired
	NotificationParmsServices notificationParmsServices;
	
	@Autowired
	BIPSBankandBranchServices bankandBranchServices;

	@Autowired
	UPIQrRep upiqrrep;
	
	private String pagesize;

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	/************************ RESET PASSword *********************/

	@RequestMapping("/ResetPassWord")
	public String ResetPassWord() {

		return "ResetPassWord.html";

	}

	/************************ RESET PASSword2 *********************/

	@RequestMapping("/ResetPassWord2")
	public String ResetPassWord1() {

		return "ResetPassWord2.html";

	}

	/******************* APPLICATION START PAGE ********************/
	@RequestMapping("/")
	public String index() {
		return "AStartpage.html";

	}

	
	@RequestMapping("/IPSDashboard")
	public String login() {
		return "IPSDashboard.html";

	}

	@RequestMapping(value = "/IPSDashboard", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboard(Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		int totalTranMC = 0;
		int totalTranINC = 0;
		int totalTranRTP = 0;
		int MCTran = 0;
		int INCTran = 0;
		int RTPTran = 0;

		int MCTranF = 0;
		int INCTranF = 0;
		int RTPTranF = 0;

		int MCTranIP = 0;
		int INCTranIP = 0;
		int RTPTranIP = 0;

		int MCTranR = 0;
		int INCTranR = 0;
		int RTPTranR = 0;
		String userid = (String) req.getSession().getAttribute("USERID");
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String tranDate = dateFormat.format(new Date());
		List<TransactionMonitoring> tranMonitor = transMoniRepositry.findAllCustom(tranDate);
		

		for (TransactionMonitoring var : tranMonitor) {

			if (var.getMsg_type().equals("OUTGOING")) {
				totalTranMC++;
				if (var.getTran_status().equals("SUCCESS")) {
					MCTran++;
				} else if (var.getTran_status().equals("FAILURE")) {
					MCTranF++;
				} else if (var.getTran_status().equals("IN_PROGRESS")) {
					MCTranIP++;
				} else if (var.getTran_status().equals("REVERSE_FAILURE")) {
					MCTranR++;
				}
			} else if (var.getMsg_type().equals("INCOMING")) {
				totalTranINC++;
				if (var.getTran_status().equals("SUCCESS")) {
					INCTran++;
				} else if (var.getTran_status().equals("FAILURE")) {
					INCTranF++;
				} else if (var.getTran_status().equals("IN_PROGRESS")) {
					INCTranIP++;
				} else if (var.getTran_status().equals("REVERSE_FAILURE")) {
					INCTranR++;
				}
			} else if (var.getMsg_type().equals("RTP")) {
				totalTranRTP++;
				if (var.getTran_status().equals("SUCCESS")) {
					RTPTran++;
				} else if (var.getTran_status().equals("FAILURE")) {
					RTPTranF++;
				} else if (var.getTran_status().equals("IN_PROGRESS")) {
					RTPTranIP++;
				} else if (var.getTran_status().equals("REVERSE_FAILURE")) {
					RTPTranR++;
				}
			}

		}
		
		
		//ipsServices.getMCIBresponse();

		md.addAttribute("totalTranMC", totalTranMC);
		md.addAttribute("totalTranINC", totalTranINC);
		md.addAttribute("totalTranRTP", totalTranRTP);

		md.addAttribute("changepassword", userProfileService.checkPasswordChangeReq(userid));
		System.out.println("changepassword" + userProfileService.checkPasswordChangeReq(userid));
		md.addAttribute("checkpassExpiry", userProfileService.checkpassexpirty(userid));
		md.addAttribute("checkAcctExpiry", userProfileService.checkAcctexpirty(userid));
		md.addAttribute("MCTran", MCTran);
		md.addAttribute("INCTran", INCTran);
		md.addAttribute("RTPTran", RTPTran);
		md.addAttribute("MCTranF", MCTranF);
		md.addAttribute("INCTranF", INCTranF);
		md.addAttribute("RTPTranF", RTPTranF);
		md.addAttribute("MCTranIP", MCTranIP);
		md.addAttribute("INCTranIP", INCTranIP);
		md.addAttribute("RTPTranIP", RTPTranIP);
		md.addAttribute("MCTranR", MCTranR);
		md.addAttribute("INCTranR", INCTranR);
		md.addAttribute("RTPTranR", RTPTranR);
		md.addAttribute("menu", "Dashboard");
		return "IPSDashboard";
	}

	/************************ NEW REGISTRATION *********************/
	@RequestMapping("/UserRegistration")
	public String UserRegistration() {
		return "UserRegistration.html";

	}

	@RequestMapping("/TranInquiry")
	public String TranInquiry() {
		return "TransactionInquiry.html";

	}

	@RequestMapping(value = "PARA", method = { RequestMethod.GET, RequestMethod.POST })
	public String wallet2(@RequestParam(required = false) String formmode, @RequestParam(required = false) String srlno,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));
		if (formmode == null || formmode.equals("add")) {

			md.addAttribute("menu", "MMenupage");
			md.addAttribute("menuname", "Login Security");
			md.addAttribute("formmode", formmode);
			md.addAttribute("loginSecurity", new LoginSecurity());
			md.addAttribute("formmode", "add"); // to set which form - valid values are "edit" , "add" & "list"
			/*
			 * md.addAttribute("RuleLists",
			 * ruleenginerepository.findAll(PageRequest.of(currentPage, pageSize)));
			 */
			System.out.println("list");

		} else if (formmode.equals("edit")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("AlertSrlNo", loginServices.getSrlNoValue());
			md.addAttribute("user", loginServices.getUser(""));
		}
		return "Parameter";

	}

	@RequestMapping("/logout")
	public String logout() {
		return "AStartpage.html";

	}

	@RequestMapping("/UserProfile")
	public String user() {
		return "UserProfilehtml.html";

	}

	/*************************************
	 * Admin ---> UserProfile ---> User creation starts
	 ****************************************/
	@RequestMapping(value = "UserProfile", method = { RequestMethod.GET, RequestMethod.POST })
	public String userprofile(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		// String loginuserid = (String) req.getSession().getAttribute("USERID");
		md.addAttribute("RuleIDType", accessandrolesrepository.roleidtype());
		md.addAttribute("menu", "UserProfile"); // To highlight the menu
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list"); // to set which form - valid values are "edit" , "add" & "list"
			// md.addAttribute("userProfiles", userProfileRep.findByAll(userid));
			md.addAttribute("userProfiles", userProfileService.getUsersList());

		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);

		} else if (formmode.equals("edit")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", userProfileService.getUser(userid));
		} else if (formmode.equals("verify")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", userProfileService.getUser2(userid));
			md.addAttribute("userProfilecheck", userProfileService.getUser2(userid));
		} else if (formmode.equals("view")) {
			System.out.println(formmode);
			md.addAttribute("formmode", "view");
			md.addAttribute("userProfile", userProfileService.getUser(userid));
		} else if (formmode.equals("cancel")) {
			System.out.println(formmode);
			md.addAttribute("formmode", "cancel");
			md.addAttribute("userProfile", userProfileService.getUser2(userid));
			md.addAttribute("userProfilecheck", userProfileService.getUser2(userid));
		} else if (formmode.equals("viewnew")) {
			System.out.println(formmode);
			md.addAttribute("formmode", "viewnew");
			md.addAttribute("userProfile", userProfileService.getUser1(userid));
		} else if (formmode.equals("delete")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("userProfile", userProfileService.getUser(userid));

		}
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return "UserProfilehtml";
	}

	@RequestMapping(value = "createUser", method = RequestMethod.POST)
	@ResponseBody
	public String createuser(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			@ModelAttribute IPSUserPreofileMod userProfile1, Model md, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.addUser(userProfile1, formmode, userid);
		md.addAttribute("menu", "UserProfile");
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return msg;

	}

	@RequestMapping(value = "editUser", method = RequestMethod.POST)
	@ResponseBody
	public String editUser(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			@ModelAttribute IPSUserPreofileMod userProfile1, Model md, HttpServletRequest rq) throws ParseException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.editUser(userProfile1, formmode, userid);
		md.addAttribute("menu", "UserProfile"); // To highlight the menu
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return msg;

	}

	@RequestMapping(value = "deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUser(@ModelAttribute UserProfile userprofile, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.deleteUser(userprofile, userid);
		md.addAttribute("menu", "UserProfile");
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return msg;

	}

	@RequestMapping(value = "verifyUser", method = RequestMethod.POST)
	@ResponseBody
	public String verifyUser(@ModelAttribute UserProfile userProfile, @ModelAttribute IPSUserPreofileMod userProfile1,
			Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.verifyUser(userProfile1, userid);
		md.addAttribute("modtable", userProfileService.deleteUser1(userProfile1, userid));
		md.addAttribute("menu", "UserProfile");
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return msg;

	}

	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(@RequestParam("oldpass") String oldpass, @RequestParam("newpass") String newpass,
			@ModelAttribute UserProfile userProfile, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.changePassword(userProfile, oldpass, newpass, userid);
		System.out.println(msg);
		return msg;

	}

	@RequestMapping(value = "passwordReset1", method = RequestMethod.POST)
	@ResponseBody
	public String passwordReset1(@RequestParam("newpass") String newpass, @RequestParam("userid") String changeuserID,
			@ModelAttribute UserProfile userProfile, Model md, HttpServletRequest rq) {
		String loginuser = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.passwordReset1(userProfile, newpass, loginuser, changeuserID);
		System.out.println(msg);
		return msg;

	}

	@RequestMapping(value = "passwordReset", method = RequestMethod.POST)
	@ResponseBody
	public String passwordReset(@ModelAttribute UserProfile userprofile, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = userProfileService.passwordReset(userprofile, userid);
		md.addAttribute("menu", "UserProfile"); // To highlight the menu
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return msg;

	}

	@RequestMapping(value = "cancelUser", method = RequestMethod.POST)
	@ResponseBody
	public String cancel(@ModelAttribute UserProfile userprofile, @RequestParam("userid") String userid,
			IPSUserPreofileMod userProfilemoden, Model md, HttpServletRequest rq) {

		String inputUser = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		md.addAttribute("flagchange", userProfileService.cancelUserentity(userprofile, userid, inputUser));
		String msg = userProfileService.cancel(userProfilemoden, userprofile, userid);
		md.addAttribute("menu", "UserProfile - CANCEL"); // To highlight the menu
		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return msg;

	}

	@RequestMapping(value = "TransMonitorMC")
	public String TransMonitorMC(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		
		
		
		
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
			
			md.addAttribute("totTr", transMoniRepositry.outwardMCListTotTxs(tranDate));
			md.addAttribute("sucTr", transMoniRepositry.outwardMCLisSucTxs(tranDate));
			md.addAttribute("failTr", transMoniRepositry.outwardMCListFailTxs(tranDate));
			md.addAttribute("totAmt", transMoniRepositry.outwardMCListTotAmt(tranDate));
			md.addAttribute("sucAmt", transMoniRepositry.outwardMCLisSucAmt(tranDate));
			md.addAttribute("failAmt", transMoniRepositry.outwardMCListFailAmt(tranDate));
		} else {
			tranDate = date;
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
				md.addAttribute("totTr", transMoniRepositry.outwardMCListTotTxs(date));
				md.addAttribute("sucTr", transMoniRepositry.outwardMCLisSucTxs(date));
				md.addAttribute("failTr", transMoniRepositry.outwardMCListFailTxs(date));
				md.addAttribute("totAmt", transMoniRepositry.outwardMCListTotAmt(date));
				md.addAttribute("sucAmt", transMoniRepositry.outwardMCLisSucAmt(date));
				md.addAttribute("failAmt", transMoniRepositry.outwardMCListFailAmt(date));

			} else {
				md.addAttribute("totTr", tranHistMonitorRep.outwardMCListTotTxs(date));
				md.addAttribute("sucTr", tranHistMonitorRep.outwardMCLisSucTxs(date));
				md.addAttribute("failTr", tranHistMonitorRep.outwardMCListFailTxs(date));
				md.addAttribute("totAmt", tranHistMonitorRep.outwardMCListTotAmt(date));
				md.addAttribute("sucAmt", tranHistMonitorRep.outwardMCLisSucAmt(date));
				md.addAttribute("failAmt", tranHistMonitorRep.outwardMCListFailAmt(date));
			}
			
		}
		
		

		md.addAttribute("tranDetail", monitorService.getTranMonitorMCList(date));
		md.addAttribute("menu", "MMenupage");
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "MC");

		return "TransactionMonitoring";
	}

	@RequestMapping(value = "TransMonitorRTP")
	public String TransMonitorRTP(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
			
			md.addAttribute("totTr", transMoniRepositry.outwardRTPListTotTxs(tranDate));
			md.addAttribute("sucTr", transMoniRepositry.outwardRTPLisSucTxs(tranDate));
			md.addAttribute("failTr", transMoniRepositry.outwardRTPListFailTxs(tranDate));
			md.addAttribute("totAmt", transMoniRepositry.outwardRTPListTotAmt(tranDate));
			md.addAttribute("sucAmt", transMoniRepositry.outwardRTPLisSucAmt(tranDate));
			md.addAttribute("failAmt", transMoniRepositry.outwardRTPListFailAmt(tranDate));
		} else {
			tranDate = date;
			
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
				md.addAttribute("totTr", transMoniRepositry.outwardRTPListTotTxs(date));
				md.addAttribute("sucTr", transMoniRepositry.outwardRTPLisSucTxs(date));
				md.addAttribute("failTr", transMoniRepositry.outwardRTPListFailTxs(date));
				md.addAttribute("totAmt", transMoniRepositry.outwardRTPListTotAmt(date));
				md.addAttribute("sucAmt", transMoniRepositry.outwardRTPLisSucAmt(date));
				md.addAttribute("failAmt", transMoniRepositry.outwardRTPListFailAmt(date));

			} else {
				md.addAttribute("totTr", tranHistMonitorRep.outwardRTPListTotTxs(date));
				md.addAttribute("sucTr", tranHistMonitorRep.outwardRTPLisSucTxs(date));
				md.addAttribute("failTr", tranHistMonitorRep.outwardRTPListFailTxs(date));
				md.addAttribute("totAmt", tranHistMonitorRep.outwardRTPListTotAmt(date));
				md.addAttribute("sucAmt", tranHistMonitorRep.outwardRTPLisSucAmt(date));
				md.addAttribute("failAmt", tranHistMonitorRep.outwardRTPListFailAmt(date));
			}
		}

		md.addAttribute("tranDetail", monitorService.getTranMonitorRTPList(date));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "RTP");

		return "TransactionMonitoring";
	}

	@RequestMapping(value = "TransMonitorRTP/{acctNumber}")
	public String TransMonitorRTPFromMYt(@PathVariable(value = "acctNumber", required = true) String acctNumber,
			Model md, HttpServletRequest req) {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		List<TransationMonitorPojo> transationMonitorPojoList = new ArrayList<TransationMonitorPojo>();
		List<TransactionMonitoring> tranMonitor = transMoniRepositry.findAllCustomRTPFromMyt(acctNumber);
		for (int i = 0; i < tranMonitor.size(); i++) {
			TransationMonitorPojo transationMonitorPojo = new TransationMonitorPojo();
			transationMonitorPojo.setBob_account(tranMonitor.get(i).getBob_account());
			transationMonitorPojo.setTran_date(tranMonitor.get(i).getTran_date());
			transationMonitorPojo.setSequence_unique_id(tranMonitor.get(i).getSequence_unique_id());
			transationMonitorPojo.setTran_audit_number(tranMonitor.get(i).getTran_audit_number());
			transationMonitorPojo.setMsg_type(tranMonitor.get(i).getMsg_type());
			transationMonitorPojo.setTran_amount(tranMonitor.get(i).getTran_amount());
			logger.info(tranMonitor.get(i).getSequence_unique_id());
			logger.info(tranMonitor.get(i).getIpsx_status_error());
			logger.info(tranMonitor.get(i).getResponse_status());

			transationMonitorPojo.setTran_currency(tranMonitor.get(i).getTran_currency());
			if (tranMonitor.get(i).getTran_status().equals("FAILURE")) {

				if (tranMonitor.get(i).getCbs_status().equals("CBS_DEBIT_ERROR")
						|| tranMonitor.get(i).getCbs_status().equals("CBS_CREDIT_ERROR")) {
					transationMonitorPojo.setTranStatusSeq(tranMonitor.get(i).getTran_status());
					transationMonitorPojo.setReason("CBS  :" + tranMonitor.get(i).getCbs_status_error());
				} else if (tranMonitor.get(i).getCbs_status().equals("VALIDATION_ERROR")) {
					transationMonitorPojo.setTranStatusSeq("VALIDATION_ERROR");
					transationMonitorPojo.setReason("BIPS :" + tranMonitor.get(i).getCbs_status_error());
				} else {
					transationMonitorPojo.setTranStatusSeq(tranMonitor.get(i).getTran_status());
					if (tranMonitor.get(i).getResponse_status() != (null)) {
						if (tranMonitor.get(i).getResponse_status().equals("RJCT")) {
							transationMonitorPojo.setReason("IPSX  :" + tranMonitor.get(i).getIpsx_status_error());
						}
					} else {
						transationMonitorPojo.setReason("");

					}
				}

			} else if (tranMonitor.get(i).getTran_status().equals("REVERSE_FAILURE")) {
				transationMonitorPojo.setReason("IPSX  :" + tranMonitor.get(i).getIpsx_status_error());
				transationMonitorPojo.setTranStatusSeq(tranMonitor.get(i).getTran_status());
			} else {
				transationMonitorPojo.setReason("");
				transationMonitorPojo.setTranStatusSeq(tranMonitor.get(i).getTran_status());

			}

			transationMonitorPojo.setTran_status(tranMonitor.get(i).getTran_status());

			transationMonitorPojoList.add(transationMonitorPojo);
		}
		logger.info("kalaivanan" + transationMonitorPojoList.size());
		System.out.println("kalaivanan" + transationMonitorPojoList.size());
		md.addAttribute("tranDetail", transationMonitorPojoList);
		md.addAttribute("menu", "MMenupage"); // To highlight the menu

		System.out.println("kalaivanan" + transationMonitorPojoList.size());

		return "TransactionMonitoring";
	}

	@RequestMapping(value = "TransMonitorINC")
	public String TransMonitorINC(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
			
			md.addAttribute("totTr", transMoniRepositry.outwardINCListTotTxs(tranDate));
			md.addAttribute("sucTr", transMoniRepositry.outwardINCLisSucTxs(tranDate));
			md.addAttribute("failTr", transMoniRepositry.outwardINCListFailTxs(tranDate));
			md.addAttribute("totAmt", transMoniRepositry.outwardINCListTotAmt(tranDate));
			md.addAttribute("sucAmt", transMoniRepositry.outwardINCLisSucAmt(tranDate));
			md.addAttribute("failAmt", transMoniRepositry.outwardINCListFailAmt(tranDate));
		} else {
			tranDate = date;
			
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
				md.addAttribute("totTr", transMoniRepositry.outwardINCListTotTxs(date));
				md.addAttribute("sucTr", transMoniRepositry.outwardINCLisSucTxs(date));
				md.addAttribute("failTr", transMoniRepositry.outwardINCListFailTxs(date));
				md.addAttribute("totAmt", transMoniRepositry.outwardINCListTotAmt(date));
				md.addAttribute("sucAmt", transMoniRepositry.outwardINCLisSucAmt(date));
				md.addAttribute("failAmt", transMoniRepositry.outwardINCListFailAmt(date));

			} else {
				md.addAttribute("totTr", tranHistMonitorRep.outwardINCListTotTxs(date));
				md.addAttribute("sucTr", tranHistMonitorRep.outwardINCLisSucTxs(date));
				md.addAttribute("failTr", tranHistMonitorRep.outwardINCListFailTxs(date));
				md.addAttribute("totAmt", tranHistMonitorRep.outwardINCListTotAmt(date));
				md.addAttribute("sucAmt", tranHistMonitorRep.outwardINCLisSucAmt(date));
				md.addAttribute("failAmt", tranHistMonitorRep.outwardINCListFailAmt(date));
			}

		}

		md.addAttribute("tranDetail", monitorService.getTranMonitorINCList(date));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "INC");

		return "TransactionMonitoring";
	}

	@RequestMapping(value = "TransMonitorBulk")
	public String TransMonitorBulk(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
		} else {
			tranDate = date;

		}

		md.addAttribute("tranDetail", monitorService.getTranMonitorBulkList(date));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "BULK");

		return "TransactionMonitoring";
	}

	@RequestMapping(value = "ReversalTransaction")
	public String ReversalTransaction(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
		} else {
			tranDate = date;
		}

		md.addAttribute("tranDetail", monitorService.getReversalTranList(date));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "REVERSAL TRAN");

		return "ReversalTransaction";
	}

	@RequestMapping(value = "TranMonitorCBS/{type}/{seqUniqueID}")
	public String TransacMonitorCBS(@PathVariable(value = "type", required = true) String type,
			@PathVariable(value = "seqUniqueID", required = true) String seqUniqueID, Model md,
			HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		List<TranCBSTable> tranMonitorCBS = tranCBSTableRep.findAllCustom(seqUniqueID);
		md.addAttribute("tranMonitorCBS", tranMonitorCBS);
		md.addAttribute("menu", "MMenupage");
		md.addAttribute("type", type);
		md.addAttribute("typeMode", "S");
		return "TranMonitorCBS";
	}

	@RequestMapping(value = "CBSMonitor")
	public String CBSMonitor(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
		} else {
			tranDate = date;
		}

		md.addAttribute("totPayAmt",tranCBSTableRep.findByCustomPayAmt(tranDate));
		md.addAttribute("totRecAmt",tranCBSTableRep.findByCustomRecAmt(tranDate));
		md.addAttribute("totSetPayAmt",tranCBSTableRep.findByCustomSettlPayAmt(tranDate));
		md.addAttribute("totSetRecAmt",tranCBSTableRep.findByCustomSettlRecAmt(tranDate));
		
		md.addAttribute("tranDetail", monitorService.getTranMonitorCBSList(date));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "CBS");

		return "CBSMonitor";
	}

	@RequestMapping(value = "TranMonitorRev/{type}")
	public String TranMonitorReverse(@PathVariable(value = "type", required = true) String type,
			@RequestParam(value = "id", required = true) String seqUniqueID, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		List<TranCBSTable> tranMonitorCBS = monitorService.findAllCustomReverse(seqUniqueID, type);

		md.addAttribute("tranMonitorCBS", tranMonitorCBS);
		md.addAttribute("menu", "MMenupage");
		md.addAttribute("type", type);
		md.addAttribute("typeMode", "A");
		md.addAttribute("id", seqUniqueID);
		return "TranMonitorCBS";
	}

	@RequestMapping(value = "ReverseTran/{tranAuditNumber}")
	public String ReverseTran(@PathVariable(value = "tranAuditNumber", required = true) String tranAuditNumber,
			@RequestParam(value = "id", required = false) String seqUniqueID, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String loginuserid = (String) req.getSession().getAttribute("USERID");
		System.out.println(loginuserid);
		TranCBSTable tranMonitorCBS = tranCBSTableRep.findAllTranAudit(tranAuditNumber);
		md.addAttribute("tranMonitorCBS", tranMonitorCBS);
		md.addAttribute("loginUser", loginuserid);
		md.addAttribute("id", seqUniqueID);
		// md.addAttribute("menu", "MMenupage");
		return "ReverseTran";
	}

	@RequestMapping(value = "MyTUserReg")
	public String MyTUserReg(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "viewType", required = false) String viewType) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		
		if(viewType==null) {
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<RegPublicKeyTmp> regPublickey = monitorService.getMytUserList(tranDate);
			md.addAttribute("regPublickey", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("D")) {
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<RegPublicKeyTmp> regPublickey = monitorService.getMytUserList(tranDate);
			md.addAttribute("regPublickey", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("M")){
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				DateFormat dateFormat1 = new SimpleDateFormat("MMMM-yyyy");
				Date tranDate1 = dateFormat1.parse(date);
			
				System.out.println("tranDate1"+tranDate1);
				
				tranDate = dateFormat.format(tranDate1);
			}
			
			
			md.addAttribute("viewType", viewType);
			List<RegPublicKeyTmp> regPublickey = monitorService.getMytUserListMonthly(tranDate);
			md.addAttribute("regPublickey", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranMonth", tranDate);
		}

		
		// md.addAttribute("menu", "MMenupage");
		return "MytUserRegistartion";
	}

	@RequestMapping(value = "SettlementReport")
	public String SettlementReport(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
			
			
			String tranDate1=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			md.addAttribute("totTr", settlementReportRep.getSettleCurrentReport1TotTr(tranDate1));
			md.addAttribute("totAmt", settlementReportRep.getSettleCurrentReport1TotAmt(tranDate1));
		} else {
			tranDate = date;
			
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			String tranDate1=new SimpleDateFormat("yyyy-MM-dd").format(enteredDate);
			
			md.addAttribute("totTr", settlementReportRep.getSettleCurrentReport1TotTr(tranDate1));
			md.addAttribute("totAmt", settlementReportRep.getSettleCurrentReport1TotAmt(tranDate1));
		}
		
		

		md.addAttribute("tranDetail", monitorService.getSettlementReport(tranDate));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "SR");

		return "SettlementReport";
	}

	@RequestMapping(value = "LoginSecurity")
	public String loginSecurity(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute LoginSecurity loginSecurity, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Login Security");
			md.addAttribute("menu", "MMenupage");

			LoginSecurity loginsec = loginSecurityServices.getLoginSec();
			if (loginsec.getCom_flg().equals("Y")) {
				md.addAttribute("compflg", true);
			} else {
				md.addAttribute("compflg", false);
			}
			md.addAttribute("loginsec", loginsec);

		} else if (formmode.equals("submit")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("formmode", formmode);
			md.addAttribute("menu", "LoginSecurity");
			// md.addAttribute("menuname", "Customer KYC Maintenance");
			String msg = loginSecurityServices.addUser(loginSecurity, formmode);
			md.addAttribute("loginSecurity", msg);
			return msg;

		}

		return "LoginSecurity";
	}

	@RequestMapping(value = "IPSLoginSecurity")
	@ResponseBody
	public String LoginSec(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute LoginSecurity loginSecurity, Model md, HttpServletRequest req) throws IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = loginSecurityServices.addUser(loginSecurity, formmode);
		md.addAttribute("loginSecurity", msg);
		return msg;

	}

	@RequestMapping(value = "TransactionEntries", method = { RequestMethod.GET, RequestMethod.POST })
	public String TransactionEntries(Model md, HttpServletRequest req) {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		md.addAttribute("Trandetail", tranNetStatRep.findAllCustom());
		md.addAttribute("menu", "MMenupage");
		return "TransactionEntries";
	}

	// ****************************
	// Service Charges And Fees
	// *************************************//
	@RequestMapping(value = "ServiceChargesAndFees")
	public String ServiceChargesAndFees(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String ref_num, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute IPSChargesAndFees ipsChargesAndFees, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("menuname", "Service Charges And Fees");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("serviceFees", ipsChargesAndFeesRep.findAll());

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Service Charges And Fees - Modify");
			md.addAttribute("serviceFees", ipsChargesAndFeesRep.findByIdReferenceNum(ref_num));

		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Service Charges And Fees - Verify");
			md.addAttribute("serviceFees", ipsChargesAndFeesRep.findByIdReferenceNum(ref_num));

		}

		return "ServiceChargesAndFees";
	}

	@RequestMapping(value = "editChargesandFees")
	@ResponseBody
	public String IPSServiceFeesAdd(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute IPSChargesAndFees ipsChargesAndFees, Model md, HttpServletRequest req) throws IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = loginSecurityServices.addFees(ipsChargesAndFees, formmode);
		md.addAttribute("ServiceFees", msg);
		return msg;

	}

	// ****************************
	// Business Hours
// *************************************//
	@RequestMapping(value = "BusinessHours", method = { RequestMethod.GET, RequestMethod.POST })
	public String BusinessHours(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String srl_no, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BusinessHours businessHours, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("menuname", "Business Hours");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("busHours", businessHoursRep.findAll());

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname1", "Business Hours - Modify");
			md.addAttribute("busHours", businessHoursRep.findByIdSrlNo(srl_no));

		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Business Hours - Verify");
			md.addAttribute("busHours", businessHoursRep.findByIdSrlNo(srl_no));

		}

		return "BusinessHours";
	}

	/*********************************************
	 * BANK AND BRANCH MASTER STARTS
	 **********************************************/
	@RequestMapping(value = "IpsBankandBranchMaster")
	public String IpsBankandBranchMaster(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String bankcode, @RequestParam(required = false) String userid,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BankAgentTable bankAgentTable, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("bankDetail", bankAgentTableRep.findAllCustom());
		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", bankAgentTableRep.findByIdCustom(bankcode));

		} else if (formmode.equals("delete")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", bankAgentTableRep.findByIdCustom(bankcode));

		} else if (formmode.equals("disable")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", bankAgentTableRep.findByIdCustom(bankcode));

		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", bankAgentTableRep.findByIdCustom(bankcode));

		}

		return "IpsBankandBranchMaster";
	}

	/*********************************************
	 * SETTLEMENT ACCOUNT STARTS
	 **********************************************/
	@RequestMapping(value = "SettlementAccount")
	public String SettlementAccount(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String acctcode, @RequestParam(required = false) String userid,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BulkTransaction bulk, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");

			md.addAttribute("SettleAccount", settlementAccountRepository.findAllCustom());
			// md.addAttribute("SettlementAccount", settlementAccountServices);

		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);

		} else if (formmode.equals("addsubmit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menu", "MMenupage");
			String msg = "ok";
			md.addAttribute("AddSettle", msg);
			return msg;

		} else if (formmode.equals("edit")) {
			md.addAttribute("menuname1", "Settlement Account- Modify");
			md.addAttribute("formmode", formmode);
			md.addAttribute("SettlementAccount", settlementAccountRepository.findByAccountCode(acctcode));

		} else if (formmode.equals("verify")) {
			md.addAttribute("menuname1", "Settlement Account- Verify");
			md.addAttribute("formmode", formmode);
			md.addAttribute("SettlementAccount", settlementAccountRepository.findByAccountCode(acctcode));

		} else if (formmode.equals("view")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("RefCodeMaster", new BulkTransaction());
			md.addAttribute("menuname1", "Bulk Transaction - Inquiry");

		}

		md.addAttribute("auditflag", "auditflag");
		return "SettlementAccount";
	}

	/*********************************************
	 * BULK TRANSACTION STARTS
	 * 
	 * @throws ParseException
	 **********************************************/
	@RequestMapping(value = "BulkCredit", method = { RequestMethod.GET, RequestMethod.POST })
	public String IPSBulkTransaction(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String screenId, @RequestParam(required = false) MultipartFile file,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "docRefId", required = false) String docRefID,
			@RequestParam(value = "tranDate", required = false) String date, @ModelAttribute BulkTransaction bulk,
			Model md, HttpServletRequest req) throws FileNotFoundException, SQLException, IOException, ParseException {

		String userID = (String) req.getSession().getAttribute("USERID");
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			String tranDate = "";
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
			} else {
				tranDate = date;

			}

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("menuname", "BulkTransaction");
			md.addAttribute("tranDate", tranDate);
			md.addAttribute("userID", userID);
			md.addAttribute("bulkCreditList", bulkServices.getBulkCreditList(tranDate));

		} else if (formmode.equals("upload")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "BulkTransaction-Upload");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("userID", userID);

		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("DOCID", sequence.generateDocRefID());
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
			// String Doc_Ref_ID = sequence.generateDocRefID();
			md.addAttribute("menuname", "BulkTransaction-Add");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("userID", userID);

		} else if (formmode.equals("submit")) {

			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", formmode);
			md.addAttribute("menu", "IPSBulkTransaction");
			md.addAttribute("menuname", "Customer KYC Maintenance");
			// String msg = bulkServices.getDetails(bulkTransaction.getBulkTranList(),
			// formmode);
			String msg = "ok";
			md.addAttribute("BulkTransaction", msg);
			return msg;

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("RECORDTYPELIST", bulkTransactionRepository.findAllCustom());
			/* md.addAttribute("userProfile", userProfileDao.getUser(userid)); */
			md.addAttribute("menuname1", "Bulk Transaction - Modify");
			md.addAttribute("RefCodeMaster", new BulkTransaction());

		} else if (formmode.equals("verify")) {
			System.out.println("verifyrule");
			md.addAttribute("formmode", formmode);
			md.addAttribute("RefCodeMaster", new BulkTransaction());
			md.addAttribute("menuname1", "Bulk Transaction- Verify");

		}

		else if (formmode.equals("view")) {
			System.out.println("viewrule1");
			md.addAttribute("formmode", formmode);
			md.addAttribute("RefCodeMaster", new BulkTransaction());
			md.addAttribute("menuname1", "Bulk Transaction - Inquiry");

		} else if (formmode.equals("delete")) {
			System.out.println("deleteParameter");
			md.addAttribute("formmode", "list");
			md.addAttribute("menuname1", "Bulk Transaction- Delete");

		} else if (formmode.equals("a")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bulk Transaction- Verify");
			md.addAttribute("docRefID", docRefID);
			md.addAttribute("userID", userID);
			List<BulkTransaction> list=bulkServices.getBulkCreditDocRefList(docRefID);
			if(list.size()>0) {
				md.addAttribute("entryUser", list.get(0).getEntry_user());
				md.addAttribute("entityFlg", list.get(0).getEntity_flg());
			}else {
				md.addAttribute("entryUser", "");
				md.addAttribute("entityFlg", "");
			}
			md.addAttribute("authList",list);
		}
		return "IPSBulkTransaction";
	}

	@RequestMapping(value = "BulkDebit", method = { RequestMethod.GET, RequestMethod.POST })
	public String BulkDebit(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String screenId, @RequestParam(required = false) MultipartFile file,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "docRefId", required = false) String docRefID,
			@RequestParam(value = "tranDate", required = false) String date, @ModelAttribute BulkTransaction bulk,
			Model md, HttpServletRequest req) throws FileNotFoundException, SQLException, IOException, ParseException {

		String userID = (String) req.getSession().getAttribute("USERID");
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			String tranDate = "";
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
			} else {
				tranDate = date;

			}

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("menuname", "Bulk Debit Transaction");
			md.addAttribute("tranDate", tranDate);
			md.addAttribute("userID", userID);
			md.addAttribute("bulkCreditList", bulkServices.getBulkDebitList(tranDate));

		} else if (formmode.equals("upload")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bulk Debit Transaction-Upload");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("userID", userID);

		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("DOCID", sequence.generateDocRefID());
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
			md.addAttribute("menuname", "Bulk Debit Transaction-Add");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("userID", userID);

		} else if (formmode.equals("submit")) {

			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", formmode);
			md.addAttribute("menu", "IPSBulkDebitTransaction");
			md.addAttribute("menuname", "Customer KYC Maintenance");
			// String msg = bulkServices.getDetails(bulkTransaction.getBulkTranList(),
			// formmode);
			String msg = "ok";
			md.addAttribute("BulkTransaction", msg);
			return msg;

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("RECORDTYPELIST", bulkTransactionRepository.findAllCustom());
			/* md.addAttribute("userProfile", userProfileDao.getUser(userid)); */
			md.addAttribute("menuname1", "Bulk Debit Transaction - Modify");
			md.addAttribute("RefCodeMaster", new BulkTransaction());

		} else if (formmode.equals("verify")) {
			System.out.println("verifyrule");
			md.addAttribute("formmode", formmode);
			md.addAttribute("RefCodeMaster", new BulkTransaction());
			md.addAttribute("menuname1", "Bulk Debit Transaction- Verify");

		}

		else if (formmode.equals("view")) {
			System.out.println("viewrule1");
			md.addAttribute("formmode", formmode);
			md.addAttribute("RefCodeMaster", new BulkTransaction());
			md.addAttribute("menuname1", "Bulk Debit Transaction- Inquiry");

		} else if (formmode.equals("delete")) {
			System.out.println("deleteParameter");
			md.addAttribute("formmode", "list");
			md.addAttribute("menuname1", "Bulk Debit Transaction- Delete");

			/* md.addAttribute("RefCodeMaster", refCodeService.getRefcode(refcode)); */

		}else if (formmode.equals("a")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Bulk Transaction- Verify");
			md.addAttribute("docRefID", docRefID);
			md.addAttribute("userID", userID);
			List<BulkTransaction> list=bulkServices.getBulkDebitDocRefList(docRefID);
			if(list.size()>0) {
				md.addAttribute("entryUser",  list.get(0).getEntry_user());
				md.addAttribute("entityFlg", list.get(0).getEntity_flg());
			}else {
				md.addAttribute("entryUser", "");
				md.addAttribute("entityFlg", "");
			}
			md.addAttribute("authList",list);
		}
		return "IPSBulkDebitTransaction";
	}

	@RequestMapping(value = "IPSBulkExe/{seqUniqueID}")
	public String IPSBulkExe(Model md, HttpServletRequest req,
			@PathVariable(value = "seqUniqueID", required = false) String seqUniqueID,
			@RequestParam(value = "form", required = false) String form,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (form == null) {

			System.out.println("okk");
			md.addAttribute("seqUniqueID", seqUniqueID);
			md.addAttribute("type", "BulkCredit");
			md.addAttribute("typeMode", "auto");
		} else {

			md.addAttribute("seqUniqueID", seqUniqueID);
			md.addAttribute("type", "BulkCredit");
			md.addAttribute("typeMode", "list");

			String tranDate = "";
			logger.info("creditTransactionnnn" + form);
			boolean currentDate = false;
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				md.addAttribute("tranList", monitorService.findAllCustomBulkCredit(seqUniqueID, tranDate));

			} else {
				tranDate = date;

				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

				Date date1 = new Date();
				String currentDate1 = dateFormat.format(date1);

				Date enteredDate = dateFormat.parse(date);
				logger.info("creditTransactionnnndfsfgdfsgsdfdfg" + form + " " + tranDate);
				if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
					md.addAttribute("tranList", monitorService.findAllCustomBulkCredit(seqUniqueID, tranDate));
				} else {
					md.addAttribute("tranList", monitorService.findAllCustomBulkCredit(seqUniqueID, tranDate));
				}
			}
		}

		return "IPSBulkTransactionExe";
	}

	@RequestMapping(value = "IPSBulkDebitExe/{seqUniqueID}")
	public String IPSBulkDebitExe(Model md, HttpServletRequest req,
			@PathVariable(value = "seqUniqueID", required = false) String seqUniqueID,
			@RequestParam(value = "form", required = false) String form,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (form == null) {

			md.addAttribute("seqUniqueID", seqUniqueID);
			md.addAttribute("type", "BulkDebit");
			md.addAttribute("typeMode", "auto");
		} else {
			md.addAttribute("seqUniqueID", seqUniqueID);
			md.addAttribute("type", "BulkDebit");
			md.addAttribute("typeMode", "list");

			String tranDate = "";
			boolean currentDate = false;
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				currentDate = true;
				md.addAttribute("tranList", monitorService.findAllCustomBulkDebit(seqUniqueID, tranDate));
			} else {
				tranDate = date;
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				Date enteredDate = dateFormat.parse(date);
				if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
					md.addAttribute("tranList", monitorService.findAllCustomBulkDebit(seqUniqueID, tranDate));
				} else {
					md.addAttribute("tranList", monitorService.findAllCustomBulkDebit(seqUniqueID, tranDate));

				}
			}

		}

		return "IPSBulkTransactionExe";
	}

	@RequestMapping(value = "IPSBulkManualExe/{seqUniqueID}")
	public String IPSBulkManualExe(Model md, HttpServletRequest req,
			@PathVariable(value = "seqUniqueID", required = false) String seqUniqueID,
			@RequestParam(value = "form", required = false) String form,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (form == null) {

			md.addAttribute("seqUniqueID", seqUniqueID);
			md.addAttribute("type", "BulkManual");
			md.addAttribute("typeMode", "auto");
		} else {

			md.addAttribute("seqUniqueID", seqUniqueID);
			md.addAttribute("type", "BulkManual");
			md.addAttribute("typeMode", "list");
			String tranDate = "";
			boolean currentDate = false;
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				md.addAttribute("tranList", monitorService.findAllCustomBulkManual(seqUniqueID, tranDate));

			} else {
				tranDate = date;

				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

				Date date1 = new Date();
				String currentDate1 = dateFormat.format(date1);

				Date enteredDate = dateFormat.parse(date);
				if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
					md.addAttribute("tranList", monitorService.findAllCustomBulkManual(seqUniqueID, tranDate));
				} else {
					md.addAttribute("tranList", monitorService.findAllCustomBulkManual(seqUniqueID, tranDate));
				}
			}
		}

		return "IPSBulkTransactionExe";
	}

	/************************** Debit Ends **********************/

	/*********************************************
	 * Manual TRANSACTION STARTS
	 * 
	 * @throws ParseException
	 **********************************************/
	@RequestMapping(value = "IPSManualTransaction")
	public String IPSManualTransaction(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String screenId, @RequestParam(required = false) MultipartFile file,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "docRefId", required = false) String docRefID,
			@ModelAttribute ManualTransaction manualTransaction, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException, ParseException {

		String userID = (String) req.getSession().getAttribute("USERID");
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			String tranDate = "";
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
			} else {
				tranDate = date;

			}

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("menuname", "Manual Transaction");
			md.addAttribute("userID", userID);
			md.addAttribute("tranDate", tranDate);
			md.addAttribute("manualTran", bulkServices.getManualTranList(tranDate));

		} else if (formmode.equals("add")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("DOCID", sequence.generateDocRefID());
			md.addAttribute("userID", userID);
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
			md.addAttribute("menuname", "ManualTransaction-Add");
			md.addAttribute("menu", "MMenupage");

		} else if (formmode.equals("submit")) {

			System.out.println(manualTransaction.getTran_date());

			md.addAttribute("formmode", "add");
			md.addAttribute("formmode", formmode);
			md.addAttribute("menu", "IPSManualTransaction");
			md.addAttribute("menuname", "");
			// String msg="hghg";
			String msg = bulkServices.getManual(manualTransaction, formmode);
			md.addAttribute("ManualTransaction", msg);
			return msg;

		}else if (formmode.equals("a")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("menuname", "Manual Transaction- Verify");
			md.addAttribute("docRefID", docRefID);
			md.addAttribute("userID", userID);
			List<ManualTransaction> list=bulkServices.getBulkManualDocRefList(docRefID);
			if(list.size()>0) {
				md.addAttribute("entryUser",  list.get(0).getEntry_user());
				md.addAttribute("entityFlg", list.get(0).getEntity_flg());
			}else {
				md.addAttribute("entryUser", "");
				md.addAttribute("entityFlg", "");
			}
			md.addAttribute("authList",list);
		}
		return "IPSManualTransaction";
	}

	@RequestMapping(value = "IPSAccessandRoles", method = { RequestMethod.GET, RequestMethod.POST })
	public String IPSAccessandRoles(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("menu", "IPSAccessandRoles");
			md.addAttribute("menuname", "Access and Roles");
			md.addAttribute("formmode", "list");
			md.addAttribute("AccessandRoles", accessandrolesrepository.rulelist());
		} else if (formmode.equals("add")) {
			md.addAttribute("menuname", "Access and Roles - ADD");
			md.addAttribute("formmode", "add");
		} else if (formmode.equals("edit")) {
			md.addAttribute("menuname", "Access and Roles - EDIT");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));
		} else if (formmode.equals("view")) {
			md.addAttribute("menuname", "Access and Roles - INQUIRY");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));

		} else if (formmode.equals("verify")) {
			md.addAttribute("menuname", "Access and Roles - VERIFY");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));

		} else if (formmode.equals("delete")) {
			md.addAttribute("menuname", "Access and Roles - DELETE");
			md.addAttribute("formmode", formmode);
			md.addAttribute("IPSAccessRole", AccessRoleService.getRoleId(userid));
		}

		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("userprofileflag", "userprofileflag");

		return "IPSAccessandRoles";
	}

	@RequestMapping(value = "createAccessRole", method = RequestMethod.POST)
	@ResponseBody
	public String createAccessRoleEn(@RequestParam("formmode") String formmode,
			@RequestParam(value = "adminValue", required = false) String adminValue,
			@RequestParam(value = "auditValue", required = false) String auditValue,
			@RequestParam(value = "IPSOperationsValue", required = false) String IPSOperationsValue,
			@RequestParam(value = "monitoringValue", required = false) String monitoringValue,
			@RequestParam(value = "my_tregistrationValue", required = false) String my_tregistrationValue,
			@RequestParam(value = "finalString", required = false) String finalString,

			@ModelAttribute IPSAccessRole alertparam, Model md, HttpServletRequest rq) {

		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = AccessRoleService.addPARAMETER(alertparam, formmode, adminValue, auditValue, IPSOperationsValue,
				monitoringValue, my_tregistrationValue, finalString, userid);

		return msg;

	}

	@RequestMapping(value = "deleteAccessRole", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAccessRoleEn(@RequestParam(value = "userid", required = false) String userid, Model md,
			HttpServletRequest rq) {

		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = AccessRoleService.deleteRole(userid);

		md.addAttribute("adminflag", "adminflag");
		md.addAttribute("menu", "monitoringparameter");

		return msg;

	}

	@RequestMapping(value = "IPSReconciliation")
	public String IPSReconciliation(Model md, HttpServletRequest req,
			@RequestParam(value = "formmode", required = false) String formmode,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));
		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());

		} else {
			tranDate = date;
		}

		if (formmode == null || formmode.equals("Main")) {
			md.addAttribute("menu", "IPSReconciliation");
			md.addAttribute("menuname", "IPSReconciliation");
			md.addAttribute("formmode", "Main");

			md.addAttribute("inwardBOBCount", monitorService.getReconciliationInwardRecord(tranDate));
			md.addAttribute("outwardBOBCount", monitorService.getReconciliationOutwardRecord(tranDate));
			md.addAttribute("offsetBOBCount", "0");
			md.addAttribute("unmatchBOBCount", monitorService.getReconciliationUnmatchRecord(tranDate));
			md.addAttribute("inwardBOMCount", monitorService.getSettlInwardRecord(tranDate));
			md.addAttribute("outwardBOMCount", monitorService.getSettlOutwardRecord(tranDate));
			md.addAttribute("offsetBOMCount", "0");
			md.addAttribute("unmatchBOMCount", "0");
			md.addAttribute("failedBOBCount", "0");
			md.addAttribute("failedBOMCount", "0");

			md.addAttribute("inwardBOBNoTxs", monitorService.getReconciliationInwardRecordNoTxs(tranDate));
			md.addAttribute("outwardBOBNoTxs", monitorService.getReconciliationOutwardRecordNoTxs(tranDate));
			md.addAttribute("offsetBOBNoTxs", monitorService.getReconciliationOffesetRecordNoTxs(tranDate));
			md.addAttribute("unmatchBOBNoTxs", monitorService.getReconciliationUnmatchRecordNoTxs(tranDate));
			md.addAttribute("inwardBOMNoTxs", monitorService.getSettlInwardRecordNoTxs(tranDate));
			md.addAttribute("outwardBOMNoTxs", monitorService.getSettlOutwardRecordNoTxs(tranDate));
			md.addAttribute("failedBOBNoTxs", monitorService.getReconciliationfailedRecordNoTxs(tranDate));
			md.addAttribute("offsetBOMNoTxs", "0");
			md.addAttribute("unmatchBOMNoTxs", "0");
			md.addAttribute("failedBOMNoTxs", "0");

			// md.addAttribute("totalBOBNoTxs",
			// monitorService.getReconciliationBOBTotRecordNoTxs(tranDate));
			md.addAttribute("totalBOBNoTxs",
					Integer.parseInt(monitorService.getReconciliationInwardRecordNoTxs(tranDate))
							+ Integer.parseInt(monitorService.getReconciliationOutwardRecordNoTxs(tranDate))
							+ Integer.parseInt(monitorService.getReconciliationUnmatchRecordNoTxs(tranDate)));

			md.addAttribute("totalBOMNoTxs", monitorService.getSettleBOBTotRecordNoTxs(tranDate));
			
			
			//md.addAttribute("totalBOMNoTxs", Integer.parseInt(monitorService.getSettleBOBTotRecordNoTxs(tranDate))+monitorService.getSettlOutwardRecordNoTxs(tranDate));


			md.addAttribute("totalBOBTotAmt",
					Double.parseDouble(monitorService.getReconciliationInwardRecord(tranDate))
							+ Double.parseDouble(monitorService.getReconciliationOutwardRecord(tranDate))
							+ Double.parseDouble(monitorService.getReconciliationUnmatchRecord(tranDate)));
			md.addAttribute("totalBOMTotAmt", Double.parseDouble(monitorService.getSettlInwardRecord(tranDate))
					+ Double.parseDouble(monitorService.getSettlOutwardRecord(tranDate)) + Double.parseDouble("0"));
		} else if (formmode.equals("BOBInward")) {
			md.addAttribute("menuname", "Inward Transactions");
			md.addAttribute("tranDetail", monitorService.getReconciliationInwardRecordList(tranDate));
			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOBOutward")) {
			md.addAttribute("menuname", "Outward Transactions");
			md.addAttribute("formmode", formmode);
			md.addAttribute("tranDetail", monitorService.getReconciliationOutwardRecordList(tranDate));

		} else if (formmode.equals("BOBOffset")) {
			md.addAttribute("menuname", "Offset Transactoins");
			md.addAttribute("tranDetail", monitorService.getReconciliationOffesetRecordList(tranDate));

			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOBUnmatched")) {
			md.addAttribute("menuname", "Unreconciled Transactions");
			md.addAttribute("tranDetail", monitorService.getReconciliationUnmatchRecordList(tranDate));
			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOBFailed")) {
			md.addAttribute("menuname", "Failed Transactions");
			md.addAttribute("tranDetail", monitorService.getReconciliationFailedRecordList(tranDate));
			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOMFailed")) {
			md.addAttribute("menuname", "Failed Transactions");
			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOMInward")) {
			md.addAttribute("menuname", "Inward Transactions");
			md.addAttribute("tranDetail", monitorService.getSettlInwardRecordList(tranDate));

			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOMOutward")) {
			md.addAttribute("menuname", "Outward Transactions");
			md.addAttribute("formmode", formmode);
			md.addAttribute("tranDetail", monitorService.getSettlOutwardRecordList(tranDate));
		} else if (formmode.equals("BOMOffset")) {
			md.addAttribute("menuname", "Offset Transactoins");

			md.addAttribute("formmode", formmode);
		} else if (formmode.equals("BOMUnmatched")) {
			md.addAttribute("menuname", "Unreconciled Transactions");

			md.addAttribute("formmode", formmode);
		}

		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);

		return "Reconciliation";
	}

	@RequestMapping(value = "snapShot")
	public String snapShot(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) MultipartFile file, @RequestParam(required = false) String userid,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute ManualTransaction manualTransaction, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException, ParseException {

		String loginuserid = (String) req.getSession().getAttribute("USERID");
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());

		} else {
			tranDate = date;
		}

		Gson gson = new Gson();

		md.addAttribute("chartDataOut", gson.toJson(monitorService.getSnapShotChartOut(tranDate)));
		md.addAttribute("chartDataIn", gson.toJson(monitorService.getSnapShotChartIn(tranDate)));

		md.addAttribute("newDate", tranDate);
		md.addAttribute("snapList", monitorService.getSnapShotDetails(tranDate));

		return "IPSSnapShot";
	}

	@RequestMapping(value = "SettlementTransaction")
	public String SettlementTransaction(@RequestParam(required = false) String acctcode,
			@RequestParam(required = false) String userid,
			@RequestParam(value = "formmode", required = false) String formmode,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@RequestParam(value = "tranDate", required = false) String date, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException, ParseException {
		String userID = (String) req.getSession().getAttribute("USERID");
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());

		} else {
			tranDate = date;
		}

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("tranDate", tranDate);
			md.addAttribute("formmode", "list");
			md.addAttribute("SettleAccount", settlementAccountServices.getSettleList());
			// md.addAttribute("SettleAccount",
			// settlementAccountRepository.findAllCustom());

		} else if (formmode.equals("Payable")) {
			Optional<SettlementAccountAmtTable> receivalble = settlAcctAmtRep
					.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
			if (receivalble.isPresent()) {
				if(receivalble.get().getPayable_flg()!=null) {
					if(receivalble.get().getPayable_flg().equals("N")) {
						md.addAttribute("trAmt",
								receivalble.get().getApp_payable_acct_bal() == null ? 0 : receivalble.get().getApp_payable_acct_bal());
						md.addAttribute("trAmtP1",
								receivalble.get().getPayable_acct_bal() == null ? 0 : receivalble.get().getPayable_acct_bal());
					}else {
						md.addAttribute("trAmt",
								receivalble.get().getApp_payable_acct_bal() == null ? 0 : receivalble.get().getApp_payable_acct_bal());
						md.addAttribute("trAmtP1",
								receivalble.get().getPayable_acct_bal() == null ? 0 : receivalble.get().getPayable_acct_bal());
					}
				}else {
					md.addAttribute("trAmt",
							receivalble.get().getPayable_acct_bal() == null ? 0 : receivalble.get().getPayable_acct_bal());
					md.addAttribute("trAmtP1",
							receivalble.get().getPayable_acct_bal() == null ? 0 : receivalble.get().getPayable_acct_bal());
				}
				//md.addAttribute("trAmt",
						//receivalble.get().getPayable_acct_bal() == null ? 0 : receivalble.get().getPayable_acct_bal());
				md.addAttribute("trFlg", receivalble.get().getPayable_flg());
				md.addAttribute("entryUser", receivalble.get().getPayable_entry_user()==null?"": receivalble.get().getPayable_entry_user());
				
			}

			md.addAttribute("trAmtSP",
					monitorService.getSettlOutwardRecord1(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
			
			
			//md.addAttribute("trAmtSP",
					//monitorService.getSettlOutwardRecord1(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay())));

			md.addAttribute("tranDate", tranDate);
			md.addAttribute("formmode", "Payable");
			md.addAttribute("tranType", "DEBIT");
			md.addAttribute("payAcctNum", settlementAccountRepository.findByAccountCode("04").getAccount_number());
			md.addAttribute("settlAcctNum", settlementAccountRepository.findByAccountCode("06").getAccount_number());
			md.addAttribute("userID", userID);
			// md.addAttribute("trAmt",
			// settlementAccountRepository.findByAccountCode("04").getAccount_number());

			md.addAttribute("formmode", "Payable");

		} else if (formmode.equals("Receivable")) {
			Optional<SettlementAccountAmtTable> receivalble = settlAcctAmtRep
					.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
			if (receivalble.isPresent()) {
				
				if(receivalble.get().getReceivable_flg()!=null) {
					if(receivalble.get().getReceivable_flg().equals("N")) {
						md.addAttribute("trAmt", receivalble.get().getApp_receivable_acct_bal()== null ? 0
								: receivalble.get().getApp_receivable_acct_bal());	
						md.addAttribute("trAmtR1", receivalble.get().getReceivable_acct_bal() == null ? 0
								: receivalble.get().getReceivable_acct_bal());
					}else {
						md.addAttribute("trAmt", receivalble.get().getApp_receivable_acct_bal()== null ? 0
								: receivalble.get().getApp_receivable_acct_bal());	
						md.addAttribute("trAmtR1", receivalble.get().getReceivable_acct_bal() == null ? 0
								: receivalble.get().getReceivable_acct_bal());
					}
				}else {
					md.addAttribute("trAmt", receivalble.get().getReceivable_acct_bal() == null ? 0
							: receivalble.get().getReceivable_acct_bal());	
					md.addAttribute("trAmtR1", receivalble.get().getReceivable_acct_bal() == null ? 0
							: receivalble.get().getReceivable_acct_bal());
				}
				//md.addAttribute("trAmt", receivalble.get().getReceivable_acct_bal() == null ? 0
						//: receivalble.get().getReceivable_acct_bal());
				md.addAttribute("trFlg", receivalble.get().getReceivable_flg());
				md.addAttribute("entryUser", receivalble.get().getReceivable_entry_user()==null?"": receivalble.get().getReceivable_entry_user());
			}
			md.addAttribute("trAmtSR",
					monitorService.getSettlInwardRecord1(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
			md.addAttribute("tranDate", tranDate);
			//md.addAttribute("formmode", "Payable");
			md.addAttribute("tranType", "CREDIT");
			md.addAttribute("receivableAcctNum",
					settlementAccountRepository.findByAccountCode("03").getAccount_number());
			md.addAttribute("settlAcctNum", settlementAccountRepository.findByAccountCode("06").getAccount_number());
			md.addAttribute("userID", userID);
			md.addAttribute("formmode", "Receivable");
		} else if (formmode.equals("Income")) {
			Optional<SettlementAccountAmtTable> income = settlAcctAmtRep
					.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
			if (income.isPresent()) {
				md.addAttribute("trAmt",
						income.get().getIncome_acct_bal() == null ? 0 : income.get().getIncome_acct_bal());
				md.addAttribute("trFlg", income.get().getIncome_flg());
			}
			md.addAttribute("tranDate", tranDate);
			md.addAttribute("formmode", "Income");
			md.addAttribute("tranType", "CREDIT");
			md.addAttribute("incomeAcctNum", settlementAccountRepository.findByAccountCode("01").getAccount_number());
			md.addAttribute("settlAcctNum", settlementAccountRepository.findByAccountCode("06").getAccount_number());

			md.addAttribute("formmode", "Income");
		} else if (formmode.equals("Expense")) {
			Optional<SettlementAccountAmtTable> expense = settlAcctAmtRep
					.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
			if (expense.isPresent()) {
				md.addAttribute("trAmt",
						expense.get().getExpense_acct_bal() == null ? 0 : expense.get().getExpense_acct_bal());
				md.addAttribute("trFlg", expense.get().getExpense_flg());
			}
			md.addAttribute("tranDate", tranDate);
			md.addAttribute("formmode", "Expense");
			md.addAttribute("tranType", "DEBIT");
			md.addAttribute("expenseAcctNum", settlementAccountRepository.findByAccountCode("02").getAccount_number());
			md.addAttribute("settlAcctNum", settlementAccountRepository.findByAccountCode("06").getAccount_number());

			md.addAttribute("formmode", "Expense");
		}

		return "IPSSettlementTran";
	}

	@RequestMapping(value = "ProceedSettlementTran")
	@ResponseBody
	public String ProceedSettlementTran(@RequestParam(required = false) String formmode,
			@RequestParam(value = "frAcct", required = false) String frAcct,
			@RequestParam(value = "toAcct", required = false) String toAcct,
			@RequestParam(value = "tranDate", required = false) String tranDate,
			@RequestParam(value = "trAmt", required = false) String trAmt,
			@RequestParam(value = "tranType", required = false) String tranType,
			@RequestParam(value = "trAmtSP", required = false) String trAmtSP,
			@RequestParam(value = "trAmtSR", required = false) String trAmtSR,
			@RequestParam(value = "trAmtPP1", required = false) String trAmtP1,
			@RequestParam(value = "trAmtRR1", required = false) String trAmtR1,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req)
			throws IOException {

		String userid = (String) req.getSession().getAttribute("USERID");
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		//String msg = settlementAccountServices.executeSettlTransaction(frAcct, toAcct, tranDate, trAmt, tranType, userid,
				//trAmtSP, trAmtSR, type, trAmtP1, trAmtR1);
		String msg = null ;
		
		System.out.println("trAmtR1"+trAmtSP);
		System.out.println("trAmtP1"+trAmtP1);
		
		if(tranIPStableRep.findByIdCustomCamt53(new SimpleDateFormat("dd-MMM-yyyy").format(new Date())).size()>0){
			if(type.equals("Payable")) {
				Optional<SettlementAccountAmtTable> payable = settlAcctAmtRep
						.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
				if (payable.isPresent()) {
					if(payable.get().getPayable_flg()!=null) {
						if(payable.get().getPayable_flg().equals("N")) {
							msg = settlementAccountServices.executeSettlTransaction(frAcct, toAcct, tranDate, trAmt, tranType, userid,
									  trAmtSP, trAmtSR, type, trAmtP1, trAmtR1);
						}else {
							msg="Settlement transaction already initiated for this account";
						}
						
					}else {
						msg = settlementAccountServices.submitSettlTransaction(frAcct, toAcct, tranDate, trAmt, tranType, userid,
								trAmtSP, trAmtSR, type, trAmtP1, trAmtR1);
					}
				}
			}else if (type.equals("Receivable")) {
				Optional<SettlementAccountAmtTable> receivable = settlAcctAmtRep
						.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
				if (receivable.isPresent()) {
					if(receivable.get().getReceivable_flg()!=null) {
						if(receivable.get().getReceivable_flg().equals("N")) {
							msg = settlementAccountServices.executeSettlTransaction(frAcct, toAcct, tranDate, trAmt, tranType, userid,
									  trAmtSP, trAmtSR, type, trAmtP1, trAmtR1);
						}else {
							msg="Settlement transaction already initiated for this account";
						}
					
					}else {
						msg = settlementAccountServices.submitSettlTransaction(frAcct, toAcct, tranDate, trAmt, tranType, userid,
								trAmtSP, trAmtSR, type, trAmtP1, trAmtR1);
					}
				}
			}
		}else {
			msg="Settlement transaction list not received from IPSX";
		}

		
		
		return msg;
	}

	@RequestMapping(value = "userActivities", method = { RequestMethod.GET, RequestMethod.POST })
	public String USERACTIVITIES(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String Fromdate, @RequestParam(required = false) String Todate,

			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req)
			throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");

		final Calendar cal = Calendar.getInstance();
		/* cal.add(Calendar.DATE, -1); */
		String date = dateFormat.format(cal.getTime());
		String date1 = dateFormat1.format(cal.getTime());

		System.out.println(date + date1);

		md.addAttribute("menuname", "User activities Audit Log");
		md.addAttribute("Fromdate", date);
		md.addAttribute("Todate", date);
		md.addAttribute("AuditList",
				monitorService.getauditListLocal(dateFormat1.parse(date1), dateFormat1.parse(date1)));
		md.addAttribute("auditflag", "auditflag");
		md.addAttribute("formmode", "list1");

		return "IPSAudit";
	}

	@RequestMapping(value = "IPSAuditInquiry", method = { RequestMethod.GET, RequestMethod.POST })
	public String BIPSAuditInquiry(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String Fromdate, @RequestParam(required = false) String Todate,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req)
			throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		Date todate1 = new SimpleDateFormat("dd/MM/yyyy").parse(Todate);
		Date fromdate1 = new SimpleDateFormat("dd/MM/yyyy").parse(Fromdate);
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));

		md.addAttribute("Fromdate", Fromdate);
		md.addAttribute("Todate", Todate);
		md.addAttribute("AuditList", monitorService.getauditListLocal(fromdate1, todate1));
		md.addAttribute("menuname", "User activities Audit Log");
		md.addAttribute("auditflag", "auditflag");
		md.addAttribute("formmode", "list1");

		return "IPSAudit";
	}

	@RequestMapping(value = "OperationLog", method = { RequestMethod.GET, RequestMethod.POST })
	public String OperationLog(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req)
			throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");

		final Calendar cal = Calendar.getInstance();
		/* cal.add(Calendar.DATE, -1); */
		String date = dateFormat.format(cal.getTime());
		String date1 = dateFormat1.format(cal.getTime());

		System.out.println(date + date1);

		md.addAttribute("menuname", "Operational Audit Log");
		md.addAttribute("auditflag", "auditflag");
		md.addAttribute("formmode", "list");

		md.addAttribute("AuditList",
				monitorService.getAuditInquries(dateFormat1.parse(date1), dateFormat1.parse(date1)));
		md.addAttribute("Fromdate", date);
		md.addAttribute("Todate", date);
		return "IPSOperationAudit";
	}

	@RequestMapping(value = "IPSAuditOperation", method = { RequestMethod.GET, RequestMethod.POST })
	public String BIPSAuditOperation(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String Fromdate, @RequestParam(required = false) String Todate,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req)
			throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		int currentPage = page.orElse(0);
		int pageSize = size.orElse(Integer.parseInt(pagesize));
		Date fromdate1 = new SimpleDateFormat("dd/MM/yyyy").parse(Fromdate);
		Date todate1 = new SimpleDateFormat("dd/MM/yyyy").parse(Todate);

		md.addAttribute("Fromdate", Fromdate);
		md.addAttribute("Todate", Todate);
		md.addAttribute("AuditList", monitorService.getAuditInquries(fromdate1, todate1));
		md.addAttribute("menuname", "Operational Audit Log");
		md.addAttribute("auditflag", "auditflag");
		md.addAttribute("formmode", "list1");

		return "IPSOperationAudit";
	}

	private Date previousDay() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	@RequestMapping(value = "Mandate", method = { RequestMethod.GET, RequestMethod.POST })
	public String Mandate(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String userid,
			@RequestParam(value = "remAcctNumber", required = false) String remAcctNumber,
			@RequestParam(value = "benAcctNumber", required = false) String benAcctNumber,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

		md.addAttribute("RuleIDType", accessandrolesrepository.roleidtype());
		md.addAttribute("menu", "Mandate"); // To highlight the menu
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu

		if (formmode == null || formmode.equals("list")) {
			md.addAttribute("formmode", "list"); // to set which form - valid values are "edit" , "add" & "list"
			md.addAttribute("mandateProfiles", mandateServices.getMandateList());
		}else if (formmode.equals("add")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("MANDATEID", sequence.generateMandateRefNo());
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
		}else if (formmode.equals("upload")) {
			md.addAttribute("formmode", formmode);
			//md.addAttribute("DOCID", sequence.generateDocRefID());
			md.addAttribute("menuname", "Mandate Management-Upload");
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
		} else if (formmode.equals("edit")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
			MandateMaster mandateMaster = mandateServices.getMandateIndList(remAcctNumber, benAcctNumber);
			md.addAttribute("mandateProfile", mandateMaster);
		} else if (formmode.equals("view")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
			MandateMaster mandateMaster = mandateServices.getMandateIndList(remAcctNumber, benAcctNumber);
			md.addAttribute("mandateProfile", mandateMaster);
		} else if (formmode.equals("delete")) {
			md.addAttribute("formmode", formmode);
			md.addAttribute("bankList", bankAgentTableRep.findAllCustom());
			MandateMaster mandateMaster = mandateServices.getMandateIndList(remAcctNumber, benAcctNumber);
			md.addAttribute("mandateProfile", mandateMaster);

		}

		return "Mandate";
	}

	@RequestMapping(value = "createMandate", method = RequestMethod.POST)
	@ResponseBody
	public String createMandate(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			@ModelAttribute MandateMaster mandateMaster,@RequestParam(value="file",required = false) MultipartFile file , Model md, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException, IOException {
		
		

		if(file!=null) {
			byte[] byteArr = file.getBytes();
			mandateMaster.setMand_doc_image(byteArr);
		}
		
		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		System.out.println(mandateMaster.toString());
		String msg = mandateServices.createMandate(mandateMaster, userid);
		md.addAttribute("menu", "Mandate");
		return msg;

	}

	@RequestMapping(value = "editMandate", method = RequestMethod.POST)
	@ResponseBody
	public String editMandate(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			@ModelAttribute MandateMaster mandateMaster,@RequestParam MultipartFile file , Model md, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException, IOException {

		

		byte[] byteArr = file.getBytes();
		mandateMaster.setMand_doc_image(byteArr);
		
		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		System.out.println(mandateMaster.toString());
		String msg = mandateServices.editMandate(mandateMaster, userid);
		md.addAttribute("menu", "Mandate");
		return msg;

	}

	@RequestMapping(value = "deleteMandate", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMandate(@RequestParam("formmode") String formmode, @ModelAttribute UserProfile userprofile,
			@ModelAttribute MandateMaster mandateMaster, Model md, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		System.out.println(mandateMaster.toString());
		String msg = mandateServices.deleteMandate(mandateMaster, userid);
		md.addAttribute("menu", "Mandate");
		return msg;

	}

	@RequestMapping(value = "downloadJasper", method = RequestMethod.GET)
	@ResponseBody
	public InputStreamResource JasperDownload(HttpServletResponse response,
			@RequestParam(value = "TRAN_DATE", required = false) String TRAN_DATE,
			@RequestParam(value = "TRAN_REASON", required = false) String TRAN_REASON,
			@RequestParam(value = "TRAN_STATUS", required = false) String TRAN_STATUS,

			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filetype", required = false) String filetype)
			throws IOException, SQLException, ParseException {

		response.setContentType("application/octet-stream");

		System.out.println(TRAN_DATE);
		System.out.println(filetype);
		System.out.println(type);

		System.out.println("reason" + TRAN_REASON);
		System.out.println("TRAN_STATUS" + TRAN_STATUS);
		InputStreamResource resource = null;
		try {

			File repfile = monitorService.getMconnectFile(TRAN_DATE, TRAN_REASON, TRAN_STATUS, filetype, type);

			if (!filetype.equals("pdf")) {
				response.setContentType("application/vnd.ms-excel");
			}
			response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
			resource = new InputStreamResource(new FileInputStream(repfile));

		} catch (JRException e) {
			e.printStackTrace();
		}

		return resource;

	}

	@RequestMapping(value = "downloadJasperBOMSettlRpt", method = RequestMethod.GET)
	@ResponseBody
	public InputStreamResource downloadJasperBOMSettlRpt(HttpServletResponse response,
			@RequestParam(value = "TRAN_DATE", required = false) String TRAN_DATE,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filetype", required = false) String filetype)
			throws IOException, SQLException, ParseException, JRException {

		response.setContentType("application/octet-stream");

		System.out.println(TRAN_DATE);
		System.out.println(filetype);
		System.out.println(type);

		InputStreamResource resource = null;
		File repfile = monitorService.getBOMSettlRptFile(TRAN_DATE, filetype, type);

		response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
		resource = new InputStreamResource(new FileInputStream(repfile));

		return resource;

	}

	@RequestMapping(value = "downloadJasperCBSRpt", method = RequestMethod.GET)
	@ResponseBody
	public InputStreamResource downloadJasperCBSRpt(HttpServletResponse response,
			@RequestParam(value = "TRAN_DATE", required = false) String TRAN_DATE,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filetype", required = false) String filetype)
			throws IOException, SQLException, ParseException, JRException {

		response.setContentType("application/octet-stream");

		System.out.println(TRAN_DATE);
		System.out.println(filetype);
		System.out.println(type);

		InputStreamResource resource = null;
		File repfile = monitorService.getCBSRptFile(TRAN_DATE, filetype, type);

		response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
		resource = new InputStreamResource(new FileInputStream(repfile));

		return resource;

	}

	
	@RequestMapping(value = "downloadJasperReCon", method = RequestMethod.GET)
	@ResponseBody
	public InputStreamResource downloadJasperIPSReConciliation(HttpServletResponse response,
			@RequestParam(value = "TRAN_DATE", required = false) String TRAN_DATE,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filetype", required = false) String filetype)
			throws IOException, SQLException, ParseException, JRException {

		response.setContentType("application/octet-stream");

		System.out.println(TRAN_DATE);
		System.out.println(filetype);
		System.out.println(type);

		InputStreamResource resource = null;
		File repfile = monitorService.getBOMReconciliationRptFile(TRAN_DATE, filetype);

		response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());
		resource = new InputStreamResource(new FileInputStream(repfile));

		return resource;

	}

	
	
	/*************INWARD CONSENT REGISTRATION START*****************/
	
	@RequestMapping(value = "InwardConReg")
	public String InwardConReg(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "viewType", required = false) String viewType) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		
		if(viewType==null) {
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getInwardConsRegList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("D")) {
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getInwardConsRegList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("M")){
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				DateFormat dateFormat1 = new SimpleDateFormat("MMMM-yyyy");
				Date tranDate1 = dateFormat1.parse(date);
			
				System.out.println("tranDate1"+tranDate1);
				
				tranDate = dateFormat.format(tranDate1);
			}
			
			
			md.addAttribute("viewType", viewType);
			List<Object[]> regPublickey = monitorService.getInwardConsRegListMonthly(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranMonth", tranDate);
		}

		
		// md.addAttribute("menu", "MMenupage");
		return "IpsInwardConsentReg";
	}
	
	
	/****************AISP REQUEST**********************/
	
	@RequestMapping(value = "AISP_REQ")
	public String AISP_REQ(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "viewType", required = false) String viewType) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		
		if(viewType==null) {
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getAISPREQList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("D")) {
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getAISPREQList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("M")){
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				DateFormat dateFormat1 = new SimpleDateFormat("MMMM-yyyy");
				Date tranDate1 = dateFormat1.parse(date);
			
				System.out.println("tranDate1"+tranDate1);
				
				tranDate = dateFormat.format(tranDate1);
			}
			
			
			md.addAttribute("viewType", viewType);
			List<Object[]> regPublickey = monitorService.getAISPREQListMonthly(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranMonth", tranDate);
		}

		
		// md.addAttribute("menu", "MMenupage");
		return "IpsAISPrequest";
	}
	

	/*************OUTWARD CONSENT REGISTRATION START*****************/
	
	@RequestMapping(value = "OutwardConReg")
	public String OutwardConReg(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "viewType", required = false) String viewType) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		
		if(viewType==null) {
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getOutwardConsRegList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("D")) {
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getOutwardConsRegList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("M")){
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				DateFormat dateFormat1 = new SimpleDateFormat("MMMM-yyyy");
				Date tranDate1 = dateFormat1.parse(date);
			
				System.out.println("tranDate1"+tranDate1);
				
				tranDate = dateFormat.format(tranDate1);
			}
			
			
			md.addAttribute("viewType", viewType);
			List<Object[]> regPublickey = monitorService.getOutwardConsRegListMonthly(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranMonth", tranDate);
		}

		
		// md.addAttribute("menu", "MMenupage");
		return "IpsOutwardConsentReg";
	}
	
	
	/****************AISP OUTWARD REQUEST**********************/
	
	@RequestMapping(value = "AISP_REQ_OUT")
	public String AISP_REQ_OUT(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "viewType", required = false) String viewType) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		
		if(viewType==null) {
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getAISPREQOUTList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("D")) {
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				tranDate = date;
			}
			
			md.addAttribute("viewType", "D");
			List<Object[]> regPublickey = monitorService.getAISPREQOUTList(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranDate", tranDate);
		}else if(viewType.equals("M")){
			
			if (date == null) {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				tranDate = dateFormat.format(new Date());
				
			} else {
				DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
				DateFormat dateFormat1 = new SimpleDateFormat("MMMM-yyyy");
				Date tranDate1 = dateFormat1.parse(date);
			
				System.out.println("tranDate1"+tranDate1);
				
				tranDate = dateFormat.format(tranDate1);
			}
			
			
			md.addAttribute("viewType", viewType);
			List<Object[]> regPublickey = monitorService.getAISPREQOUTListMonthly(tranDate);
			md.addAttribute("consentInqList", regPublickey);
			md.addAttribute("tranMonitorType", "MYT");
			md.addAttribute("tranMonth", tranDate);
		}

		
		// md.addAttribute("menu", "MMenupage");
		return "IpsAISPOutrequest";
	}
	
	@RequestMapping(value = "TransMonitorOutwardRTP")
	public String TransMonitorOutwardRTP(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());
			
			md.addAttribute("totTr", transMoniRepositry.outwardRTPListTotTxs(tranDate));
			md.addAttribute("sucTr", transMoniRepositry.outwardRTPLisSucTxs(tranDate));
			md.addAttribute("failTr", transMoniRepositry.outwardRTPListFailTxs(tranDate));
			md.addAttribute("totAmt", transMoniRepositry.outwardRTPListTotAmt(tranDate));
			md.addAttribute("sucAmt", transMoniRepositry.outwardRTPLisSucAmt(tranDate));
			md.addAttribute("failAmt", transMoniRepositry.outwardRTPListFailAmt(tranDate));
		} else {
			tranDate = date;
			
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
				md.addAttribute("totTr", transMoniRepositry.outwardRTPListTotTxs(date));
				md.addAttribute("sucTr", transMoniRepositry.outwardRTPLisSucTxs(date));
				md.addAttribute("failTr", transMoniRepositry.outwardRTPListFailTxs(date));
				md.addAttribute("totAmt", transMoniRepositry.outwardRTPListTotAmt(date));
				md.addAttribute("sucAmt", transMoniRepositry.outwardRTPLisSucAmt(date));
				md.addAttribute("failAmt", transMoniRepositry.outwardRTPListFailAmt(date));

			} else {
				md.addAttribute("totTr", tranHistMonitorRep.outwardRTPListTotTxs(date));
				md.addAttribute("sucTr", tranHistMonitorRep.outwardRTPLisSucTxs(date));
				md.addAttribute("failTr", tranHistMonitorRep.outwardRTPListFailTxs(date));
				md.addAttribute("totAmt", tranHistMonitorRep.outwardRTPListTotAmt(date));
				md.addAttribute("sucAmt", tranHistMonitorRep.outwardRTPLisSucAmt(date));
				md.addAttribute("failAmt", tranHistMonitorRep.outwardRTPListFailAmt(date));
			}
		}

		md.addAttribute("tranDetail", monitorService.getTranMonitorRTPList(date));
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "RTP");

		return "TransactionMonitoringOutward";
	}
	
	@RequestMapping(value = "TransMonitorOutwardMC")
	public String TransMonitorOutwardMC(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date) throws ParseException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String tranDate = "";

		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());

			md.addAttribute("totTr", rtpOutgoingRTP.outwardMCListTotTxs(tranDate));
			md.addAttribute("sucTr", rtpOutgoingRTP.outwardMCLisSucTxs(tranDate));
			md.addAttribute("failTr", rtpOutgoingRTP.outwardMCListFailTxs(tranDate));
			md.addAttribute("totAmt", rtpOutgoingRTP.outwardMCListTotAmt(tranDate));
			md.addAttribute("sucAmt", rtpOutgoingRTP.outwardMCLisSucAmt(tranDate));
			md.addAttribute("failAmt", rtpOutgoingRTP.outwardMCListFailAmt(tranDate));
		} else {
			tranDate = date;
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
				md.addAttribute("totTr", rtpOutgoingRTP.outwardMCListTotTxs(date));
				md.addAttribute("sucTr", rtpOutgoingRTP.outwardMCLisSucTxs(date));
				md.addAttribute("failTr", rtpOutgoingRTP.outwardMCListFailTxs(date));
				md.addAttribute("totAmt", rtpOutgoingRTP.outwardMCListTotAmt(date));
				md.addAttribute("sucAmt", rtpOutgoingRTP.outwardMCLisSucAmt(date));
				md.addAttribute("failAmt", rtpOutgoingRTP.outwardMCListFailAmt(date));

			} else {
				md.addAttribute("totTr", rtpOutgoingHistRep.outwardMCListTotTxs(date));
				md.addAttribute("sucTr", rtpOutgoingHistRep.outwardMCLisSucTxs(date));
				md.addAttribute("failTr", rtpOutgoingHistRep.outwardMCListFailTxs(date));
				md.addAttribute("totAmt", rtpOutgoingHistRep.outwardMCListTotAmt(date));
				md.addAttribute("sucAmt", rtpOutgoingHistRep.outwardMCLisSucAmt(date));
				md.addAttribute("failAmt", rtpOutgoingHistRep.outwardMCListFailAmt(date));
			}

		}

		md.addAttribute("tranDetail", monitorService.getTranMonitorOutwardMCList(date));
		md.addAttribute("menu", "MMenupage");
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "MC");

		return "TransactionMonitoringMC";
	}
	
	@RequestMapping(value = "RTPOUTING",method = RequestMethod.GET)
	public ModelAndView RTPOUTING(Model md, HttpServletRequest req,
			@RequestParam(value = "tranDate", required = false) String date,
			@RequestParam(value = "page", required = false)  Optional<Integer> page,
			@RequestParam(value = "size", required = false)  Optional<Integer> size) throws ParseException {
		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		System.out.println("page"+page);
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(100);
		
		int startItem = currentPage * pageSize;
		
		
		String tranDate = "";
		if (date == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			tranDate = dateFormat.format(new Date());

			md.addAttribute("totTr", rtpOutgoingRTP.outwardOUTListTotTxs(tranDate));
			md.addAttribute("sucTr", rtpOutgoingRTP.outwardOUTLisSucTxs(tranDate));
			md.addAttribute("failTr", rtpOutgoingRTP.outwardOUTListFailTxs(tranDate));
			md.addAttribute("totAmt", rtpOutgoingRTP.outwardOUTListTotAmt(tranDate));
			md.addAttribute("sucAmt", rtpOutgoingRTP.outwardOUTLisSucAmt(tranDate));
			md.addAttribute("failAmt", rtpOutgoingRTP.outwardOUTListFailAmt(tranDate));
		} else {
			tranDate = date;

			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date enteredDate = dateFormat.parse(date);
			if (dateFormat.format(enteredDate).equals(dateFormat.format(new Date()))) {
				md.addAttribute("totTr", rtpOutgoingRTP.outwardOUTListTotTxs(date));
				md.addAttribute("sucTr", rtpOutgoingRTP.outwardOUTLisSucTxs(date));
				md.addAttribute("failTr", rtpOutgoingRTP.outwardOUTListFailTxs(date));
				md.addAttribute("totAmt", rtpOutgoingRTP.outwardOUTListTotAmt(date));
				md.addAttribute("sucAmt", rtpOutgoingRTP.outwardOUTLisSucAmt(date));
				md.addAttribute("failAmt", rtpOutgoingRTP.outwardOUTListFailAmt(date));

			} else {
				md.addAttribute("totTr", rtpOutgoingHistRep.outwardOUTListTotTxs(date));
				md.addAttribute("sucTr", rtpOutgoingHistRep.outwardOUTLisSucTxs(date));
				md.addAttribute("failTr", rtpOutgoingHistRep.outwardOUTListFailTxs(date));
				md.addAttribute("totAmt", rtpOutgoingHistRep.outwardOUTListTotAmt(date));
				md.addAttribute("sucAmt", rtpOutgoingHistRep.outwardOUTLisSucAmt(date));
				md.addAttribute("failAmt", rtpOutgoingHistRep.outwardOUTListFailAmt(date));
			}
		}

		md.addAttribute("tranDetail", monitorService.getTranMonitorOutwardMCRTPList(date));
		//md.addAttribute("tranDetail",dataListPage);
		md.addAttribute("menu", "MMenupage"); // To highlight the menu
		md.addAttribute("tranDate", tranDate);
		md.addAttribute("tranMonitorType", "RTP-OUT");

		ModelAndView mv=new ModelAndView();
		mv.setViewName("RTPoutgoingMon");
		//mv.addObject("tranDetail", dataList);

		return mv;
	}
	

	/*********************************************
	 * MERCHANT QR CODE STARTS
	 **********************************************/
	@RequestMapping(value = "MerchantQRREG")
	public String MerchantQRREG(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String merchant_acct_no, @RequestParam(required = false) String userid,
			@RequestParam(required = false) Optional<Integer> page,@RequestParam(required = false) String merchantid,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BankAgentTable bankAgentTable, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("bankDetail", merchantQrCodeRegRep.findAllData());
		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);
			String paycode = env.getProperty("ipsx.qr.payeecode");
			md.addAttribute("paycode",paycode);
			md.addAttribute("merchantcategory", merchantCategoryRep.findAllCustom());

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", merchantQrCodeRegRep.findByIdCustom(merchant_acct_no,merchantid));
			md.addAttribute("merchantcategory", merchantCategoryRep.findAllCustom());

		} else if (formmode.equals("delete")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", merchantQrCodeRegRep.findByIdCustom(merchant_acct_no,merchantid));

		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", merchantQrCodeRegRep.findByIdCustom(merchant_acct_no,merchantid));

		}else if (formmode.equals("qrcode")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("formmode1", formmode);
			md.addAttribute("bankDetail", merchantQrCodeRegRep.findAllData());
			md.addAttribute("branchDet", merchantQrCodeRegRep.findByIdCustom(merchant_acct_no,merchantid));
			String msg = bankandBranchServices.getqrcode(merchant_acct_no,merchantid);
			md.addAttribute("msg", msg);
			System.out.println("msg :"+msg);
		}

		return "MerchantQrRegistration";
	}
	
	/*********************************************
	 * MERCHANT QR CODE STARTS
	 **********************************************/
	@RequestMapping(value = "MerchantCategoryCode")
	public String MerchantCategory(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String merchant_acct_no, @RequestParam(required = false) String userid,
			@RequestParam(required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute MerchantCategoryCodeEntity bankAgentTable, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));
 

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("merchantcategory", merchantCategoryRep.findAllCustom());
		 

		return "MerchantCategory";
	}
	@RequestMapping(value = "createmerchantcategory", method = RequestMethod.POST)
	@ResponseBody
	public String createmerchantcategory(@RequestParam("formmode") String formmode,
			@ModelAttribute UserProfile userprofile,@RequestParam(required = false) String refcode,
			@RequestParam(required = false) String refdesc,
			@ModelAttribute  MerchantCategoryCodeEntity merchantCategoryCodeEntity,
			@RequestParam(value = "file", required = false) MultipartFile file, Model md, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException, IOException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		String roleId = (String) rq.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		String msg = notificationParmsServices.createmerchantcate(refcode,refdesc, userid,formmode);
		md.addAttribute("menu", "Mandate");
		return msg;

	}

	/*********************************************
	 * MERCHANT QR CODE STARTS
	 **********************************************/
	@RequestMapping(value = "MerchantUPIQRREG")
	public String MerchantUPIQRREG(@RequestParam(required = false) String formmode,
			@RequestParam(required = false) String merchant_acct_no, @RequestParam(required = false) String userid,
			@RequestParam(required = false) Optional<Integer> page,@RequestParam(required = false) String merchantid,
			@RequestParam(value = "size", required = false) Optional<Integer> size,
			@ModelAttribute BankAgentTable bankAgentTable, Model md, HttpServletRequest req)
			throws FileNotFoundException, SQLException, IOException {

		String roleId = (String) req.getSession().getAttribute("ROLEID");
		md.addAttribute("IPSRoleMenu", AccessRoleService.getRoleMenu(roleId));

		if (formmode == null || formmode.equals("list")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("menu", "MMenupage");
			md.addAttribute("bankDetail", upiqrrep.findAllData());
		} else if (formmode.equals("add")) {

			md.addAttribute("formmode", formmode);
			String paycode = env.getProperty("ipsx.qr.payeecode");
			md.addAttribute("paycode",paycode);
			md.addAttribute("merchantcategory", merchantCategoryRep.findAllCustom());

		} else if (formmode.equals("edit")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", upiqrrep.findByIdCustom(merchant_acct_no));
			md.addAttribute("merchantcategory", merchantCategoryRep.findAllCustom());

		} else if (formmode.equals("delete")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", upiqrrep.findByIdCustom(merchant_acct_no));

		} else if (formmode.equals("verify")) {

			md.addAttribute("formmode", formmode);
			md.addAttribute("branchDet", upiqrrep.findByIdCustom(merchant_acct_no));

		}else if (formmode.equals("qrcode")) {

			md.addAttribute("formmode", "list");
			md.addAttribute("formmode1", formmode);
			md.addAttribute("bankDetail", upiqrrep.findAllData());
			md.addAttribute("branchDet", upiqrrep.findByIdCustom(merchant_acct_no));
			String msg = bankandBranchServices.getqrcode(merchant_acct_no,merchantid);
			md.addAttribute("msg", msg);
			System.out.println("msg :"+msg);
		}

		return "MerchantUPIQrCodeRegistration";
	}

}
