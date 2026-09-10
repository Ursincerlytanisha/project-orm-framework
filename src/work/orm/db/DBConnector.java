package work.orm.db;
import java.sql.*;
import java.io.*;
import java.util.*;
public class DBConnector 
{
private DBConnector(){}
static public Connection connection=null;
public static Connection connect()
{
try
{
File file= new File("db.conf");
if(file.exists()==false) throw new Exception("db.conf is not exists");
Map<String,String> keyValue= new HashMap<>();
String line;
String splits[];
RandomAccessFile randomAccessFile= new RandomAccessFile(file, "rw");
if(randomAccessFile.length()==0) 
{
randomAccessFile.close();
throw new Exception("db.conf is empty");
}
while(randomAccessFile.getFilePointer() < randomAccessFile.length())
{
line= randomAccessFile.readLine();
splits=line.split("=");
if(splits.length==2)
{
keyValue.put(splits[0].trim().toUpperCase(), splits[1].trim());
}
}
randomAccessFile.close();
String protocol=keyValue.get("PROTOCOL");
if(protocol==null) throw new Exception("protocol is missing");
String driver=keyValue.get("DRIVER");
if(driver==null) throw new Exception("driver is missing");
String server=keyValue.get("SERVER");
if(server==null) throw new Exception("server is missing");
String port=keyValue.get("PORT");
if(port==null) throw new Exception("port is missing");
String database=keyValue.get("DATABASE");
if(database==null) throw new Exception("database is missing");
String username=keyValue.get("USERNAME");
if(username==null) throw new Exception("username is missing");
String password=keyValue.get("PASSWORD");
if(password==null) throw new Exception("password is missing");
Class.forName(driver);
connection=DriverManager.getConnection(protocol+"://"+server+":"+port+"/"+database, username, password);
}
catch(Exception e)
{
System.out.println(e);
System.exit(1);
}
return connection;
}
}