package org.una.programmingIII.Assignment_Manager.Service.Implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Repository.FileRepository;
import org.una.programmingIII.Assignment_Manager.Service.FileService;
@AllArgsConstructor
@Service
public class FileServiceImplementation implements FileService {
    private final FileRepository fileRepository;

}
