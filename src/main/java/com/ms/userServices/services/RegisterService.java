package com.ms.userServices.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class RegisterService {

//	@Autowired
//	private VehicleRepository repo;
	
	private final String baseUploadDir = "uploads";

	public byte[] generateBankDetailsPdf(String accountName, String bsbNumber, String accountNumber, String financialInstName, byte[] signatureImageBytes) throws Exception {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, out);
		document.open();
		
		Image signatureImage = Image.getInstance(signatureImageBytes);
		signatureImage.scaleToFit(120f, 60f); // Resize to fit signature area
		signatureImage.setAlignment(Image.ALIGN_LEFT);
		
		String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

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
		//document.add(new Paragraph("Signature: "));
		document.add(signatureImage);
		document.add(new Paragraph("Signature:                      \t\t Date: "+ currentDate +" \n\n"));
		//document.add(new Paragraph("Signature: _____________ \t Date: _________________________"));
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
	
	public void saveFile(Long userId, MultipartFile file, String docType) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = docType + "." + fileExtension;

        Path userDir = Paths.get(baseUploadDir, String.valueOf(userId));
        Files.createDirectories(userDir);

        Path filePath = userDir.resolve(fileName);

        // Save or overwrite existing file
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String getFileExtension(String filename) {
        String cleanName = StringUtils.cleanPath(filename);
        int index = cleanName.lastIndexOf('.');
        return (index > 0) ? cleanName.substring(index + 1) : "pdf";
    }
}
