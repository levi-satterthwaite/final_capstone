package com.techelevator.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


@Service // Annotation needed for Service classes on the server side to allow the class to be autowired into Spring
public class FileSystemStorageService implements StorageService {
    // root location is the absolute path to the upload directory (this is the path where the files are uploaded/retrieved)
    private final Path rootLocation;

    @Autowired //Allows for dependency injection
    public FileSystemStorageService(StorageProperties properties) {
        // creating the absolute path
        this.rootLocation = Paths.get(properties.getLocation());
    }
    @Override
    public String store(MultipartFile file) {
        String outputFileName = "";
        try {
            // If user tries to upload an empty file throw an exception
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            // if user tries to upload file with an existing file name, we will
            // generate a new file name by appending a random number to it
            String[] fileNameParts = file.getOriginalFilename().split("\\.");
            int randomNumber = ThreadLocalRandom.current().nextInt();
            if(randomNumber < 0) {
                randomNumber *= -1;
            }
            for(int i = 0; i <= fileNameParts.length - 2; i++) {
                outputFileName += fileNameParts[i] + ".";
            }
            outputFileName += randomNumber + "." + fileNameParts[fileNameParts.length - 1];
            // Takes file from request and saves to the upload directory
            Files.copy(file.getInputStream(), this.rootLocation.resolve(outputFileName));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
        return outputFileName;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            // Collects and returns a list of all file names that were uploaded
            // .walk() iterates over all the contents of a directory
            // maxDepth will limit how deep we go down in the directory tree
            return Files.walk(this.rootLocation, 1)
                    // filtering for files that are not the root location
                    .filter(path -> !path.equals(this.rootLocation))
                    // takes the path and generates a relative path to that file
                    // .relativize() takes the absolute path and turns it into a relative path
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        // .resolve() takes a file name and generates and absolute path to it
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            // takes file name and gets absolute path relative to the root location
            Path file = load(filename);
            // generates a resource from the files URI
            Resource resource = new UrlResource(file.toUri());
            // if the resource exists OR is readable return the resource, else throw an exception
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        // .deleteRecursively() will delete all the files and folders from the root folder (bottom up)
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            // if the root location doesn't exist, create it
            if(!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}