package com.vansh.file.service;

import com.vansh.file.entity.ImageData;
import com.vansh.file.repo.ImageRepo;
import com.vansh.file.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ImageRepo repo;

    public String uploadFile(MultipartFile file) throws IOException {
        ImageData imageData = repo.save(ImageData.builder().name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes())).build());

        if(imageData != null){
        return "Successfully Uploaded";
        }
        return "Failed To Upload ";
    }

    @Transactional
    public byte[] downloadImage(String fileName) {
        Connection connection = null;
        byte[] images = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);  // Disable auto-commit mode

            // Retrieve image data from the repository
            Optional<ImageData> imageData = repo.findByName(fileName);

            if (imageData.isPresent()) {
                // Decompress the image data
                images = ImageUtil.decompressImage(imageData.get().getImageData());
            }

            connection.commit();  // Commit the transaction

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();  // Rollback the transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // Re-enable auto-commit mode
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return images;
    }
}
