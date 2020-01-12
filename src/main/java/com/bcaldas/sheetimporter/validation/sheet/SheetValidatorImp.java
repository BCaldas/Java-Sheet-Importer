package com.bcaldas.sheetimporter.validation.sheet;

import com.bcaldas.sheetimporter.annotations.SheetCollumns;
import com.bcaldas.sheetimporter.annotations.SheetRows;
import com.bcaldas.sheetimporter.exception.SheetValidationException;
import com.bcaldas.sheetimporter.utils.SheetUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class SheetValidatorImp<T> implements SheetValidator<T> {

    private Sheet sheet;
    private final Class<T> typeParameterClass;

    public SheetValidatorImp(Sheet sheet, Class<T> typeParameterClass) {
        this.sheet = sheet;
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void validate() {
        verifyNumberOfCollums();
        verifyNumberOfRows();
    }

    private void verifyNumberOfCollums() {
        AtomicInteger cellCount = new AtomicInteger();
        if (typeParameterClass.isAnnotationPresent(SheetCollumns.class)) {
            SheetCollumns sheetCollumns = typeParameterClass.getDeclaredAnnotation(SheetCollumns.class);
            Row firstRow = SheetUtils.getHeadersRow(sheet);
            Iterator<Cell> cellIt = firstRow.cellIterator();
            cellIt.forEachRemaining(cell -> cellCount.getAndIncrement());

            if (cellCount.get() < sheetCollumns.min() || cellCount.get() > sheetCollumns.max()) {
                throw new SheetValidationException(sheetCollumns.message());
            }
        }
    }

    private void verifyNumberOfRows() {
        AtomicInteger rowsCount = new AtomicInteger();
        if (typeParameterClass.isAnnotationPresent(SheetRows.class)) {
            SheetRows sheetRows = typeParameterClass.getDeclaredAnnotation(SheetRows.class);
            Iterator<Row> rowsIt = sheet.rowIterator();

            rowsIt.forEachRemaining(cell -> rowsCount.getAndIncrement());

            if (rowsCount.get() < sheetRows.min() || rowsCount.get() > sheetRows.max()) {
                throw new SheetValidationException(sheetRows.message());
            }
        }
    }
}
