package com.example.pizza_backend;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FileUploadUtilTest {

    private static final String UPLOAD_DIR = "temp-test-dir";

    @BeforeEach
    void setUp() throws IOException {
        // สร้าง temp directory
        Files.createDirectories(Paths.get(UPLOAD_DIR));
    }

    @AfterEach
    void tearDown() throws IOException {
        // ลบ temp directory และไฟล์ทั้งหมด
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testSaveFile_Success() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        String fileName = "testfile.txt";
        FileUploadUtil.saveFile(UPLOAD_DIR, multipartFile, fileName);

        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        assertThat(Files.exists(filePath)).isTrue();

        String content = Files.readString(filePath);
        assertThat(content).isEqualTo("test content");
    }

    @Test
    void testSaveFile_EmptyFile() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(true);

        String fileName = "emptyfile.txt";
        IOException ex = assertThrows(IOException.class,
                () -> FileUploadUtil.saveFile(UPLOAD_DIR, multipartFile, fileName));
        assertThat(ex.getMessage()).isEqualTo("Image file is null or empty");
    }

    @Test
    void testDeleteFile_Success() throws IOException {
        // สร้างไฟล์ก่อน
        String fileName = "file-to-delete.txt";
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.writeString(filePath, "delete me");

        assertThat(Files.exists(filePath)).isTrue();

        FileUploadUtil.deleteFile(UPLOAD_DIR, fileName);

        assertThat(Files.exists(filePath)).isFalse();
    }

    @Test
    void testDeleteFile_FileNotFound() throws IOException {
        String fileName = "nonexistent.txt";
        // Should not throw exception
        FileUploadUtil.deleteFile(UPLOAD_DIR, fileName);
    }
}
