package work.orm.db;
public class TableColumn
{
private String name;
private String type;
private int width;
private int precision;
private boolean isAutoIncrementable;
private boolean hasPrimaryKeyConstraintApplied;
private boolean hasUniqueConstraintApplied;
private boolean hasNotNullConstraintApplied;
//private String defaultValue;
public TableColumn()
{
this.name="";
this.type="";
this.width=0;
this.precision=0;
this.isAutoIncrementable=false;
this.hasPrimaryKeyConstraintApplied=false;
this.hasUniqueConstraintApplied=false;
this.hasNotNullConstraintApplied=false;
}
public void setName(java.lang.String name)
{
this.name=name;
}
public java.lang.String getName()
{
return this.name;
}
public void setType(java.lang.String type)
{
this.type=type;
}
public java.lang.String getType()
{
return this.type;
}
public void setWidth(int width)
{
this.width=width;
}
public int getWidth()
{
return this.width;
}
public void setPrecision(int precision)
{
this.precision=precision;
}
public int getPrecision()
{
return this.precision;
}
public void setIsAutoIncrementable(boolean isAutoIncrementable)
{
this.isAutoIncrementable=isAutoIncrementable;
}
public boolean getIsAutoIncrementable()
{
return this.isAutoIncrementable;
}
public void setHasPrimaryKeyConstraintApplied(boolean hasPrimaryKeyConstraintApplied)
{
this.hasPrimaryKeyConstraintApplied=hasPrimaryKeyConstraintApplied;
}
public boolean getHasPrimaryKeyConstraintApplied()
{
return this.hasPrimaryKeyConstraintApplied;
}
public void setHasUniqueConstraintApplied(boolean hasUniqueConstraintApplied)
{
this.hasUniqueConstraintApplied=hasUniqueConstraintApplied;
}
public boolean getHasUniqueConstraintApplied()
{
return this.hasUniqueConstraintApplied;
}
public void setHasNotNullConstraintApplied(boolean hasNotNullConstraintApplied)
{
this.hasNotNullConstraintApplied=hasNotNullConstraintApplied;
}
public boolean getHasNotNullConstraintApplied()
{
return this.hasNotNullConstraintApplied;
}

}