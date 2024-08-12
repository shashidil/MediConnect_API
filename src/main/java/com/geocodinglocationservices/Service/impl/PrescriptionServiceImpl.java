package com.geocodinglocationservices.Service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.geocodinglocationservices.Service.AmazonS3Service;
import com.geocodinglocationservices.Service.PharmacistService;
import com.geocodinglocationservices.Service.PrescriptionService;
import com.geocodinglocationservices.models.Pharmacist;
import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.Role;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.PharmacistIdRequest;
import com.geocodinglocationservices.payload.request.PrescriptionRequest;
import com.geocodinglocationservices.payload.response.PrescriptionDTO;
import com.geocodinglocationservices.payload.response.UserDTO;
import com.geocodinglocationservices.repository.PrescriptionRepo;
import com.geocodinglocationservices.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    PrescriptionRepo prescriptionRepo;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    private final Path fileStorageLocation;

    @Autowired
    PharmacistService pharmacistService;

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Value("${aws.s3.bucket_name}")
    private String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    public PrescriptionServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public Prescription storeFile(MultipartFile file, Long userId, PharmacistIdRequest pharmacistIdRequest) {
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = String.format("%s", file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            if (file.isEmpty())
                throw new IllegalStateException("Cannot upload empty file");
            String path = String.format("%s/%s", bucketName, UUID.randomUUID());
           // String path = String.format("mediconnectimg/%s", UUID.randomUUID().toString());

            System.out.println(path);

            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));
            // Generate a unique file name
           /// String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            // Upload file to S3
           // amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));

            amazonS3Service.upload(
                    path, fileName, Optional.of(metadata), file.getInputStream());

            // Retrieve user from the repository
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Save prescription details
            Prescription prescription = new Prescription();
            prescription.setFileName(fileName);
            //prescription.setFilePath(bucketName + "/" + fileName);
            prescription.setFilePath(path);
            prescription.setUser(user);
            prescription.setPharmacists(pharmacistIdRequest.getPharmacistId()
                    .stream()
                    .map(pharmacistService::getPharmacistById)
                    .collect(Collectors.toList()));

            return prescriptionRepo.save(prescription);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Prescription storeMedicine(User userId, PrescriptionRequest prescriptionRequest, PharmacistIdRequest pharmacistIdRequest) {
        Prescription prescription = modelMapper.map(prescriptionRequest, Prescription.class);
        prescription.setUser(userId);
        prescription.setPharmacists((List<Pharmacist>) pharmacistIdRequest);
        return prescriptionRepo.save(prescription);

    }


    @Override
    public List<PrescriptionDTO> getAllPrescriptionsForPharmacist(Long pharmacistId) {
        return prescriptionRepo.findAll().stream()
                .filter(prescription -> prescription.getPharmacists().stream()
                        .anyMatch(pharmacist -> pharmacist.getId().equals(pharmacistId)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PrescriptionDTO convertToDTO(Prescription prescription) {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setId(prescription.getId());
        prescriptionDTO.setFileName(prescription.getFileName());
        prescriptionDTO.setFilePath(prescription.getFilePath());

        User user = prescription.getUser();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getUsername());
        userDTO.setEmail(user.getEmail());

        prescriptionDTO.setUser(userDTO);

        // Fetch image data from S3
        byte[] imageData = downloadImageFromS3(prescription.getFilePath(),prescription.getFileName());
        prescriptionDTO.setImageData(imageData);

        return prescriptionDTO;
    }

    private byte[] downloadImageFromS3(String filePath, String fileName) {
        try {

//            if (filePath.startsWith("s3://")) {
//                filePath = filePath.substring(5);
//            }

            logger.info("Downloading image from S3 with key: {}", filePath);
           // S3Object s3Object = amazonS3.getObject(bucketName, filePath);
            S3Object s3Object= amazonS3Service.download(filePath,fileName);

            InputStream inputStream = s3Object.getObjectContent();
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.error("Error downloading image from S3", e);
            return new byte[0]; // Return empty byte array if error occurs
        } catch (Exception e) {
            logger.error("S3 key not found: {}",filePath, e);
            return new byte[0]; // Return empty byte array if key does not exist
        }

    }

}



