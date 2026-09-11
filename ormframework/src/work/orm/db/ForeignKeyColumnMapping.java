package work.orm.db;
public class ForeignKeyColumnMapping
{
private String childTableColumnName;
private String parentTableColumnName;
public ForeignKeyColumnMapping(String childTableColumnName, String parentTableColumnName)
{
this.childTableColumnName=childTableColumnName;
this.parentTableColumnName=parentTableColumnName;
}
public void setChildTableColumnName(String childTableColumnName)
{
this.childTableColumnName=childTableColumnName;
}
public String getChildTableColumnName()
{
return this.childTableColumnName;
}
public void setParentTableColumnName(String parentTableColumnName)
{
this.parentTableColumnName=parentTableColumnName;
}
public String getParentTableColumnName()
{
return this.parentTableColumnName;
}
}