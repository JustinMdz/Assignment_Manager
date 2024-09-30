package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.File;
import org.una.programmingIII.Assignment_Manager.Repository.FileRepository;
import org.una.programmingIII.Assignment_Manager.Service.FileService;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@AllArgsConstructor
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

    @Override
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
