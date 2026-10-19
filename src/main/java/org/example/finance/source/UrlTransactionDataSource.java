package org.example.finance.source;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class UrlTransactionDataSource implements TransactionDataSource {
    private final String address;

    @Override
    public Reader openReader() throws IOException {
        return new InputStreamReader(URI.create(address).toURL().openStream(), StandardCharsets.UTF_8);
    }
}
