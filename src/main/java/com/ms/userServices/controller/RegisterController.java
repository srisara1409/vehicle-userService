package com.ms.userServices.controller;

import java.util.Base64;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.model.registerRequest;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.repository.VehicleRepository;
import com.ms.userServices.services.RegisterService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	private UserLoginRepository userRepo;

	@Autowired
	private RegisterService registerService;


	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<byte[]> registerUser(
			@RequestPart("formData") registerRequest data,
			@RequestPart(value = "licensePhoto", required = false) MultipartFile licensePhoto,
			@RequestPart(value = "passportCopy", required = false) MultipartFile passportCopy,
			@RequestPart(value = "photoIdCopy", required = false) MultipartFile photoIdCopy
			//@RequestPart(value = "signature", required = false) MultipartFile signature
			) throws Exception {
		UserInfo user = new UserInfo();
		BeanUtils.copyProperties(data, user);

		user.setLicensePhoto(licensePhoto != null ? licensePhoto.getBytes() : null);
		user.setPassportCopy(passportCopy != null ? passportCopy.getBytes() : null);
		user.setPhotoIdCopy(photoIdCopy != null ? photoIdCopy.getBytes() : null);
		//user.setSignature(signature != null ? signature.getBytes() : null);
		byte[] signatureBlob = extractSignatureBlob(data.getSignature());
		user.setSignature(signatureBlob);
		
		byte[] pdf = registerService.generateBankDetailsPdf(
		        user.getAccountName(), user.getBsbNumber(), user.getAccountNumber(),user.getFinancialInstName()
		    );
		user.setStatus("PENDING");
		user.setBankDetailsPdf(pdf);
		userRepo.save(user);
		return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BankDetails.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdf);
	}

	@GetMapping("/file/{id}/{type}")
	public ResponseEntity<byte[]> getFile(
			@PathVariable("id") Long id,
			@PathVariable("type") String type
			) {
		UserInfo user = userRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		byte[] fileData;
		String filename;
		String contentType = "application/octet-stream"; // default

		switch (type.toLowerCase()) {
		case "license":
			fileData = user.getLicensePhoto();
			filename = "license_photo.pdf";
			break;
		case "passport":
			fileData = user.getPassportCopy();
			filename = "passport_copy.pdf";
			break;
		case "photoid":
			fileData = user.getPhotoIdCopy();
			filename = "photo_id.pdf";
			break;
		case "bankpdf":
			fileData = user.getBankDetailsPdf();
			filename = "bank_details.pdf";
			break;
		case "signature":
			fileData = user.getSignature();
			filename = "signature_image.pdf";
			break;
		default:
			throw new IllegalArgumentException("Invalid file type: " + type);
		}

		// Detect content type if you want (optional)
		if (fileData != null && fileData.length > 4 && fileData[0] == (byte) 0x25) {
			contentType = "application/pdf";
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
				.contentType(MediaType.parseMediaType(contentType))
				.body(fileData);
	}

	private byte[] extractSignatureBlob(String base64String) {
	    if (base64String != null && base64String.startsWith("data:image")) {
	        base64String = base64String.substring(base64String.indexOf(",") + 1);
	    }
	    return Base64.getDecoder().decode(base64String);
	}
}
