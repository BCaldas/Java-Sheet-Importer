package com.bcaldas.sheetimporter.validation.cell;

import org.apache.poi.ss.usermodel.Cell;

public interface CellValidator<T> {
    void validateCell(Cell cell);
}
