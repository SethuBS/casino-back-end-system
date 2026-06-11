package com.rank.interactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CasinoBackEndSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CasinoBackEndSystemApplication.class, args);
    }

}
