package work.orm.db;
import java.util.*;
import java.sql.*;
import work.orm.exceptions.*;
public class DatabaseUtil
{
private DatabaseUtil(){}
private static Database database=new Database();
public static Database getDatabase(Connection connection) throws DBException
{
try
{
if(connection==null)
{
throw new DBException("Driver is not connected from Database");
}
DatabaseMetaData databaseMetaData = connection.getMetaData();
String types[]={"TABLE"};
String tableName;
String columnName;
String columnType;
int columnWidth;
boolean isPrimaryKey=false;
boolean isAutoIncrement=false;
boolean isNullable=false;
boolean isUnique=false;
int precision;
String defaultValue;
List<Table> tables;
List<TableColumn> tableColumns;
Set<String> primaryKeys, uniqueColumns;
List<ForeignKey> foreignKeys;
List<ForeignKeyColumnMapping> foreignKeyColumnMappings;
ForeignKey foreignKey;
ForeignKeyColumnMapping foreignKeyColumnMapping;
String parentTableName;
String childTableColumnName;
String parentTableColumnName;
Map<String,ForeignKey> foreignKeyNameMap;
Table table;
TableColumn tableColumn;
//--------------------------fetch all tables-----------------
tables=new ArrayList<>();
try(ResultSet tableResultSet=databaseMetaData.getTables(connection.getCatalog(),null,"%", types)){
while(tableResultSet.next())
{
table = new Table();
tableName=tableResultSet.getString("TABLE_NAME");
if(tableName==null) continue;
tableName=tableName.trim();
table.setName(tableName);
//fetch columns for the current table
// Get PRIMARY KEY columns
// --------------------------------------------------
ResultSet primaryKeyResultSet=databaseMetaData.getPrimaryKeys(connection.getCatalog(),null,tableName);
primaryKeys = new java.util.HashSet<>();
while(primaryKeyResultSet.next())
{
primaryKeys.add(primaryKeyResultSet.getString("COLUMN_NAME").trim());
}
primaryKeyResultSet.close();

//get Foreign key------------------------
foreignKeys = new ArrayList<>();
try(ResultSet foreignKeyResultSet= databaseMetaData.getImportedKeys(connection.getCatalog(), null,tableName))
{
String foreignKeyName;
int fk_id=0;
foreignKeyNameMap= new HashMap<>();
while(foreignKeyResultSet.next())
{
foreignKeyColumnMappings = new ArrayList<>();
foreignKeyName=foreignKeyResultSet.getString("FK_NAME");
childTableColumnName=foreignKeyResultSet.getString("FKCOLUMN_NAME");
parentTableColumnName=foreignKeyResultSet.getString("PKCOLUMN_NAME");
parentTableName=foreignKeyResultSet.getString("PKTABLE_NAME");
int sequence=foreignKeyResultSet.getInt("KEY_SEQ");

if(foreignKeyName==null)
{
if(sequence==1) fk_id++;  //seq is 1 when FK is non composite , sometimes foreignKeyName is not generated
//by DBMS , so we explicitly create foreignKeyName
foreignKeyName="FKid_"+fk_id;
}

if(foreignKeyNameMap.containsKey(foreignKeyName))
{
foreignKey=foreignKeyNameMap.get(foreignKeyName);
}
else
{
foreignKey=new ForeignKey();
foreignKey.setParentTableName(parentTableName);
foreignKey.setForeignKeyColumnMappings(new ArrayList<>());
foreignKeyNameMap.put(foreignKeyName, foreignKey);
foreignKeys.add(foreignKey);
}
foreignKeyColumnMapping=new ForeignKeyColumnMapping(childTableColumnName, parentTableColumnName);
foreignKey.addForeignKeyColumnMapping(foreignKeyColumnMapping);
}

}// try ends
// Get UNIQUE columns
// --------------------------------------------------
ResultSet indexResultSet = databaseMetaData.getIndexInfo(connection.getCatalog(),null,tableName,true,false);
// unique indexes only
uniqueColumns = new java.util.HashSet<>();
while(indexResultSet.next()) 
{
String indexName = indexResultSet.getString("INDEX_NAME");
String vcolumnName = indexResultSet.getString("COLUMN_NAME");
// Ignore PRIMARY KEY
if(indexName != null && !"PRIMARY".equalsIgnoreCase(indexName) && vcolumnName!=null) 
{
uniqueColumns.add(vcolumnName);
}
}
indexResultSet.close();
// --------------------------------------------------
//Get COLUMN information
// --------------------------------------------------
tableColumns=new ArrayList<>();
ResultSet columnResultSet=databaseMetaData.getColumns(connection.getCatalog(),null,tableName,null);
while(columnResultSet.next())
{
tableColumn=new TableColumn();
columnName =columnResultSet.getString("COLUMN_NAME");
columnType =columnResultSet.getString("TYPE_NAME");
columnWidth =columnResultSet.getInt("COLUMN_SIZE");
precision= columnResultSet.getInt("DECIMAL_DIGITS");
isPrimaryKey =primaryKeys.contains(columnName);
String vautoIncrement =columnResultSet.getString("IS_AUTOINCREMENT");
isAutoIncrement ="YES".equalsIgnoreCase(vautoIncrement);
isUnique =uniqueColumns.contains(columnName);

boolean hasNotNullConstraintApplied;
if(columnResultSet.getString("IS_NULLABLE").equalsIgnoreCase("YES")==true) hasNotNullConstraintApplied=false;
else hasNotNullConstraintApplied=true;

tableColumn.setName(columnName);
tableColumn.setWidth(columnWidth);
tableColumn.setPrecision(precision);
tableColumn.setType(columnType);
tableColumn.setHasPrimaryKeyConstraintApplied(isPrimaryKey);
tableColumn.setIsAutoIncrementable(isAutoIncrement);
tableColumn.setHasNotNullConstraintApplied(hasNotNullConstraintApplied);
tableColumn.setHasUniqueConstraintApplied(isUnique);
tableColumns.add(tableColumn);
}//inner while
table.setTableColumns(tableColumns);
table.setForeignKeys(foreignKeys);
tables.add(table);
columnResultSet.close();
}//outerWhile
database.setTables(tables);
}
}
catch(SQLException sqlException)
{
throw new DBException(sqlException.getMessage());
}
catch(Exception exception)
{
throw new DBException(exception.getMessage());
}
return database;
}
}