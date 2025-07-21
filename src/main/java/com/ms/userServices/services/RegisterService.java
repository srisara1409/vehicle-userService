package com.ms.userServices.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.UserVehicleInfoRepository;

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
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RegisterService {

	private static final Logger LOGGER = Logger.getLogger(RegisterService.class.getName());
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	private final String baseUploadDir = "uploads";

	public byte[] generateBankDetailsPdf(String accountName, String bsbNumber, String accountNumber, String BankName, byte[] signatureImageBytes) throws Exception {
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
		document.add(new Paragraph("Financial Institution: " + BankName));
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
    
    public boolean updateUserInfo(Long id, UserInfo updatedUser) {
        Optional<UserInfo> optionalUser = userLoginRepository.findById(id);
        if (optionalUser.isPresent()) {
            UserInfo existingUser = optionalUser.get();
            LOGGER.info("Updating user info for ID: " + id);

            applyUserUpdates(existingUser, updatedUser);

            userLoginRepository.save(existingUser);
            LOGGER.info("User info updated and saved successfully for ID: " + id);
            return true;
        } else {
            LOGGER.warning("User with ID " + id + " not found.");
            return false;
        }
    }

    /**
     * Applies only non-null fields from updatedUser to existingUser.
     */
    private void applyUserUpdates(UserInfo existingUser, UserInfo updatedUser) {
        // Basic Info
        if (updatedUser.getFirstName() != null) existingUser.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null) existingUser.setLastName(updatedUser.getLastName());
        if (updatedUser.getDateOfBirth() != null) existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getMobileNumber() != null) existingUser.setMobileNumber(updatedUser.getMobileNumber());

        // Emergency
        if (updatedUser.getEmergencyContactName() != null) existingUser.setEmergencyContactName(updatedUser.getEmergencyContactName());
        if (updatedUser.getEmergencyContactNumber() != null) existingUser.setEmergencyContactNumber(updatedUser.getEmergencyContactNumber());

        // Address
        if (updatedUser.getAddressLine1() != null) existingUser.setAddressLine1(updatedUser.getAddressLine1());
        if (updatedUser.getAddressLine2() != null) existingUser.setAddressLine2(updatedUser.getAddressLine2());
        if (updatedUser.getCity() != null) existingUser.setCity(updatedUser.getCity());
        if (updatedUser.getState() != null) existingUser.setState(updatedUser.getState());
        if (updatedUser.getPostalCode() != null) existingUser.setPostalCode(updatedUser.getPostalCode());
        if (updatedUser.getCountry() != null) existingUser.setCountry(updatedUser.getCountry());

        // Bank
        if (updatedUser.getBankName() != null) existingUser.setBankName(updatedUser.getBankName());
        if (updatedUser.getAccountName() != null) existingUser.setAccountName(updatedUser.getAccountName());
        if (updatedUser.getBsbNumber() != null) existingUser.setBsbNumber(updatedUser.getBsbNumber());
        if (updatedUser.getAccountNumber() != null) existingUser.setAccountNumber(updatedUser.getAccountNumber());

        // License
        if (updatedUser.getVehicleType() != null) existingUser.setVehicleType(updatedUser.getVehicleType());
        if (updatedUser.getLicenseNumber() != null) existingUser.setLicenseNumber(updatedUser.getLicenseNumber());
        if (updatedUser.getLicenseState() != null) existingUser.setLicenseState(updatedUser.getLicenseState());
        if (updatedUser.getLicenseCountry() != null) existingUser.setLicenseCountry(updatedUser.getLicenseCountry());
    }
}
