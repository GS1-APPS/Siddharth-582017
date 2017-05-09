package org.gs1us.sgl.export;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelRowColSink implements RowColSink
{
    private SXSSFWorkbook m_workbook;
    private CellStyle m_boldStyle;
    private CellStyle m_currencyStyle;
    private Sheet m_sheet;
    private int m_rowNum;
    private Row m_row;
    private int m_colNum;
    private OutputStream m_outputStream;
    
    public ExcelRowColSink(OutputStream outputStream)
    {
        m_workbook = new SXSSFWorkbook(100);
        
        m_boldStyle = m_workbook.createCellStyle();
        Font boldFont = m_workbook.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        m_boldStyle.setFont(boldFont);
        
        m_currencyStyle = m_workbook.createCellStyle();
        m_currencyStyle.setDataFormat(m_workbook.createDataFormat().getFormat("#,##0.00"));

        m_sheet = m_workbook.createSheet();
        m_rowNum = 0;
        m_row = null;
        m_colNum = 0;
        m_outputStream = outputStream;
    }
    
    @Override
    public void heading(String heading)
    {
        cell(heading, m_boldStyle);
    }

    @Override
    public void endRow()
    {
        m_colNum = 0;
        m_row = null;
        m_rowNum++;
    }

    @Override
    public void cell(String value)
    {
        cell(value, null);
    }
    
    private void cell(String value, CellStyle style)
    {
        if (value == null)
            cell();
        else
        {
            beginRowIfNeeded();
            Cell cell = m_row.createCell(m_colNum);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value);
            if (style != null)
                cell.setCellStyle(style);
            m_colNum++;
        }
    }

    @Override
    public void cell(Number number)
    {
        if (number == null)
            cell();
        else
        {
            beginRowIfNeeded();
            Cell cell = m_row.createCell(m_colNum);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(number.doubleValue());
            cell.setCellStyle(m_currencyStyle);
            m_colNum++;
        }
    }

    @Override
    public void cell()
    {
        beginRowIfNeeded();
        Cell cell = m_row.createCell(m_colNum);
        cell.setCellType(Cell.CELL_TYPE_BLANK);
        m_colNum++;
    }

    private void beginRowIfNeeded()
    {
        if (m_row == null)
        {
            m_row = m_sheet.createRow(m_rowNum);
        }
        
    }

    @Override
    public void finish()
    {
        try
        {
            m_workbook.write(m_outputStream);
            m_outputStream.flush();

            m_workbook.dispose();
        }
        catch (IOException e)
        {
            
        }

    }

}
