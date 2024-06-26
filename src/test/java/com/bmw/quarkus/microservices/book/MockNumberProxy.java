package com.bmw.quarkus.microservices.book;

import io.quarkus.test.Mock;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RestClient
@Mock
public class MockNumberProxy implements NumberProxy{

    @Override
    public IsbnThirteen generateIsbnNumbers() {
        return IsbnThirteen.builder()
                .isbn13("13-:Mock")
                .build();
    }
}
