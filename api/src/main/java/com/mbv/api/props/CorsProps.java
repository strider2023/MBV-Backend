package com.mbv.api.props;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CorsProps {
    // Default allowed origin domains are 'maybrightventures.com' and 'localhost'
    private Set<String> allowedOrigins = new HashSet<>(Arrays.asList("maybrightventures.com", "localhost"));

    public Set<String> getAllowedOrigins() {
        return allowedOrigins;
    }
}
