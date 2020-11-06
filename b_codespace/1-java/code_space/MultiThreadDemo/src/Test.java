
public class Test {
	public static void main(String[] args) {
		System.out.println("main start");
		test();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					System.out.println(Thread.currentThread().getName()+"in main run");
				}
				
			}
		}).start();
	
		try {
			Thread.sleep(10);
			System.out.println("main end");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void test() {
		System.out.println("test start");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					System.out.println(Thread.currentThread().getName()+"in test method run");
				}
				
			}
		}).start();
		System.out.println("test end");
	}
	
}
