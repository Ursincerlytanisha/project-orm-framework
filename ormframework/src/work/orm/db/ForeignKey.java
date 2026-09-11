package work.orm.db;
import java.util.*;
public class ForeignKey implements java.io.Serializable
{
private String parentTableName;
private List<ForeignKeyColumnMapping> foreignKeyColumnMappings;
public ForeignKey(String parentTableName,List<ForeignKeyColumnMapping> foreignKeyColumnMappings)
{
this.parentTableName=parentTableName;
this.foreignKeyColumnMappings=foreignKeyColumnMappings;
}
public ForeignKey()
{
this.parentTableName="";
this.foreignKeyColumnMappings=null;
}
public void setParentTableName(String parentTableName)
{
this.parentTableName=parentTableName;
}
public String getParentTableName()
{
return this.parentTableName;
}
public void addForeignKeyColumnMapping(ForeignKeyColumnMapping foreignKeyColumnMapping)
{
this.foreignKeyColumnMappings.add(foreignKeyColumnMapping);
}
public void setForeignKeyColumnMappings(List<ForeignKeyColumnMapping> foreignKeyColumnMappings)
{
this.foreignKeyColumnMappings=foreignKeyColumnMappings;
}

public List<ForeignKeyColumnMapping> getForeignKeyColumnMappings()
{
return this.foreignKeyColumnMappings;
}
}
