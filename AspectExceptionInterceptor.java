package com.example.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class AspectExceptionInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AspectExceptionInterceptor.class);
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @AfterThrowing(pointcut = "execution(* com.example.demo..*(..)) && @annotation(com.example.demo.RetryProcess)")
    public void tryAgain(JoinPoint point) throws InterruptedException {
        try {
            Object target = point.getTarget();
            MethodSignature methodSignature = (MethodSignature)point.getSignature();
            RetryProcess retryProcess = methodSignature.getMethod().getAnnotation(RetryProcess.class);

            if(atomicInteger.intValue() < retryProcess.value()){
                int i = atomicInteger.incrementAndGet();
                TimeUnit.SECONDS.sleep(1);

                logger.info("开始重试第"+i+"次");
                MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) point;
                methodPoint.proceed();
            }

        }catch (Throwable throwable){
            tryAgain(point);
        }
    }
}
