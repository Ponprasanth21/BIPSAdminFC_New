package com.bornfire.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bornfire.entity.BulkTransaction;
import com.bornfire.entity.CustomException;
import com.bornfire.entity.MandateMaster;
import com.bornfire.services.UploadServices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class FileUploaderController {
	 //Save the uploaded file to this folder
    //private static String UPLOADED_FOLDER = "F://temp//";
	private final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

	@Autowired
	UploadServices uploadServices;

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	
	@PostMapping(value="/ws/fileUploadCredit")
	@ResponseBody
	public List<BulkTransaction> uploadFileCredit(@RequestParam("file") MultipartFile file,
			@RequestParam("bulkServices") String screenId, HttpServletRequest rq)
			throws FileNotFoundException, SQLException, IOException {

		System.out.println("fileSize"+file.getSize());
		
		if(file.getSize()<500000) {
			String userid = (String) rq.getSession().getAttribute("USERID");
			List<BulkTransaction> msg = uploadServices.processUploadFilesCredit(screenId, file, userid);
			return msg;
		}else {
			throw new CustomException("File has not been successfully uploaded. Requires less than 128 KB size.");
		}
		
		
	}
	
	@PostMapping(value="/ws/fileUploadDebit")
	@ResponseBody
	public List<BulkTransaction> fileUploadDebit(@RequestParam("file") MultipartFile file,
			@RequestParam("bulkServices") String screenId, HttpServletRequest rq)
			throws FileNotFoundException, SQLException, IOException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		List<BulkTransaction> msg = uploadServices.processUploadFilesDebit(screenId, file, userid);
		return msg;
	}
	@PostMapping(value="/ws/fileUploadMandate")
	@ResponseBody
	public List<MandateMaster> fileUploadMandate(@RequestParam("file") MultipartFile file,
			@RequestParam("mandateServices") String screenId, HttpServletRequest rq)
			throws FileNotFoundException, SQLException, IOException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		List<MandateMaster> msg = uploadServices.processUploadFilesMandate(screenId, file, userid);
		return msg;
	}
	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
	
	
 
}
