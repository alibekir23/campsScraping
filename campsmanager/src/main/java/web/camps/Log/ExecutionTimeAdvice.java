package web.camps.Log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled:true}")
public class ExecutionTimeAdvice {

    @Around("@annotation(web.camps.Log.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws IOException, Throwable {
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endtime = System.currentTimeMillis();


        // FileWriter fileWriter = new FileWriter("test.txt");
        //  PrintWriter printWriter = new PrintWriter(fileWriter);
        //  printWriter.printf("Class Name: "+ point.getSignature().getDeclaringTypeName()+"\n"+". Method Name: "+ point.getSignature().getName()+"\n"+ ". Execution Time : " + (endtime-startTime) +"ms"+"\n", 1000);
        //printWriter.close();
        log.info("Class Name: " + point.getSignature().getDeclaringTypeName() + "\n" + ". Method Name: " + point.getSignature().getName() + "\n" + ". Execution Time : " + (endtime - startTime) + "ms" + "\n");

        return object;
    }
}