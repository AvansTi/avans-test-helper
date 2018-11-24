package nl.avans.testhelper;

public class SearchNode extends SearchResult
{
	SearchResult parent;
	int index;

	public SearchNode(SearchResult parent, String data, int index)
	{
		super(data);
		this.parent = parent;
		this.index = index;
	}

}
