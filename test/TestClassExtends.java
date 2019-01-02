public class TestClassExtends extends TestClass
{
	private String otherValue;

	public TestClassExtends(String value, String otherValue) {
		super(value);
		this.otherValue = otherValue;
	}

	public String getOtherValue() {
		return otherValue;
	}

	public void setOtherValue(String otherValue) {
		this.otherValue = otherValue;
	}
}
