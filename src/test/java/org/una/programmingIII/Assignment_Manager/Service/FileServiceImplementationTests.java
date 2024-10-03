/*package org.una.programmingIII.Assignment_Manager.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.una.programmingIII.Assignment_Manager.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager.Mapper.GenericMapperFactory;
import org.una.programmingIII.Assignment_Manager.Model.File;
import org.una.programmingIII.Assignment_Manager.Repository.FileRepository;
import org.una.programmingIII.Assignment_Manager.Service.Implementation.FileServiceImplementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplementationTests {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private GenericMapperFactory mapperFactory;

    @Mock
    GenericMapper<File, FileDto> fileMapper;
    @InjectMocks
    private FileServiceImplementation fileServiceImplementation;
    FileDto fileDto;
    File file;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fileDto = new FileDto();
        fileDto.setId(1L);
        fileDto.setName("testFile.txt");
        fileDto.setFileSize(100L);
        fileDto.setSubmission(null);
        fileDto.setFilePath("/uploads/testFile.txt");

        file = new File();
        file.setId(1L);
        file.setName("testFile.txt");
        file.setFilePath("/uploads/testFile.txt");
        ReflectionTestUtils.setField(fileServiceImplementation, "uploadDir", "uploadDir");
        fileServiceImplementation = new FileServiceImplementation(fileRepository, mapperFactory);
    }
 @Test
void saveFileChunkTest() throws IOException {
    fileDto.setName("testFile.txt");
    MockMultipartFile multipartFile = new MockMultipartFile("file", "testFile.txt", "text/plain", "Test Content".getBytes());

     Path tempDir = Files.createTempDirectory("test_chunks");
     Path fileChunkStorageLocation = tempDir.resolve(fileDto.getName() + "_chunks").normalize();
     Files.createDirectories(fileChunkStorageLocation);
     Path targetLocation = fileChunkStorageLocation.resolve(fileDto.getName() + ".part1");

    fileServiceImplementation.saveFileChunk(multipartFile, fileDto, 1, 1);

    assertTrue(Files.exists(targetLocation));
    verify(fileRepository, times(1)).save(any(File.class));
}

    @Test
    void saveFileChunk_throwsIOExceptionWhenFileChunkIsNull() {
        FileDto fileDto = new FileDto();
        fileDto.setName("testFile");
        int chunkNumber = 1;
        int totalChunks = 3;

        assertThrows(IOException.class, () -> {
            fileServiceImplementation.saveFileChunk(null, fileDto, chunkNumber, totalChunks);
        });
    }

    @Test
    void saveFileChunk_createsDirectoriesIfNotExist() throws IOException {
        MultipartFile fileChunk = mock(MultipartFile.class);
        FileDto fileDto = new FileDto();
        fileDto.setName("testFile");
        int chunkNumber = 1;
        int totalChunks = 3;

        Path fileChunkStorageLocation = Paths.get("uploadDir", fileDto.getName() + "_chunks").toAbsolutePath().normalize();

        when(fileChunk.getInputStream()).thenReturn(mock(java.io.InputStream.class));

        fileServiceImplementation.saveFileChunk(fileChunk, fileDto, chunkNumber, totalChunks);

        assertTrue(Files.exists(fileChunkStorageLocation));
    }

    @Test
    void saveFileChunk_callsSaveFileWhenLastChunk() throws IOException {
        MultipartFile fileChunk = mock(MultipartFile.class);
        FileDto fileDto = new FileDto();
        fileDto.setName("testFile");
        int chunkNumber = 3;
        int totalChunks = 3;

        when(fileChunk.getInputStream()).thenReturn(mock(java.io.InputStream.class));
        doNothing().when(fileServiceImplementation).saveFile(fileDto, totalChunks, Paths.get("uploadDir", fileDto.getName() + "_chunks").toAbsolutePath().normalize());

        fileServiceImplementation.saveFileChunk(fileChunk, fileDto, chunkNumber, totalChunks);

        verify(fileServiceImplementation, times(1)).saveFile(fileDto, totalChunks, Paths.get("uploadDir", fileDto.getName() + "_chunks").toAbsolutePath().normalize());
    }

    @Test
    void downloadFileInChunks_returnsFileSuccessfully() throws IOException {

        when(fileRepository.findById(1L)).thenReturn(java.util.Optional.of(file));

        ResponseEntity<InputStreamResource> response = fileServiceImplementation.downloadFileInChunks(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
        assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_LENGTH));
    }

    @Test
    void downloadFileInChunks_throwsElementNotFoundExceptionWhenFileNotFound() {
        Long fileId = 1L;

        when(fileRepository.findById(fileId)).thenReturn(java.util.Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> {
            fileServiceImplementation.downloadFileInChunks(fileId);
        });
    }

    @Test
    void deleteFile_deletesFileSuccessfully() throws IOException {
        FileDto fileDto = new FileDto();
        fileDto.setId(1L);
        fileDto.setFilePath("/uploads/testFile.txt");

        Path fileLocation = Paths.get(fileDto.getFilePath()).toAbsolutePath().normalize();

        fileServiceImplementation.deleteFile(fileDto);

        verify(fileRepository, times(1)).deleteById(fileDto.getId());
        assertTrue(Files.notExists(fileLocation));
    }
}*/
