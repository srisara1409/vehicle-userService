package com.ms.userServices.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ms.userServices.entity.UserInfo;
import com.ms.userServices.model.registerRequest;
import com.ms.userServices.repository.UserLoginRepository;
import com.ms.userServices.services.RegisterService;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

	@Autowired
	private UserLoginRepository userRepo;

	@Autowired
	private RegisterService registerService;
	
//	private final FileStorageService fileService;
//
//    public FileUpdateController(FileStorageService fileService) {
//        this.fileService = fileService;
//    }

    @PostMapping("/updateFiles")
    public ResponseEntity<String> updateFiles(
    	@RequestPart("userId") Long userId,
        @RequestPart(value = "licenseFile", required = false) MultipartFile licenseFile,
        @RequestPart(value = "passportFile", required = false) MultipartFile passportFile,
        @RequestPart(value = "photoIdCopy", required = false) MultipartFile photoIdFile,
        @RequestPart(value = "bankFile", required = false) MultipartFile bankFile
    ) {
        try {
            if (licenseFile != null && !licenseFile.isEmpty()) {
            	registerService.saveFile(userId, licenseFile, "license");
            }
            if (passportFile != null && !passportFile.isEmpty()) {
            	registerService.saveFile(userId, passportFile, "passport");
            }
            if (photoIdFile != null && !photoIdFile.isEmpty()) {
            	registerService.saveFile(userId, photoIdFile, "photoIdCopy");
            }
            if (bankFile != null && !bankFile.isEmpty()) {
            	registerService.saveFile(userId, bankFile, "bankpdf");
            }
            return ResponseEntity.ok("Files updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update files.");
        }
    }


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
		        user.getAccountName(), user.getBsbNumber(), user.getAccountNumber(),user.getFinancialInstName(), 
		        user.getSignature());
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

		switch (type.toLowerCase()) {
		case "license":
			fileData = user.getLicensePhoto();
			filename = "license_photo";
			break;
		case "passport":
			fileData = user.getPassportCopy();
			filename = "passport_copy";
			break;
		case "photoid":
			fileData = user.getPhotoIdCopy();
			filename = "photo_id";
			break;
		case "bankpdf":
			fileData = user.getBankDetailsPdf();
			filename = "bank_details";
			break;
		case "signature":
			fileData = user.getSignature();
			filename = "signature_image.pdf";
			break;
		default:
			throw new IllegalArgumentException("Invalid file type: " + type);
		}

		// Detect content type if you want (optional)
	    if (fileData == null || fileData.length == 0) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    // Detect MIME type from file content
	    String contentType = "application/octet-stream";
	    try {
	        contentType = java.net.URLConnection.guessContentTypeFromStream(new java.io.ByteArrayInputStream(fileData));
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }
	    } catch (IOException e) {
	        contentType = "application/octet-stream";
	    }

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + getFileExtension(contentType) + "\"")
				.contentType(MediaType.parseMediaType(contentType))
				.body(fileData);
	}

	private byte[] extractSignatureBlob(String base64String) {
	    if (base64String != null && base64String.startsWith("data:image")) {
	        base64String = base64String.substring(base64String.indexOf(",") + 1);
	    }
	    return Base64.getDecoder().decode(base64String);
	}
	private String getFileExtension(String mimeType) {
	    switch (mimeType) {
	        case "image/jpeg": return ".jpg";
	        case "image/png": return ".png";
	        case "application/pdf": return ".pdf";
	        default: return "";
	    }
	}
}
