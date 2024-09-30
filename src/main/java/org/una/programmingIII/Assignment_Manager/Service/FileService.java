package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    void saveFileChunk(MultipartFile fileChunk, FileDto fileDto, int chunkNumber, int totalChunks) throws IOException;
    void saveFile(FileDto fileDto, int totalChunks, Path fileChunkStorageLocation) throws Exception;
    void deleteFile(FileDto fileDto) throws Exception;
    FileDto getFile(Long id);
    FileDto getFileBySubmission(Long id);

}
