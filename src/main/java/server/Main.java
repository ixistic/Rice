package server;

import com.sample.Drools;

public class Main {
	public static final void main(String[] args) {
		int port = 5000;
		if ( args.length >= 1 ) {
			port = Integer.parseInt(args[0]);
		}
		
		final Drools drools = new Drools();
		new ESServer(port, new ExpertSystem() {
			@Override
			public String getAnswer(String question) {
				String answer = drools.getAnswer(question);
				if (answer.isEmpty()) {
					return "ไม่มีข้อมูลที่ค้นหา";
				}
				System.out.println(answer);
				return answer;
			}

			@Override
			public void reInitiate() {
				drools.reInitiate();				
			}
		});
	}
}
