package nl.avans.testhelper;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;

public class SearchResult extends ArrayList<SearchNode>
{
	String data;

	public SearchResult(String data) {
		this.data = data;
	}

	int[] findIndices(String substring, int startIndex)
	{
		ArrayList<Integer> indices = new ArrayList<>();
		int i = startIndex;
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
		SearchResult result = new SearchResult(data);

		for(SearchNode n : this) {
			int[] indices = n.findIndices(substring, 0);
			for(int index : indices)
				result.add(new SearchNode(this, n.data, index));
		}
		assertFalse(substring + " not found", result.isEmpty());
		return result;
	}


	public SearchResult then(String substring)
	{
		SearchResult result = new SearchResult(data);

		for(SearchNode n : this) {
			int[] indices = n.findIndices(substring, n.index);
			for(int index : indices)
				result.add(new SearchNode(this, n.data, index));
		}
		assertFalse(substring + " not found", result.isEmpty());
		return result;
	}

	public SearchResult line()
	{
		SearchResult result = new SearchResult(data);

		for(SearchNode n : this) {

			int lineEnd = data.indexOf("\n", n.index);
			if (lineEnd == -1)
				lineEnd = data.length();
			else
				lineEnd++;

			int lineStart = data.lastIndexOf("\n", lineEnd - 2);
			if(lineStart == -1)
				lineStart = 0;

			String line = data.substring(lineStart, lineEnd);
			result.add(new SearchNode(this, line, 0));
		}
		return result;
	}
}
