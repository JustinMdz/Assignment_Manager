package org.una.programmingIII.Assignment_Manager.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager.Exception.CustomErrorResponse;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final GenericMapper<FileInput, FileDto> fileMapper;
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService, GenericMapperFactory mapperFactory) {
        this.fileService = fileService;
        this.fileMapper = mapperFactory.createMapper(FileInput.class, FileDto.class);
    }

    @Operation(summary = "Create a new File", description = "This endpoint creates a new File.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping("/")
    public ResponseEntity<?> createFile(@RequestBody FileInput fileInput) {
        try {
            FileDto fileDto = fileService.createFile(fileMapper.convertToDTO(fileInput));
            return new ResponseEntity<>(fileDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorResponse("Error creating File", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @Operation(summary = "Upload file chunk", description = "This endpoint uploads a file chunk to the server.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chunk uploaded successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFileChunk(@RequestParam("fileChunk") MultipartFile fileChunk,
                                             @RequestParam("fileId") Long fileId,
                                             @RequestParam("chunkNumber") int chunkNumber,
                                             @RequestParam("totalChunks") int totalChunks) {
        try {
            fileService.saveFileChunk(fileChunk, fileId, chunkNumber, totalChunks);
            return ResponseEntity.ok("Chunk uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorResponse("Error uploading chunk", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @Operation(summary = "Download file in chunks", description = "This endpoint downloads a file in chunks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFileInChunks(@PathVariable Long fileId, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            return fileService.downloadFileInChunks(fileId, rangeHeader);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @Operation(summary = "Delete a File", description = "This endpoint deletes a File by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        try {
            fileService.deleteFile(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorResponse("Error deleting File", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
