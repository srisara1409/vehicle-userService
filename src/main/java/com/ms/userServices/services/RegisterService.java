package com.ms.userServices.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import com.ms.userServices.repository.VehicleRepository;

@Service
public class RegisterService {

	@Autowired
	private VehicleRepository repo;

	public byte[] generateBankDetailsPdf(String accountName, String bsbNumber, String accountNumber, String financialInstName) throws Exception {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, out);
		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
		document.add(new Paragraph("Cheque/Savings Account", font));
		document.add(new Paragraph(
				"\nI/We request and authorise ZUBER CAR RENTAL PTY LTD (314011) to arrange, through its own financial institution, a debit "
						+ "to your nominated account any amount ZUBER CAR RENTAL PTY LTD (314011), has deemed payable by you. This debit or charge will be "
						+ "made through the Bulk Electronic Clearing System (BECS) from your account held at the financial institution you have nominated below "
						+ "and will be subject to the terms and conditions of the Direct Debit Request Service Agreement.\n",
						FontFactory.getFont(FontFactory.HELVETICA, 12)
				));

		document.add(new Paragraph(" ")); // spacing
		document.add(new Paragraph("Financial Institution: " +financialInstName));
		document.add(new Paragraph("Account Name: " + accountName));
		document.add(new Paragraph("BSB Number: " + bsbNumber));
		document.add(new Paragraph("Account Number: " + accountNumber));
		document.add(new Paragraph(
				"\nI/We request and authorise Acknowledement. By signing and/or providing us with a valid instruction in respect to your "
						+ "Direct Debit Request, you have understood and agreed to the terms and conditions governing the debit arrangements "
						+ "between you and ZUBER CAR RENTAL PTY LTD as set out in this Request and in your Direct Debit Request Service Agreement. \n\n",
						FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph("Signature: ___________________________ \t Date: _________________________ \n\n"));
		document.add(new Paragraph("Signature: ___________________________ \t Date: _________________________"));
		document.add(new Paragraph("             If debiting from a joint bank account, both signatures are required."));

		document.add(new Paragraph("Completed Application", font));
		document.add(new Paragraph("_________________________________________________________________________"));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Return your completed application by mail to:-\n"
				+ "          Mail: 			 4/64 hillard street \n"
				+ "				          		Wiley park, NSW, Australia      2195"));
		document.close();
		return out.toByteArray();
	}	   
}
