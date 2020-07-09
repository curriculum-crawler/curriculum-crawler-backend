package edu.harvard.cscis71.curriculum.io;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class StorageService {

    public void store(InputStream in, URI target) throws IOException {
        Files.copy(in, Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
    }

    public void delete(URI target) throws IOException {
        Files.delete(Paths.get(target));
    }
}
