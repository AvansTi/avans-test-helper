import nl.avans.testhelper.ContainOrderTester;
import org.junit.Test;

public class TestContain {

	@Test
	public void testContains()
	{
		String[] data = new String[] {
			"Year 1",
			"Savings (initial 500.0) - 472.5",
			"Savings (initial 1500.0) - 1522.5",
			"Deposit (initial 500.0) - 495.0",
			"Deposit (initial 1500.0) - 1495.0",
			"",
			"Year 2",
			"Savings (initial 500.0) - 443.63",
			"Savings (initial 1500.0) - 1546.13",
			"Deposit (initial 500.0) - 490.0",
			"Deposit (initial 1500.0) - 1490.0",
			"",
			"Year 3",
			"Savings (initial 500.0) - 413.31",
			"Savings (initial 1500.0) - 1570.94",
			"Deposit (initial 500.0) - 485.0",
			"Deposit (initial 1500.0) - 1485.0"
		};

		new ContainOrderTester(data).contains("Year 1").then("Savings").line().then("initial 500.0").then("472.5");


		new ContainOrderTester(data).contains("Year 3").then("Deposit").line().then("initial 1500.0").then("1485.0");


	}


}
