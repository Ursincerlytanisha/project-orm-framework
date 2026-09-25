package work.orm;
import java.sql.*;
import java.io.*;
import java.util.*;
import work.orm.db.*;
import work.orm.exceptions.*;
public class DTOGenerator
{
private static Map<String, String> javaTypes;
private static Map<String, String> defaultValues;
static 
{
javaTypes= new HashMap<>();
javaTypes.put("INT", "int");
javaTypes.put("TINYINT", "byte");
javaTypes.put("BIGINT", "long");
javaTypes.put("BIT", "long");
javaTypes.put("SMALLINT", "short");
javaTypes.put("FLOAT", "float");
javaTypes.put("DOUBLE", "double");
javaTypes.put("DECIMAL","java.math.BigDecimal");
javaTypes.put("NUMERIC", "java.math.BigDecimal");
javaTypes.put("INTEGER", "java.lang.Integer");
javaTypes.put("MEDIUMINT", "java.lang.Integer");
javaTypes.put("CHAR", "java.lang.String");
javaTypes.put("VARCHAR", "java.lang.String");
javaTypes.put("TEXT", "java.lang.String");
javaTypes.put("DATE","java.sql.Date");
javaTypes.put("YEAR", "java.time.Year");
javaTypes.put("DATETIME", "java.sql.TimeStamp");
javaTypes.put("TIMESTAMP", "java.sql.TimeStamp");
javaTypes.put("BOOLEAN", "boolean");
javaTypes.put("TIME", "java.sql.Time");
javaTypes.put("BINARY", "byte[]");
javaTypes.put("VARBINARY", "byte[]");
javaTypes.put("BLOB", "java.sql.Blob");
javaTypes.put("CLOB", "java.sql.Clob");
javaTypes.put("JSON", "java.lang.String");
javaTypes.put("XML", "java.lang.String");
javaTypes.put("ENUM", "java.lang.Enum");
javaTypes.put("SET", "java.util.Set<String>");
javaTypes.put("VECTOR", "byte[]");

defaultValues= new HashMap<>();
defaultValues.put("int", "0");
defaultValues.put("byte", "0");
defaultValues.put("long", "0");
defaultValues.put("short", "0");
defaultValues.put("double","0.0");
defaultValues.put("float","0.0f");
defaultValues.put("java.math.BigDecimal","new java.math.BigDecimal(\"0.0\")");
defaultValues.put("java.lang.String", "\"\"");
defaultValues.put("java.lang.Integer", "new java.lang.Integer(\"0\")");
defaultValues.put("java.sql.Date", "new java.sql.Date()");
defaultValues.put("java.sql.TimeStamp", "null");
defaultValues.put("boolean", "false");
defaultValues.put("java.sql.Time","null");
defaultValues.put("java.time.Year","null");
defaultValues.put("byte[]","0");
defaultValues.put("java.sql.Blob","null");
defaultValues.put("java.sql.Clob","null");
defaultValues.put("java.lang.Enum","null");
defaultValues.put("java.util.Set<String>", "new HashSet<String>()");
}
public static void generateDTO(Database database, File packageFile, String packageName) throws Exception
{
try
{
String tableName=null;
String className=null;
String line;
String javaFileName="";
File javaFile;
List<TableColumn> columns;
Vector<String> properties=new Vector<>();
Vector<String> propertyTypes=new Vector<>();
Vector<String> primaryKeys=new Vector<>();
Vector<String> primaryKeyTypes=new Vector<>();
String javaProperty;
String javaPropertyType;
String classPointerName="";
RandomAccessFile randomAccessFile=null;
List<Table> tables= database.getTables();
if(tables.size()==0) throw new Exception("Nothing to generate");

for(Table table: tables)
{
tableName= table.getName();
className= getClassName(tableName);
classPointerName=className.substring(0,1).toLowerCase();
if(className.length()>1) classPointerName+=className.substring(1);
javaFileName= className+".java";
javaFile= new File(packageFile, javaFileName);
if(javaFile.exists()) javaFile.delete();
randomAccessFile=new RandomAccessFile(javaFile, "rw");
line="package "+packageName+";\n";
randomAccessFile.writeBytes(line);
line="import com.orm.annotation.*;\n";
randomAccessFile.writeBytes(line);
//annotation add 
line="public class "+className+" implements java.io.Serializable, Comparable\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
//property declaration starts
properties.clear();
propertyTypes.clear();
primaryKeys.clear();
primaryKeyTypes.clear();
columns= table.getTableColumns();
if(columns.size()==0) continue;
String defValue="";
System.out.println("table name: "+className);

for(TableColumn tableColumn: columns)
{
javaProperty=getPropertyName(tableColumn.getName());
properties.add(javaProperty);
javaPropertyType=javaTypes.get(tableColumn.getType());
propertyTypes.add(javaPropertyType);
line="private "+javaPropertyType+" "+javaProperty+";\n";
randomAccessFile.writeBytes(line);
if(tableColumn.getHasPrimaryKeyConstraintApplied())
{
primaryKeys.add(javaProperty);
primaryKeyTypes.add(javaPropertyType);
System.out.println("primary key:"+javaProperty);
}
}
//constructor
randomAccessFile.writeBytes("public "+className+"()\n");
randomAccessFile.writeBytes("{\n");
for(int i=0; i<properties.size(); i++)
{
defValue=defaultValues.get(propertyTypes.get(i));
randomAccessFile.writeBytes("this."+properties.get(i)+"="+defValue+";\n");
}
line="}\n";
randomAccessFile.writeBytes(line);
//parameterized constructor

line="public "+className+"(";
randomAccessFile.writeBytes(line);
int index=0;
while(index<properties.size())
{
line=propertyTypes.get(index)+" "+properties.get(index);
randomAccessFile.writeBytes(line);
if(index!=properties.size()-1) randomAccessFile.writeBytes(", ");
index++;
}
randomAccessFile.writeBytes(")\n");
randomAccessFile.writeBytes("{\n");
index=0;
while(index<properties.size())
{
line="this."+properties.get(index)+"="+properties.get(index)+";\n";
randomAccessFile.writeBytes(line);
index++;
}
randomAccessFile.writeBytes("}\n");
//parameterized construtor(leave auto increment fields)
index=0;
int counter=0;
for(; index<columns.size(); index++)
{
if(columns.get(index).getIsAutoIncrementable()) counter++;
}
if(counter< columns.size())   //means all the fields is not autoIncrement in  the table
{
line="public "+className+"(";
randomAccessFile.writeBytes(line);
line="";
index=0;
while(index<properties.size())
{
if(columns.get(index).getIsAutoIncrementable())
{
index++;
continue;
}
line+=", "+propertyTypes.get(index)+" "+properties.get(index);
index++;
}
line=line.substring(1); //skip the comma at zero index
randomAccessFile.writeBytes(line);
randomAccessFile.writeBytes(")\n");
randomAccessFile.writeBytes("{\n");

index=0;
for(String prop: properties)
{
if(columns.get(index).getIsAutoIncrementable())
{
randomAccessFile.writeBytes("this."+prop+"="+defaultValues.get(propertyTypes.get(index))+";\n");
}
else
{
line="this."+prop+"="+prop+";\n";
randomAccessFile.writeBytes(line);
}
index++;
}
randomAccessFile.writeBytes("}\n");
}
//setter getter
String setProp;
index=0;
for(String property:properties)
{
//setter
line="";
setProp=getSetterName(property);
line="public void "+setProp+"("+propertyTypes.get(index)+" "+property+")\n";
line+="{\n";
line+="this."+property+"="+property+";\n";
line+="}\n";
//getter
line+="public "+propertyTypes.get(index)+" "+getGetterName(property)+"()\n";
line+="{\n";
line+="return this."+property+";\n";
line+="}\n";
randomAccessFile.writeBytes(line);
index++;
}
//equals
line="public boolean equals("+className+" "+classPointerName+")\n";
line+="{\n";
randomAccessFile.writeBytes(line);
index=0;
String keyType="";
randomAccessFile.writeBytes("return ");
for(String key: primaryKeys)
{
System.out.println("key:" +key);
keyType=primaryKeyTypes.get(index);
if(isPrimitive(keyType)) 
{
randomAccessFile.writeBytes("this."+key+"=="+classPointerName+"."+key);
}
else
{
randomAccessFile.writeBytes("this."+key+".equals("+classPointerName+"."+key+")");
}
if(index< primaryKeys.size()-1)
{
randomAccessFile.writeBytes(" && ");
}
index++;
}
line=";\n";
line+="}\n";
randomAccessFile.writeBytes(line);
//compareTo
randomAccessFile.writeBytes("public int compareTo("+className+" "+classPointerName+")\n";
randomAccessFile.writeBytes("{\n");
randomAccessFile.writeBytes("return ");
index=0;
for(String key: primaryKeys)
{
if(isPrimitive(primaryKeyTypes.get(index))) 
{
randomAccessFile.writeBytes("this."+key+"-"+classPointerName+"."+key);
}
else
{
randomAccessFile.writeBytes("this."+key+".compareTo("+classPointerName+"."+key+")");
}
if(index< primaryKeys.size()-1)
{
randomAccessFile.writeBytes(" && ");
}
index++;
}
//hashCode

//toString
line="public String toString()\n";
line+="{\n";
randomAccessFile.writeBytes(line);
randomAccessFile.writeBytes("return "+className+"[");
index=0;
for(String prop: properties)
{
if(index>0)
{
randomAccessFile.writeBytes(", ");
}
randomAccessFile.writeBytes(prop+"=\" + "+prop);
index++;
}

}
randomAccessFile.writeBytes("}"); //class ends
randomAccessFile.close();
}catch(Throwable throwable)
{
System.out.println(throwable.getMessage());
System.exit(1);
}
}
private static String getSetterName(String prop)
{String property="";
property="set"+prop.substring(0,1).toUpperCase();
if(prop.length()>1) property+=prop.substring(1);
return property;
}
private static String getGetterName(String prop)
{
String property="get"+prop.substring(0,1).toUpperCase();
if(prop.length()>1) property+=prop.substring(1);
return property;
}

private static String getPropertyName(String columnName)
{
String propertyName="";
String splits[]= columnName.split("_");
for(String part: splits)
{
if(part.trim().length()==0) continue;
propertyName+=part.substring(0,1).toUpperCase();
if(columnName.length()>0) propertyName+=part.substring(1).toLowerCase();
}
if(columnName.length()>1) return propertyName.substring(0,1).toLowerCase()+propertyName.substring(1);
else
{
return propertyName.toLowerCase();
}
}
private static String getClassName(String tableName)
{
String className="";
String splits[]= tableName.split("_");
for(String part: splits)
{
if(part.trim().length()==0) continue;
className+=part.substring(0,1).toUpperCase();
if(tableName.length()>0) className+=part.substring(1).toLowerCase();
}
return className;
}
private static boolean isPrimitive(String propType)
{
if(propType.equals("int")) return true;
else if(propType.equals("long")) return true;
else if(propType.equals("short")) return true;
else if(propType.equals("byte")) return true;
else if(propType.equals("double")) return true;
else if(propType.equals("float")) return true;
else if(propType.equals("char")) return true;
else if(propType.equals("boolean")) return true;
else return false;
}
public static void main(String[] args)
{
if(args.length !=2)
{
System.out.println("Pass target-path:dirName package-name:package_name as arguments");
System.exit(1);
}
try
{
String targetPath=null;
String packageName=null;
String key=null;
int index;
for(int i=0; i<args.length;i++)
{
index=args[i].indexOf(":");
if(index!= -1)
{
key=args[i].substring(0,index);
if(key.equalsIgnoreCase("target-path")) targetPath=args[i].substring(index+1);
else if(key.equalsIgnoreCase("package-name")) packageName=args[i].substring(index+1);
}
}
if(targetPath==null || packageName==null)
{
boolean printSomething=false;
System.out.print("Specify: ");
if(targetPath==null)
{
printSomething=true;
System.out.print("target-name");
}
if(packageName==null)
{
if(printSomething) System.out.print(" and ");
System.out.print("package-name");
}
System.out.print("\n");
System.exit(1);
}
Connection connection=DBConnector.connect();
Database database=DatabaseUtil.getDatabase(connection);
File targetDirectory= new File(targetPath);
if(targetDirectory.exists()==false) // directory is not exists
{
if(!targetDirectory.mkdirs()) // and unable to create it,no permission to create it
{
throw new IOException("Unable to create:"+targetDirectory);
}
}
else if(!targetDirectory.isDirectory()) //path doesnot exist or it exists but is not a file
{
throw new IOException(targetDirectory+" is not a directory");
}
String packageNameString= packageName.replace(".", File.separator); 
File packageDirectory= new File(targetDirectory, packageNameString); // (parent, child) 
//create a new file instance from parent pathname and child pathname
if(packageDirectory.exists()==false)
{
if(!packageDirectory.mkdirs())
{
throw new Exception("Unable to create package directory:"+packageName);
}
}
else if(!packageDirectory.isDirectory())
{
throw new Exception("path exists and is not a directory");
}

generateDTO(database, packageDirectory, packageName);
connection.close();
}catch(Throwable throwable)
{
System.out.println(throwable.getMessage());
System.exit(1);
}
} 
}