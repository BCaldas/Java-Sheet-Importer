package com.bcaldas.sheetimporter.cellparse;

public class CellParse {

    public void doParse(CellImportStrategy cellImportStrategy) {
        if (cellImportStrategy != null) {
            cellImportStrategy.parse();
        }
    }
}
