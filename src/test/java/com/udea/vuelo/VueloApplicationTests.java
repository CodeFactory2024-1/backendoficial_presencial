package com.udea.vuelo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VueloApplicationTests {

    @Test
    void contextLoads() {
        boolean result = true;
        assertThat(result).isTrue();
    }

}
