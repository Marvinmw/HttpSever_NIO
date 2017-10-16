package com.nio.http;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.nio.config.SpringConfig;

@ContextConfiguration(classes = SpringConfig.class)
public abstract class BaseSpringTest extends AbstractTestNGSpringContextTests {
    // nothing to see here
}
