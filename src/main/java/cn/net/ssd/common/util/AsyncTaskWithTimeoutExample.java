package cn.net.ssd.common.util;

import java.util.concurrent.*;

public class AsyncTaskWithTimeoutExample {

public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(2); // 示例使用的线程池

    CompletableFuture<String> asyncTask = CompletableFuture.supplyAsync(() -> {
        try {
            System.out.println("asyncTask running...");
            // 模拟耗时操作，如网络请求、数据库查询等
            Thread.sleep(5000);
            System.out.println("asyncTask completed..."); // 修复拼写错误
        } catch (InterruptedException e) {
            // 如果任务被中断，重新设置中断状态，然后退出任务
            Thread.currentThread().interrupt();
            return "Task interrupted";
        }
        return "Task completed";
    }, executor);

    try {
        String result = asyncTask.get(2, TimeUnit.SECONDS); // 设置超时时间为2秒
        System.out.println("Result: " + result);
    } catch (TimeoutException e) {
        System.out.println("Timeout occurred, cancelling the task...");
        asyncTask.cancel(true); // 超时后取消任务
    } catch (InterruptedException | ExecutionException e) {
        System.out.println("Other exception caught: " + e.getMessage());
        e.printStackTrace();
    }

    // 关闭线程池
    executor.shutdown();
}

}
