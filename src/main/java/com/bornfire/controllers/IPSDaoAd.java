package com.bornfire.controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.entity.EncodeQRFormatResponse;
import com.bornfire.entity.MCMerchantQRcodeRequest;
import com.bornfire.entity.MCMerchantResponse;
import com.bornfire.entity.MerchantQrGenTable;
import com.bornfire.entity.MerchantQrGenTablerep;
import com.bornfire.entity.cimMerchantQRcodeResponse;
import com.bornfire.exception.IPSXException;
import com.bornfire.qrcode.core.isos.Currency;
import com.bornfire.qrcode.model.mpm.AdditionalDataField;
import com.bornfire.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformationReservedAdditional;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.bornfire.qrcode.model.mpm.MerchantPresentedMode;
import com.bornfire.qrcode.validators.MerchantPresentedModeValidate;
import com.bornfire.valid.context.ValidationResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


@Service
@Transactional
public class IPSDaoAd {

	private static final Logger logger = LoggerFactory.getLogger(IPSDaoAd.class);
	@Autowired
	ErrorResponseCode errorCode;
	
	@Autowired
	Environment env;
	
public boolean checkConvenienceFeeValidationQR(MCMerchantQRcodeRequest mcCreditTransferRequest) {
		
		/*if (mcCreditTransferRequest.isConvenienceIndicator()) {
				if (!String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
						.equals("null")
						&& !String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
								.equals("")) {
					if (String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
							.equals("Fixed")||String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
							.equals("Percentage")) {
						if (!String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
								.equals("null")
								&& !String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
										.equals("")) {
							if (String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
									.equals("Fixed")) {
								if(String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee()).length()<=15){
									return true;

								}else {
									throw new IPSXException(errorCode.validationError("BIPS15-5"));
								}
							} else {
								if (String.valueOf(
										mcCreditTransferRequest.getConvenienceIndicatorFee())
										.length() <= 5) {
									return true;

								} else {
									throw new IPSXException(errorCode.validationError("BIPS15-6"));
								}
							}
							
						}else {
							throw new IPSXException(errorCode.validationError("BIPS15-4"));
						}
						
					}else {
						throw new IPSXException(errorCode.validationError("BIPS15-3"));
					}
				}else {
					throw new IPSXException(errorCode.validationError("BIPS15-2"));
				}
			}else {
				return true;
			}*/

			
		if(!String.valueOf(mcCreditTransferRequest.getTipOrConvenienceIndicator()).equals("null")&&
				!String.valueOf(mcCreditTransferRequest.getTipOrConvenienceIndicator()).equals("")) {
			if(mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("01")||
					mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("02")||
					mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("03")) {
				
				if(!mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("01")) {
					if (!String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
							.equals("null")
							&& !String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
									.equals("")) {
						if (String.valueOf(mcCreditTransferRequest.getTipOrConvenienceIndicator())
								.equals("02")) {
							if(String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee()).length()<=15){
								return true;

							}else {
								throw new IPSXException(errorCode.validationError("BIPS15-5"));
							}
						} else {
							if (String.valueOf(
									mcCreditTransferRequest.getConvenienceIndicatorFee())
									.length() <= 5) {
								return true;

							} else {
								throw new IPSXException(errorCode.validationError("BIPS15-6"));
							}
						}
						
					}else {
						throw new IPSXException(errorCode.validationError("BIPS15-4"));
					}
				}else {
					return true;
				}

				
			}else {
				throw new IPSXException(errorCode.validationError("BIPS15-9"));
			}
		}else {
			return true;
		}
	

	}



public boolean invaliQRdBankCode(String payeeParticipantCode) {
	boolean valid = true;
	try {

		if (payeeParticipantCode.equals(env.getProperty("ipsx.qrPartiipant"))) {
			
			valid = false;
		} else {
			valid = true;
		}
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}

	return valid;
}


