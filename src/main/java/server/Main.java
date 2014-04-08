package server;

import com.sample.Drools;

public class Main {
	public static final void main(String[] args) {
		final Drools drools = new Drools();
		new ESServer(5000, new ExpertSystem() {
			@Override
			public String getAnswer(String question) {
				String answer = drools.getAnswer(question);
				if (answer.isEmpty()) {
					return "ไม่มีข้อมูลที่ค้นหา";
				}
//				System.out.println(answer);
				return answer;
			}
		});
	}
}
