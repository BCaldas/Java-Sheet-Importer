package com.bcaldas.sheetimporter;

import com.bcaldas.sheetimporter.annotations.SheetNotDuplicate;
import com.bcaldas.sheetimporter.cellparse.*;
import com.bcaldas.sheetimporter.exception.ParseValueException;
import com.bcaldas.sheetimporter.utils.SheetUtils;
import com.bcaldas.sheetimporter.validation.cell.CellValidator;
import com.bcaldas.sheetimporter.validation.cell.CellValidatorImp;
import com.bcaldas.sheetimporter.validation.sheet.SheetValidator;
import com.bcaldas.sheetimporter.validation.sheet.SheetValidatorImp;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.*;

public class SheetImporter<T> {

    private Sheet sheet;
    private final Class<T> targetModelClass;
    private List<String> modelFields;
    private SheetValidator<T> sheetValidator;
    private CellValidator<T> cellValidator;

    //TODO: Possibilitar que se use validadores de planilha e células próprios. Usar builder talvez.
    public SheetImporter(Sheet sheet, Class<T> clazz) {
        this.sheet = sheet;
        this.targetModelClass = clazz;
        this.sheetValidator = new SheetValidatorImp<>(sheet, clazz);
        this.cellValidator = new CellValidatorImp<>(clazz);
        getModelFields();
    }

    private void getModelFields() {
        this.modelFields = new ArrayList<>();
        Field[] fields = this.targetModelClass.getDeclaredFields();

        for (Field field : fields) {
            modelFields.add(field.getName());
        }
    }

    public List<T> importSheet() throws IllegalAccessException, InstantiationException {
        sheetValidator.validate();

        Map<Integer, String> mapHeaderToField = mapHeaders();

        List<T> objectList = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            T object = null;
            object = targetModelClass.newInstance();
            T finalObject = object;

            row.forEach(cell -> {
                cellValidator.validateCell(cell);
                String columnName = mapHeaderToField.get(cell.getColumnIndex());
                try {
                    if (modelFields.contains(columnName)) {
                        Field field = finalObject.getClass().getDeclaredField(columnName);
                        field.setAccessible(true);
                        parseValue(cell, field, finalObject);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    throw new ParseValueException("Cannot parse value " + cell.toString() + " from collumn " + columnName + ". Cause is: " + e.getMessage());
                }
            });
            objectList.add(finalObject);
        }

        //TODO: ver maneira de lançar exceção em caso de duplicata
        //TODO: perguntar para a Fernanda se não pode simplesmente ignorar as duplicatas ao invés de para a importação.
        if (targetModelClass.isAnnotationPresent(SheetNotDuplicate.class)) {
            Set<T> classSet = new HashSet<>(objectList);
            objectList.clear();
            objectList.addAll(classSet);
        }
        return objectList;
    }

    private void parseValue(Cell cell, Field field, T finalObject) {

        CellImportStrategy cellImportStrategy;
        CellParse cellParse = new CellParse();

        switch (cell.getCellTypeEnum()) {
            case STRING:
                cellImportStrategy = new CellString(cell, field, finalObject);
                break;
            case NUMERIC:
                cellImportStrategy = new CellNumber(cell, field, finalObject);
                break;

            case BOOLEAN:
                cellImportStrategy = new CellBoolean(cell, field, finalObject);
                break;

            case BLANK:
                cellImportStrategy = null;
                break;

            default:
                throw new ParseValueException("Cannot determine type of value: " + cell.toString() + "from collumn " + cell.getColumnIndex());
        }

        cellParse.doParse(cellImportStrategy);
    }

    private Map<Integer, String> mapHeaders() {
        Row headersRow = SheetUtils.getHeadersRow(sheet);
        Map<Integer, String> mapHeaderToField = new HashMap<>();

        headersRow.forEach(cell -> {
            mapHeaderToField.put(cell.getColumnIndex(), cell.getStringCellValue());
        });

        return mapHeaderToField;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
}
