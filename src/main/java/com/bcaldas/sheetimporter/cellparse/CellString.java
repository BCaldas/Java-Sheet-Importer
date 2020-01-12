package com.bcaldas.sheetimporter.cellparse;

import com.bcaldas.sheetimporter.exception.ParseValueException;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class CellString implements CellImportStrategy {

    private Cell cell;
    private Field field;
    private Object finalObject;

    public CellString(Cell cell, Field field, Object finalObject) {
        this.cell = cell;
        this.field = field;
        this.finalObject = finalObject;
    }

    @Override
    public void parse() {
        try {
            field.set(finalObject, cell.getStringCellValue());
        } catch (IllegalAccessException e) {
            throw new ParseValueException("Impossible to parse the STRING value " + cell.toString());
        }
    }
}
