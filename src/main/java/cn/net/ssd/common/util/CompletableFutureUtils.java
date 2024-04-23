package cn.net.ssd.common.util;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * CompletableFuture 扩展工具
 */
public class CompletableFutureUtils {
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors());


    public static <T> CompletableFuture<T> failAfter(Duration duration){
        /// need a schedular executor
        final CompletableFuture<T> timer = new CompletableFuture<>();
        scheduler.schedule(()->{
            TimeoutException timeoutException = new TimeoutException("time out after "+ duration.getSeconds());
            return timer.completeExceptionally(timeoutException);
        },duration.toMillis(), TimeUnit.MILLISECONDS);
        return timer;
    }

    public static <T> CompletableFuture<T> within(CompletableFuture<T> taskFuture, Duration duration){
        CompletableFuture<T> timeoutWatcher = failAfter(duration);
        return taskFuture.applyToEither(timeoutWatcher, Function.identity());
    }


    public static void run(){
        /// do logical here
        CompletableFuture<String> slowlyPrinter = CompletableFuture.supplyAsync(
                ()->{
                    String msg = "say hello!";
                    try {
                        Integer  sl = ThreadLocalRandom.current().nextInt(20);
                        Thread.sleep(sl * 1000);
                        System.out.println("finish slow printer after + "+ sl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return msg;
                });

        CompletableFuture<String> chains = within(slowlyPrinter, Duration.ofSeconds(10));
        chains.thenAccept(System.out::println)
                .exceptionally((e)->{
                    System.out.println(e.getMessage());
                    return null;
                });

    }

    public static void main(String[] args){
        run();
    }
}

