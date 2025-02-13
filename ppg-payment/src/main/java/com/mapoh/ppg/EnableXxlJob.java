package com.mapoh.ppg;

import com.mapoh.ppg.config.XxlJobConfig;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author bachmar
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({XxlJobConfig.class})
public @interface  EnableXxlJob {
}

