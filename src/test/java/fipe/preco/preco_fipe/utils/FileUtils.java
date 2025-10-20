package fipe.preco.preco_fipe.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class FileUtils {
    private final ResourceLoader resourceLoader;

    public String readResourceFile(String path) throws IOException {
        var filePath = resourceLoader.getResource("classpath:%s".formatted(path)).getFile().toPath();
        return new String(Files.readAllBytes(filePath));
    }
}
