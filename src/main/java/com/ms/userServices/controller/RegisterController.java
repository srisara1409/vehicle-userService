package com.ms.userServices.controller;

import java.io.IOException;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.DeleteMapping;
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
			@RequestPart("formData") String formDataJson,
			@RequestPart(value = "licensePhoto", required = false) MultipartFile licensePhoto,
			@RequestPart(value = "passportCopy", required = false) MultipartFile passportCopy,
			@RequestPart(value = "photoIdCopy", required = false) MultipartFile photoIdCopy,
			@RequestPart(value = "signatureFile", required = false) MultipartFile signature
			) {
		try {
		
		System.out.println("Received JSON: " + formDataJson);
		
		ObjectMapper objectMapper = new ObjectMapper();
		registerRequest registerRequest = objectMapper.readValue(formDataJson, registerRequest.class);

		UserInfo user = new UserInfo();
		BeanUtils.copyProperties(registerRequest, user);

		user.setLicensePhoto(licensePhoto != null ? licensePhoto.getBytes() : null);
		user.setPassportCopy(passportCopy != null ? passportCopy.getBytes() : null);
		user.setPhotoIdCopy(photoIdCopy != null ? photoIdCopy.getBytes() : null);
		user.setSignature(signature != null ? signature.getBytes() : null);
		//byte[] signatureBlob = extractSignatureBlob(registerRequest.getSignature());
		//user.setSignature(signatureBlob);
		
		byte[] pdf = registerService.generateBankDetailsPdf(
		        user.getAccountName(), user.getBsbNumber(), user.getAccountNumber(),user.getBankName(), 
		        user.getSignature());
		user.setStatus("PENDING");
		user.setBankDetailsPdf(pdf);
		userRepo.save(user);
		return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BankDetails.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdf);
		} catch (Exception e) {
			System.err.println("Registration failed: " + e.getMessage());
		    //e.printStackTrace();  // Log the exact parsing error
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
		}
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
	    if (fileData == null || fileData.length == 0) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    // Detect MIME type from file content
	    String contentType;
	    try {
	        contentType = java.net.URLConnection.guessContentTypeFromStream(new java.io.ByteArrayInputStream(fileData));
	        if (contentType == null) {
	            if (type.equalsIgnoreCase("license") || type.equalsIgnoreCase("passport") || type.equalsIgnoreCase("photoid")) {
	            	if (isPdf(fileData)) {
	                    contentType = "application/pdf";
	                } else {
	                    contentType = "image/jpeg";
	                }
	            } else {
	                contentType = "application/pdf";
	            }
	        }
	    } catch (IOException e) {
	        contentType = "application/octet-stream";
	    }

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + getFileExtension(contentType) + "\"")
	            .contentType(MediaType.parseMediaType(contentType))
	            .body(fileData);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
	    if (!userRepo.existsById(id)) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    }

	    userRepo.deleteById(id);
	    return ResponseEntity.ok("User deleted successfully");
	}

//	private byte[] extractSignatureBlob(String base64String) {
//	    if (base64String != null && base64String.startsWith("data:image")) {
//	        base64String = base64String.substring(base64String.indexOf(",") + 1);
//	    }
//	    return Base64.getDecoder().decode(base64String);
//	}
	private String getFileExtension(String mimeType) {
	    switch (mimeType) {
	        case "image/jpeg": return ".jpg";
	        case "image/png": return ".png";
	        case "application/pdf": return ".pdf";
	        default: return "";
	    }
	}
	private boolean isPdf(byte[] data) {
	    // PDF files start with "%PDF"
	    return data != null && data.length >= 4 &&
	           data[0] == 0x25 && data[1] == 0x50 &&
	           data[2] == 0x44 && data[3] == 0x46;
	}
}
