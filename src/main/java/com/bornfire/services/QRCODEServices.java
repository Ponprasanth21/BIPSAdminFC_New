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
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.bornfire.entity.QRUrlGobalEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
@ConfigurationProperties("output")
@Transactional
public class QRCODEServices {

	@Autowired
	DataSource srcdataSource;

	private static final Logger logger = LoggerFactory.getLogger(QRCODEServices.class);

	public String createQrcode(QRUrlGobalEntity qrcodeen, String userid) {

		String msg = "";

		String initenturl = "";

		initenturl += "upiGlobal://pay?ver=" + qrcodeen.getVers() + "&mode=" + qrcodeen.getModes() + "&purpose="
				+ qrcodeen.getPurpose();

		if (qrcodeen.getOrgid()!=null) {
			initenturl += "&orgid=" + qrcodeen.getOrgid();
		}
		if (qrcodeen.getTr()!=null) {
			initenturl += "&tr=" + qrcodeen.getTr();
		}
		if (qrcodeen.getTn()!=null) {
			initenturl += "&tn=" + qrcodeen.getTn();
		}if (qrcodeen.getCategorys()!=null) {
			initenturl += "&category=" + qrcodeen.getCategorys();
		}if (qrcodeen.getUrls()!=null) {
			initenturl += "&url=" + qrcodeen.getUrls();
		}
		if (qrcodeen.getPa()!=null) {
			initenturl += "&pa=" + qrcodeen.getPa();
		}
		if (qrcodeen.getPn()!=null) {
			initenturl += "&pn=" + qrcodeen.getPn();
		}
		if (qrcodeen.getMc()!=null) {
			initenturl += "&mc=" + qrcodeen.getMc();
		}
		if (qrcodeen.getMid()!=null) {
			initenturl += "&mid=" + qrcodeen.getMid();
		}if (qrcodeen.getAm()!=null) {
			initenturl += "&am=" + qrcodeen.getAm();
		}
		if (qrcodeen.getMsid()!=null) {
			initenturl += "&msid=" + qrcodeen.getMsid();
		}
		if (qrcodeen.getMtid()!=null) {
			initenturl += "&mtid=" + qrcodeen.getMtid();
		}
		if (qrcodeen.getMtype()!=null) {
			initenturl += "&mType=" + qrcodeen.getMtype();
		}if (qrcodeen.getMgr()!=null) {
			initenturl += "&mGr=" + qrcodeen.getMgr();
		}
		if (qrcodeen.getMerchant_onboarding()!=null) {
			initenturl += "&mOnboarding=" + qrcodeen.getMerchant_onboarding();
		}if (qrcodeen.getMerchant_location()!=null) {
			initenturl += "&mLoc=" + qrcodeen.getMerchant_location();
		}
		if (qrcodeen.getBrand()!=null) {
			initenturl += "&brand=" + qrcodeen.getBrand();
		}if ( qrcodeen.getTipsorconv() !=null && qrcodeen.getTipsorconv().equals("Tip") ) {
			initenturl += "&enTips=Y";
		}if (qrcodeen.getTipsorconv() !=null && qrcodeen.getTipsorconv().equals("Convenience")) {
			if(qrcodeen.getCov_fee_type().equals("Fixed")) {
				initenturl += "&split=CONFEE:"+ qrcodeen.getVal_con_fee();
			}else if(qrcodeen.getCov_fee_type().equals("Percentage")) {
				initenturl += "&split=CONPCT:"+ qrcodeen.getVal_con_fee();
			}
			
		}
		if (!qrcodeen.getCcs().equals(null)) {
			initenturl += "&cc=" + qrcodeen.getCcs();
		}
		if (qrcodeen.getCurr()!=null) {
			initenturl += "&cu=" + qrcodeen.getCurr();
		}
		if (qrcodeen.getQrmedium()!=null) {
			initenturl += "&qrMedium=" + qrcodeen.getQrmedium();
		}
		if (qrcodeen.getInvoiceno()!=null) {
			initenturl += "&invoiceNo=" + qrcodeen.getInvoiceno();
		}
		if (qrcodeen.getInvoicedate()!=null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
			
			initenturl += "&invoiceDate=" + dateFormat.format(qrcodeen.getInvoicedate());
		}if (qrcodeen.getQrexpire()!=null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+04:00");
			
			initenturl += "&QRexpire=" + dateFormat.format(qrcodeen.getQrexpire());
		}
		
		String without = initenturl.replace(" ", "%20");
		String Qrcode = getsign(without);

		return without;
	}


	public String getsign(String intentURL) {
		String finalBHIMURL ="";
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
			System.out.println(kpg.getAlgorithm());
			ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256r1");
			kpg.initialize(ecsp); // ecsp
			KeyPair kyp = kpg.genKeyPair();
			PublicKey pubKey = kyp.getPublic();
			PrivateKey privKey = kyp.getPrivate();
			System.out.println("\n\n");
//Signature with Sha-256
			Signature dsa = Signature.getInstance("SHA256withECDSA");
			dsa.initSign(privKey);
			//String intentURL = "upi://pay?ver=01&mode=05&orgid=700004&tid=TESTGST123456qazqwe12345hs29hnd1qa2&tr=MerRef123&tn=GST%20QR&category=02&url=https://www.test.com&pa=merchant@npci&pn=Test%20Merchant&mc=5411&am=100.00&cu=INR&mid=TST5432&msid=TSTABC123&mtid=TSTABC1234&gstBrkUp=CGST:08.45|SGST:08.45&qrMedium=02&invoiceNo=BillRef123&invoiceDate=2019-06-11T13:21:50+05:30&invoiceName=Dummy%20Customer&QRexpire=2019-06-11T13:21:50+05:30&QRts=2019-06-12T13:21:50+05:30&pinCode=400063&tier=TIER1&gstIn=GSTNUM1234567890";
			System.out.println("\nintentURL:" + intentURL);
			byte[] strByte = intentURL.getBytes("UTF-8");
			dsa.update(strByte);
//Sign with private key
			byte[] realSig = dsa.sign();
			System.out.println("\nrealSig:" + realSig.toString());
			String encodedSign = java.util.Base64.getEncoder().encodeToString(realSig);

//Append with Signed URL
			 finalBHIMURL = intentURL + "&sign=" + encodedSign;
			System.out.println("\nSignature: " + new BigInteger(1, realSig).toString(16));
			System.out.println("\nEncoded Signature:" + encodedSign);
			System.out.println("\nFinal BHIM URL:" + finalBHIMURL);
//Decode the signed URL with base 64
			String[] bhimReceivedURL = finalBHIMURL.split("&sign=");
			byte[] decodedSign = java.util.Base64.getDecoder().decode(bhimReceivedURL[1]);
			System.out.println("\nDecoded Signature: " + new BigInteger(1, decodedSign).toString(16));
//Initialise verification with public key
			dsa.initVerify(pubKey);
//Initialise using plain text bytes dsa.update(bhimReceivedURL[0].getBytes("UTF8"));
//Verify against sign
			boolean verified = dsa.verify(decodedSign);
			System.out.println("\nSignature Verify Result:" + verified);
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return finalBHIMURL;
	}
	
	

}
