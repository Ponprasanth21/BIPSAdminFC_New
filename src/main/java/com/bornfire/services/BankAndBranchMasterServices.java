package com.bornfire.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.MerchantQRRegistration;
import com.bornfire.entity.MerchantQrCodeRegRep;
import com.bornfire.entity.QRUrlGobalEntity;
import com.bornfire.entity.UPIQrRep;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ch.qos.logback.classic.Logger;

@Service
@ConfigurationProperties("output")
@Transactional

public class BankAndBranchMasterServices {
	
	private static final org.jboss.logging.Logger logger = LoggerFactory.logger(BankAndBranchMasterServices.class);

	@Autowired
	BankAgentTableRep bankAgentTableRep;
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	MerchantQrCodeRegRep merchantQrCodeRegRep;
	
	@Autowired
	UPIQrRep upiqrrep;
	

	public String addBank(BankAgentTable bankAgentTable, String formmode,String userID) {
		Session hs = sessionFactory.getCurrentSession();
		String msg = "";

		if (formmode.equals("editsubmit")) {

			BankAgentTable bankAgentTableOrgl=bankAgentTableRep.findById(bankAgentTable.getBank_code()).get();
			
			if(bankAgentTableOrgl.getBank_name().equals(bankAgentTable.getBank_name())&&
					bankAgentTableOrgl.getBank_agent().equals(bankAgentTable.getBank_agent())&&
					bankAgentTableOrgl.getBank_agent_account().equals(bankAgentTable.getBank_agent_account())&&	
					bankAgentTableOrgl.getAddress().equals(bankAgentTable.getAddress())) {
				msg="No any modification done";
		
			}else {
				BankAgentTable up = bankAgentTable;
				up.setDel_flg("N");
				up.setEntity_flg("N");
				up.setModify_flg("Y");
				up.setModify_time(new Date());
				up.setModify_user(userID);
				
				bankAgentTableRep.save(up);
				msg = "Modified Successfully";
			}
		

		} else if (formmode.equals("verifysubmit")) {

			BankAgentTable up = bankAgentTable;
			up.setDel_flg("N");
			up.setEntity_flg("Y");
			up.setModify_flg("N");

			up.setVerify_time(new Date());
			
			bankAgentTableRep.save(up);
			msg = "Verified Successfully";

		} else if (formmode.equals("deletesubmit")) {

			BankAgentTable up = bankAgentTable;
			up.setDel_flg("Y");
			
			up.setDel_time(new Date());
			
			bankAgentTableRep.save(up);
			msg = "Deleted Successfully";

		}else if (formmode.equals("disablesubmit")) {

			BankAgentTable up = bankAgentTableRep.findByIdCustom(bankAgentTable.getBank_code());
			up.setDisable_flg("Y");
			
			up.setDisable_time(new Date());
			up.setDisable_user(userID);
			bankAgentTableRep.save(up);
			msg = "Disabled Successfully";

		}else if (formmode.equals("enablesubmit")) {

			BankAgentTable up = bankAgentTableRep.findByIdCustom(bankAgentTable.getBank_code());
			up.setDisable_flg("N");
			
			up.setDisable_time(new Date());
			up.setDisable_user(userID);
			bankAgentTableRep.save(up);
			msg = "Enabled Successfully";

		} else if (formmode.equals("addsubmit")) {

			Optional<BankAgentTable> bankList=bankAgentTableRep.findById(bankAgentTable.getBank_code());
			if(bankList.isPresent()) {
				if(bankList.get().getDel_flg().equals("Y")) {
					BankAgentTable up = bankAgentTable;
					up.setEntity_flg("Y");
					up.setModify_flg("N");
					up.setDel_flg("N");
					up.setEntry_time(new Date());

					bankAgentTableRep.save(up);
					msg = "Bank Added Successfully";
				}else {
					msg = "Bank Code Already Added";
				}
			}else {
				BankAgentTable up = bankAgentTable;
				System.out.println("Entry User"+bankAgentTable.getEntry_user());
				up.setEntity_flg("Y");
				up.setModify_flg("N");
				up.setDel_flg("N");
				up.setEntry_time(new Date());

				bankAgentTableRep.save(up);
				msg = "Bank Added Successfully";
			}
			

		}
		return msg;

	}

	
	public String addMerchantQRcode(MerchantQRRegistration merchantQRgenerator, String formmode,String userID) throws UnknownHostException {
		Session hs = sessionFactory.getCurrentSession();
		String msg = "";
		
		
		if (formmode.equals("editsubmit")) {

			/*ResponseEntity<cimMerchantQRcodeResponse> merchantQRResponse=ipsServices.getMerchantQrCode(merchantQRgenerator);

			if(merchantQRResponse.getStatusCode()==HttpStatus.OK) {
				String QrImg=merchantQRResponse.getBody().getBase64QR();
				
				byte[] decodedBytesQR = Base64.getDecoder().decode(QrImg);*/
				
				MerchantQRRegistration up = merchantQRgenerator;
				up.setDel_flg("N");
				up.setEntity_flg("N");
				up.setModify_flg("Y");
				up.setModify_time(new Date());
				up.setEntry_time(new Date());
				up.setVerify_time(new Date());
				up.setModify_user(userID);
				//up.setQr_code(decodedBytesQR);
				
				merchantQrCodeRegRep.save(up);
				msg = "Modified Successfully";
			/*}else {
				if(merchantQRResponse.getStatusCode()==HttpStatus.BAD_REQUEST) {
					msg=merchantQRResponse.getBody().getError().toString()+":"+
				merchantQRResponse.getBody().getError_Desc().get(0);
				}else {
					msg="Something went wrong at server end";
				}
			}*/
				
			}
		  else if (formmode.equals("verifysubmit")) {

			  MerchantQRRegistration up = merchantQRgenerator;
			up.setDel_flg("N");
			up.setEntity_flg("Y");
			up.setModify_flg("N");

			up.setVerify_time(new Date());
			
			merchantQrCodeRegRep.save(up);
			msg = "Verified Successfully";

		} else if (formmode.equals("deletesubmit")) {

			MerchantQRRegistration up = merchantQRgenerator;
			up.setDel_flg("Y");
			
			up.setModify_time(new Date());
			
			merchantQrCodeRegRep.save(up);
			msg = "Deleted Successfully";

		} else if (formmode.equals("addsubmit")) {

			Optional<MerchantQRRegistration> bankList=merchantQrCodeRegRep.findById(merchantQRgenerator.getMerchant_acct_no());
			if(bankList.isPresent()) {
				if(bankList.get().getDel_flg().equals("Y")) {
					MerchantQRRegistration up = merchantQRgenerator;
					up.setEntity_flg("Y");
					up.setModify_flg("N");
					up.setDel_flg("N");
					up.setEntry_time(new Date());

					merchantQrCodeRegRep.save(up);
					msg = "Merchant QR Added Successfully";
				}else {
					msg = "Merchant Account Already Exist";
				}
			}else {
				/*ResponseEntity<cimMerchantQRcodeResponse> merchantQRResponse=ipsServices.getMerchantQrCode(merchantQRgenerator);

				if(merchantQRResponse.getStatusCode()==HttpStatus.OK) {
					
					String QrImg=merchantQRResponse.getBody().getBase64QR();
					
					byte[] decodedBytesQR = Base64.getDecoder().decode(QrImg);*/

					MerchantQRRegistration up = merchantQRgenerator;
					System.out.println("Entry User"+merchantQRgenerator.getEntry_user());
					up.setEntity_flg("Y");
					up.setModify_flg("N");
					up.setDel_flg("N");
					up.setEntry_time(new Date());
					//up.setQr_code(decodedBytesQR);
					merchantQrCodeRegRep.save(up);
					msg = "Merchant QR Added Successfully";
				/*}else {
					if(merchantQRResponse.getStatusCode()==HttpStatus.BAD_REQUEST) {
						msg=merchantQRResponse.getBody().getError().toString()+":"+
					merchantQRResponse.getBody().getError_Desc().get(0);
					}else {
						msg="Something went wrong at server end";
					}
				}*/
				
				
			}
			

		}
		return msg;

	}
	
