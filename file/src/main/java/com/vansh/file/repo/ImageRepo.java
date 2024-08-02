package com.vansh.file.repo;

import com.vansh.file.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<ImageData,Integer> {
     Optional<ImageData> findByName(String name);
}
