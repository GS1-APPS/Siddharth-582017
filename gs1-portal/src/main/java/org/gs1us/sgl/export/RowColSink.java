package org.gs1us.sgl.export;

public interface RowColSink
{
    public void heading(String heading);
    public void endRow();
    public void cell(String value);
    public void cell(Number number);
    public void cell();
    public void finish();
}
