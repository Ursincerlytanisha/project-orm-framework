package work.orm.db;
import java.util.*;
public class Table
{
private String name;
private List<TableColumn> tableColumns;
private List<ForeignKey> foreignKeys;
public Table(String name, List<TableColumn> tableColumns, List<ForeignKey>foreignKeys)
{
this.name=name;
this.tableColumns=tableColumns;
this.foreignKeys=foreignKeys;
}
public Table()
{
this.name="";
this.tableColumns=null;
this.foreignKeys=null;
}
public void setName(String name)
{
this.name = name;
}
public String getName()
{
return this.name;
}
public void setTableColumns(List<TableColumn> tableColumns)
{
this.tableColumns= tableColumns;
}
public List<TableColumn> getTableColumns()
{
return this.tableColumns;
}
public void setForeignKeys(List<ForeignKey> foreignKeys)
{
this.foreignKeys=foreignKeys;
}
public List<ForeignKey> getForeignKeys()
{
return this.foreignKeys;
}

}