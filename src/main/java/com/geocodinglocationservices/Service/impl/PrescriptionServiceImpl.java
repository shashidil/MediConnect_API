package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.PrescriptionService;
import com.geocodinglocationservices.models.Prescription;
import com.geocodinglocationservices.models.Role;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.request.PrescriptionRequest;
import com.geocodinglocationservices.repository.PrescriptionRepo;
import com.geocodinglocationservices.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    PrescriptionRepo prescriptionRepo;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    private final Path fileStorageLocation;

    public PrescriptionServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    @Override
    public Prescription storeFile(MultipartFile file, User userId) {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());


            try {
                // Check if the file's name contains invalid characters
                if (fileName.contains("..")) {
                    throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
                }

                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                Prescription prescription = new Prescription();
                prescription.setFileName(fileName);
                prescription.setFilePath(targetLocation.toString());
                prescription.setUser(userId);

               return prescriptionRepo.save(prescription);


            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
            }

        }

    @Override
    public Prescription storeMedicine(User userId, PrescriptionRequest prescriptionRequest) {
        Prescription prescription = modelMapper.map(prescriptionRequest, Prescription.class);
        prescription.setUser(userId);
        return prescriptionRepo.save(prescription);

    }

    @Override
    public List<Prescription> getAllPrescriptionsForPharmacist(Long userId) {
        Optional<User> user = userRepository.findById(userId);


        if (!user.isPresent()) {
            throw new RuntimeException("User Not Found");
        }

        Set<Role> roles = user.get().getRoles();

        // Check if the user has the 'CUSTOMER' role
        boolean isPharmacist = roles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_PHARMACIST"));

        if (isPharmacist) {
            return prescriptionRepo.findByUser(user);
        } else {
            throw new RuntimeException("Unauthorized Access");
        }


    }
}



