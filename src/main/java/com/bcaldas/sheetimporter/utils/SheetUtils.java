package com.bcaldas.sheetimporter.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class SheetUtils {

    public static Row getHeadersRow(Sheet sheet) {
        Iterator<Row> rowsIt = sheet.rowIterator();
        return rowsIt.next();
    }
}