public cimMerchantQRcodeResponse  createMerchantQRConnection(String psuDeviceID, String psuIpAddress, String psuID,
		 MCMerchantQRcodeRequest qrrequest,String p_id,
			String channelID,String resvField1,String resvField2)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

	cimMerchantQRcodeResponse response=new cimMerchantQRcodeResponse();

		
		String status= regMerchantQR(p_id,psuDeviceID,psuIpAddress,channelID,qrrequest);
		if(status.equals("1")) {
			
		   /* Optional<MerchantQRRegistration> data=merchantQrCodeRegRep.findById(qrrequest.getMerchantAcctInformation().getMerchantAcctNumber());
		    
		    if(data.isPresent()) {*/
		    	EncodeQRFormatResponse encodeQRresponse=encodeQRCodeFormat(qrrequest);
				
				if(encodeQRresponse.isSuccess()) {
					String[] displayText= {qrrequest.getMerchantName(),""};
					String[] titletextDesc= {"Scan here to pay"};
					String qrImageCode=generateQRCode(encodeQRresponse.getQrMsg(),displayText,titletextDesc,350,350);
					response.setBase64QR(qrImageCode);
					System.out.println("qrImageCode :"+encodeQRresponse.getQrMsg() );
					System.out.println("qrImageCode :"+qrImageCode );
					updateMerchantQRData(p_id,"SUCCESS",qrImageCode);
					return response;
				}else {
					//String responseStatus = errorCode.validationError("BIPS17");
					
					System.out.println("QR Code Error:"+encodeQRresponse.getError_desc().get(0).toString());
					updateMerchantQRData(p_id,"FAILURE",encodeQRresponse.getError_desc().get(0).toString());
					throw new IPSXException("BIPS17:"+encodeQRresponse.getError_desc().get(0));
				}
		   /* }else {
				throw new IPSXException("BIPS17:Merchant Details Not Found");
		    }*/
			
		}else {
			throw new IPSXException("BIPS500:Internel Error");
		}
		
		
	}


public MCMerchantResponse createMerchantQRConnection1(String psuDeviceID, String psuIpAddress, String psuID,
		 MCMerchantQRcodeRequest qrrequest,String p_id,
			String channelID,String resvField1,String resvField2)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

	MCMerchantResponse response=new MCMerchantResponse();

		
		String status= regMerchantQR(p_id,psuDeviceID,psuIpAddress,channelID,qrrequest);
		if(status.equals("1")) {
			
		   /* Optional<MerchantQRRegistration> data=merchantQrCodeRegRep.findById(qrrequest.getMerchantAcctInformation().getMerchantAcctNumber());
		    
		    if(data.isPresent()) {*/
		    	EncodeQRFormatResponse encodeQRresponse=encodeQRCodeFormat(qrrequest);
				
				if(encodeQRresponse.isSuccess()) {
					String[] displayText= {qrrequest.getMerchantName(),""};
					String[] titletextDesc= {"Scan here to pay"};
					String qrImageCode=generateQRCode(encodeQRresponse.getQrMsg(),displayText,titletextDesc,350,350);
					response.setBase64QR(qrImageCode);
					System.out.println("qrImageCode :"+encodeQRresponse.getQrMsg() );
					System.out.println("qrImageCode :"+qrImageCode );
					updateMerchantQRData(p_id,"SUCCESS",qrImageCode);
					return response;
				}else {
					//String responseStatus = errorCode.validationError("BIPS17");
					
					System.out.println("QR Code Error:"+encodeQRresponse.getError_desc().get(0).toString());
					updateMerchantQRData(p_id,"FAILURE",encodeQRresponse.getError_desc().get(0).toString());
					throw new IPSXException("BIPS17:"+encodeQRresponse.getError_desc().get(0));
				}
		   /* }else {
				throw new IPSXException("BIPS17:Merchant Details Not Found");
		    }*/
			
		}else {
			throw new IPSXException("BIPS500:Internel Error");
		}
		
		
	}



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
					"/static/Image/MauCAS_logo.png"));
			//BufferedImage logo = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			Graphics gn = outputImage3.getGraphics();
			gn.setColor(Color.BLACK);

			//gn.drawImage(picture, 300, 5, 180, 180, null);
			gn.drawImage(picture, 200, 3, 400, 180, null);
			
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

@Autowired
MerchantQrGenTablerep mercantQrGenTableRep;

