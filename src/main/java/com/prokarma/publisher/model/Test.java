package com.prokarma.publisher.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Test {
    @NotEmpty(message = "Not empty")
    @Size(max = 5, message = "size > 5")
    String test;
}
