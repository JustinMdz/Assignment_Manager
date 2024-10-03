package org.una.programmingIII.Assignment_Manager.Service.Implementation;


import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.File;
import org.una.programmingIII.Assignment_Manager.Repository.FileRepository;
import org.una.programmingIII.Assignment_Manager.Service.FileService;


import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

@Service
public class FileServiceImplementation implements FileService {
    private final FileRepository fileRepository;
    private final GenericMapper<File, FileDto> fileMapper;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public FileServiceImplementation(FileRepository repository, GenericMapperFactory mapperFactory) {
        this.fileRepository = repository;
        this.fileMapper = mapperFactory.createMapper(File.class, FileDto.class);
    }

    @Override
    public void saveFileChunk(MultipartFile fileChunk, FileDto fileDto, int chunkNumber, int totalChunks) throws IOException {
        Path fileChunkStorageLocation = Paths.get(uploadDir, fileDto.getName() + "_chunks").toAbsolutePath().normalize();
        Files.createDirectories(fileChunkStorageLocation);

        Path targetLocation = fileChunkStorageLocation.resolve(fileDto.getName() + ".part" + chunkNumber);
        Files.copy(fileChunk.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        if (chunkNumber == totalChunks) {
            saveFile(fileDto, totalChunks, fileChunkStorageLocation);
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public void saveFile(FileDto fileDto, int totalChunks, Path fileChunkStorageLocation) throws IOException {
        Path finalFileLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileDto.getName());
        try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(finalFileLocation))) {
            for (int i = 1; i <= totalChunks; i++) {
                Path chunkPath = fileChunkStorageLocation.resolve(fileDto.getName() + ".part" + i);
                Files.copy(chunkPath, outputStream);
            }
        }

        FileUtils.deleteDirectory(fileChunkStorageLocation.toFile());

        fileDto.setFilePath(finalFileLocation.toString());
        fileDto.setFileSize(Files.size(finalFileLocation));

        File fileEntity = fileMapper.convertToEntity(fileDto);

        fileRepository.save(fileEntity);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFileInChunks(Long fileId, @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ElementNotFoundException("File not found"));
        Path filePath = Paths.get(file.getFilePath()).toAbsolutePath().normalize();
        long fileSize = Files.size(filePath);

        long chunkSize = 512 * 1024;
        long start = 0;
        long end = chunkSize - 1;

        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            end = Math.min(end, fileSize - 1);
        } else {
            List<HttpRange> httpRanges = HttpRange.parseRanges(rangeHeader);
            HttpRange httpRange = httpRanges.get(0);
            start = httpRange.getRangeStart(fileSize);
            end = httpRange.getRangeEnd(fileSize);
        }

        if (end >= fileSize) {
            end = fileSize - 1;
        }

        long contentLength = end - start + 1;

        FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
        fileChannel.position(start);
        InputStreamResource resource = new InputStreamResource(Channels.newInputStream(fileChannel));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .body(resource);
    }

    @Override
    public void deleteFile(FileDto fileDto) throws IOException {
        Path fileLocation = Paths.get(fileDto.getFilePath()).toAbsolutePath().normalize();
        Files.delete(fileLocation);
        fileRepository.deleteById(fileDto.getId());
    }
    @Override
    public FileDto getFile(Long id) {
        File file = fileRepository.findById(id).orElse(null);
        return fileMapper.convertToDTO(file);
    }
    @Override
    public FileDto getFileBySubmission(Long id) {
        File file = fileRepository.findBySubmissionId(id);
        return fileMapper.convertToDTO(file);
    }
}
