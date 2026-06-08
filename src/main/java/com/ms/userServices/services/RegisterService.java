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
	
	
	public byte[] generateTermsAndConditionsPdf(byte[] signatureImageBytes) throws Exception {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, out);
		document.open();
		
		Image signatureImage = Image.getInstance(signatureImageBytes);
		signatureImage.scaleToFit(120f, 60f); // Resize to fit signature area
		signatureImage.setAlignment(Image.ALIGN_LEFT);
		
		String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
		document.add(new Paragraph("ZUBER Terms and Conditions", font));
		document.add(new Paragraph(
				"\n1. Introduction\r\n"
				+ "Welcome to ZUBER Car Rentals. By accessing or using our services—including vehicle rentals (cars, motorbikes, e-bikes),"
				+ " repairs, purchases, or accident vehicle exchanges—you agree to be bound by the following Terms and Conditions. These terms"
				+ " govern your rights and obligations and form a binding agreement between you and ZUBER CAR RENTAL PTY "
				+ "LTD (“ZUBER”, “we”, “us”, or “our”).\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n2. Rental Eligibility\r\n"
				+ "Renters must be at least 21 years old and hold a valid driver’s license (international licenses must be in English or "
				+ "officially translated).Proof of identity, address, and a security deposit (bond) are required.E-bike rentals are permitted"
				+ " from 18 years with valid photo ID.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n3. Rental Terms\r\n"
				+ "Rental periods start and end on the agreed date/time. Vehicle must be returned in the same condition it was rented"
				+ " (excluding reasonable wear and tear). Late returns incur hourly or daily charges as per our rate schedule. Fuel "
				+ "must be refilled to the same level or refueling charges apply. Mileage limits may apply; additional km charges "
				+ "may be added.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n4. Insurance and Liability\r\n"
				+ "All rentals include compulsory third-party insurance. Additional coverage is available for purchase. Renters "
				+ "are liable for damage due to misuse, negligence, or unauthorized use. Accidents must be reported within 12 hours "
				+ "with a police report if applicable. Insurance excess is payable unless waived with an excess reduction package.\r\n"
				+ "Additional Insurance & Excess Conditions:\r\n"
				+ "\r\n"
				+ "Car accident basic excess: AUD $1,200\r\n"
				+ "Motorbike and scooter basic excess: AUD $1,500\r\n"
				+ "Stolen bike excess: AUD $2,000\r\n"
				+ "Additional excess applies for drivers under 25 years of age.\r\n"
				+ "Drivers under 21 years are not covered under insurance.\r\n"
				+ "E-bikes are not covered by any insurance policy. Renters assume full responsibility for loss or damage.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n5. Prohibited Uses\r\n"
				+ "Vehicles must not be used for commercial purposes unless pre-approved. No involvement in unlawful activities or races. "
				+ "Only licensed and authorized drivers may operate the vehicle. Off-road use is "
				+ "prohibited unless specified.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n6. Breakdown and Repairs\r\n"
				+ "Vehicles are regularly maintained. Contact 24/7 support in case of breakdown. Unauthorized repairs or "
				+ "modifications are not allowed. ZUBER is not liable for delays due to breakdowns unless caused "
				+ "by known issues.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n7. Buying and Selling Vehicles\r\n"
				+ "We sell used vehicles. Test drives are available upon booking with valid ID. All sales are final unless "
				+ "otherwise stated. Inspection reports are available. Owners may list or exchange used or damaged "
				+ "vehicles with us.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n8. Accident-Damage Exchange\r\n"
				+ "We accept accident-damaged vehicles for exchange towards another ZUBER vehicle. Inspection and documentation "
				+ "are required. Final valuation is based on damage, age, and model.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n9. Payment and Bond\r\n"
				+ "Full payment is due before rental begins (via card or bank transfer). A refundable bond is collected and released "
				+ "after inspection. Admin fees may apply for bond processing or contract breach.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n10. Cancellation and Refunds\r\n"
				+ "Full refund (excluding fees) if cancelled 24+ hours before rental. Same-day cancellations may incur 50% charge. No "
				+ "refunds for early returns unless approved.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n11. Privacy and Data\r\n"
				+ "We collect personal data for rental, payment, and insurance purposes. We do not share your data except as "
				+ "required by law or partners (e.g., insurers, regulators).\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		document.add(new Paragraph(
				"\n12. Amendments\r\n"
				+ "ZUBER reserves the right to update these Terms and Conditions at any time. Continued use of the platform constitutes"
				+ " acceptance of the updated terms.\n", FontFactory.getFont(FontFactory.HELVETICA, 12)
				));
		//document.add(new Paragraph("Signature: "));
		document.add(signatureImage);
		document.add(new Paragraph("Signature:                      \t\t Date: "+ currentDate +" \n\n"));
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
