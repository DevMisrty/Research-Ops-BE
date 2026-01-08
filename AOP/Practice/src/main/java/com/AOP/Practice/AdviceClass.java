package com.AOP.Practice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
class AdviceClass {

//    @Before("execution( String com.AOP.Practice.*.*(..))")
//    public void advice1(){
//        log.error("calling before the request");
//    }
//
//    @After("execution( String com.AOP.Practice.*.*(..))")
//    public void advice2(){
//        log.error("calling after the request");
//    }

    @Around("execution( String com.AOP.Practice.*.*(..)")
    public Object advice(ProceedingJoinPoint jp) throws Throwable {
        log.info("{}around the method", jp.getSignature());
        jp.proceed();
        log.info("After the method" + jp.getSignature());
        return new Object();
    }
}
