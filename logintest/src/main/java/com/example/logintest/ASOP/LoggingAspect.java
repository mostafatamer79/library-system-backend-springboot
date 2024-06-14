package com.example.logintest.ASOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.logintest.service.UserService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("LoggingAspect: Method " + joinPoint.getSignature() + " is about to be executed.");
    }

    @After("execution(* com.example.logintest.service.UserService.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("LoggingAspect: Method " + joinPoint.getSignature() + " is executed.");
    }

    @Around("execution(* com.example.logintest.service.UserService.*(..))")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("PerformanceMonitoringAspect: Method " + joinPoint.getSignature() +
                " executed in " + executionTime + " milliseconds");
        return result;
    }


    @Before("execution(* com.example.logintest.service.BookService.*(..))")
    public void logBookServiceBefore(JoinPoint joinPoint) {
        System.out.println("LoggingAspect: Method " + joinPoint.getSignature() + " in BookService is about to be executed.");
    }

    @After("execution(* com.example.logintest.service.BookService.*(..))")
    public void logBookServiceAfter(JoinPoint joinPoint) {
        System.out.println("LoggingAspect: Method " + joinPoint.getSignature() + " in BookService is executed.");
    }

    @Around("execution(* com.example.logintest.service.BookService.*(..))")
    public Object monitorPerformanceBookService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("PerformanceMonitoringAspect: Method " + joinPoint.getSignature() +
                " in BookService executed in " + executionTime + " milliseconds");
        return result;
    }
}



