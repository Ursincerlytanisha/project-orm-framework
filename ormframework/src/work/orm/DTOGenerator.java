package work.orm;
import java.sql.*;
import work.orm.db.*;
import work.orm.exceptions.*;
public class DTOGenerator
{
public static void main(String[] args)
{
//java -cp c:\javaeg\ormframework\lib\* DTOGenerator target-path:c:\bla\bla package-name: com.book.invoice
if(args.length !=2)
{
System.out.println("Pass target-path:dirName package-name:package_name as arguments");
System.exit(1);
}
try
{
String targetName=null;
String packageName=null;
String key=null;
int index;
for(int i=0; i<args.length;i++)
{
index=args[i].indexOf(":");
if(index!= -1)
{
key=args[i].substring(0,index);
if(key.equalsIgnoreCase("target-path")) targetName=args[i].substring(index+1);
else if(key.equalsIgnoreCase("package-name")) packageName=args[i].substring(index+1);
}
}
boolean printSomething=false;
if(targetName==null || packageName==null)
{
System.out.print("Specify: ");
if(targetName==null)
{
printSomething=true;
System.out.print("targetName");
}
if(packageName==null)
{
if(printSomething) System.out.print(" and ");
System.out.print("packageName");
}
System.out.print("\n");
System.exit(1);
}
Connection connection=DBConnector.connect();
Database database=DatabaseUtil.getDatabase(connection);
File targetDirectory= new File(targetPath);
if(targetDirectory.exists()==false) 
{
if(!targetDirectory.mkdirs()) //os cannnot create directory, it returns false
{
throw new IOException("Unable to create:" +targetDirectory);
}
}
List<Table> tables=database.getTables();
for(int i=0; i<
getClassName()
}catch(Throwable throwable)
{
System.out.println(throwable.getMessage());
}
}
}