	private String isNullCheck(String inpData) {
		String outData="";
		if(inpData!=null) {
			if(!String.valueOf(inpData).equals("null")){
				if(!String.valueOf(inpData).equals("")) {
					outData=String.valueOf(inpData);
					return outData;
				}
			}
		}
		return outData;
	}
	
	
	@Autowired 
	QRCODEServices qrCODEServices;
	
	public String addMerchantUPIQRcode(QRUrlGobalEntity merchantQRgenerator, String formmode,String userID) throws UnknownHostException {
		Session hs = sessionFactory.getCurrentSession();
		String msg = "";
		
		
		if (formmode.equals("editsubmit")) {

			/*ResponseEntity<cimMerchantQRcodeResponse> merchantQRResponse=ipsServices.getMerchantQrCode(merchantQRgenerator);

			if(merchantQRResponse.getStatusCode()==HttpStatus.OK) {
				String QrImg=merchantQRResponse.getBody().getBase64QR();
				
				byte[] decodedBytesQR = Base64.getDecoder().decode(QrImg);*/
				
			QRUrlGobalEntity up = merchantQRgenerator;
				up.setDel_flg('N');
				up.setEntity_flg('N');
				up.setModify_flg('Y');
				up.setModify_date(new Date());
				up.setEntry_date(new Date());
				up.setVerify_date(new Date());
				up.setModify_user(userID);
				String qrcode = qrCODEServices.createQrcode(merchantQRgenerator,userID);
				String[] displayText= {merchantQRgenerator.getPn(),""};
				String[] titletextDesc= {"Scan here to pay"};
				//String generateQRCode = generateQRCode(qrcode,displayText,titletextDesc,350,350);
				//byte[] decodedBytesQR = Base64.getDecoder().decode(generateQRCode);
				//up.setQrcode(decodedBytesQR);
				
				upiqrrep.save(up);
				msg = "Modified Successfully";
			/*}else {
				if(merchantQRResponse.getStatusCode()==HttpStatus.BAD_REQUEST) {
					msg=merchantQRResponse.getBody().getError().toString()+":"+
				merchantQRResponse.getBody().getError_Desc().get(0);
				}else {
					msg="Something went wrong at server end";
				}
			}*/
				
			}
		  else if (formmode.equals("verifysubmit")) {

			  QRUrlGobalEntity up = merchantQRgenerator;
			up.setDel_flg('N');
			up.setEntity_flg('Y');
			up.setModify_flg('N');

			up.setVerify_date(new Date());
			
			upiqrrep.save(up);
			msg = "Verified Successfully";

		} else if (formmode.equals("deletesubmit")) {

			QRUrlGobalEntity up = merchantQRgenerator;
			up.setDel_flg('Y');
			
			up.setModify_date(new Date());
			
			upiqrrep.save(up);
			msg = "Deleted Successfully";

		} else if (formmode.equals("addsubmit")) {

			Optional<QRUrlGobalEntity> bankList=upiqrrep.findById(merchantQRgenerator.getMid());
			if(bankList.isPresent()) {
				if(bankList.get().getDel_flg().equals("Y")) {
					QRUrlGobalEntity up = merchantQRgenerator;
					up.setEntity_flg('Y');
					up.setModify_flg('N');
					up.setDel_flg('N');
					up.setEntry_date(new Date());
					String qrcode = qrCODEServices.createQrcode(merchantQRgenerator,userID);
					String[] displayText= {merchantQRgenerator.getPn(),""};
					String[] titletextDesc= {"Scan here to pay"};
					String generateQRCode = generateQRCode(qrcode,displayText,titletextDesc,350,350);
					byte[] decodedBytesQR = Base64.getDecoder().decode(generateQRCode);
					up.setQrcode(decodedBytesQR);

					upiqrrep.save(up);
					msg = "Merchant QR Added Successfully";
				}else {
					msg = "Merchant Account Already Exist";
				}
			}else {
				/*ResponseEntity<cimMerchantQRcodeResponse> merchantQRResponse=ipsServices.getMerchantQrCode(merchantQRgenerator);

				if(merchantQRResponse.getStatusCode()==HttpStatus.OK) {
					
					String QrImg=merchantQRResponse.getBody().getBase64QR();
					
					byte[] decodedBytesQR = Base64.getDecoder().decode(QrImg);*/

				QRUrlGobalEntity up = merchantQRgenerator;
					System.out.println("Entry User"+merchantQRgenerator.getEntry_user());
					up.setEntity_flg('Y');
					up.setModify_flg('N');
					up.setDel_flg('N');
					up.setEntry_date(new Date());
					String qrcode = qrCODEServices.createQrcode(merchantQRgenerator,userID);
					String[] displayText= {merchantQRgenerator.getPn(),""};
					String[] titletextDesc= {"Scan here to pay"};
					String generateQRCode = generateQRCode(qrcode,displayText,titletextDesc,350,350);
//					byte[] decodedBytesQR = Base64.getDecoder().decode(generateQRCode);
//					up.setQrcode(decodedBytesQR);
					//up.setQr_code(decodedBytesQR);
					upiqrrep.save(up);
					msg = "Merchant QR Added Successfully";
				/*}else {
					if(merchantQRResponse.getStatusCode()==HttpStatus.BAD_REQUEST) {
						msg=merchantQRResponse.getBody().getError().toString()+":"+
					merchantQRResponse.getBody().getError_Desc().get(0);
					}else {
						msg="Something went wrong at server end";
					}
				}*/
				
				
			}
			

		}
		return msg;

	}

