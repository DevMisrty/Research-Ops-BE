package com.practice.researchopsproject.utilities;

import com.practice.researchopsproject.repository.UsersRepository;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class ImageUploadUtilities {

    @Value("${file.upload-dir}")
    private String directory;

    private final UsersRepository usersRepository;

    public String createFileName(String email){
        String fileName = email;

        fileName = fileName.replace("@","");
        fileName = fileName.replace(".","");
        fileName = fileName.concat(System.currentTimeMillis()+"");
        fileName = fileName.concat("_profileImage");

        return fileName;
    }

    public String storeFile(MultipartFile file,String email ) throws IOException {
        if(file.isEmpty()){
            throw new RuntimeException(Messages.FILE_EMPTY);
        }

        final Path storageLocation = Paths.get(directory);

        String fileName = createFileName(email);

        String extension = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        fileName = fileName.concat(extension);

        Path path = storageLocation.resolve(fileName); 

        Files.copy(file.getInputStream(), path);

        return fileName;
    }
}
