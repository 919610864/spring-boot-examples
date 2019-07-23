package com.neo.paralle;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.*;

/**
 * 并行化任务工具类
 * Created by aa on 2017-08-21.
 */
public class Task {

    private static Logger logger = LoggerFactory.getLogger(Task.class);
    private static volatile int autoId = 0;
    private static ExecutorService threadPool = null;
    private static final int CREATED = 0;

    private int id;
    private TaskStatus status;
    private int taskType = 1;
    private Action action;
    private ActionWithArgs actionWithArgs;
    private Func func;
    private FuncWithArgs funcWithArgs;
    private Object[] args;
    private Future future;
    private Object taskResult;
    private Exception exception;

    /**
     * 初始化工具类
     */
    static {
        int threadCount = Runtime.getRuntime().availableProcessors() + 1;
        threadPool = Executors.newFixedThreadPool(threadCount);
        logger.info("并行初始化，设置线程数：{}", threadCount);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("并行工具类退出线程池");
            threadPool.shutdown();
        }));
    }

    /**
     * 私有化构造函数，避免被直接new出来
     *
     * @param action 要执行的任务
     */
    private Task(Action action) {
        this.action = action;
        this.taskType = 1;
        this.run();
    }

    /**
     * 私有化构造函数，避免被直接new出来
     *
     * @param action 要执行的任务
     * @param args   要传递的参数
     */
    private Task(ActionWithArgs action, Object... args) {
        this.actionWithArgs = action;
        this.taskType = 2;
        this.args = getArgs(args);
        this.run();
    }

    /**
     * 私有化构造函数，避免被直接new出来
     *
     * @param func 要执行的任务
     */
    private Task(Func func) {
        this.func = func;
        this.taskType = 3;
        this.run();
    }

    /**
     * 私有化构造函数，避免被直接new出来
     *
     * @param func 要执行的任务
     * @param args 要传递的参数
     */
    private Task(FuncWithArgs func, Object... args) {
        this.funcWithArgs = func;
        this.taskType = 4;
        this.args = getArgs(args);
        this.run();
    }

    /**
     * 开始一个无参的没有返回值的任务
     *
     * @param action 要执行的任务
     * @return
     */
    public static Task start(Action action) {
        Task task = new Task(action);
        return task;
    }

    /**
     * 开始一个有参数的没有返回值的任务
     *
     * @param action 要执行的任务
     * @param args   要传递给任务的参数
     * @return
     */
    public static Task startWithArgs(ActionWithArgs action, Object... args) {
        Task task = new Task(action, args);
        return task;
    }

    /**
     * 开始一个无参的且有返回值的任务
     *
     * @param func 要执行的任务
     * @return
     */
    public static Task startFunc(Func func) {
        Task task = new Task(func);
        return task;
    }

    /**
     * 开始一个有参的且有返回值的任务
     *
     * @param func 要执行的任务
     * @param args 要传递给任务的参数
     * @return
     */
    public static Task startFuncWithArgs(FuncWithArgs func, Object... args) {
        Task task = new Task(func, args);
        return task;
    }

    /**
     * 加入线程池并执行
     *
     * @return
     */
    private Task run() {
        if (this.id < 1) {
            this.id = ++autoId;
        }
        status = TaskStatus.CREATED;
        Callable callable = null;
        switch (this.taskType) {
            case 1:
                callable = () -> {
                    this.status = TaskStatus.RUNNING;
                    try {
                        this.action.doAction();
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.exception = e;
                    } finally {
                        this.status = TaskStatus.FINISHED;
                    }
                    return null;
                };
                break;
            case 2:
                callable = () -> {
                    this.status = TaskStatus.RUNNING;
                    try {
                        this.actionWithArgs.doAction(this.args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.exception = e;
                    } finally {
                        this.status = TaskStatus.FINISHED;
                    }
                    return null;
                };
                break;
            case 3:
                callable = () -> {
                    this.status = TaskStatus.RUNNING;
                    Object runResult = null;
                    try {
                        runResult = this.func.doFunc();
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.exception = e;
                    } finally {
                        this.status = TaskStatus.FINISHED;
                    }
                    return runResult;
                };
                break;
            case 4:
                callable = () -> {
                    this.status = TaskStatus.RUNNING;
                    Object runResult = null;
                    try {
                        runResult = this.funcWithArgs.doFunc(this.args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.exception = e;
                    } finally {
                        this.status = TaskStatus.FINISHED;
                    }
                    return runResult;
                };
                break;
        }
        CompletionService completionService = new ExecutorCompletionService(threadPool);
        future = completionService.submit(callable);
        status = TaskStatus.QUEUED;
        return this;
    }

    /**
     * 等待任务执行结束并返回结果
     *
     * @return
     */
    public Object await() {
        try {
            taskResult = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("并行任务等待时发生异常：{}", e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error("并行任务等待时发生异常：{}", e);
        }
        return taskResult;
    }

    /**
     * 取消任务的执行
     *
     * @param mayInterruptIfRunning 是否强制中断
     * @return
     */
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    /**
     * 获取任务Id
     *
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * 获取任务状态
     *
     * @return
     */
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * 获取任务的执行结果
     *
     * @return
     */
    public Object getResult() {
        return await();
    }

    /**
     * 将可变参数转为数组
     *
     * @param args 可变参数
     * @return
     */
    public static Object[] getArgs(Object... args) {
        if (args == null || args.length < 1) {
            return new Object[0];
        }
        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = args[i];
        }
        return result;
    }

    /**
     * 等待指定的所有任务执行结束
     *
     * @param tasks 要等待的任务
     */
    public static void waitAll(Task... tasks) {
        if (tasks != null && tasks.length > 0) {
            for (int i = 0; i < tasks.length; i++) {
                tasks[i].await();
            }
        }
    }

    /**
     * 等待指定的任意一个任务执行结束，并返回执行结束的任务
     *
     * @param tasks 要等待的任务
     * @return
     */
    public static Task waitAny(Task... tasks) {
        throw new NotImplementedException();
    }

    /**
     * 获取任务执行期间捕获的异常
     * @return
     */
    public Exception getException() {
        return exception;
    }

    /**
     * 任务是否无异常结束
     * @return
     */
    public boolean isSuccess() {
        return exception == null;
    }
}