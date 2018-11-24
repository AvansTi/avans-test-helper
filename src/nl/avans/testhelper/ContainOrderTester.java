package nl.avans.testhelper;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class ContainOrderTester {
	private String data;

	public ContainOrderTester(String[] lines) {
		data = String.join("\n", lines);
	}

	private int[] findIndices(String substring)
	{
		ArrayList<Integer> indices = new ArrayList<>();
		int i = 0;
		while(i > -1)
		{
			i = data.indexOf(substring, i);
			if(i > -1) {
				indices.add(i);
				i++;
			}
		}
		return indices.stream().mapToInt(ii -> ii).toArray();
	}


	public SearchResult contains(String substring)
	{
		int[] indices = findIndices(substring);
		assertTrue("Text " + substring + " not found", indices.length > 0);

		SearchResult result = new SearchResult(data);
		for(int index : indices)
		{
			result.add(new SearchNode(null, data, index));
		}
		return result;
	}
}


