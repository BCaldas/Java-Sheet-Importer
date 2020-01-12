package com.bcaldas.sheetimporter.cellparse;

import com.bcaldas.sheetimporter.exception.ParseValueException;
import com.bcaldas.sheetimporter.utils.DateUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CellNumber implements CellImportStrategy {

    private Cell cell;
    private Field field;
    private Object finalObject;

    public CellNumber(Cell cell, Field field, Object finalObject) {
        this.cell = cell;
        this.field = field;
        this.finalObject = finalObject;
    }

    @Override
    public void parse() {

        try {
            if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                field.set(finalObject, cell.getNumericCellValue());
            } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                field.set(finalObject, (int) cell.getNumericCellValue());
            } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                field.set(finalObject, (long) cell.getNumericCellValue());
            } else if (field.getType().equals(Date.class)) {
                field.set(finalObject, cell.getDateCellValue());
            } else if (field.getType().equals(LocalDate.class)) {
                field.set(finalObject, DateUtils.parseDateToLocalDate(cell.getDateCellValue()));
            } else if (field.getType().equals(LocalDateTime.class)) {
                field.set(finalObject, DateUtils.parseDateToLocalDateTime(cell.getDateCellValue()));
            }
        } catch (IllegalAccessException e) {
            throw new ParseValueException("Impossible to parse the NUMERIC value " + cell.toString());
        }

    }
}
