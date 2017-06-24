package org.gs1us.fileimport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

public class XlsxRowSourceSource implements RowSourceSource<String>
{
    public static void main(String[] args) throws IOException, ImportException
    {
        File file = new File(args[0]);
        XlsxRowSourceSource rowSourceSource = new XlsxRowSourceSource(file);
        
        while (rowSourceSource.next())
        {
            RowSource<String> rowSource = rowSourceSource.getRowSource();
            System.out.println("Sheet " + rowSourceSource.getRowSourceName());
            
            while (rowSource.next())
            {
                System.out.format("  Line %d: %s\n",  rowSource.getRowIndex(), rowSource.getRowContents());
            }
            System.out.println();
        }
    }
    private static final QName R_QNAME = new QName("r");
    private static final QName T_QNAME = new QName("t");

    private String[] m_sharedStringsTable = null;
    //private Iterator<InputStream> m_sheetStreamsIterator;
    private RowSource<String> m_currentRowSource;
    private String m_currentRowSourceName;
    private List<SheetDesc> m_sheetDescs;
    private int m_nextSheetIndex = 0;
    private XSSFReader m_xssfReader;
    
    private ValueConditioner<String> m_valueConditioner = new IdentityValueConditioner<String>();

    public XlsxRowSourceSource(File file) throws IOException 
    {
        try
        {
            OPCPackage pkg = OPCPackage.open(file);
            init(pkg);
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    public XlsxRowSourceSource(InputStream is) throws IOException 
    {
        try
        {
            OPCPackage pkg = OPCPackage.open(is);
            init(pkg);
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }
    
    public ValueConditioner<String> getValueConditioner()
    {
        return m_valueConditioner;
    }

    public void setValueConditioner(ValueConditioner<String> valueConditioner)
    {
        m_valueConditioner = valueConditioner;
    }

    private void init(OPCPackage pkg)
        throws IOException, OpenXML4JException, InvalidFormatException,
        XMLStreamException, FactoryConfigurationError
    {
        m_xssfReader = new XSSFReader( pkg );

        InputStream sstis = m_xssfReader.getSharedStringsData();
        readSharedStrings(sstis);
        
        InputStream wis = m_xssfReader.getWorkbookData();
        m_sheetDescs = readSheetDescs(wis);
        
        //m_sheetStreamsIterator = r.getSheetsData();
    }

    private List<SheetDesc> readSheetDescs(InputStream inputStream) throws XMLStreamException, FactoryConfigurationError
    {
        XMLEventReader r = XMLInputFactory.newFactory().createXMLEventReader(inputStream);
        
        List<SheetDesc> result = new ArrayList<>();
        
        while (r.hasNext())
        {
            XMLEvent event = r.nextEvent();
            if (event.isStartElement())
            {
                StartElement start = (StartElement)event;
                if (start.getName().getLocalPart().equals("sheet"))
                {
                    Attribute nameAttribute = start.getAttributeByName(new QName("name"));
                    Attribute idAttribute = start.getAttributeByName(new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id"));
                    if (nameAttribute != null && idAttribute != null)
                    {
                        result.add(new SheetDesc(idAttribute.getValue(), nameAttribute.getValue()));
                    }
 
                }
            }
        }
        return result;
    }
    
    private static class SheetDesc
    {
        private String m_id;
        private String m_name;
        
        public SheetDesc(String id, String name)
        {
            super();
            m_id = id;
            m_name = name;
        }
        public String getId()
        {
            return m_id;
        }
        public String getName()
        {
            return m_name;
        }
        
        
    }

    private void readSharedStrings(InputStream inputStream) throws XMLStreamException, FactoryConfigurationError
    {
        XMLEventReader r = XMLInputFactory.newFactory().createXMLEventReader(inputStream);
        
        int count = 0;
        
        while (r.hasNext())
        {
            XMLEvent event = r.nextEvent();
            if (event.isStartElement())
            {
                StartElement start = (StartElement)event;
                if (start.getName().getLocalPart().equals("sst"))
                {
                    Attribute uniqueAttribute = start.getAttributeByName(new QName(/*"http://schemas.openxmlformats.org/spreadsheetml/2006/main",*/ "uniqueCount"));
                    if (uniqueAttribute != null)
                    {
                        String uniqueString = uniqueAttribute.getValue();
                        try
                        {
                            int unique = Integer.parseInt(uniqueString);
                            m_sharedStringsTable = new String[unique];
                        }
                        catch (NumberFormatException e)
                        {
                            
                        }
                    }
                }
                else if (start.getName().getLocalPart().equals("t"))
                {
                    String content = r.getElementText();
                    if (m_sharedStringsTable != null && count < m_sharedStringsTable.length)
                    {
                        if (m_sharedStringsTable[count] == null)
                            m_sharedStringsTable[count] = content;
                        else
                            m_sharedStringsTable[count] = m_sharedStringsTable[count] + content;
                                                    
                    }
                    else
                        System.out.format("Extra %d = %s\n", count, content);
                }
            }
            else if (event.isEndElement())
            {
                EndElement end = (EndElement)event;
                if (end.getName().getLocalPart().equals("si"))
                {
                    count++;
                }
            }
        }
    }


    /*
    @Override
    public boolean next() throws ImportException
    {
        try
        {
            if (m_sheetStreamsIterator.hasNext())
            {
                m_currentRowSource = new SheetRowSource(m_sheetStreamsIterator.next());
                m_currentRowSourceName = null; // TODO: implement
                return true;
            }
            else
            {
                m_currentRowSource = null;
                m_currentRowSourceName = null;
                return false;
            }
        }
        catch (XMLStreamException e)
        {
            throw new ImportException(e);
        }
    }
    */
    @Override
    public boolean next() throws ImportException
    {
        try
        {
            if (m_nextSheetIndex < m_sheetDescs.size())
            {
                SheetDesc sheetDesc = m_sheetDescs.get(m_nextSheetIndex);
                m_currentRowSource = new SheetRowSource(m_xssfReader.getSheet(sheetDesc.getId()));
                m_currentRowSourceName = sheetDesc.getName();
                m_nextSheetIndex++;
                return true;
            }
            else
            {
                m_currentRowSource = null;
                m_currentRowSourceName = null;
                return false;
            }
        }
        catch (XMLStreamException | InvalidFormatException | FactoryConfigurationError | IOException e)
        {
            throw new ImportException(e);
        }
    }
    
    @Override
    public RowSource<String> getRowSource()
    {
        return m_currentRowSource;
    }

    @Override
    public String getRowSourceName()
    {
        return m_currentRowSourceName;
    }



    private class SheetRowSource implements RowSource<String>
    {
        private XMLEventReader m_reader;
        private List<String> m_currentRowContents = new ArrayList<>();
        private int m_currentRowIndex = 0;
        private boolean m_finished = false;
        
        public SheetRowSource(InputStream inputStream) throws XMLStreamException, FactoryConfigurationError
        {
            super();
            m_reader = XMLInputFactory.newFactory().createXMLEventReader(inputStream);
        }
        
        

 
        @Override
        public List<String> getRowContents()
        {
            return m_currentRowContents;
        }




        @Override
        public int getRowIndex()
        {
            return m_currentRowIndex;
        }




        private XMLEvent nextElementNamed(String localName) throws XMLStreamException
        {
            while (m_reader.hasNext())
            {
                XMLEvent event = m_reader.nextEvent();
                if (event.isStartElement())
                {
                    StartElement start = (StartElement)event;
                    if (start.getName().getLocalPart().equals(localName))
                    {
                        return event;
                    }
                }
            }
            return null;
            
        }

        @Override
        public boolean next() throws ImportException
        {
            try
            {
                if (m_finished)
                    return false;
                
                XMLEvent row = nextElementNamed("row");
                if (row == null)
                {
                    m_finished = true;
                    return false;
                }
                else
                {
                    m_currentRowContents.clear();

                    readRowContentsInto(m_currentRowContents);
                    m_currentRowIndex++;

                    return true;
                }
            }
            catch (XMLStreamException e)
            {
                throw new ImportException(e);
            }
        }
        
        private void readRowContentsInto(List<String> values) throws XMLStreamException
        {
            while (m_reader.hasNext())
            {
                XMLEvent event = m_reader.nextEvent();
                if (event.isEndElement())
                {
                    EndElement end = (EndElement)event;
                    if (end.getName().getLocalPart().equals("row"))
                    {
                        return;
                    }
                }
                else if (event.isStartElement())
                {
                    StartElement start = (StartElement)event;
                    if (start.getName().getLocalPart().equals("c"))
                    {
                        readColContentsInto(start, values);
                    }
                }
            }    
        }

        private void readColContentsInto(StartElement colStart, List<String> values) throws XMLStreamException
        {
            Attribute typeattr = colStart.getAttributeByName(T_QNAME);
            Attribute cellAddr = colStart.getAttributeByName(R_QNAME);
            int col = cellAddrCol(cellAddr.getValue());
            while (col >= values.size())
                values.add(null);

            
            boolean isSharedString = typeattr != null && typeattr.getValue().equals("s");
            XMLEvent event = m_reader.nextEvent();
            if (event.isEndElement())
            {
                return;
            }
            else if (event.isStartElement())
            {
                // assuming it's the V element
                String contents = m_valueConditioner.condition(m_reader.getElementText());
                if (contents == null)
                {
                    return;
                }
                else if (isSharedString)
                {
                    try
                    {
                        int index = Integer.parseInt(contents);
                        if (index >= 0 && index < m_sharedStringsTable.length)
                        {
                            if (col >= 0)
                                values.set(col,  m_valueConditioner.condition(m_sharedStringsTable[index]));
                            return;
                        }
                        else
                        {
                            if (col >= 0)
                                values.set(col, contents);
                            return;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        {
                            if (col >= 0)
                                values.set(col, contents);
                            return;
                        }

                    }
                }
                else
                {
                    if (col >= 0)
                        values.set(col, contents);
                    return;
                }

            }
            else
                return;
        }



        private int cellAddrCol(String cellAddr)
        {
            if (Character.isLetter(cellAddr.charAt(1)))
                return (cellAddr.charAt(0) - 'A' + 1) * 26 + cellAddr.charAt(1) - 'A';
            else
                return cellAddr.charAt(0) - 'A';
        }

        
    }

}
