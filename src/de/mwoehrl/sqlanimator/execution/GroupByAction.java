package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Row;

public abstract class GroupByAction extends AbstractAction {

	protected final ArrayList<ArrayList<Row>> bucketList;
	
	protected GroupByAction(Query query, ArrayList<ArrayList<Row>> bucketList) {
		super(query);
		this.bucketList = bucketList;
	}
}
