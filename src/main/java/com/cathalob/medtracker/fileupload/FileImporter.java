package com.cathalob.medtracker.fileupload;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
@Slf4j
public abstract class FileImporter {


    public void processMultipartFile(MultipartFile fileToImport) {
        this.logProcessing(fileToImport.getOriginalFilename());
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileToImport.getInputStream());
            this.processWorkbook(workbook);
        } catch (EncryptedDocumentException | IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    public void processFileNamed(String filename) {
        this.logProcessing(filename);
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filename));
            this.processWorkbook(workbook);
        } catch (EncryptedDocumentException | IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    abstract public void processWorkbook(XSSFWorkbook workbook);
    abstract public void logProcessing(String originalFilename);
}
