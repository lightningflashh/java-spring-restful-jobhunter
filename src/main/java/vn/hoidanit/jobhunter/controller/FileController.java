package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.StorageException;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    @Value("${cheesethank.upload-file.base-uri}")
    private String baseUri;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload a file successfully")
    public ResponseEntity<ResUploadFileDTO> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder)
            throws URISyntaxException, IOException, StorageException {

        // validate file
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please select a file to upload.");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg",
                "png", "doc", "docx");
        List<String> allowedMimeTypes = Arrays.asList(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // Validate extension
        boolean isValidExtension = allowedExtensions.stream()
                .anyMatch(ext -> fileName.toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new StorageException("Invalid file type based on extension.");
        }

        // Validate MIME type
        String contentType = file.getContentType();
        if (!allowedMimeTypes.contains(contentType)) {
            throw new StorageException("Invalid file type based on MIME type.");
        }

        // Check file size
        long maxSize = 5 * 1024 * 1024; // 5 MB in bytes
        if (file.getSize() > maxSize) {
            // todo
            throw new StorageException("File size exceeds the limit of 5MB.");
        }

        // handle creating upload directory if not exists
        this.fileService.createUploadDirectory(baseUri + folder);

        // handle storing file in the directory
        String uploadedFile = this.fileService.store(file, folder);

        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(uploadedFile, Instant.now());

        return ResponseEntity.ok().body(resUploadFileDTO);

    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        // Handle file download
        // ...existing code...
        return ResponseEntity.status(HttpStatus.OK).body(new byte[0]);
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        // Handle file deletion
        // ...existing code...
        return ResponseEntity.status(HttpStatus.OK).body("File deleted successfully");
    }
}