	/*private String generateUPIQR(QRUrlGobalEntity merchantQRgenerator) {
		String qrcode = qrCODEServices.createQrcode(merchantQRgenerator,"");
		String[] displayText= {merchantQRgenerator.getPn(),""};
		String[] titletextDesc= {"Scan here to pay"};
		String generateQRCode = generateQRCode(qrcode,displayText,titletextDesc,350,350);
	//	byte[] decodedBytesQR = Base64.getDecoder().decode(generateQRCode);
		
		return generateQRCode;
	}*/
	
private String generateQRCode(String qrMsg,String[] displayTextQR,String[] titleText,Integer width,Integer height) {
		
		String encodedQRImage="0";
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrMsg, BarcodeFormat.QR_CODE, width, height);

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
				g.setFont(new Font("Arial Black", Font.ITALIC, 14));
				Color textColor = Color.BLACK;
				g.setColor(textColor);
				FontMetrics fm = g.getFontMetrics();
				int startingYposition = height + 5;
				for (String displayText : displayTextQR) {
					g.drawString(displayText, (outputImage.getWidth() / 2) - (fm.stringWidth(displayText) / 2),
							startingYposition);
					startingYposition += 20;
				}

				ByteArrayOutputStream outputbytestream1 = new ByteArrayOutputStream();
				ImageIO.write(outputImage, "PNG", outputbytestream1);

