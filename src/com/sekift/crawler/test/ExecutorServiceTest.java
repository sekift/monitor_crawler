package com.sekift.crawler.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author:Administrator
 * @time:2015年4月29日 上午11:03:43
 * @version:
 */
public class ExecutorServiceTest {

	private static ExecutorService threadpool = newExecutorService(2, 5, 60, "数据");

	public void setAsynQueue(final String str) {
		threadpool.execute(new Runnable() {
			public void run() {
				listShow(str);
				System.out.println(str + ";" + Thread.currentThread());
			}
		});
	}

	public void listShow(String str) {
		WebCatchTest test = new WebCatchTest(str);
		test.run();
	}

	public static void main(String[] args) {
		ExecutorServiceTest tt = new ExecutorServiceTest();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				String str = "http://www.lanrenzhijia.com/jquery/list_5_" + i + j + ".html";
				tt.setAsynQueue(str);
			}
			// SleepUtil.sleepBySecond(1, 1);
		}

		// 休眠一段时间后再退出
		// SleepUtil.sleepByMinute(10, 10);
	}

	/**
	 * 根据参数创建执行者服务
	 * 
	 * @param coreSize
	 *            -- 线程池核心线程数
	 * @param maxSize
	 *            -- 线程池最大线程数
	 * @param keepAlive
	 *            -- 线程最大空闲时间(单位:秒)
	 * @param nameTemplate
	 *            -- 线程名称模板
	 * @return -- 执行者服务
	 */
	public static ExecutorService newExecutorService(int coreSize, int maxSize, int keepAlive, final String nameTemplate) {
		/**
		 * 所有 BlockingQueue 都可用于传输和保持提交的任务。可以使用此队列与池大小进行交互： 如果运行的线程少于
		 * corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。 如果运行的线程等于或多于
		 * corePoolSize，则 Executor 始终首选将请求加入队列，而不添加新的线程。
		 * 如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。
		 * 排队有三种通用策略： 
		 * 直接提交。 工作队列的默认选项是
		 * SynchronousQueue，它将任务直接提交给线程而不保持它们。在此，如果不存在可用于立即运行任务的线程
		 * ，则试图把任务加入队列将失败，因此会构造一个新的线程。此策略可以避免在处理可能具有内部依赖性的请求集时出现锁。直接提交通常要求无界
		 * maximumPoolSizes 以避免拒绝新提交的任务。当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。
		 * 
		 * 无界队列。使用无界队列（例如，不具有预定义容量的 LinkedBlockingQueue）将导致在所有 corePoolSize
		 * 线程都忙时新任务在队列中等待。这样，创建的线程就不会超过 corePoolSize。（因此，maximumPoolSize
		 * 的值也就无效了。）当每个任务完全独立于其他任务，即任务执行互不影响时，适合于使用无界队列；例如，在 Web
		 * 页服务器中。这种排队可用于处理瞬态突发请求，当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。
		 * 
		 * 有界队列。当使用有限的 maximumPoolSizes 时，有界队列（如
		 * ArrayBlockingQueue）有助于防止资源耗尽，但是可能较难调整和控制
		 * 。队列大小和最大池大小可能需要相互折衷：使用大型队列和小型池可以最大限度地降低 CPU
		 * 使用率、操作系统资源和上下文切换开销，但是可能导致人工降低吞吐量。如果任务频繁阻塞（例如，如果它们是 I/O
		 * 边界），则系统可能为超过您许可的更多线程安排时间。使用小型队列通常要求较大的池大小，CPU
		 * 使用率较高，但是可能遇到不可接受的调度开销，这样也会降低吞吐量。
		 */
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(100);// new
		// SynchronousQueue<Runnable>();
		ThreadFactory fac = new ThreadFactory() {
			// 创建一个新的线程, 同时设置它的名称和daemon模式
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setName(nameTemplate + "@[" + System.identityHashCode(t) + "]");
				t.setDaemon(false);// 控制是否后台进程
				return t;
			}
		};
		return new ThreadPoolExecutor(coreSize, maxSize, keepAlive, TimeUnit.SECONDS, queue, fac);
	}
}
