package com.techelevator.storage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@CrossOrigin
@RestController
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files")
    public List<String> listUploadedFiles(Model model) throws IOException {
        return storageService.loadAll().map(
                // MvcUriComponentsBuilder builds URIs (for the files the user uploads) from URI components
                // fromMethodName() takes a class and a method name, calls it with the path's file name which returns a
                // URI Component Object
                // from the URI component, build it, convert it to a URI Object and converts it into a string
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                // collects everything we mapped into a list of Strings
                .collect(Collectors.toList());
    }
    // .+ means any characters and any number of characters (respectively) regex
    @ResponseBody
    @RequestMapping(path = "/files/{filename}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    // serveFile() is taking the file from the upload directory and returning it to the client
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        // CONTENT_DISPOSITION tells the client there is an attachment with a file name
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // taking uploaded file and storing it in the storage service
        storageService.store(file);
        return "You successfully uploaded " + file.getOriginalFilename() + "!";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