				int totalTextLineToadd2 = titleText.length;

				BufferedImage outputImage2 = new BufferedImage(outputImage.getWidth(),
						outputImage.getHeight() + 15 * totalTextLineToadd2, BufferedImage.TYPE_INT_ARGB);
				Graphics gq = outputImage2.getGraphics();
				gq.setColor(Color.WHITE);
				gq.drawImage(outputImage, 0, 0, null);
				gq.setFont(new Font("Arial Black", Font.ITALIC, 16));
				Color textColor2 = Color.BLACK;
				gq.setColor(textColor2);
				FontMetrics fm2 = gq.getFontMetrics();
				int startingYposition2 = height - 330;
				for (String displayText : titleText) {
					gq.drawString(displayText, (outputImage2.getWidth() / 2) - (fm2.stringWidth(displayText) / 2),
							startingYposition2);
					startingYposition2 += 20;
				}

				ByteArrayOutputStream outputbytestream2 = new ByteArrayOutputStream();

				//File outputnew = new File("E:\\log3.png");
				ImageIO.write(outputImage2, "PNG", outputbytestream2);

				BufferedImage outputImage3 = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
				Graphics g2 = outputImage3.getGraphics();
				g2.setColor(Color.BLACK);
				g2.drawRect(0, 0, 800, 800);

				g2.dispose();
				
				
				
				Image picture = ImageIO.read(this.getClass().getResourceAsStream(
						"/static/images/MauCAS_logo.png"));
				//BufferedImage logo = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
				Graphics gn = outputImage3.getGraphics();
				gn.setColor(Color.BLACK);

				//gn.drawImage(picture, 300, 5, 180, 180, null);
				//gn.drawImage(picture, 200, 3, 400, 180, null);
				
				gn.dispose();

				Graphics g3 = outputImage3.getGraphics();

				g3.setColor(Color.black);

				g3.drawRoundRect(130, 170, 550, 550, 20, 20);


				g3.drawImage(outputImage2, 230, 250, outputImage2.getWidth(), outputImage2.getHeight(), null);
				g3.dispose();

				ByteArrayOutputStream outputByteStreamDataImage = new ByteArrayOutputStream();

				//File output = new File("E:\\logofinal.png");
				ImageIO.write(outputImage3, "PNG", outputByteStreamDataImage);
				encodedQRImage = Base64.getEncoder().encodeToString(outputByteStreamDataImage.toByteArray());
				
				return encodedQRImage;

			}
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			return encodedQRImage;
		}
		return encodedQRImage;

	}
	

}
