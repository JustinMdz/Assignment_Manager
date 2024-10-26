package org.una.programmingIII.Assignment_Manager.Service;

import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Model.File;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    void saveFileChunk(MultipartFile fileChunk, Long fileId, int chunkNumber, int totalChunks) throws IOException;
    void saveFile(Long fileId, int totalChunks, Path fileChunkStorageLocation,String uniqueFileName) throws Exception;
    FileDto createFile(FileDto fileDto);
    void deleteFile(FileDto fileDto) throws Exception;
    FileDto getFile(Long id);
    FileDto getFileBySubmission(Long id);
    ResponseEntity<InputStreamResource> downloadFileInChunks(Long fileId, @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException;
}
