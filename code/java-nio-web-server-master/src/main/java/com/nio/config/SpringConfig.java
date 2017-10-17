package com.nio.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Spring configuration for the application. Used by spring to find beans to
 * manage and their dependencies.
 *
 * @author Jay Paulynice (jay.paulynice@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = { SpringConfig.BASE_PACKGE },
        excludeFilters = @Filter(type = FilterType.REGEX, pattern = SpringConfig.EXCL_PACKAGE))
public class SpringConfig {
    /** base packages to start scanning beans */
    protected static final String BASE_PACKGE = "com.nio.http";

    /** exclude the config classes from being picked up by spring */
    protected static final String EXCL_PACKAGE = "com.nio.config.*";
}