public String regMerchantQR(String p_id, String psuDeviceID, String psuIpAddress, String channelID,
		MCMerchantQRcodeRequest qrrequest) {
	String status="0";
	try {
		MerchantQrGenTable merchantQrGenTable=new MerchantQrGenTable();
		merchantQrGenTable.setP_id(p_id);
		merchantQrGenTable.setPsu_device_id(psuDeviceID);
		merchantQrGenTable.setPsu_ip_address(psuIpAddress);
		merchantQrGenTable.setPsu_channel(channelID);
		
		merchantQrGenTable.setPayload_format_indicator(qrrequest.getPayloadFormatIndiator());
		merchantQrGenTable.setPoi_method(qrrequest.getPointOfInitiationFormat());
		merchantQrGenTable.setGlobal_unique_id(qrrequest.getMerchantAcctInformation().getGlobalID());
		merchantQrGenTable.setPayee_participant_code(qrrequest.getMerchantAcctInformation().getPayeeParticipantCode());
		merchantQrGenTable.setMerchant_acct_no(qrrequest.getMerchantAcctInformation().getMerchantAcctNumber());
		merchantQrGenTable.setMerchant_id(qrrequest.getMerchantAcctInformation().getMerchantID());
		merchantQrGenTable.setMerchant_category_code(qrrequest.getMCC());
		merchantQrGenTable.setTransaction_crncy(qrrequest.getCurrency());
		merchantQrGenTable.setTip_or_conv_indicator(qrrequest.getTipOrConvenienceIndicator());
		merchantQrGenTable.setValue_conv_fees(qrrequest.getConvenienceIndicatorFee());
		merchantQrGenTable.setCountry(qrrequest.getCountryCode());
		merchantQrGenTable.setMerchant_name(qrrequest.getMerchantName());
		merchantQrGenTable.setCity(qrrequest.getCity());
		merchantQrGenTable.setZip_code(qrrequest.getPostalCode());
		merchantQrGenTable.setBill_number(qrrequest.getAdditionalDataInformation().getBillNumber());
		merchantQrGenTable.setMobile(qrrequest.getAdditionalDataInformation().getMobileNumber());
		merchantQrGenTable.setReference_label(qrrequest.getAdditionalDataInformation().getReferenceLabel());
		merchantQrGenTable.setCustomer_label(qrrequest.getAdditionalDataInformation().getCustomerLabel());
		merchantQrGenTable.setTerminal_label(qrrequest.getAdditionalDataInformation().getTerminalLabel());
		merchantQrGenTable.setPurpose_of_tran(qrrequest.getAdditionalDataInformation().getPurposeOfTransaction());
		merchantQrGenTable.setStore_label(qrrequest.getAdditionalDataInformation().getStoreLabel());
		merchantQrGenTable.setLoyalty_number(qrrequest.getAdditionalDataInformation().getLoyaltyNumber());
		merchantQrGenTable.setAdditional_details(qrrequest.getAdditionalDataInformation().getAddlDataRequest());
		
		mercantQrGenTableRep.save(merchantQrGenTable);
		status="1";
	}catch(Exception e){
		status="0";
	}
	return status;
	
}

