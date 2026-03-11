package com.tperons.file.importer.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tperons.exception.BadRequestException;
import com.tperons.file.importer.contract.FileImporter;
import com.tperons.file.importer.impl.CsvImporter;
import com.tperons.file.importer.impl.XlsxImporter;

@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    private final ApplicationContext context;

    public FileImporterFactory(ApplicationContext context) {
        this.context = context;
    }

    public FileImporter getImporter(String fileName) throws Exception {
        logger.info("Determining file importer for file: {}", fileName);
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            logger.error("Failed to determine importer. Unsupported file extension for: {}", fileName);
            throw new BadRequestException("Invalid file format!");
        }
    }

}
