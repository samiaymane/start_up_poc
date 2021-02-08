package fr.tse.fise3.info6.start_up_poc.utils;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExportPdfAction {

    private LocalDate startDate;

    private LocalDate endDate;

}
