package com.bcaldas.sheetimporter.validation.cell;

import com.bcaldas.sheetimporter.annotations.SheetCellNotEmpty;
import com.bcaldas.sheetimporter.exception.CellValidationException;
import org.apache.poi.ss.usermodel.Cell;

public class CellValidatorImp<T> implements CellValidator<T> {

    private final Class<T> modelClass;

    public CellValidatorImp(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public void validateCell(Cell cell) {
        verifyEmptyCell(cell);
    }

    private void verifyEmptyCell(Cell cell) {
        if (modelClass.isAnnotationPresent(SheetCellNotEmpty.class) && cell.toString().trim().isEmpty()) {
            throw new CellValidationException(modelClass.getDeclaredAnnotation(SheetCellNotEmpty.class).message());
        }
    }
}
