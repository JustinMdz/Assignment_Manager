package org.una.programmingIII.Assignment_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {
    private final GenericMapper<FileInput, FileDto> fileMapper;
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService, GenericMapperFactory mapperFactory) {
        this.fileService = fileService;
        this.fileMapper = mapperFactory.createMapper(FileInput.class, FileDto.class);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFileChunk(@RequestParam("fileChunk") MultipartFile fileChunk,
                                             @RequestParam("fileInput") FileInput fileInput,
                                             @RequestParam("chunkNumber") int chunkNumber,
                                             @RequestParam("totalChunks") int totalChunks) {
        try {
            FileDto fileDto = fileMapper.convertToDTO(fileInput);
            fileService.saveFileChunk(fileChunk, fileDto, chunkNumber, totalChunks);
            return ResponseEntity.ok("Chunk uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading chunk");
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFileInChunks(@PathVariable Long fileId, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            return fileService.downloadFileInChunks(fileId, rangeHeader);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