private EncodeQRFormatResponse encodeQRCodeFormat(MCMerchantQRcodeRequest qrReuest) {

	EncodeQRFormatResponse response=new EncodeQRFormatResponse();

	final MerchantPresentedMode merchantPresentMode = new MerchantPresentedMode();
	
	///Payload Format Indicator(00)
	merchantPresentMode.setPayloadFormatIndicator(qrReuest.getPayloadFormatIndiator());
	//Point Of Initiation Method(01)
	if(!String.valueOf(qrReuest.getPointOfInitiationFormat()).equals("null")&&
			!String.valueOf(qrReuest.getPointOfInitiationFormat()).equals("")) {
		merchantPresentMode.setPointOfInitiationMethod(qrReuest.getPointOfInitiationFormat());
	}
	///02-25///
	///	final MerchantAccountInformationTemplate merchanAccountInformationReserved = getMerchanAccountInformationReserved();
	///merchantPresentMode.addMerchantAccountInformation(merchanAccountInformationReserved);
	
	///Maucas Merchant Account Information(26)
	final MerchantAccountInformationReservedAdditional merchantAccountInformationValue = new MerchantAccountInformationReservedAdditional();
	merchantAccountInformationValue.setGloballyUniqueIdentifier(qrReuest.getMerchantAcctInformation().getGlobalID());
	merchantAccountInformationValue.setPayeeParticipantCode(qrReuest.getMerchantAcctInformation().getPayeeParticipantCode());
	merchantAccountInformationValue.setMerchantAccountNumber(qrReuest.getMerchantAcctInformation().getMerchantAcctNumber());
	merchantAccountInformationValue.setMerchantID(qrReuest.getMerchantAcctInformation().getMerchantID());
	final MerchantAccountInformationTemplate merchanAccountInformationReservedAdditional =new MerchantAccountInformationTemplate("26", merchantAccountInformationValue);
	// merchantAccountInformationValue.addPaymentNetworkSpecific(paymentNetworkSpecific);
	merchantPresentMode.addMerchantAccountInformation(merchanAccountInformationReservedAdditional);

	  
	////Merchant Category Code(52)
	merchantPresentMode.setMerchantCategoryCode(qrReuest.getMCC());
	
	
	merchantPresentMode.setTransactionCurrency(Currency.entryOf(qrReuest.getCurrency()).getNumber());

	////Transaction Amount(optional)(54)
	if(!String.valueOf(qrReuest.getTrAmt()).equals("null")&&
			!String.valueOf(qrReuest.getTrAmt()).equals("")	) {
		merchantPresentMode.setTransactionAmount(qrReuest.getTrAmt());
	}

	
	/*if(qrReuest.isConvenienceIndicator()) {
		if(String.valueOf(qrReuest.getConvenienceIndicatorFeeType()).equals("Fixed")) {

			////Tip or Convenience Indicator(55)
			merchantPresentMode.setTipOrConvenienceIndicator("02");

			///Convenience Indicator Fee Fixed(56)
			merchantPresentMode.setValueOfConvenienceFeeFixed(qrReuest.getConvenienceIndicatorFee());

		}else if(String.valueOf(qrReuest.getConvenienceIndicatorFeeType()).equals("Percentage")) {			

			//// Tip or Convenience Indicator(55)
			merchantPresentMode.setTipOrConvenienceIndicator("03");

			//// Convenience Indicator Fee Percentage(57)
			 merchantPresentMode.setValueOfConvenienceFeePercentage(qrReuest.getConvenienceIndicatorFee());
		}
	}*/
	
	if(!String.valueOf(qrReuest.getTipOrConvenienceIndicator()).equals("null")&&
			!String.valueOf(qrReuest.getTipOrConvenienceIndicator()).equals("")) {
		
		if(String.valueOf(qrReuest.getTipOrConvenienceIndicator()).equals("01")) {

			////Tip or Convenience Indicator(55)
			merchantPresentMode.setTipOrConvenienceIndicator("01");

		}else if(String.valueOf(qrReuest.getTipOrConvenienceIndicator()).equals("02")) {

			////Tip or Convenience Indicator(55)
			merchantPresentMode.setTipOrConvenienceIndicator("02");

			///Convenience Indicator Fee Fixed(56)
			merchantPresentMode.setValueOfConvenienceFeeFixed(qrReuest.getConvenienceIndicatorFee());

		}else if(String.valueOf(qrReuest.getTipOrConvenienceIndicator()).equals("03")) {			

			//// Tip or Convenience Indicator(55)
			merchantPresentMode.setTipOrConvenienceIndicator("03");

			//// Convenience Indicator Fee Percentage(57)
			 merchantPresentMode.setValueOfConvenienceFeePercentage(qrReuest.getConvenienceIndicatorFee());
		}
	}
	
	////Country Code (58)
	merchantPresentMode.setCountryCode(qrReuest.getCountryCode());
	
	////Merchant Name(59)
	merchantPresentMode.setMerchantName(qrReuest.getMerchantName());

	////Merchant City(60)
	merchantPresentMode.setMerchantCity(qrReuest.getCity());

	////Postal Code (61)
	if(!String.valueOf(qrReuest.getPostalCode()).equals("null")&&
			!String.valueOf(qrReuest.getPostalCode()).equals("")	) {
		merchantPresentMode.setPostalCode(qrReuest.getPostalCode());
	}
	

	if(qrReuest.getAdditionalDataInformation()!=null) {

		if((!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals(""))||
				(!String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction()).equals(""))) {
			
		////Additional Data Information(62)
			final AdditionalDataField additionalDataFieldValue = new AdditionalDataField();
			
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals("")	) {
				additionalDataFieldValue.setBillNumber(qrReuest.getAdditionalDataInformation().getBillNumber());
			}
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals("")	) {
				additionalDataFieldValue.setMobileNumber(qrReuest.getAdditionalDataInformation().getMobileNumber());
			}
			if (!String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals("null")
					&& !String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals("")) {
				additionalDataFieldValue
						.setReferenceLabel(qrReuest.getAdditionalDataInformation().getReferenceLabel());

			}

			if (!String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")
					&& !String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction())
							.equals("")) {
				additionalDataFieldValue
						.setPurposeTransaction(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction());

			}
			
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals("")
					) {
				additionalDataFieldValue.setStoreLabel(qrReuest.getAdditionalDataInformation().getStoreLabel());

			}
			
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals("")
					) {
				additionalDataFieldValue.setLoyaltyNumber(qrReuest.getAdditionalDataInformation().getLoyaltyNumber());

			}
			
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals("")
					) {
				additionalDataFieldValue.setCustomerLabel(qrReuest.getAdditionalDataInformation().getCustomerLabel());

			}
			
			
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals("")
					) {
				System.out.println(qrReuest.getAdditionalDataInformation().getTerminalLabel());
				additionalDataFieldValue.setTerminalLabel(qrReuest.getAdditionalDataInformation().getTerminalLabel());

			}
			
			if(!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&
					!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals("")
					) {
				additionalDataFieldValue.setAdditionalConsumerDataRequest(qrReuest.getAdditionalDataInformation().getAddlDataRequest());

			}

			
			final AdditionalDataFieldTemplate additionalDataField = new AdditionalDataFieldTemplate();
			additionalDataField.setValue(additionalDataFieldValue);
			
			merchantPresentMode.setAdditionalDataField(additionalDataField);

		}

	}

	
	

	//final MerchantInformationLanguageTemplate merchantInformationLanguage = getMerchantInformationLanguage();
	//final UnreservedTemplate unreserved = getUnreserved();
	//final TagLengthString rFUforEMVCo = new TagLengthString("65", "00");

	//merchantPresentMode.setMerchantInformationLanguage(merchantInformationLanguage);
	
	
	//merchantPresentMode.addRFUforEMVCo(rFUforEMVCo);
	//merchantPresentMode.addUnreserved(unreserved);

	final ValidationResult validationResult = MerchantPresentedModeValidate.validate(merchantPresentMode);
	System.out.println(validationResult.getErrors().toString());

	if (validationResult.isValid()) {
		response.setSuccess(true);
		response.setQrMsg(merchantPresentMode.toString());
	} else {
		response.setSuccess(false);
		List<String> desc=new ArrayList<>();
		Collection<com.bornfire.valid.context.Error> error=validationResult.getErrors();
				for (com.bornfire.valid.context.Error elem : error) {
					desc.add(elem.getMessage());
					break;
				}
		response.setError_desc(desc);
	}
	System.out.println(merchantPresentMode.toString());


	return response;

}


public void updateMerchantQRData(String p_id, String status, String reason) {
	Optional<MerchantQrGenTable> data=mercantQrGenTableRep.findById(p_id);
	if(data.isPresent()) {
		MerchantQrGenTable subData=data.get();
		subData.setStatus(status);
		if(status.equals("SUCCESS")) {
			byte[] decodedBytesQR = Base64.getDecoder().decode(reason);
			subData.setQr_code(decodedBytesQR);
		}else {
			subData.setReason(reason);
		}
		
		mercantQrGenTableRep.save(subData);
	}
	
}
}
