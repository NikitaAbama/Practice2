package org.example.finance.source;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class FileTransactionDataSource implements TransactionDataSource {
    private final Path path;

    @Override
    public Reader openReader() throws IOException {
        return Files.newBufferedReader(path, StandardCharsets.UTF_8);
    }
}
