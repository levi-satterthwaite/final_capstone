package com.techelevator.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    // Initialization method
    void init();
    // Method to store files
    void store(MultipartFile file);
    // Method to load all paths
    Stream<Path> loadAll();
    // Method to load single path
    Path load(String filename);
    // Loading path as resource
    Resource loadAsResource(String filename);
    // Destroys all files
    void deleteAll();

}
